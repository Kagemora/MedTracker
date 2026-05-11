package com.yamazaki.medtracker.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yamazaki.medtracker.domain.model.DrugInfo
import com.yamazaki.medtracker.domain.usecase.CreateMedicineWithScheduleUseCase
import com.yamazaki.medtracker.domain.usecase.drugsearch.SearchDrugUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchDrugUseCase: SearchDrugUseCase,
    private val createMedicineWithScheduleUseCase: CreateMedicineWithScheduleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Content())
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    fun processCommand(command: SearchCommand) {
        when (command) {
            is SearchCommand.InputQuery -> inputQuery(command.query)
            is SearchCommand.SelectDrug -> selectDrug(command.drug)
            is SearchCommand.DismissSheet -> dismissSheet()
            is SearchCommand.SaveMedicine -> saveMedicine(
                dosage = command.dosage,
                unit = command.unit,
                hour = command.hour,
                minute = command.minute,
                days = command.days
            )
        }
    }

    private fun inputQuery(query: String) {
        updateContent { it.copy(query = query, error = null) }
        _searchQuery.value = query
        if (query.isBlank()) {
            updateContent { it.copy(results = emptyList()) }
        }
    }

    private fun selectDrug(drug: DrugInfo) {
        updateContent { it.copy(selectedDrug = drug, showAddSheet = true) }
    }

    private fun dismissSheet() {
        updateContent { it.copy(showAddSheet = false, selectedDrug = null) }
    }

    private fun saveMedicine(
        dosage: String,
        unit: String,
        hour: Int,
        minute: Int,
        days: List<Int>
    ) {
        val currentState = _state.value
        if (currentState !is SearchState.Content) return
        val drug = currentState.selectedDrug ?: return

        viewModelScope.launch {
            try {
                createMedicineWithScheduleUseCase(
                    drug = drug,
                    dosage = dosage,
                    unit = unit,
                    hour = hour,
                    minute = minute,
                    days = days
                )
                updateContent { it.copy(showAddSheet = false, selectedDrug = null) }
            } catch (e: Exception) {
                updateContent { it.copy(error = "Ошибка сохранения: ${e.message}") }
            }
        }
    }

    private fun observeSearchQuery() {
        _searchQuery
            .debounce(500L)
            .distinctUntilChanged()
            .filter {
                it.length >= 2
            }
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun performSearch(query: String) {
        updateContent {
            it.copy(isLoading = true, error = null)
        }
        try {
            val results = searchDrugUseCase(query)
            updateContent { it.copy(results = results, isLoading = false) }
        } catch (e: Exception) {
            updateContent { it.copy(error = "Ошибка поиска: ${e.message}", isLoading = false) }
        }
    }

    private fun updateContent(transform: (SearchState.Content) -> SearchState.Content) {
        _state.update { current ->
            if (current is SearchState.Content) transform(current) else current
        }
    }
}

sealed interface SearchCommand {
    data class InputQuery(val query: String) : SearchCommand
    data class SelectDrug(val drug: DrugInfo) : SearchCommand
    data object DismissSheet : SearchCommand
    data class SaveMedicine(
        val dosage: String,
        val unit: String,
        val hour: Int,
        val minute: Int,
        val days: List<Int>
    ) : SearchCommand
}

sealed interface SearchState {
    data object Loading : SearchState
    data class Content(
        val query: String = "",
        val results: List<DrugInfo> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedDrug: DrugInfo? = null,
        val showAddSheet: Boolean = false
    ) : SearchState

    data class Error(val error: String) : SearchState
}

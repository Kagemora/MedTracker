package com.yamazaki.medtracker.domain.model

data class DrugInfo(
    val id: String,
    val brandName: String,
    val genericName: String,
    val manufacturer: String,
    val purpose: String,
    val warnings: String
)

/*
DrugInfo — временная модель, которая приходит из OpenFDA.
НЕ сохраняется в Room!
Используется только на экране поиска.
Когда пользователь нажимает "Добавить" — на основе DrugInfo создаётся объект Medicine
- brandName - торговое название препарата
- genericName - международное непатентованное название (МНН)
- manufacturer - производитель препарата
- purpose - краткое описание назначения препарата
- warnings - предупреждения и противопоказания
*/
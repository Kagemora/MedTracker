package com.yamazaki.medtracker.domain.model

data class Schedule(
    val id: Long = 0,
    val medicineId: Long,
    val medicineName: String,
    val hour: Int,
    val minute: Int,
    val days: List<Int>,
    val isEnabled: Boolean = true
)

/*
Класс Schedule хранит все свойства одного расписания приёма лекарства:
- id — уникальный идентификатор записи
- medicineId — внешний ключ на Medicine
- medicineName — дублируемое название для уведомлений
- hour, minute — время приёма
- days — дни недели, когда нужно принимать
- isEnabled — активность расписания
*/
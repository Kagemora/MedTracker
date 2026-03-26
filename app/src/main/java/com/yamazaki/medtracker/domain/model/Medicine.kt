package com.yamazaki.medtracker.domain.model

data class Medicine(
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val unit: String,
    val color: Int,
    val notes: String = "",
    val isActive: Boolean = true
)

/*
Класс Medicine хранит все основные свойства лекарства:
- id - уникальный идентификатор лекаства для ROOM. 0 чтобы рум автоматом создавал новый идентификатор
- name — название
- dosage — дозировка "500", "10"
- unit — единица измерения "мг", "мл", "таблетка"
- сolor — цвет иконки в UI
- notes - заметки пользователя
- isActive — флаг мягкого удаления, мы не стриаем из БД, потому что есть история логов
*/
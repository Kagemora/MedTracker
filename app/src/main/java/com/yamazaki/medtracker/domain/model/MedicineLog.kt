package com.yamazaki.medtracker.domain.model

data class MedicineLog(
    val id: Long = 0,
    val scheduleId: Long,
    val medicineName: String,
    val dosage: String,
    val unit: String,
    val scheduledAt: Long,
    val takenAt: Long? = null,
    val status: LogStatus = LogStatus.PENDING
)

/*
Класс MedicineLog хранит запись факта приёма лекарства:
- id — уникальный идентификатор записи
- scheduleId — внешний ключ на Schedule
- medicineName, dosage, unit — денормализованные данные для читаемой истории
- scheduledAt — запланированное время приёма
- takenAt — фактическое время приёма (null если ещё не принят)
- status — статус приёма: PENDING / TAKEN / SKIPPED
*/

enum class LogStatus {
    PENDING,
    TAKEN,
    SKIPPED
}

/*
LogStatus описывает статус одной записи MedicineLog.
Состояния:
- PENDING — запись создана, пользователь ещё не подтвердил приём (начальное состояние)
- TAKEN — пользователь принял лекарство
- SKIPPED — пользователь пропустил приём
*/
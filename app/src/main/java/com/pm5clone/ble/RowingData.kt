package com.pm5clone.ble

/**
 * Данные тренировки в формате, максимально близком к PM5:
 * - elapsedTimeSec: время с начала тренировки, сек (у PM5 отображается как мм:сс.д)
 * - distanceMeters: суммарная дистанция, м
 * - strokeRatePerMin: темп гребли, гребков/мин (SPM)
 * - splitPer500mSec: текущий темп в пересчёте на 500м, сек (главная цифра PM5)
 * - watts: мгновенная мощность, Вт
 * - caloriesPerHour: калории/час (PM5 показывает именно так, а не тотал)
 * - heartRateBpm: пульс, если тренажёр/ремень его транслирует (может быть null)
 * - strokeCount: число гребков
 */
data class RowingData(
    val elapsedTimeSec: Double = 0.0,
    val distanceMeters: Int = 0,
    val strokeRatePerMin: Int = 0,
    val splitPer500mSec: Double = 0.0,
    val watts: Int = 0,
    val caloriesPerHour: Int = 0,
    val heartRateBpm: Int? = null,
    val strokeCount: Int = 0,
    val connected: Boolean = false
)

/** Один сырой пакет, для панели калибровки протокола. */
data class RawPacket(
    val serviceUuid: String,
    val characteristicUuid: String,
    val hex: String,
    val timestampMs: Long
)

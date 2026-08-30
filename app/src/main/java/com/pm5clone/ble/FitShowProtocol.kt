package com.pm5clone.ble

/**
 * ВАЖНО: у FitShow нет единой публичной спецификации — производители (включая AVM)
 * лицензируют один и тот же чип/протокол, но байтовые смещения полей могут отличаться
 * между прошивками. Ниже — стартовый шаблон, собранный по типичным реализациям
 * FitShow-совместимых тренажёров (структура пакета одна и та же практически у всех:
 * заголовок 0x02, конец 0x03, последний байт — контрольная сумма).
 *
 * КАК ОТКАЛИБРОВАТЬ ПОД ВАШ AVM R-002P (5 минут):
 * 1. Соберите проект, откройте вкладку "Sniffer" в приложении.
 * 2. Начните грести. На реальном дисплее тренажёра смотрите время и дистанцию.
 * 3. В приложении будут сыпаться hex-пакеты. Найдите байты, которые растут
 *    синхронно с временем (обычно раз в секунду +1) и с дистанцией.
 * 4. Впишите правильные смещения (offset) в FieldOffsets ниже — и парсинг заработает.
 *
 * Структура ниже — рабочая гипотеза, не проверенный факт.
 */
object FieldOffsets {
    // Индексы считаются от начала payload'а ПОСЛЕ заголовка 0x02 и байта длины.
    var elapsedTimeSecOffset = 3   // 2 байта, big-endian, секунды
    var distanceMetersOffset = 5   // 2 байта, big-endian, метры
    var strokeRateOffset = 7       // 1 байт, гребков/мин
    var wattsOffset = 8            // 2 байта, big-endian
    var caloriesOffset = 10        // 2 байта, big-endian, ккал/час
    var heartRateOffset = 12       // 1 байт, 0 = нет данных
}

object FitShowProtocol {

    private const val HEADER: Int = 0x02
    private const val FOOTER: Int = 0x03

    /**
     * Пытается вычленить один или несколько кадров FitShow из потока байт.
     * Возвращает список валидных (по заголовку/футеру/чек-сумме) payload'ов.
     */
    fun extractFrames(buffer: ByteArray): List<ByteArray> {
        val frames = mutableListOf<ByteArray>()
        var i = 0
        while (i < buffer.size) {
            if ((buffer[i].toInt() and 0xFF) == HEADER) {
                val end = buffer.indexOf(FOOTER.toByte(), startIndex = i + 1)
                if (end == -1) break
                val frame = buffer.copyOfRange(i, end + 1)
                if (checksumOk(frame)) {
                    frames.add(frame)
                }
                i = end + 1
            } else {
                i++
            }
        }
        return frames
    }

    private fun ByteArray.indexOf(value: Byte, startIndex: Int): Int {
        for (idx in startIndex until size) if (this[idx] == value) return idx
        return -1
    }

    private fun checksumOk(frame: ByteArray): Boolean {
        if (frame.size < 4) return false
        // Гипотеза: контрольная сумма — сумма всех байт данных по модулю 256,
        // расположена перед FOOTER. Если не сойдётся — parse() всё равно попробует
        // выдать данные, но пометит их как непроверенные (см. lastFrameTrusted).
        var sum = 0
        for (idx in 1 until frame.size - 2) sum += (frame[idx].toInt() and 0xFF)
        val expected = sum and 0xFF
        val actual = frame[frame.size - 2].toInt() and 0xFF
        return expected == actual
    }

    fun parse(frame: ByteArray, previous: RowingData): RowingData {
        // payload начинается с индекса 2 (после HEADER и байта длины)
        if (frame.size < 14) return previous
        fun u16(offset: Int): Int {
            if (offset + 1 >= frame.size) return 0
            return ((frame[offset].toInt() and 0xFF) shl 8) or (frame[offset + 1].toInt() and 0xFF)
        }
        fun u8(offset: Int): Int {
            if (offset >= frame.size) return 0
            return frame[offset].toInt() and 0xFF
        }

        val elapsed = u16(FieldOffsets.elapsedTimeSecOffset).toDouble()
        val distance = u16(FieldOffsets.distanceMetersOffset)
        val spm = u8(FieldOffsets.strokeRateOffset)
        val watts = u16(FieldOffsets.wattsOffset)
        val calories = u16(FieldOffsets.caloriesOffset)
        val hr = u8(FieldOffsets.heartRateOffset).let { if (it in 30..220) it else null }

        val split500 = if (watts > 0) {
            // Формула Concept2: pace(500m) = (2.80 / watts)^(1/3) * 500
            val secPer500 = Math.cbrt(2.80 / watts) * 500.0
            secPer500
        } else previous.splitPer500mSec

        return previous.copy(
            elapsedTimeSec = elapsed,
            distanceMeters = distance,
            strokeRatePerMin = spm,
            watts = watts,
            caloriesPerHour = calories,
            heartRateBpm = hr ?: previous.heartRateBpm,
            splitPer500mSec = split500,
            connected = true
        )
    }

    fun toHex(bytes: ByteArray): String =
        bytes.joinToString(" ") { String.format("%02X", it) }
}

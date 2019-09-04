package org.cipo.statsit.calcu_list.db

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.round

fun encodeDoubleStringAsInt(entry: String, precision: Int = 2): Long {
    return try {
        val times = 10.0.pow(precision.toDouble())
        return round(entry.toDouble() * times).toLong()
    } catch (exception: java.lang.NumberFormatException) {
        0
    }
}

fun decodeIntAsString(entry: Long, precision: Int = 2): String {
    val df = DecimalFormat("0.00")
    df.roundingMode = RoundingMode.HALF_EVEN
    val times = Math.pow(10.0, precision.toDouble())
    return df.format(entry.toDouble() / times)
}


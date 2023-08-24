package com.jojo.aerocalculator.tools

import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.truncate

fun Float.toFormattedTime(): String {
    val decimal = DecimalFormat("00")

    return "${decimal.format(truncate(this) / 60)}h ${decimal.format(truncate(this).toInt() % 60)}min ${
        decimal.format(
            ((this - truncate(this)) * 60f).roundToInt()
        )
    }sec"
}
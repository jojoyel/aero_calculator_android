package com.jojo.aerocalculator.tools

import kotlin.math.roundToInt


fun Float.roundWithOneDecimal() = ((this * 10).roundToInt() / 10f)
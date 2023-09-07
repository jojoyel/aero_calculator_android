package com.jojo.aerocalculator

import com.jojo.aerocalculator.tools.toFormattedTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testFloatToFormattedTime() {
        assertEquals("00h 44min 06sec", 44.1f.toFormattedTime())
    }
}
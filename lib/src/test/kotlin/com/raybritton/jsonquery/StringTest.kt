package com.raybritton.jsonquery

import com.raybritton.jsonquery.ext.unescapeArrayNotation
import com.raybritton.jsonquery.ext.unescapeDotNotation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StringTest {

    @Test
    fun testUnescaping() {
        //Given a range of samples
        val input1 = ".items"
        val input2 = ".items[0]"
        val input3 = ".ite\\.ms"
        val input4 = ".ite\"ms"
        val input5 = ".ite ms"
        val input6 = ".ite£$%^&*()ms"
        val input7 = ".itehgtdj432564746565345tjrr#][.#[.[#]].ms"
        val input8 = ".items\\[0]"
        val input9 = "\\\\Hi\\\".tyas[0]"
        val input10 = ".items\\\\[0]"

        //When unescaped
        val result1 = input1.unescapeDotNotation().unescapeArrayNotation()
        val result2 = input2.unescapeDotNotation().unescapeArrayNotation()
        val result3 = input3.unescapeDotNotation().unescapeArrayNotation()
        val result4 = input4.unescapeDotNotation().unescapeArrayNotation()
        val result5 = input5.unescapeDotNotation().unescapeArrayNotation()
        val result6 = input6.unescapeDotNotation().unescapeArrayNotation()
        val result7 = input7.unescapeDotNotation().unescapeArrayNotation()
        val result8 = input8.unescapeDotNotation().unescapeArrayNotation()
        val result9 = input9.unescapeDotNotation().unescapeArrayNotation()
        val result10 = input10.unescapeDotNotation().unescapeArrayNotation()

        //Check they are valid
        assertEquals("input 1", ".items", result1)
        assertEquals("input 2", ".items[0]", result2)
        assertEquals("input 3", ".ite.ms", result3)
        assertEquals("input 4", ".ite\"ms", result4)
        assertEquals("input 5", ".ite ms", result5)
        assertEquals("input 6", ".ite£\$%^&*()ms", result6)
        assertEquals("input 7", ".itehgtdj432564746565345tjrr#][.#[.[#]].ms", result7)
        assertEquals("input 8", ".items[0]", result8)
        assertEquals("input 9", "\\\\Hi\\\".tyas[0]", result9)
        assertEquals("input 10", ".items\\[0]", result10)
    }
}
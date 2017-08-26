package com.raybritton.jsonquery

import com.raybritton.jsonquery.utils.unescape
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
        val result1 = input1.unescape()
        val result2 = input2.unescape()
        val result3 = input3.unescape()
        val result4 = input4.unescape()
        val result5 = input5.unescape()
        val result6 = input6.unescape()
        val result7 = input7.unescape()
        val result8 = input8.unescape()
        val result9 = input9.unescape()
        val result10 = input10.unescape()

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
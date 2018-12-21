package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert
import org.junit.Test

class WhereTests {
//    @Test
//    fun simpleObject() {
//        val json = "{'a':'test','b':1}"
//
//        val output1 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.' WHERE 'b' == 1")
//        val output2 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.' WHERE 'b' > 0")
//        val output3 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.' WHERE 'b' < 2")
//        val output4 = JsonQuery().loadJson(json).query("SELECT 'b' FROM '.' WHERE 'a' == 'test' ")
//        val output5 = JsonQuery().loadJson(json).query("SELECT 'b' FROM '.' WHERE 'a' # 'tes'")
//        val output6 = JsonQuery().loadJson(json).query("SELECT 'b' FROM '.' WHERE 'a' !# 'quit' ")
//        val output7 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.' WHERE 'b' != 2")
//
//
//        val fail1 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.' WHERE 'b' == 2")
//        val fail2 = JsonQuery().loadJson(json).query("SELECT 'b' FROM '.' WHERE 'a' == 'hello'")
//
//        Assert.assertEquals("Output 1", "\"test\"", output1)
//        Assert.assertEquals("Output 2", "\"test\"", output2)
//        Assert.assertEquals("Output 3", "\"test\"", output3)
//        Assert.assertEquals("Output 4", "1.0", output4)
//        Assert.assertEquals("Output 5", "1.0", output5)
//        Assert.assertEquals("Output 6", "1.0", output6)
//        Assert.assertEquals("Output 7", "\"test\"", output7)
//
//        Assert.assertEquals("Fail 1", "", fail1)
//        Assert.assertEquals("Fail 2", "", fail2)
//    }
}
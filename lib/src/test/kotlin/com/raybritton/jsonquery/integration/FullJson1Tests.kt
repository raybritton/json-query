package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class FullJson1Tests {
    private val json = """[{"created_at":"Thu Jun 22 21:00:00 +0000 2017","id":877994,"id_str":"877994604561387520","text":"Creating a Grocery List Manager Using Angular, Part 1: Add &amp; Display Items https://t.co/xFox78juL1 #Angular","truncated":false,"entities":{"hashtags":[{"text":"Angular","indices":[103,111]}],"symbols":[],"user_mentions":[],"urls":[{"url":"https://t.co/xFox78juL1","expanded_url":"http://buff.ly/2sr60pf","display_url":"buff.ly/2sr60pf","indices":[79,102]}]},"source":"<a href=\"http://bufferapp.com\" rel=\"nofollow\">Buffer</a>","user":{"id":772682964,"id_str":"772682964","name":"SitePoint JavaScript","screen_name":"SitePointJS","location":"Melbourne, Australia","description":"Keep up with JavaScript tutorials, tips, tricks and articles at SitePoint.","url":"http://t.co/cCH13gqeUK","entities":{"url":{"urls":[{"url":"http://t.co/cCH13gqeUK","expanded_url":"http://sitepoint.com/javascript","display_url":"sitepoint.com/javascript","indices":[0,22]}]},"description":{"urls":[]}},"protected":false,"followers_count":2145,"friends_count":18,"listed_count":328,"created_at":"Wed Aug 22 02:06:33 +0000 2012","favourites_count":57,"utc_offset":43200,"time_zone":"Wellington"}}]"""

    @Test
    fun `select field from nested objects`() {
        val result = JsonQuery(json).query(""" SELECT ('truncated','user.protected', 'id') from '.' """)

        assertEquals("""{truncated: false, id: 877994.0, protected: false}""", result)
    }

    @Test
    fun `select field from nested objects values only`() {
        val result = JsonQuery(json).query(""" SELECT VALUES FROM (SELECT ('truncated','user.protected', 'id') from '.' AS JSON)""")

        assertEquals("""{false, 877994.0, false}""", result)
    }

    @Test
    fun `select field in nested object and array`() {
        val result = JsonQuery(json).query("SELECT 'user.entities.url.urls.url' FROM '.'")

        assertEquals("url: \"http://t.co/cCH13gqeUK\"", result)
    }

    @Test
    fun `describe object in object`() {
        val result = JsonQuery(json).query("DESCRIBE (SELECT 'user.entities.url.urls.url' FROM '.' AS JSON) ")

        assertEquals("ARRAY(OBJECT(OBJECT(OBJECT(OBJECT(ARRAY(OBJECT(STRING)))))))", result)
    }

    @Test
    fun `describe renamed field in object in object`() {
        val result = JsonQuery(json).query("DESCRIBE (SELECT 'user.entities.url.urls.url' AS 'foo' FROM '.' AS JSON) ")

        assertEquals("ARRAY(OBJECT(STRING))", result)
    }

    @Test
    fun `search for string match`() {
        val result = JsonQuery(json).query("SEARCH '.' FOR ANY # 'Creat'")

        assertEquals(""".[0].created_at
.[0].text
.[0].user.created_at""", result)
    }

    @Test
    fun `search for string match with values`() {
        val result = JsonQuery(json).query("SEARCH '.' FOR KEY == 'url' WITH VALUES CASE SENSITIVE")

        assertEquals(""".[0].entities.urls.[0].url: https://t.co/xFox78juL1
.[0].user.url: http://t.co/cCH13gqeUK
.[0].user.entities.url.urls.[0].url: http://t.co/cCH13gqeUK""", result)
    }
}
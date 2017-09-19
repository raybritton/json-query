package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonFileTest4 {

    val json = """[{"attributes":{"GlobalId":"0a04ed12-9af8-415f-a4d7-6ecb9980fa6e","dbId":89,"type":"Start shift","startTime":1505139342509,"finishTime":1505139621043,"dutyStatement":"{B032B9B3-58B2-43B5-82C5-868C0D516424}","isEditableStartShift":"T"}},{"attributes":{"GlobalId":"c61936b4-6731-4cd5-b774-ea3b7dae4a1c","dbId":90,"type":"Create PCN","startTime":1505139621043,"finishTime":1505139812204,"dutyStatement":"{B032B9B3-58B2-43B5-82C5-868C0D516424}","details":"GF00264455","isEditableStartShift":"F"}},{"attributes":{"GlobalId":"ed853f06-95bf-45e0-b76e-5d028fdf718a","dbId":91,"type":"double yellow","startTime":1505139812204,"finishTime":1505140371305,"dutyStatement":"{B032B9B3-58B2-43B5-82C5-868C0D516424}","details":"double yellow......","isEditableStartShift":"F"}},{"attributes":{"GlobalId":"69e0692f-8256-40cf-943b-0ca06e1a7f4c","type":"End shift","startTime":1505140371305,"finishTime":1505140371305,"dutyStatement":"{B032B9B3-58B2-43B5-82C5-868C0D516424}","isEditableStartShift":"F"}}]"""

    @Test
    fun testSelectingElement() {
        //Given query to select element from object in the list as json
        val query = """SELECT "attributes.GlobalId" FROM ".""""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result = jsonQuery.query(query)

        //Then only the globalids are returned (and not all elements)
        Assert.assertEquals("result", """[0a04ed12-9af8-415f-a4d7-6ecb9980fa6e, c61936b4-6731-4cd5-b774-ea3b7dae4a1c, ed853f06-95bf-45e0-b76e-5d028fdf718a, 69e0692f-8256-40cf-943b-0ca06e1a7f4c]""", result)
    }
}
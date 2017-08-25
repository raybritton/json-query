import com.raybritton.jsonquery.ProgArgs
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProgArgsTest {
    @Rule
    @JvmField
    val exception = ExpectedException.none()

    @Test
    fun testOrderIndepence() {
        //Given several sample args
        val args1 = arrayOf("jsonq.jar", "-q", "SOME QUERY", "-i", "input.json")
        val args2 = arrayOf("jsonq.jar", "-i", "input.json", "-q", "SOME QUERY")

        //When processed
        val progArgs1 = ProgArgs.build(args1)
        val progArgs2 = ProgArgs.build(args2)

        //Then the details should match
        assertEquals("args1 - query", args1[2], progArgs1.query)
        assertEquals("args1 - input", args1[4], progArgs1.input)
        assertEquals("args2 - query", args2[4], progArgs2.query)
        assertEquals("args2 - input", args2[2], progArgs2.input)
    }

    @Test
    fun testNoQuery() {
        //Given args with no query
        val args = arrayOf("jsonq.jar", "-i", "input.json")

        //When processed
        try {
            val progArgs = ProgArgs.build(args)
            fail("No exception thrown")
        } catch (e: IllegalStateException) {
            if (e.message!!.contains("query")) {

            } else {
                fail("Message did not contain query: ${e.message}")
            }
        }

        //Then exception should be thrown
    }
}
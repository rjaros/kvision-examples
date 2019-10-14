package test.com.example

import pl.treksoft.kvision.test.DomSpec
import kotlin.test.Test
import kotlin.test.assertTrue

class AppSpec : DomSpec {

    @Test
    fun render() {
        run {
            assertTrue(true, "Dummy test")
        }
    }
}

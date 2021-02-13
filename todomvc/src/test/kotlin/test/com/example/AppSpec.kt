package test.com.example

import io.kvision.test.DomSpec
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

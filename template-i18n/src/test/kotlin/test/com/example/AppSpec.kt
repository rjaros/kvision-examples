package test.com.example

import com.example.App
import kotlin.browser.document
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

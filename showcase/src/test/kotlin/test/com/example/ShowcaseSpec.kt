package test.com.example

import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test
import kotlin.test.assertTrue

class ShowcaseSpec : DomSpec {

    override fun getTestId() = "showcase"

    @Test
    fun render() {
        run {
            val element = document.getElementById("showcase")
            assertTrue(element != null, "Element exists")
        }
    }
}

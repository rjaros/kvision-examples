package test.com.example

import com.example.Showcase
import pl.treksoft.kvision.test.DomSpec
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertTrue

class ShowcaseSpec : DomSpec {

    @Test
    fun render() {
        run {
            val showcase = Showcase()
            showcase.start(mapOf())
            val element = document.getElementById("showcase")
            assertTrue(element != null, "Element exists")
        }
    }
}

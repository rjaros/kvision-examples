package test.com.example

import com.example.Helloworld
import io.kvision.i18n.I18n
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test
import kotlin.test.assertTrue

class HelloworldSpec : DomSpec {

    @Test
    fun render() {
        run {
            val helloworld = Helloworld("test")
            I18n.language = "en"
            helloworld.start(mapOf())
            val element = document.getElementById("test")
            assertTrue(
                element?.innerHTML?.contains("Hello world!") ?: false,
                "Application should render Hello world! text"
            )
        }
    }
}

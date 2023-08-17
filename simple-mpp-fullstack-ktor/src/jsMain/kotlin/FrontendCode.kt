package io.ktor.samples.fullstack.mpp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.browser.document

@JsName("app")
fun app() {
    val pingService = PingService()

    GlobalScope.launch {
        document.getElementById("js-response")?.textContent = pingService.ping("Hello World from Client!")
    }
}

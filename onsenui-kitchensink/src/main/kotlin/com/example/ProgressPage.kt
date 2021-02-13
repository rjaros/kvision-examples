package com.example

import kotlinx.browser.window
import io.kvision.core.Color
import io.kvision.core.TextAlign
import io.kvision.core.onEvent
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.html.section
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.form.onsButton
import io.kvision.onsenui.toolbar.toolbar
import io.kvision.onsenui.visual.icon
import io.kvision.onsenui.visual.progressBar
import io.kvision.onsenui.visual.progressCircular
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.utils.px

object ProgressModel {
    val progress = ObservableValue(0)

    fun runCounter() {
        progress.value = 0
        var timer: Int? = null
        timer = window.setInterval({
            if (progress.value == 100) {
                timer?.let { window.clearInterval(it) }
            } else {
                progress.value++
            }
        }, 40)
    }
}

fun Navigator.progressPage() {
    page("progress") {
        toolbar("Progress") {
            left {
                backButton("Home")
            }
        }
        progressBar().bind(ProgressModel.progress) {
            value = it
        }
        section {
            marginTop = 40.px
            marginLeft = 16.px
            marginRight = 16.px
            p().bind(ProgressModel.progress) {
                content = "Progress: $it%"
            }
            p {
                progressBar(20)
            }
            p {
                progressBar(40, 80)
            }
            p {
                progressBar(indeterminate = true)
            }
            div {
                textAlign = TextAlign.CENTER
                margin = 40.px
                color = Color.hex(0x666666)
                p {
                    progressCircular(20)
                    +" "
                    progressCircular(40, 80)
                    +" "
                    progressCircular(indeterminate = true)
                }
                p {
                    icon("ion-ios-refresh", spin = true, size = "30px")
                    +" "
                    icon("ion-ios-refresh-circle", spin = true, size = "30px")
                    +" "
                    icon("ion-ios-sync", spin = true, size = "30px")
                    +" "
                    icon("ion-md-refresh", spin = true, size = "30px")
                    +" "
                    icon("ion-md-refresh-circle", spin = true, size = "30px")
                    +" "
                    icon("ion-md-sync", spin = true, size = "30px")
                }
                p {
                    icon("fa-spinner", spin = true, size = "26px")
                    +" "
                    icon("fa-circle-notch", spin = true, size = "26px")
                }
                p {
                    icon("md-spinner", spin = true, size = "30px")
                }
            }
            p(rich = true) {
                onsButton(large = true) {
                    icon("ion-ios-sync", spin = true, size = "26px")
                }
                +"<br><br>"
                onsButton(large = true, disabled = true) {
                    icon("ion-ios-sync", spin = true, size = "26px")
                }
            }
        }
        onEvent {
            onsShow = {
                ProgressModel.runCounter()
            }
            onsHide = {
                ProgressModel.progress.value = 0
            }
        }
    }
}

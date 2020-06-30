package com.example

import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.TextAlign
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.p
import pl.treksoft.kvision.html.section
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.form.onsButton
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.onsenui.visual.progressBar
import pl.treksoft.kvision.onsenui.visual.progressCircular
import pl.treksoft.kvision.state.ObservableValue
import pl.treksoft.kvision.state.bind
import pl.treksoft.kvision.utils.px
import kotlin.browser.window

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

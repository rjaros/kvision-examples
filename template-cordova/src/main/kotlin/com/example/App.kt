package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.cordova.Camera
import pl.treksoft.kvision.cordova.CameraException
import pl.treksoft.kvision.cordova.CameraOptions
import pl.treksoft.kvision.cordova.File
import pl.treksoft.kvision.cordova.Result
import pl.treksoft.kvision.cordova.failure
import pl.treksoft.kvision.cordova.success
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.image
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.simplePanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.redux.RAction
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.state.stateBinding
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

data class ImageState(val url: String?, val errorMessage: String?)

sealed class ImageAction : RAction {
    data class Image(val url: String) : ImageAction()
    data class Error(val errorMessage: String) : ImageAction()
}

fun imageReducer(state: ImageState, action: ImageAction): ImageState = when (action) {
    is ImageAction.Image -> ImageState(action.url, null)
    is ImageAction.Error -> ImageState(null, action.errorMessage)
}

class App : Application() {
    init {
        require("css/kvapp.css")
    }

    private val store = createReduxStore(::imageReducer, ImageState(null, null))

    override fun start() {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )

        root("kvapp") {
            vPanel(alignItems = FlexAlignItems.STRETCH, spacing = 10) {
                width = 100.perc
                marginTop = 10.px
                button(tr("Take a photo"), "fa-camera") {
                    alignItems = FlexAlignItems.CENTER
                    width = 200.px
                    onClick {
                        GlobalScope.launch {
                            val result = Camera.getPicture(
                                CameraOptions(
                                    mediaType = Camera.MediaType.PICTURE,
                                    destinationType = Camera.DestinationType.FILE_URI
                                )
                            )
                            processCameraResult(result)
                        }
                    }
                }
                simplePanel {
                    margin = 10.px
                }.stateBinding(store) { state ->
                    if (state.errorMessage != null) {
                        div(state.errorMessage)
                    } else if (state.url != null) {
                        image(state.url, centered = true, responsive = true)
                    }
                }
            }
        }
        Camera.addCameraResultCallback {
            processCameraResult(it)
        }

    }

    private fun processCameraResult(result: Result<String, CameraException>) {
        result.success {
            GlobalScope.launch {
                File.resolveLocalFileSystemURLForFile(it).success {
                    store.dispatch(ImageAction.Image(it.toInternalURL()))
                }
            }
        }
        result.failure {
            store.dispatch(ImageAction.Error(it.message ?: tr("No data")))
        }
    }
}

fun main() {
    startApplication(::App)
}

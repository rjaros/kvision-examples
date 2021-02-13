package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.kvision.Application
import io.kvision.cordova.Camera
import io.kvision.cordova.CameraException
import io.kvision.cordova.CameraOptions
import io.kvision.cordova.File
import io.kvision.cordova.Result
import io.kvision.cordova.failure
import io.kvision.cordova.success
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.panel.FlexAlignItems
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.panel.vPanel
import io.kvision.redux.RAction
import io.kvision.redux.createReduxStore
import io.kvision.require
import io.kvision.startApplication
import io.kvision.state.stateBinding
import io.kvision.utils.perc
import io.kvision.utils.px

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
    startApplication(::App, module.hot)
}

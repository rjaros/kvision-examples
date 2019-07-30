package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.cordova.Camera
import pl.treksoft.kvision.cordova.CameraException
import pl.treksoft.kvision.cordova.CameraOptions
import pl.treksoft.kvision.cordova.File
import pl.treksoft.kvision.cordova.Result
import pl.treksoft.kvision.cordova.failure
import pl.treksoft.kvision.cordova.success
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Image.Companion.image
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SimplePanel.Companion.simplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.redux.StateBinding.Companion.stateBinding
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px
import redux.RAction

data class ImageState(val url: String?, val errorMessage: String?)

sealed class ImageAction : RAction {
    data class Image(val url: String) : ImageAction()
    data class Error(val errorMessage: String) : ImageAction()
}

fun imageReducer(state: ImageState, action: ImageAction): ImageState = when (action) {
    is ImageAction.Image -> ImageState(action.url, null)
    is ImageAction.Error -> ImageState(null, action.errorMessage)
}

object App : ApplicationBase {

    private val store = createReduxStore(::imageReducer, ImageState(null, null))

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )

        root = Root("kvapp") {
            vPanel(alignItems = FlexAlignItems.STRETCH, spacing = 10) {
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

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }

    val css = require("css/kvapp.css")
}

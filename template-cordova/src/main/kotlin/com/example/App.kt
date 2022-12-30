package com.example

import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.cordova.Camera
import io.kvision.cordova.CameraOptions
import io.kvision.cordova.File
import io.kvision.cordova.readAsDataURL
import io.kvision.core.AlignItems
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.panel.vPanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.state.bind
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class ImageState(val url: String?, val errorMessage: String?)

sealed class ImageAction {
    data class Image(val url: String) : ImageAction()
    data class Error(val errorMessage: String) : ImageAction()
}

fun imageReducer(state: ImageState, action: ImageAction): ImageState = when (action) {
    is ImageAction.Image -> ImageState(action.url, null)
    is ImageAction.Error -> ImageState(null, action.errorMessage)
}


class App : Application() {

    val AppScope = CoroutineScope(window.asCoroutineDispatcher())

    init {
        require("css/kvapp.css")
    }

    val actionFlow = MutableSharedFlow<ImageAction>()

    override fun start() {
        val stateFlow = MutableStateFlow(ImageState(null, null))
        AppScope.launch {
            actionFlow.collect {
                stateFlow.value = imageReducer(stateFlow.value, it)
            }
        }

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )

        root("kvapp") {
            vPanel(alignItems = AlignItems.STRETCH, spacing = 10) {
                width = 100.perc
                marginTop = 10.px
                button(tr("Take a photo"), "fa-camera") {
                    alignItems = AlignItems.CENTER
                    width = 200.px
                    onClickLaunch {
                        val result = Camera.getPicture(
                            CameraOptions(
                                mediaType = Camera.MediaType.PICTURE,
                                destinationType = Camera.DestinationType.FILE_URI
                            )
                        )
                        processCameraResult(result)
                    }
                }
                simplePanel {
                    margin = 10.px
                }.bind(stateFlow) { state ->
                    if (state.errorMessage != null) {
                        div(state.errorMessage)
                    } else if (state.url != null) {
                        image(state.url, centered = true, responsive = true)
                    }
                }
            }
        }
        Camera.addCameraResultCallback {
            AppScope.launch {
                processCameraResult(it)
            }
        }

    }

    private suspend fun processCameraResult(result: Result<String>) {
        result.fold({
            File.resolveLocalFileSystemURLForFile(it).getOrNull()?.let {
                it.readAsDataURL().getOrNull()?.let {
                    actionFlow.emit(ImageAction.Image(it))
                }
            }
        }, {
            actionFlow.emit(ImageAction.Error(it.message ?: tr("No data")))
        })
    }
}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

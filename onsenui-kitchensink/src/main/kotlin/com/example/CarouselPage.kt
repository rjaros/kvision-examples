package com.example

import io.kvision.core.Background
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.onClick
import io.kvision.core.onEvent
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.onsenui.carousel.carousel
import io.kvision.onsenui.carousel.carouselItem
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.toolbar.toolbar
import io.kvision.state.ObservableValue
import io.kvision.state.bind

object CarouselModel {
    val carouselColors = mapOf(
        "gray" to Color.name(Col.GRAY),
        "blue" to Color.hex(0x085078),
        "dark" to Color.hex(0x373B44),
        "orange" to Color.hex(0xD38312)
    )
    val currentIndex = ObservableValue(0)
}

fun Navigator.carouselPage() {
    page("carousel") {
        toolbar("Carousel") {
            left {
                backButton("Home")
            }
        }
        val carousel = carousel(
            fullscreen = true,
            swipeable = true,
            overscrollable = true,
            autoScroll = true
        ) {
            CarouselModel.carouselColors.forEach {
                carouselItem(className = "carousel-item") {
                    background = Background(it.value)
                    div(it.key, className = "color-tag")
                }
            }
            onEvent {
                onsPostchange = {
                    CarouselModel.currentIndex.value = self.getActiveIndex().toInt()
                }
            }
        }
        div(className = "dots").bind(CarouselModel.currentIndex) {
            for (i in CarouselModel.carouselColors.keys.indices) {
                span(if (i == it) "\u25CF" else "\u25CB").onClick {
                    carousel.setActiveIndex(i)
                }
            }
        }
        onEvent {
            onsHide = {
                CarouselModel.currentIndex.value = 0
            }
        }
    }
}

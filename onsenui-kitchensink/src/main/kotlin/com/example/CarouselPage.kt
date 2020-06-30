package com.example

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.onClick
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.onsenui.carousel.carousel
import pl.treksoft.kvision.onsenui.carousel.carouselItem
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.state.ObservableValue
import pl.treksoft.kvision.state.bind

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

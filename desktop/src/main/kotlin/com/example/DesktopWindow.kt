/*
 * Copyright (c) 2018. Robert Jaros
 */

package com.example

import com.example.App.Companion.addTask
import com.example.App.Companion.removeTask
import pl.treksoft.kvision.core.Component
import pl.treksoft.kvision.core.CssSize
import pl.treksoft.kvision.core.UNIT
import pl.treksoft.kvision.core.Widget
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.utils.vh
import pl.treksoft.kvision.utils.vw
import pl.treksoft.kvision.window.Window
import kotlin.random.Random

open class DesktopWindow(caption: String, icon: String, width: Int, height: Int) :
    Window(
        caption,
        width.px,
        height.px,
        closeButton = true,
        maximizeButton = true,
        minimizeButton = true,
        icon = icon
    ) {

    override var top: CssSize?
        get() = super.top
        set(value) {
            if (maximized || value?.first?.toInt() ?: 0 > 50 && value?.second == UNIT.px) {
                super.top = value
            }
        }

    val task: Component
    var storedWidth: CssSize? = null
    var storedHeight: CssSize? = null
    var storedTop: CssSize? = null
    var storedLeft: CssSize? = null
    var maximized: Boolean = false
    var minimized: Boolean = false

    init {
        left = ((Random.nextDouble() * 800).toInt()).px
        top = (51 + (Random.nextDouble() * 100).toInt()).px
        task = addTask(this)
    }

    override fun hide(): Widget {
        super.hide()
        removeTask(task)
        this.dispose()
        return this
    }

    override fun toggleMaximize() {
        if (!maximized) {
            maximized = true
            storedWidth = width
            storedHeight = height
            storedTop = top
            storedLeft = left
            top = 0.px
            left = 0.px
            height = 100.vh
            width = 100.vw
            height = 100.vh
            zIndex = zIndex?.plus(10000)
        } else {
            width = storedWidth
            height = storedHeight
            top = storedTop
            left = storedLeft
            maximized = false
            zIndex = zIndex?.minus(10000)
        }
    }

    override fun toggleMinimize() {
        if (!minimized) {
            visible = false
            minimized = true
        } else {
            visible = true
            minimized = false
        }
    }

}

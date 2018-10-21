/*
 * Copyright (c) 2018. Robert Jaros
 */

package com.example

import org.w3c.dom.CanvasRenderingContext2D
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Canvas
import pl.treksoft.kvision.panel.DockPanel.Companion.dockPanel
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.Side
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px
import kotlin.math.PI
import kotlin.math.abs

class Paint : DesktopWindow("Paint", 600, 400) {

    lateinit var buttonPoint: Button
    lateinit var buttonPencil: Button
    lateinit var buttonLine: Button
    lateinit var buttonRectangle: Button
    lateinit var buttonCircle: Button

    init {
        dockPanel {
            height = 100.perc
            val canvas = PaintCanvas(510, 320)
            add(canvas, Side.CENTER)
            val buttons = VPanel(spacing = 5) {
                width = 80.px
                height = 100.perc
                alignItems = FlexAlignItems.CENTER
                paddingTop = 5.px
                buttonPoint = button("", "fa-circle") {
                    title = "Point"
                    onClick {
                        canvas.selectedTool = Tool.POINT
                        selectTool(this)
                    }
                }
                buttonPencil = button("", "fa-pencil") {
                    title = "Pencil"
                    onClick {
                        canvas.selectedTool = Tool.PENCIL
                        selectTool(this)
                    }
                    this.border = Border(1.px, BorderStyle.SOLID, Col.BLUE)
                }
                buttonLine = button("", "fa-minus") {
                    title = "Line"
                    onClick {
                        canvas.selectedTool = Tool.LINE
                        selectTool(this)
                    }
                }
                buttonRectangle = button("", "fa-square-o") {
                    title = "Rectangle"
                    onClick {
                        canvas.selectedTool = Tool.RECTANGLE
                        selectTool(this)
                    }
                }
                buttonCircle = button("", "fa-circle-thin") {
                    title = "Circle"
                    onClick {
                        canvas.selectedTool = Tool.CIRCLE
                        selectTool(this)
                    }
                }
            }
            add(buttons, Side.LEFT)
            val colors = HPanel {
                height = 60.px
                width = 100.perc
            }
            add(colors, Side.DOWN)
            this@Paint.setEventListener<Paint> {
                resizeWindow = { e ->
                    canvas.canvasWidth = e.detail.width - 90
                    canvas.canvasHeight = e.detail.height - 131
                    canvas.redraw()
                }
            }
        }
    }

    fun selectTool(button: Button) {
        buttonPoint.border = null
        buttonPencil.border = null
        buttonLine.border = null
        buttonRectangle.border = null
        buttonCircle.border = null
        button.border = Border(1.px, BorderStyle.SOLID, Col.BLUE)
    }

    companion object {
        fun run(container: Container) {
            container.add(Paint())
        }
    }
}

class PaintCanvas(width: Int, height: Int) : Canvas(width, height) {

    var selectedTool = Tool.PENCIL
    private var drawing = mutableListOf<Fig>()
    private var currentFig: Fig? = null

    init {
        border = Border(1.px, BorderStyle.SOLID, Col.BLACK)

        setEventListener {
            mousedown = { e ->
                if (currentFig == null) handleMouseDown(e.offsetX.toInt(), e.offsetY.toInt())
            }
            mousemove = { e ->
                currentFig?.let { handleMouseMove(e.offsetX.toInt(), e.offsetY.toInt(), it) }
            }
            mouseup = { _ ->
                currentFig?.let { handleMouseUp(it) }
            }
        }
    }

    private fun handleMouseDown(x: Int, y: Int) {
        when (selectedTool) {
            Tool.POINT -> {
                currentFig = Point(x, y)
                context2D.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
            }
            Tool.PENCIL -> {
                currentFig = Pencil(x, y)
                context2D.beginPath()
                context2D.moveTo(x.toDouble(), y.toDouble())
            }
            Tool.LINE -> {
                currentFig = Line(x, y)
            }
            Tool.RECTANGLE -> {
                currentFig = Rectangle(x, y)
            }
            Tool.CIRCLE -> {
                currentFig = Circle(x, y)
            }
        }
    }

    private fun handleMouseMove(x: Int, y: Int, fig: Fig) {
        when (fig) {
            is Pencil -> {
                context2D.lineTo(x.toDouble(), y.toDouble())
                context2D.stroke()
                fig.addPoint(x, y)
            }
            is Line -> {
                redraw()
                context2D.beginPath()
                context2D.moveTo(fig.startX.toDouble(), fig.startY.toDouble())
                context2D.lineTo(x.toDouble(), y.toDouble())
                context2D.stroke()
                fig.endOfLine = Pair(x, y)
            }
            is Rectangle -> {
                redraw()
                context2D.strokeRect(
                    fig.startX.toDouble(),
                    fig.startY.toDouble(),
                    (x - fig.startX).toDouble(),
                    (y - fig.startY).toDouble()
                )
                fig.endOfRect = Pair(x, y)
            }
            is Circle -> {
                redraw()
                context2D.beginPath()
                context2D.ellipse(
                    fig.startX.toDouble(),
                    fig.startY.toDouble(),
                    abs((x - fig.startX).toDouble()),
                    abs((y - fig.startY).toDouble()), 0.0, 0.0, 2 * PI
                )
                context2D.stroke()
                fig.endOfCircle = Pair(x, y)
            }
        }
    }

    private fun handleMouseUp(fig: Fig) {
        drawing.add(fig)
        currentFig = null
    }

    fun redraw() {
        context2D.clearRect(
            0.toDouble(),
            0.toDouble(),
            (canvasWidth ?: 0).toDouble(),
            (canvasHeight ?: 0).toDouble()
        )
        drawing.forEach { it.draw(context2D) }
    }
}

enum class Tool {
    POINT,
    PENCIL,
    LINE,
    RECTANGLE,
    CIRCLE
}

abstract class Fig {
    abstract fun draw(context: CanvasRenderingContext2D)
}

class Point(val x: Int, val y: Int) : Fig() {
    override fun draw(context: CanvasRenderingContext2D) {
        context.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
    }
}

class Pencil(val startX: Int, val startY: Int) : Fig() {
    private val points = mutableListOf<Pair<Int, Int>>()

    fun addPoint(x: Int, y: Int) {
        points.add(Pair(x, y))
    }

    override fun draw(context: CanvasRenderingContext2D) {
        context.beginPath()
        context.moveTo(startX.toDouble(), startY.toDouble())
        points.forEach {
            context.lineTo(it.first.toDouble(), it.second.toDouble())
            context.stroke()
        }
    }
}

class Line(val startX: Int, val startY: Int) : Fig() {
    var endOfLine: Pair<Int, Int>? = null

    override fun draw(context: CanvasRenderingContext2D) {
        endOfLine?.let {
            context.beginPath()
            context.moveTo(startX.toDouble(), startY.toDouble())
            context.lineTo(it.first.toDouble(), it.second.toDouble())
            context.stroke()
        }
    }
}

class Rectangle(val startX: Int, val startY: Int) : Fig() {
    var endOfRect: Pair<Int, Int>? = null

    override fun draw(context: CanvasRenderingContext2D) {
        endOfRect?.let {
            context.strokeRect(
                startX.toDouble(),
                startY.toDouble(),
                (it.first - startX).toDouble(),
                (it.second - startY).toDouble()
            )
        }
    }
}

class Circle(val startX: Int, val startY: Int) : Fig() {
    var endOfCircle: Pair<Int, Int>? = null

    override fun draw(context: CanvasRenderingContext2D) {
        endOfCircle?.let {
            context.beginPath()
            context.ellipse(
                startX.toDouble(),
                startY.toDouble(),
                abs((it.first - startX).toDouble()),
                abs((it.second - startY).toDouble()), 0.0, 0.0, 2 * PI
            )
            context.stroke()
        }
    }
}

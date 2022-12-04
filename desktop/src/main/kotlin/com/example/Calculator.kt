package com.example

import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Container
import io.kvision.core.JustifyItems
import io.kvision.html.Align
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.panel.gridPanel
import io.kvision.utils.px

enum class Operator {
    PLUS,
    MINUS,
    DIVIDE,
    MULTIPLY
}

class Calculator : DesktopWindow("Calculator", "fas fa-calculator", 280, 290) {

    val inputDiv: Div
    var input: String = "0"
    var cleared = true
    var isDivider = false

    var first: Double = 0.0
    var operator: Operator? = null

    init {
        isResizable = false
        maximizeButton = false
        minimizeButton = false
        inputDiv = div("0", align = Align.RIGHT) {
            padding = 5.px
            marginTop = 15.px
            marginLeft = 15.px
            marginRight = 15.px
            border = Border(2.px, BorderStyle.SOLID)
        }
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = JustifyItems.CENTER) {
            padding = 10.px
            add(CalcButton("AC").apply { onClick { this@Calculator.clear() } }, 4, 1)
            add(CalcButton("7").apply { onClick { this@Calculator.number(7) } }, 1, 2)
            add(CalcButton("8").apply { onClick { this@Calculator.number(8) } }, 2, 2)
            add(CalcButton("9").apply { onClick { this@Calculator.number(9) } }, 3, 2)
            add(CalcButton("4").apply { onClick { this@Calculator.number(4) } }, 1, 3)
            add(CalcButton("5").apply { onClick { this@Calculator.number(5) } }, 2, 3)
            add(CalcButton("6").apply { onClick { this@Calculator.number(6) } }, 3, 3)
            add(CalcButton("1").apply { onClick { this@Calculator.number(1) } }, 1, 4)
            add(CalcButton("2").apply { onClick { this@Calculator.number(2) } }, 2, 4)
            add(CalcButton("3").apply { onClick { this@Calculator.number(3) } }, 3, 4)
            add(CalcButton("0").apply { onClick { this@Calculator.number(0) } }, 1, 5)
            add(CalcButton(".").apply { onClick { this@Calculator.divider() } }, 2, 5)
            add(CalcButton("=").apply { onClick { this@Calculator.calculate() } }, 3, 5)
            add(CalcButton("/").apply { onClick { this@Calculator.operator(Operator.DIVIDE) } }, 4, 2)
            add(CalcButton("*").apply { onClick { this@Calculator.operator(Operator.MULTIPLY) } }, 4, 3)
            add(CalcButton("-").apply { onClick { this@Calculator.operator(Operator.MINUS) } }, 4, 4)
            add(CalcButton("+").apply { onClick { this@Calculator.operator(Operator.PLUS) } }, 4, 5)
        }
    }

    private fun clear() {
        input = "0"
        cleared = true
        isDivider = false
        first = 0.0
        operator = null
        printInput()
    }

    private fun number(num: Int) {
        if (input == "0" || cleared) {
            input = "$num"
        } else {
            input += "$num"
        }
        cleared = false
        printInput()
    }

    private fun divider() {
        if (!isDivider) {
            if (input == "0" || cleared) {
                input = "0."
            } else {
                input += "."
            }
            isDivider = true
        }
        cleared = false
        printInput()
    }

    private fun operator(op: Operator) {
        if (operator != null) {
            calculate()
        }
        first = input.toDouble()
        operator = op
        cleared = true
        isDivider = false
    }

    private fun calculate() {
        val second = input.toDouble()
        val result = when (operator) {
            Operator.PLUS -> first + second
            Operator.MINUS -> first - second
            Operator.MULTIPLY -> first * second
            Operator.DIVIDE -> first / second
            else -> input.toDouble()
        }
        input = result.toString()
        printInput()
        cleared = true
        operator = null
        isDivider = false
    }

    private fun printInput() {
        inputDiv.content = "$input"
    }

    companion object {
        fun run(container: Container) {
            container.add(Calculator())
        }
    }
}

class CalcButton(label: String) : Button(label, style = ButtonStyle.SECONDARY) {
    init {
        width = 50.px
    }
}

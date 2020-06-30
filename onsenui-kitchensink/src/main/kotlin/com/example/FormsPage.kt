package com.example

import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.label
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.html.tag
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.form.onsCheckBoxInput
import pl.treksoft.kvision.onsenui.form.onsRadioInput
import pl.treksoft.kvision.onsenui.form.onsRangeInput
import pl.treksoft.kvision.onsenui.form.onsSelectInput
import pl.treksoft.kvision.onsenui.form.onsSwitchInput
import pl.treksoft.kvision.onsenui.form.onsTextInput
import pl.treksoft.kvision.onsenui.grid.col
import pl.treksoft.kvision.onsenui.grid.row
import pl.treksoft.kvision.onsenui.list.DividerType
import pl.treksoft.kvision.onsenui.list.header
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.tabbar.Tab
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.state.ObservableValue
import pl.treksoft.kvision.state.bind
import pl.treksoft.kvision.state.observableSetOf
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

object FormsModel {
    val textValue = ObservableValue<String?>(null)
    val volume = ObservableValue(25)
    val switch1 = ObservableValue(true)
    val selectOptions = listOf("KVision" to "KVision", "Vue" to "Vue", "React" to "React")
    val select = ObservableValue<String?>("KVision")
    val vegetables = listOf("Apples", "Bananas", "Oranges")
    val selectedVegetable = ObservableValue("Bananas")
    val colors = listOf("Red", "Green", "Blue")
    val selectedColors = observableSetOf("Green", "Blue")
}

fun Tab.formsPage(app: App) {
    page {
        onsList {
            header("Text input")
            item(divider = if (app.isMD) DividerType.NONE else null) {
                left {
                    icon("md-face", className = "list-item__icon")
                }
                center {
                    onsTextInput(placeholder = "Name", floatLabel = true) {
                        maxlength = 20
                        bind(FormsModel.textValue) {
                            value = it
                        }
                        onEvent {
                            input = {
                                FormsModel.textValue.value = self.value
                            }
                        }
                    }
                }
            }
            item(divider = if (app.isMD) DividerType.NONE else null) {
                left {
                    icon("fa-question-circle", className = "far list-item__icon")
                }
                center {
                    onsTextInput(TextInputType.SEARCH, placeholder = "Search") {
                        maxlength = 20
                        bind(FormsModel.textValue) {
                            value = it
                        }
                        onEvent {
                            input = {
                                FormsModel.textValue.value = self.value
                            }
                        }
                    }
                }
            }
            item {
                right {
                    addCssClass("right-label")
                    bind(FormsModel.textValue) {
                        +"Hello ${it ?: "anonymous"}! "
                        icon("fa-hand-spock", size = "lg", className = "far right-icon")
                    }
                }
            }
            header("Range slider")
            item {
                +"Adjust the volume:"
                row {
                    col(align = Align.CENTER, colWidth = 40.px) {
                        lineHeight = 31.px
                        icon("md-volume-down")
                    }
                    col {
                        onsRangeInput(value = FormsModel.volume.value) {
                            width = 100.perc
                            onEvent {
                                input = {
                                    FormsModel.volume.value = self.value?.toInt() ?: 0
                                }
                            }
                        }
                    }
                    col(align = Align.CENTER, colWidth = 40.px) {
                        lineHeight = 31.px
                        icon("md-volume-up")
                    }
                }
                span().bind(FormsModel.volume) {
                    content = "Volume: $it" + if (it > 80) " (careful, that's loud)" else ""
                }
            }
            header("Switches")
            item {
                center {
                    label(forId = "switch1").bind(FormsModel.switch1) {
                        width = 100.perc
                        content = "Switch (" + (if (it) "on" else "off") + ")"
                    }
                }
                right {
                    onsSwitchInput(FormsModel.switch1.value, "switch1").onClick {
                        FormsModel.switch1.value = this.value
                    }
                }
            }
            item {
                center {
                    label(forId = "switch2").bind(FormsModel.switch1) {
                        width = 100.perc
                        content = if (it) "Enabled switch" else "Disabled switch"
                    }
                }
                right {
                    onsSwitchInput(inputId = "switch2").bind(FormsModel.switch1) {
                        disabled = !it
                    }
                }
            }
            header("Select")
            item {
                onsSelectInput(FormsModel.selectOptions, FormsModel.select.value) {
                    width = 120.px
                    onEvent {
                        change = {
                            FormsModel.select.value = self.value
                        }
                    }
                }
                right {
                    addCssClass("right-label")
                    rich = true
                    bind(FormsModel.select) {
                        if (it != "KVision") tag(TAG.S, it)
                        +"&nbsp;KVision is awesome!"
                    }
                }
            }
            header("Radio buttons")
            FormsModel.vegetables.forEachIndexed { index, vegetable ->
                item(
                    tappable = true,
                    divider = if (index == FormsModel.vegetables.size - 1) DividerType.LONG else null
                ) {
                    left {
                        onsRadioInput(FormsModel.selectedVegetable.value == vegetable, inputId = "radio-$index") {
                            name = "vegetables"
                            onClick {
                                if (this.value) FormsModel.selectedVegetable.value = vegetable
                            }
                        }
                    }
                    center {
                        label(vegetable, forId = "radio-$index") {
                            width = 100.perc
                        }
                    }
                }
            }
            item().bind(FormsModel.selectedVegetable) {
                content = "I love $it!"
            }
            header().bind(FormsModel.selectedColors) {
                content = "Checkboxes - [${it.joinToString(", ")}]"
            }
            FormsModel.colors.forEachIndexed { index, color ->
                item(tappable = true) {
                    left {
                        onsCheckBoxInput(FormsModel.selectedColors.contains(color), inputId = "checkbox-$index") {
                            onClick {
                                if (this.value)
                                    FormsModel.selectedColors.add(color)
                                else FormsModel.selectedColors.remove(color)
                            }
                        }
                    }
                    center {
                        label(color, forId = "checkbox-$index") {
                            width = 100.perc
                        }
                    }
                }
            }
        }
    }
}

package com.example

import io.kvision.core.FlexWrap
import io.kvision.core.FontWeight
import io.kvision.core.onEvent
import io.kvision.form.check.CheckStyle
import io.kvision.form.check.checkBox
import io.kvision.form.text.text
import io.kvision.form.text.textInput
import io.kvision.html.ButtonStyle
import io.kvision.html.InputType
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.i18n.I18n.trans
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.utils.px

class DataTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        vPanel(spacing = 30) {

            val store = object : ObservableValue<String>("") {
                fun update(v: String) {
                    if (value != v) {
                        value = v
                    }
                }

                fun addDot() {
                    value += "."
                }
            }
            div {
                text(label = tr("Input")).bind(store) {
                    placeholder = tr("Add some input")
                    value = it
                }.subscribe { store.update(it ?: "") }
                text(label = tr("Value")).bind(store) {
                    readonly = true
                    value = it
                }
                div(className = "form-group mb-3") {
                    button(tr("Add a dot")).onClick { store.addDot() }
                }
            }

            val panel = vPanel(spacing = 5, useWrappers = true)

            data class DataModel(val checked: Boolean, val text: String)
            data class DataState(val list: List<DataModel>, var searchFilter: String?)

            val dataState = ObservableValue(
                DataState(
                    listOf(
                        DataModel(false, tr("January")),
                        DataModel(false, tr("February")),
                        DataModel(false, tr("March")),
                        DataModel(false, tr("April")),
                        DataModel(false, tr("May")),
                        DataModel(false, tr("June")),
                        DataModel(false, tr("July")),
                        DataModel(false, tr("August")),
                        DataModel(false, tr("September")),
                        DataModel(false, tr("October")),
                        DataModel(false, tr("November"))
                    ), null
                )
            )

            val dataContainer = HPanel(spacing = 10, wrap = FlexWrap.WRAP).bind(dataState) { state ->
                state.list.filter { model ->
                    state.searchFilter?.let {
                        trans(model.text).contains(it, ignoreCase = true)
                    } ?: true
                }.forEach { model ->
                    checkBox(
                        value = model.checked,
                        label = model.text
                    ) {
                        flabel.fontWeight = if (model.checked) FontWeight.BOLD else null
                        style = CheckStyle.PRIMARY
                        onClick {
                            val idx = state.list.indexOf(model)
                            dataState.value = dataState.value.copy(
                                list = state.list.mapIndexed { index, dataModel -> if (index == idx) dataModel.copy(checked = this.value) else dataModel }
                            )
                        }
                    }
                }
            }
            panel.add(dataContainer)

            panel.add(HPanel(spacing = 10, wrap = FlexWrap.WRAP) {
                textInput(type = InputType.SEARCH) {
                    width = 200.px
                    placeholder = tr("Search ...")
                    onEvent {
                        input = {
                            dataState.value = dataState.value.copy(searchFilter = self.value)
                        }
                    }
                }
                button(tr("Add december"), style = ButtonStyle.SUCCESS).onClick {
                    dataState.value = dataState.value.copy(list = dataState.value.list + DataModel(true, tr("December")))
                }
                button(tr("Check all"), style = ButtonStyle.INFO).onClick {
                    dataState.value = dataState.value.copy(list = dataState.value.list.map { it.copy(checked = true) })
                }
                button(tr("Uncheck all"), style = ButtonStyle.INFO).onClick {
                    dataState.value = dataState.value.copy(list = dataState.value.list.map { it.copy(checked = false) })
                }
                button(tr("Reverse list"), style = ButtonStyle.DANGER).onClick {
                    dataState.value = dataState.value.copy(list = dataState.value.list.reversed())
                }
                button(tr("Remove checked"), style = ButtonStyle.DANGER).onClick {
                    dataState.value = dataState.value.copy(list = dataState.value.list.filter { !it.checked })
                }
            })
        }
    }
}

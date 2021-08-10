package com.example

import io.kvision.core.FlexWrap
import io.kvision.core.FontWeight
import io.kvision.core.onEvent
import io.kvision.data.BaseDataComponent
import io.kvision.data.DataContainer
import io.kvision.form.check.CheckBox
import io.kvision.form.check.CheckBoxStyle
import io.kvision.form.text.TextInputType
import io.kvision.form.text.text
import io.kvision.form.text.textInput
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.i18n.I18n.trans
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.state.observableListOf
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

            class DataModel(checked: Boolean, text: String) : BaseDataComponent() {
                var checked: Boolean by obs(checked)
                var text: String by obs(text)
            }

            val list = observableListOf(
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
            )

            var searchFilter: String? = null

            val dataContainer = DataContainer(list, { model, _, _ ->
                CheckBox(
                    value = model.checked,
                    label = model.text
                ).apply {
                    flabel.fontWeight = if (model.checked) FontWeight.BOLD else null
                    style = CheckBoxStyle.PRIMARY
                    onClick {
                        model.checked = this.value
                    }
                }
            }, filter = { model ->
                searchFilter?.let {
                    trans(model.text).contains(it, ignoreCase = true)
                } ?: true
            }, container = HPanel(spacing = 10, wrap = FlexWrap.WRAP))
            panel.add(dataContainer)

            panel.add(HPanel(spacing = 10, wrap = FlexWrap.WRAP) {
                textInput(type = TextInputType.SEARCH) {
                    width = 200.px
                    placeholder = tr("Search ...")
                    onEvent {
                        input = {
                            searchFilter = self.value
                            dataContainer.update()
                        }
                    }
                }
                button(tr("Add december"), style = ButtonStyle.SUCCESS).onClick {
                    list.add(DataModel(true, tr("December")))
                }
                button(tr("Check all"), style = ButtonStyle.INFO).onClick {
                    list.forEach { it.checked = true }
                }
                button(tr("Uncheck all"), style = ButtonStyle.INFO).onClick {
                    list.forEach { it.checked = false }
                }
                button(tr("Reverse list"), style = ButtonStyle.DANGER).onClick {
                    list.reverse()
                }
                button(tr("Remove checked"), style = ButtonStyle.DANGER).onClick {
                    list.filter { it.checked }.forEach { list.remove(it) }
                }
            })
        }
    }
}

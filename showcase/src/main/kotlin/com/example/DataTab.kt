package com.example

import com.lightningkite.kotlin.observable.list.observableListOf
import pl.treksoft.kvision.data.BaseDataComponent
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.check.CheckBoxStyle
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

class DataTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        val panel = vPanel(spacing = 5)

        class DataModel(checked: Boolean, text: String) : BaseDataComponent() {
            var checked: Boolean by obs(checked)
            var text: String by obs(text)
        }

        val list = observableListOf(
            DataModel(false, "January"),
            DataModel(false, "February"),
            DataModel(false, "March"),
            DataModel(false, "April"),
            DataModel(false, "May"),
            DataModel(false, "June"),
            DataModel(false, "July"),
            DataModel(false, "August"),
            DataModel(false, "September"),
            DataModel(false, "October"),
            DataModel(false, "November")
        )

        var searchFilter: String? = null

        val dataContainer = DataContainer(list, { index ->
            CheckBox(
                value = list[index].checked,
                label = if (list[index].checked) "<b>${list[index].text}</b>" else "${list[index].text}"
            ).apply {
                rich = true
                style = CheckBoxStyle.PRIMARY
                onClick {
                    list[index].checked = this.value
                }
            }
        }, filter = { index ->
            searchFilter?.let {
                list[index].text.contains(it, ignoreCase = true)
            } ?: true
        }, child = HPanel(spacing = 10, wrap = FlexWrap.WRAP))
        panel.add(dataContainer)

        panel.add(HPanel(spacing = 10, wrap = FlexWrap.WRAP) {
            textInput(type = TextInputType.SEARCH) {
                placeholder = "Search ..."
                setEventListener<TextInput> {
                    input = {
                        searchFilter = self.value
                        dataContainer.update()
                    }
                }
            }
            button("Add December", style = ButtonStyle.SUCCESS).onClick {
                list.add(DataModel(true, "December"))
            }
            button("Check all", style = ButtonStyle.INFO).onClick {
                list.forEach { it.checked = true }
            }
            button("Uncheck all", style = ButtonStyle.INFO).onClick {
                list.forEach { it.checked = false }
            }
            button("Reverse list", style = ButtonStyle.DANGER).onClick {
                list.reverse()
            }
            button("Remove checked", style = ButtonStyle.DANGER).onClick {
                list.filter { it.checked }.forEach { list.remove(it) }
            }
        })
    }
}

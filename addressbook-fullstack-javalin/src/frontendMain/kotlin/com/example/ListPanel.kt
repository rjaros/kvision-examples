package com.example

import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.data.dataContainer
import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.check.radioGroup
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.link
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.table.cell
import pl.treksoft.kvision.table.HeaderCell
import pl.treksoft.kvision.table.Row
import pl.treksoft.kvision.table.Table
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.utils.px

object ListPanel : SimplePanel() {

    init {
        padding = 10.px

        val table = Table(types = setOf(TableType.STRIPED, TableType.HOVER)) {
            addHeaderCell(sortingHeaderCell(tr("First name"), Sort.FN))
            addHeaderCell(sortingHeaderCell(tr("Last name"), Sort.LN))
            addHeaderCell(sortingHeaderCell(tr("E-mail"), Sort.E))
            addHeaderCell(sortingHeaderCell("", Sort.F))
            addHeaderCell(HeaderCell(""))
        }

        hPanel(FlexWrap.WRAP, alignItems = FlexAlignItems.CENTER, spacing = 20) {
            textInput(TextInputType.SEARCH) {
                placeholder = "${tr("Search")} ..."
                setEventListener<TextInput> {
                    input = {
                        Model.search = self.value
                    }
                }
            }
            radioGroup(listOf("all" to tr("All"), "fav" to tr("Favourites")), "all", inline = true) {
                marginBottom = 0.px
                setEventListener<RadioGroup> {
                    change = {
                        Model.types = self.value ?: "all"
                    }
                }
            }
        }

        dataContainer(
            Model.addresses, { address, index, _ ->
                Row {
                    cell(address.firstName)
                    cell(address.lastName)
                    cell {
                        address.email?.let {
                            link(it, "mailto:$it") {
                                fontStyle = FontStyle.ITALIC
                            }
                        }
                    }
                    cell {
                        address.favourite?.let {
                            if (it) icon("far fa-heart") {
                                title = tr("Favourite")
                            }
                        }
                    }
                    cell {
                        icon("fas fa-times") {
                            title = tr("Delete")
                            onEvent {
                                click = { e ->
                                    e.stopPropagation()
                                    Confirm.show("Are you sure?", "Do you want to delete this address?") {
                                        EditPanel.delete(index)
                                    }
                                }
                            }
                        }
                    }
                    onEvent {
                        click = {
                            EditPanel.edit(index)
                        }
                    }
                }
            }, container = table
        )
    }

    private fun sortingHeaderCell(title: String, sort: Sort) = HeaderCell(title) {
        onEvent {
            click = {
                Model.sort = sort
            }
        }
    }
}

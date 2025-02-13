package com.example

import io.kvision.core.AlignItems
import io.kvision.core.FontStyle
import io.kvision.core.onEvent
import io.kvision.form.check.RadioGroup
import io.kvision.form.check.radioGroup
import io.kvision.form.text.TextInput
import io.kvision.form.text.text
import io.kvision.html.InputType
import io.kvision.html.icon
import io.kvision.html.link
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Confirm
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.bind
import io.kvision.table.HeaderCell
import io.kvision.table.TableType
import io.kvision.table.cell
import io.kvision.table.row
import io.kvision.table.table
import io.kvision.utils.px

object ListPanel : SimplePanel() {

    init {
        padding = 5.px

        hPanel(alignItems = AlignItems.CENTER, spacing = 20) {
            text(InputType.SEARCH) {
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

        table(types = setOf(TableType.STRIPED, TableType.HOVER)) {
            addHeaderCell(this@ListPanel.sortingHeaderCell(tr("First name"), Sort.FN))
            addHeaderCell(this@ListPanel.sortingHeaderCell(tr("Last name"), Sort.LN))
            addHeaderCell(this@ListPanel.sortingHeaderCell(tr("E-mail"), Sort.E))
            addHeaderCell(this@ListPanel.sortingHeaderCell("", Sort.F))
            addHeaderCell(HeaderCell(""))
            bind(Model.addresses) { addresses ->
                addresses.forEachIndexed { index, address ->
                    row {
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
                }
            }
        }
    }

    private fun sortingHeaderCell(title: String, sort: Sort) = HeaderCell(title) {
        onEvent {
            click = {
                Model.sort = sort
            }
        }
    }
}

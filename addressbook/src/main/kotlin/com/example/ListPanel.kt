package com.example

import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.FlexWrap
import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.core.onClick
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.check.radioGroup
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.form.text.text
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.link
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.simplePanel
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.table.cell
import pl.treksoft.kvision.table.headerCell
import pl.treksoft.kvision.table.row
import pl.treksoft.kvision.table.table
import pl.treksoft.kvision.utils.px

fun Container.listPanel() {
    simplePanel {
        padding = 5.px

        hPanel(FlexWrap.WRAP, alignItems = AlignItems.CENTER, spacing = 20) {
            text(TextInputType.SEARCH) {
                placeholder = tr("Search ...")
                onEvent {
                    input = {
                        Model.setSearch(self.value)
                    }
                }
            }
            radioGroup(
                listOf(Filter.ALL.name to tr("All"), Filter.FAVOURITE.name to tr("Favourites")),
                Filter.ALL.name,
                inline = true
            ).onEvent {
                change = {
                    Model.setFilter(Filter.valueOf(self.value!!))
                }
            }
        }

        table(Model.addressBook, types = setOf(TableType.STRIPED, TableType.HOVER)) { state ->
            headerCell(tr("First name")).onClick {
                Model.setSort(Sort.FN)
            }
            headerCell(tr("Last name")).onClick {
                Model.setSort(Sort.LN)
            }
            headerCell(tr("E-mail")).onClick {
                Model.setSort(Sort.E)
            }
            headerCell("").onClick {
                Model.setSort(Sort.F)
            }
            headerCell("")
            state.addresses.mapIndexed { index, address -> index to address }
                .filter {
                    it.second.match(state.search) && (state.filter == Filter.ALL || it.second.favourite ?: false)
                }.sortedBy {
                    when (state.sort) {
                        Sort.FN -> it.second.firstName?.toLowerCase()
                        Sort.LN -> it.second.lastName?.toLowerCase()
                        Sort.E -> it.second.email?.toLowerCase()
                        Sort.F -> it.second.favourite.toString()
                    }
                }.forEach { (index, address) ->
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
                                        Confirm.show(tr("Are you sure?"), tr("Do you want to delete this address?")) {
                                            Model.delete(index)
                                        }
                                    }
                                }
                            }
                        }
                        onEvent {
                            click = {
                                Model.edit(index)
                            }
                        }
                    }
                }
        }
    }
}

package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Container
import io.kvision.core.FontStyle
import io.kvision.core.onClick
import io.kvision.core.onEvent
import io.kvision.form.check.radioGroup
import io.kvision.form.text.text
import io.kvision.html.InputType
import io.kvision.html.div
import io.kvision.html.icon
import io.kvision.html.link
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Confirm
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.state.bind
import io.kvision.table.TableType
import io.kvision.table.cell
import io.kvision.table.headerCell
import io.kvision.table.row
import io.kvision.table.table
import io.kvision.utils.px

fun Container.listPanel() {
    simplePanel {
        padding = 5.px

        hPanel(alignItems = AlignItems.CENTER, spacing = 20) {
            text(InputType.SEARCH) {
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

        div().bind(Model.addressBook) { state ->
            table(types = setOf(TableType.STRIPED, TableType.HOVER)) {
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
                            Sort.FN -> it.second.firstName?.lowercase()
                            Sort.LN -> it.second.lastName?.lowercase()
                            Sort.E -> it.second.email?.lowercase()
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
                                            Confirm.show(
                                                tr("Are you sure?"),
                                                tr("Do you want to delete this address?")
                                            ) {
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
}

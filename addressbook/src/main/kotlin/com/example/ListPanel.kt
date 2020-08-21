package com.example

import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.FlexWrap
import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.data.SorterType
import pl.treksoft.kvision.data.dataContainer
import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.check.radioGroup
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.form.text.textInput
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.link
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.table.HeaderCell
import pl.treksoft.kvision.table.Row
import pl.treksoft.kvision.table.Table
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.table.cell
import pl.treksoft.kvision.utils.px


enum class Sort {
    FN, LN, E, F
}

object ListPanel : SimplePanel() {

    private val container: DataContainer<Address, Row, Table>
    private lateinit var search: TextInput
    private lateinit var types: RadioGroup
    private var sort = Sort.FN
        set(value) {
            field = value
            container.update()
        }

    init {
        padding = 10.px

        val table = Table(types = setOf(TableType.STRIPED, TableType.HOVER)) {
            addHeaderCell(HeaderCell(tr("First name")) {
                onEvent {
                    click = {
                        sort = Sort.FN
                    }
                }
            })
            addHeaderCell(HeaderCell(tr("Last name")) {
                onEvent {
                    click = {
                        sort = Sort.LN
                    }
                }
            })
            addHeaderCell(HeaderCell(tr("E-mail")) {
                onEvent {
                    click = {
                        sort = Sort.E
                    }
                }
            })
            addHeaderCell(HeaderCell("") {
                onEvent {
                    click = {
                        sort = Sort.F
                    }
                }
            })
            addHeaderCell(HeaderCell(""))
        }

        hPanel(FlexWrap.WRAP, alignItems = AlignItems.CENTER, spacing = 20) {
            search = textInput(TextInputType.SEARCH) {
                placeholder = tr("Search ...")
            }
            types = radioGroup(listOf("all" to tr("All"), "fav" to tr("Favourites")), "all", inline = true) {
                marginBottom = 0.px
            }
        }

        container = dataContainer(
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
                                    Confirm.show(tr("Are you sure?"), tr("Do you want to delete this address?")) {
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
            }, table, filter = { address ->
                address.match(search.value) && (types.value == "all" || address.favourite ?: false)
            }, sorter = {
                when (sort) {
                    Sort.FN -> it.firstName?.toLowerCase()
                    Sort.LN -> it.lastName?.toLowerCase()
                    Sort.E -> it.email?.toLowerCase()
                    Sort.F -> it.favourite
                }
            }, sorterType = {
                when (sort) {
                    Sort.F -> SorterType.DESC
                    else -> SorterType.ASC
                }
            }
        )
        search.onEvent {
            input = {
                container.update()
            }
        }
        types.onEvent {
            change = {
                container.update()
            }
        }
    }
}

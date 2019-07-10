package com.example

import kotlinx.serialization.UnstableDefault
import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.data.DataContainer.Companion.dataContainer
import pl.treksoft.kvision.data.SorterType
import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.check.RadioGroup.Companion.radioGroup
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Icon.Companion.icon
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.table.Cell.Companion.cell
import pl.treksoft.kvision.table.HeaderCell
import pl.treksoft.kvision.table.Row
import pl.treksoft.kvision.table.Table
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.utils.px


enum class Sort {
    FN, LN, E, F
}

@UnstableDefault
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
                setEventListener {
                    click = {
                        sort = Sort.FN
                    }
                }
            })
            addHeaderCell(HeaderCell(tr("Last name")) {
                setEventListener {
                    click = {
                        sort = Sort.LN
                    }
                }
            })
            addHeaderCell(HeaderCell(tr("E-mail")) {
                setEventListener {
                    click = {
                        sort = Sort.E
                    }
                }
            })
            addHeaderCell(HeaderCell("") {
                setEventListener {
                    click = {
                        sort = Sort.F
                    }
                }
            })
            addHeaderCell(HeaderCell(""))
        }

        hPanel(FlexWrap.WRAP, alignItems = FlexAlignItems.CENTER, spacing = 20) {
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
                        if (it) icon("fa-heart-o") {
                            title = tr("Favourite")
                        }
                    }
                }
                cell {
                    icon("fa-times") {
                        title = tr("Delete")
                        setEventListener {
                            click = { e ->
                                e.stopPropagation()
                                Confirm.show(tr("Are you sure?"), tr("Do you want to delete this address?")) {
                                    EditPanel.delete(index)
                                }
                            }
                        }
                    }
                }
                setEventListener {
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
        search.setEventListener {
            input = {
                container.update()
            }
        }
        types.setEventListener {
            change = {
                container.update()
            }
        }
    }
}

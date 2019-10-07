package com.example

import pl.treksoft.kvision.chart.Chart.Companion.chart
import pl.treksoft.kvision.chart.ChartScales
import pl.treksoft.kvision.chart.ChartType
import pl.treksoft.kvision.chart.Configuration
import pl.treksoft.kvision.chart.DataSets
import pl.treksoft.kvision.chart.LegendOptions
import pl.treksoft.kvision.chart.ChartOptions
import pl.treksoft.kvision.chart.TitleOptions
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.GridPanel.Companion.gridPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px
import kotlin.math.sin

class ChartTab : SimplePanel() {
    init {
        this.marginTop = 10.px

        gridPanel(templateColumns = "50% 50%", columnGap = 30, rowGap = 30) {
            @Suppress("UnsafeCastFromDynamic")
            chart(
                Configuration(
                    ChartType.SCATTER,
                    listOf(
                        DataSets(
                            pointBorderColor = listOf(Color(Col.RED)),
                            backgroundColor = listOf(Color(Col.LIGHTGREEN)),
                            data = (-60..60).map {
                                obj {
                                    x = it.toDouble() / 10
                                    y = sin(it.toDouble() / 10)
                                }
                            }
                        )
                    ),
                    options = ChartOptions(legend = LegendOptions(display = false), showLines = true)
                )
            )
            chart(
                Configuration(
                    ChartType.BAR,
                    listOf(
                        DataSets(
                            data = listOf(6, 12, 19, 13, 7),
                            backgroundColor = listOf(
                                Color(0x3e95cd),
                                Color(0x8e5ea2),
                                Color(0x3cba9f),
                                Color(0xe8c3b9),
                                Color(0xc45850)
                            )
                        )
                    ),
                    listOf(
                        tr("Africa"),
                        tr("Asia"),
                        tr("Europe"),
                        tr("Latin America"),
                        tr("North America")
                    ),
                    ChartOptions(legend = LegendOptions(display = false), scales = ChartScales(yAxes = listOf(obj {
                        ticks = obj {
                            suggestedMin = 0
                            suggestedMax = 20
                        }
                    })), title = TitleOptions(display = false))
                )
            )
            chart(
                Configuration(
                    ChartType.PIE,
                    listOf(
                        DataSets(
                            data = listOf(6, 12, 19, 13, 7),
                            backgroundColor = listOf(
                                Color(0x3e95cd),
                                Color(0x8e5ea2),
                                Color(0x3cba9f),
                                Color(0xe8c3b9),
                                Color(0xc45850)
                            )
                        )
                    ), listOf(
                        tr("Africa"),
                        tr("Asia"),
                        tr("Europe"),
                        tr("Latin America"),
                        tr("North America")
                    )
                )
            )
            chart(
                Configuration(
                    ChartType.POLARAREA,
                    listOf(
                        DataSets(
                            data = listOf(6, 12, 19, 13, 7),
                            backgroundColor = listOf(
                                Color(0x3e95cd),
                                Color(0x8e5ea2),
                                Color(0x3cba9f),
                                Color(0xe8c3b9),
                                Color(0xc45850)
                            )
                        )
                    ), listOf(
                        tr("Africa"),
                        tr("Asia"),
                        tr("Europe"),
                        tr("Latin America"),
                        tr("North America")
                    )
                )
            )
        }
    }
}

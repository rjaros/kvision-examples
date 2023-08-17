package com.example

import io.kvision.chart.ChartOptions
import io.kvision.chart.ChartScales
import io.kvision.chart.ChartType
import io.kvision.chart.Configuration
import io.kvision.chart.DataSets
import io.kvision.chart.LegendOptions
import io.kvision.chart.PluginsOptions
import io.kvision.chart.TitleOptions
import io.kvision.chart.chart
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.i18n.I18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.gridPanel
import io.kvision.utils.obj
import io.kvision.utils.px
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
                            pointBorderColor = listOf(Color.name(Col.RED)),
                            backgroundColor = listOf(Color.name(Col.LIGHTGREEN)),
                            data = (-60..60).map {
                                obj {
                                    x = it.toDouble() / 10
                                    y = sin(it.toDouble() / 10)
                                }
                            }
                        )
                    ),
                    options = ChartOptions(
                        plugins = PluginsOptions(legend = LegendOptions(display = false)),
                        showLine = true
                    )
                )
            )
            chart(
                Configuration(
                    ChartType.BAR,
                    listOf(
                        DataSets(
                            data = listOf(6, 12, 19, 13, 7),
                            backgroundColor = listOf(
                                Color.hex(0x3e95cd),
                                Color.hex(0x8e5ea2),
                                Color.hex(0x3cba9f),
                                Color.hex(0xe8c3b9),
                                Color.hex(0xc45850)
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
                    ChartOptions(
                        plugins = PluginsOptions(
                            legend = LegendOptions(display = false),
                            title = TitleOptions(display = false)
                        ),
                        scales = mapOf("y" to ChartScales(suggestedMin = 0, suggestedMax = 20))
                    )
                )
            )
            chart(
                Configuration(
                    ChartType.PIE,
                    listOf(
                        DataSets(
                            data = listOf(6, 12, 19, 13, 7),
                            backgroundColor = listOf(
                                Color.hex(0x3e95cd),
                                Color.hex(0x8e5ea2),
                                Color.hex(0x3cba9f),
                                Color.hex(0xe8c3b9),
                                Color.hex(0xc45850)
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
                                Color.hex(0x3e95cd),
                                Color.hex(0x8e5ea2),
                                Color.hex(0x3cba9f),
                                Color.hex(0xe8c3b9),
                                Color.hex(0xc45850)
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

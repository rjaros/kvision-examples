package com.example

import pl.treksoft.jquery.jQuery
import pl.treksoft.jquery.invoke
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.onsenui.OnsenUi
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.navigator
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.dialog.showToast
import pl.treksoft.kvision.onsenui.splitter.Collapse
import pl.treksoft.kvision.onsenui.splitter.Side
import pl.treksoft.kvision.onsenui.splitter.SideAnimation
import pl.treksoft.kvision.onsenui.splitter.splitter
import pl.treksoft.kvision.onsenui.splitter.splitterContent
import pl.treksoft.kvision.onsenui.splitter.splitterSide
import pl.treksoft.kvision.onsenui.tabbar.Tab
import pl.treksoft.kvision.onsenui.tabbar.Tabbar
import pl.treksoft.kvision.onsenui.tabbar.TabsPosition
import pl.treksoft.kvision.onsenui.tabbar.tab
import pl.treksoft.kvision.onsenui.tabbar.tabbar
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.onsenui.toolbar.toolbarButton
import pl.treksoft.kvision.panel.ContainerType
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.px

class App : Application() {
    init {
        require("css/kvapp.css")
    }

    private val mdColors = listOf(Color.hex(0xf44336), Color.hex(0xf44336), Color.hex(0x1e88e5), Color.hex(0x673ab7))

    val isMD = OnsenUi.isAndroid()

    lateinit var navigator: Navigator
    lateinit var tabbar: Tabbar

    var noTips = false
    var showingTip = false

    override fun start() {
        root("kvapp", ContainerType.NONE, false) {
            if (!OnsenUi.isAndroid(true) && !OnsenUi.isIOS(true)) {
                jQuery("body").addClass("mobile-emulate")
                OnsenUi.mockStatusBar()
            }
            navigator = navigator(forceSwipeable = true) {
                swipeTargetWidth = 50
                page {
                    splitter {
                        val splitterSide = splitterSide(
                            animation = if (isMD) SideAnimation.OVERLAY else SideAnimation.REVEAL,
                            swipeable = true,
                            collapse = Collapse.COLLAPSE,
                            side = Side.RIGHT
                        ) {
                            sideWidth = 260.px
                            menuPage(this@App)
                        }
                        splitterContent {
                            page splitterContentPage@{
                                val toolbar = toolbar(if (isMD) "Onsen UI" else "Home") {
                                    modifier = "white-content"
                                    if (isMD) background = Background(mdColors[1])
                                    right {
                                        toolbarButton(icon = "ion-ios-menu, material:md-menu") {
                                            modifier = "white-content"
                                            onClick {
                                                splitterSide.toggle()
                                                showTip("Try dragging from right edge!")
                                            }
                                        }
                                    }
                                }
                                tabbar = tabbar(TabsPosition.AUTO, swipeable = true) {
                                    modifier = if (isMD) "autogrow white-content" else "autogrow"
                                    tab(if (isMD) null else "Camera", "ion-ios-camera, material:md-camera") {
                                        if (isMD) maxWidth = 60.px
                                        cameraPage()
                                    }
                                    tab("Home", if (isMD) null else "ion-ios-home", active = true) {
                                        homePage(this@App)
                                    }
                                    tab("Forms", if (isMD) null else "ion-ios-create") {
                                        formsPage(this@App)
                                    }
                                    tab("Anim", if (isMD) null else "ion-ios-film") {
                                        animPage(this@App)
                                    }
                                    if (isMD) {
                                        tabbarStyle { index ->
                                            background = Background(mdColors[index])
                                            setStyle("transition", "all 0.3s")
                                        }
                                    }
                                    onEvent {
                                        onsPostchange = {
                                            showTip("Tip: Try swiping pages!")
                                        }
                                        onsPrechange = {
                                            @Suppress("UnsafeCastFromDynamic")
                                            val index: Int = it.detail.asDynamic().detail.index
                                            if (!isMD) {
                                                toolbar.centerPanel.content = getTab(index)?.label
                                            } else {
                                                if (index == 0) {
                                                    this@splitterContentPage.top = (-105).px
                                                } else {
                                                    this@splitterContentPage.top = 0.px
                                                }
                                                this@splitterContentPage.setStyle("transition", "all 0.3s")
                                                toolbar.background = Background(mdColors[index])
                                                toolbar.setStyle("transition", "all 0.3s")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                pullHookPage(this@App)
                dialogsPage(this@App)
                buttonsPage()
                carouselPage()
                infiniteScrollPage(this@App)
                progressPage()
                presentationPage()
                onEvent {
                    onsPostpush = {
                        showTip("Try swipe-to-pop from left side!")
                    }
                }
            }
        }
    }

    fun getTab(index: Int): Tab? {
        return tabbar.getChildren().getOrNull(index) as? Tab
    }

    fun selectTab(index: Int) {
        tabbar.setActiveTab(index)
    }

    fun showTip(message: String) {
        if (noTips || showingTip) return
        showingTip = true
        showToast(message, "Shut up!", timeout = 2000).then {
            if (it == 0) noTips = true
            showingTip = false
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}

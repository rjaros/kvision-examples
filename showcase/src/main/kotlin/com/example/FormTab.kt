@file:UseContextualSerialization(Date::class)

package com.example

import io.kvision.core.AlignItems
import io.kvision.core.FlexWrap
import io.kvision.core.onClickLaunch
import io.kvision.form.check.Radio
import io.kvision.form.check.RadioGroup
import io.kvision.form.check.TriStateCheckBox
import io.kvision.form.formPanel
import io.kvision.form.getDataWithFileContent
import io.kvision.form.number.Range
import io.kvision.form.select.Select
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectRenders
import io.kvision.form.number.Spinner
import io.kvision.form.text.ImaskOptions
import io.kvision.form.text.Password
import io.kvision.form.text.PatternMask
import io.kvision.form.text.RichText
import io.kvision.form.text.Text
import io.kvision.form.text.TextArea
import io.kvision.form.text.TomTypeahead
import io.kvision.form.time.DateTime
import io.kvision.form.upload.BootstrapUpload
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Alert
import io.kvision.modal.Confirm
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.progress.Progress
import io.kvision.progress.progressNumeric
import io.kvision.rest.RestClient
import io.kvision.rest.callDynamic
import io.kvision.types.KFile
import io.kvision.utils.obj
import io.kvision.utils.px
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlin.js.Date

@Serializable
data class Form(
    val text: String? = null,
    val password: String? = null,
    val password2: String? = null,
    val textarea: String? = null,
    val richtext: String? = null,
    val typeahead: String? = null,
    val date: Date? = null,
    val time: Date? = null,
    val checkbox: Boolean? = null,
    val radio: Boolean = false,
    val select: String? = null,
    val tomSelect: String? = null,
    val ajaxselect: String? = null,
    val spinner: Double? = null,
    val range: Double? = null,
    val radiogroup: String? = null,
    val upload: List<KFile>? = null
)

class FormTab : SimplePanel() {
    init {
        val restClient = RestClient()
        this.marginTop = 10.px
        val formPanel = formPanel<Form> {
            add(
                Form::text,
                Text(label = tr("Required text field with a mask and a regexp [0-9] validator")) {
                    placeholder = tr("Enter your age")
                    maskOptions = ImaskOptions(pattern = PatternMask("000", lazy = false, eager = true))
                },
                required = true,
                requiredMessage = tr("Value is required"),
                validatorMessage = { tr("Only numbers are allowed") }) {
                it.getValue()?.let { "^\\d+$".toRegex().matches(it) }
            }
            add(Form::password, Password(label = tr("Password field with minimum length validator"), floating = true),
                validatorMessage = { tr("Password too short") }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Form::password2, Password(label = tr("Password confirmation"), floating = true),
                validatorMessage = { tr("Password too short") }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Form::textarea, TextArea(label = tr("Text area field")))
            add(
                Form::richtext,
                RichText(label = tr("Rich text field with a placeholder")) {
                    inputHeight = 200.px
                    placeholder = tr("Add some info")
                })
            add(
                Form::typeahead,
                TomTypeahead(
                    listOf("Alabama", "Alaska", "Arizona", "Arkansas", "California"),
                    label = tr("Typeahead")
                ), required = true, requiredMessage = tr("Value is required")
            )
            add(
                Form::date,
                DateTime(format = "YYYY-MM-DD", label = tr("Date field with a placeholder")).apply {
                    placeholder = tr("Enter date")
                }, legend = tr("Date and time fieldset")
            )
            add(
                Form::time,
                DateTime(format = "HH:mm", label = tr("Time field")), legend = tr("Date and time fieldset")
            )
            add(
                Form::checkbox,
                TriStateCheckBox(label = tr("Required tri-state checkbox")),
                required = true,
                requiredMessage = tr("Value is required"),
                validatorMessage = { tr("Value is required") }
            ) { it.getValue() }
            add(Form::radio, Radio(label = tr("Radio button")))
            add(
                Form::select, Select(
                    options = listOf("first" to tr("First option"), "second" to tr("Second option")),
                    emptyOption = true,
                    label = tr("Simple select")
                ), required = true, requiredMessage = tr("Value is required")
            )
            add(
                Form::tomSelect, TomSelect(
                    options = listOf("first" to tr("First option"), "second" to tr("Second option")),
                    label = tr("Advanced select")
                ), required = true, requiredMessage = tr("Value is required")
            )
            add(Form::ajaxselect, TomSelect(label = tr("Select with remote data source")).apply {
                emptyOption = true
                tsCallbacks = TomSelectCallbacks(
                    load = { query, callback ->
                        restClient.callDynamic("https://api.github.com/search/repositories") {
                            data = obj { q = query }
                            resultTransform = { it.items }
                        }.then { items: dynamic ->
                            @Suppress("UnsafeCastFromDynamic")
                            callback(items.map { item ->
                                obj {
                                    this.value = item.id
                                    this.text = item.name
                                    this.subtext = item.owner.login
                                }
                            })
                        }
                    },
                    shouldLoad = { it.length >= 3 }
                )
                tsRenders = TomSelectRenders(option = { item, escape ->
                    """
                        <div>
                            <span class="title">${escape(item.text)}</span>
                            <small>(${escape(item.subtext)})</small>
                        </div>
                    """.trimIndent()
                })
            }, required = true, requiredMessage = tr("Value is required"))
            add(Form::spinner, Spinner(label = tr("Spinner field 10 - 20"), min = 10, max = 20))
            add(Form::range, Range(label = tr("Range field 10 - 20"), min = 10, max = 20))
            add(
                Form::radiogroup, RadioGroup(
                    listOf("option1" to tr("First option"), "option2" to tr("Second option")),
                    inline = true, label = tr("Radio button group")
                ), required = true, requiredMessage = tr("Value is required")
            )
            add(Form::upload, BootstrapUpload("/", multiple = true, label = tr("Upload files (images only)")).apply {
                showUpload = false
                showCancel = false
                explorerTheme = true
                dropZoneEnabled = false
                allowedFileTypes = setOf("image")
            })
            validator = {
                val result = it[Form::password] == it[Form::password2]
                if (!result) {
                    it.getControl(Form::password)?.validatorError = tr("Passwords are not the same")
                    it.getControl(Form::password2)?.validatorError = tr("Passwords are not the same")
                }
                result
            }
            validatorMessage = { tr("The passwords are not the same.") }
        }
        formPanel.add(HPanel(spacing = 10, alignItems = AlignItems.CENTER, wrap = FlexWrap.WRAP) {
            val p = Progress(0, 100) {
                marginBottom = 0.px
                width = 300.px
                progressNumeric {
                    striped = true
                }
            }
            button(tr("Show data"), "fas fa-info", ButtonStyle.SUCCESS).onClickLaunch {
                console.log(formPanel.getDataJson())
                Alert.show(tr("Form data in plain JSON"), JSON.stringify(formPanel.getDataJson(), space = 1))
                val content = formPanel.getDataWithFileContent()
                console.log(content)
            }
            button(tr("Clear data"), "fas fa-times", ButtonStyle.DANGER).onClick {
                Confirm.show(
                    tr("Are you sure?"),
                    tr("Do you want to clear your data?"),
                    yesTitle = tr("Yes"),
                    noTitle = tr("No"),
                    cancelTitle = tr("Cancel")
                ) {
                    formPanel.clearData()
                    p.getFirstProgressBar()?.value = 0
                    formPanel.clearValidation()
                }
            }
            button(tr("Validate"), "fas fa-check", ButtonStyle.INFO).onClick {
                p.getFirstProgressBar()?.value = 100
                formPanel.validate()
            }
            add(p)
        })
    }
}
package com.example

import io.kvision.core.onEvent
import io.kvision.form.FormPanel
import io.kvision.form.formPanel
import io.kvision.form.text.Password
import io.kvision.form.text.Text
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Alert
import io.kvision.modal.Dialog
import io.kvision.remote.Credentials
import io.kvision.remote.LoginService
import io.kvision.remote.SecurityMgr
import io.kvision.utils.ENTER_KEY
import kotlinx.coroutines.launch

class LoginWindow : Dialog<Credentials>(closeButton = false, escape = false, animation = false) {

    private val loginPanel: FormPanel<Credentials>
    private val loginButton: Button
    private val userButton: Button
    private val registerPanel: FormPanel<Profile>
    private val registerButton: Button
    private val cancelButton: Button

    init {
        loginPanel = formPanel {
            add(Credentials::username, Text(label = "${tr("Login")}:"), required = true)
            add(Credentials::password, Password(label = "${tr("Password")}:"), required = true)
            onEvent {
                keydown = {
                    if (it.keyCode == ENTER_KEY) {
                        this@LoginWindow.processCredentials()
                    }
                }
            }
        }
        registerPanel = formPanel {
            add(Profile::name, Text(label = "${tr("Your name")}:"), required = true)
            add(Profile::username, Text(label = "Login:"), required = true)
            add(
                Profile::password, Password(label = "${tr("Password")}:"), required = true,
                validatorMessage = { "Password too short" }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Profile::password2, Password(label = "${tr("Confirm password")}:"), required = true,
                validatorMessage = { tr("Password too short") }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            validator = {
                val result = it[Profile::password] == it[Profile::password2]
                if (!result) {
                    it.getControl(Profile::password)?.validatorError = tr("Passwords are not the same")
                    it.getControl(Profile::password2)?.validatorError = tr("Passwords are not the same")
                }
                result
            }
            validatorMessage = { tr("Passwords are not the same") }

        }
        cancelButton = Button(tr("Cancel"), "fas fa-times") {
            onClick {
                this@LoginWindow.hideRegisterForm()
            }
        }
        registerButton = Button(tr("Register"), "fas fa-check", ButtonStyle.PRIMARY) {
            onClick {
                this@LoginWindow.processRegister()
            }
        }
        loginButton = Button(tr("Login"), "fas fa-check", ButtonStyle.PRIMARY) {
            onClick {
                this@LoginWindow.processCredentials()
            }
        }
        userButton = Button(tr("Register user"), "fas fa-user") {
            onClick {
                this@LoginWindow.showRegisterForm()
            }
        }
        addButton(userButton)
        addButton(loginButton)
        addButton(cancelButton)
        addButton(registerButton)
        hideRegisterForm()
    }

    private fun showRegisterForm() {
        loginPanel.hide()
        registerPanel.show()
        registerPanel.clearData()
        loginButton.hide()
        userButton.hide()
        cancelButton.show()
        registerButton.show()
    }

    private fun hideRegisterForm() {
        loginPanel.show()
        registerPanel.hide()
        loginButton.show()
        userButton.show()
        cancelButton.hide()
        registerButton.hide()
    }

    private fun processCredentials() {
        if (loginPanel.validate()) {
            setResult(loginPanel.getData())
            loginPanel.clearData()
        }
    }

    private fun processRegister() {
        if (registerPanel.validate()) {
            val userData = registerPanel.getData()
            AppScope.launch {
                if (Model.registerProfile(userData, userData.password!!)
                ) {
                    Alert.show(text = tr("User registered. You can now log in.")) {
                        hideRegisterForm()
                    }
                } else {
                    Alert.show(text = tr("This login is not available. Please try again."))
                }
            }
        }
    }
}

object Security : SecurityMgr() {

    private val loginService = LoginService("/login")
    private val loginWindow = LoginWindow()

    override suspend fun login(): Boolean {
        return loginService.login(loginWindow.getResult())
    }

    override suspend fun afterLogin() {
        Model.readProfile()
    }
}

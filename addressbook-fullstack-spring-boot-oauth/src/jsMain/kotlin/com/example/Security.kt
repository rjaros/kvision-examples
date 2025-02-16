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
import kotlinx.browser.document
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
actual data class Profile(
    val name: String? = null,
    val username: String? = null,
    val password: String? = null,
    val password2: String? = null
)

class LoginWindow : Dialog<Credentials>(closeButton = false, escape = false, animation = false) {

    private val loginButton: Button

    init {
        loginButton = Button(tr("Login"), "fas fa-check", ButtonStyle.PRIMARY) {
            onClick {
                this@LoginWindow.processCredentials()
            }
        }
        addButton(loginButton)
    }

    private fun processCredentials() {
        document.location?.href = "/oauth2/authorization/google"
    }

}

object Security : SecurityMgr() {

    private val loginService = LoginService("/oauth2/authorization/google")
    private val loginWindow = LoginWindow()

    override suspend fun login(): Boolean {
        return loginService.login(loginWindow.getResult())
    }

    override suspend fun afterLogin() {
        Model.readProfile()
    }
}

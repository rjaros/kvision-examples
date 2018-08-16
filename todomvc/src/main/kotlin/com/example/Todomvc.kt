package com.example

import com.lightningkite.kotlin.observable.list.observableListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import org.w3c.dom.get
import org.w3c.dom.set
import pl.treksoft.kvision.data.BaseDataComponent
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.data.DataContainer.Companion.dataContainer
import pl.treksoft.kvision.form.FieldLabel
import pl.treksoft.kvision.form.check.CheckInput
import pl.treksoft.kvision.form.check.CheckInput.Companion.checkInput
import pl.treksoft.kvision.form.check.CheckInputType
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Link
import pl.treksoft.kvision.html.ListTag.Companion.listTag
import pl.treksoft.kvision.html.ListType
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.routing.routing
import kotlin.browser.localStorage

const val ENTER_KEY = 13
const val ESCAPE_KEY = 27

@Serializable
open class BaseTodo(open var completed: Boolean, open var title: String) : BaseDataComponent()

class Todo(completed: Boolean, title: String) : BaseTodo(completed, title) {
    constructor(base: BaseTodo) : this(base.completed, base.title)

    override var completed: Boolean by obs(completed)
    override var title: String by obs(title)
}

enum class TODOMODE {
    ALL,
    ACTIVE,
    COMPLETED
}

object Todomvc : ApplicationBase {

    private lateinit var root: Root

    private val model = observableListOf<Todo>()

    private val checkAllInput = CheckInput(classes = setOf("toggle-all")).apply {
        id = "toggle-all"
        onClick {
            val value = this.value
            model.forEach { it.completed = value }
        }
    }
    private val allLink = Link("All", "#!/", classes = setOf("selected"))
    private val activeLink = Link("Active", "#!/active")
    private val completedLink = Link("Completed", "#!/completed")
    private val clearCompletedButton = Button("Clear completed", classes = setOf("clear-completed")).onClick {
        model.filter { it.completed }.forEach { model.remove(it) }
    }

    private val countTag = Tag(TAG.STRONG, "0")
    private val itemsLeftTag = Tag(TAG.SPAN, " items left", classes = setOf("todo-count")).apply {
        add(countTag)
    }
    private var mode: TODOMODE = TODOMODE.ALL

    private var container: DataContainer<Todo, Tag>? = null

    private val header = genHeader()
    private val main = genMain()
    private val footer = genFooter()

    override fun start(state: Map<String, Any>) {
        root = Root("todomvc") {
            tag(TAG.SECTION, classes = setOf("todoapp")) {
                add(this@Todomvc.header)
                add(this@Todomvc.main)
                add(this@Todomvc.footer)
            }
        }
        loadModel()
        checkModel()
        routing.on("/", { _ -> all() })
            .on("/active", { _ -> active() })
            .on("/completed", { _ -> completed() })
            .resolve()
    }

    private fun loadModel() {
        localStorage.get("todos-kvision")?.let {
            JSON.parse(BaseTodo.serializer().list, it).map { model.add(Todo(it)) }
        }
    }

    private fun saveModel() {
        val jsonString = JSON.indented.stringify(BaseTodo.serializer().list, model.toList())
        localStorage.set("todos-kvision", jsonString)
    }

    private fun checkModel() {
        val countActive = model.filter { !it.completed }.size
        val countCompleted = model.filter { it.completed }.size
        this.main.visible = model.isNotEmpty()
        this.footer.visible = model.isNotEmpty()
        this.countTag.content = countActive.toString()
        this.itemsLeftTag.content = when (countActive) {
            1 -> " item left"
            else -> " items left"
        }
        this.checkAllInput.value = (countActive == 0)
        this.clearCompletedButton.visible = countCompleted > 0
        saveModel()
    }

    private fun all() {
        this.mode = TODOMODE.ALL
        this.allLink.addCssClass("selected")
        this.activeLink.removeCssClass("selected")
        this.completedLink.removeCssClass("selected")
        this.container?.update()
    }

    private fun active() {
        this.mode = TODOMODE.ACTIVE
        this.allLink.removeCssClass("selected")
        this.activeLink.addCssClass("selected")
        this.completedLink.removeCssClass("selected")
        this.container?.update()
    }

    private fun completed() {
        this.mode = TODOMODE.COMPLETED
        this.allLink.removeCssClass("selected")
        this.activeLink.removeCssClass("selected")
        this.completedLink.addCssClass("selected")
        this.container?.update()
    }

    private fun genHeader(): Tag {
        return Tag(TAG.HEADER, classes = setOf("header")) {
            tag(TAG.H1, "todos")
            textInput(classes = setOf("new-todo")) {
                placeholder = "What needs to be done?"
                autofocus = true
                setEventListener<TextInput> {
                    keydown = { e ->
                        if (e.keyCode == ENTER_KEY) {
                            addTodo(self.value)
                            self.value = null
                        }
                    }
                }
            }
        }
    }

    private fun addTodo(value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            model.add(Todo(false, v))
        }
    }

    private fun editTodo(index: Int, value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            model[index].title = v
        } else {
            model.removeAt(index)
        }
    }

    private fun genMain(): Tag {
        return Tag(TAG.SECTION, classes = setOf("main")) {
            add(checkAllInput)
            add(FieldLabel("toggle-all", "Mark all as complete"))
            container = dataContainer(model, { index, todo ->
                val li = Tag(TAG.LI)
                li.apply {
                    if (todo.completed) addCssClass("completed")
                    val edit = TextInput(classes = setOf("edit"))
                    val view = Tag(TAG.DIV, classes = setOf("view")) {
                        checkInput(
                            CheckInputType.CHECKBOX, todo.completed, classes = setOf("toggle")
                        ).onClick {
                            todo.completed = this.value
                        }
                        tag(TAG.LABEL, todo.title) {
                            setEventListener<Tag> {
                                dblclick = {
                                    li.getElementJQuery()?.addClass("editing")
                                    edit.value = todo.title
                                    edit.getElementJQuery()?.focus()
                                }
                            }
                        }
                        button("", classes = setOf("destroy")).onClick {
                            model.removeAt(index)
                        }
                    }
                    edit.setEventListener<TextInput> {
                        blur = {
                            if (li.getElementJQuery()?.hasClass("editing") == true) {
                                li.getElementJQuery()?.removeClass("editing")
                                editTodo(index, self.value)
                            }
                        }
                        keydown = { e ->
                            if (e.keyCode == ENTER_KEY) {
                                li.getElementJQuery()?.removeClass("editing")
                                editTodo(index, self.value)
                            }
                            if (e.keyCode == ESCAPE_KEY) {
                                li.getElementJQuery()?.removeClass("editing")
                            }
                        }
                    }
                    add(view)
                    add(edit)
                }
            }, { _, todo ->
                when (mode) {
                    TODOMODE.ALL -> true
                    TODOMODE.ACTIVE -> !todo.completed
                    TODOMODE.COMPLETED -> todo.completed
                }
            }, container = Tag(TAG.UL, classes = setOf("todo-list"))).onUpdate {
                checkModel()
            }
        }
    }

    private fun genFooter(): Tag {
        return Tag(TAG.FOOTER, classes = setOf("footer")) {
            add(itemsLeftTag)
            listTag(ListType.UL, classes = setOf("filters")) {
                add(allLink)
                add(activeLink)
                add(completedLink)
            }
            add(clearCompletedButton)
        }
    }


    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}

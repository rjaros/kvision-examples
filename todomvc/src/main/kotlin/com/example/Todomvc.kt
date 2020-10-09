package com.example

import com.example.MODE.*
import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.form.check.checkBoxInput
import pl.treksoft.kvision.form.fieldLabel
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.textInput
import pl.treksoft.kvision.html.ListType
import pl.treksoft.kvision.html.TAG.*
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.link
import pl.treksoft.kvision.html.listTag
import pl.treksoft.kvision.html.tag
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.redux.RAction
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.routing.routing
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.state.bind
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.ESC_KEY

enum class MODE {
    ALL,
    ACTIVE,
    COMPLETED
}

@Serializable
data class Todo(val completed: Boolean, val title: String)

@Serializable
data class State(val todos: List<Todo>, val mode: MODE) {
    fun areAllCompleted() = todos.find { !it.completed } == null
    fun activeList() = todos.filter { !it.completed }
    fun completedList() = todos.filter { it.completed }
    fun allListIndexed() = todos.mapIndexed { index, todo -> index to todo }
    fun activeListIndexed() = allListIndexed().filter { !it.second.completed }
    fun completedListIndexed() = allListIndexed().filter { it.second.completed }
}

sealed class TodoAction : RAction {
    data class Load(val todos: List<Todo>) : TodoAction()
    data class Add(val todo: Todo) : TodoAction()
    data class ChangeTitle(val index: Int, val title: String) : TodoAction()
    data class ToggleActive(val index: Int) : TodoAction()
    data class Delete(val index: Int) : TodoAction()
    object ToggleAll : TodoAction()
    object ClearCompleted : TodoAction()
    object ShowAll : TodoAction()
    object ShowActive : TodoAction()
    object ShowCompleted : TodoAction()
}

fun todoReducer(state: State, action: TodoAction): State = when (action) {
    is TodoAction.Load -> state.copy(todos = action.todos, mode = ALL)
    is TodoAction.Add -> state.copy(todos = state.todos + action.todo)
    is TodoAction.ChangeTitle -> state.copy(todos = state.todos.mapIndexed { index, todo ->
        if (index == action.index) todo.copy(title = action.title) else todo
    })
    is TodoAction.ToggleActive -> state.copy(todos = state.todos.mapIndexed { index, todo ->
        if (index == action.index) todo.copy(completed = !todo.completed) else todo
    })
    is TodoAction.ToggleAll -> {
        val areAllCompleted = state.areAllCompleted()
        state.copy(todos = state.todos.map { it.copy(completed = !areAllCompleted) })
    }
    is TodoAction.Delete -> state.copy(todos = state.todos.filterIndexed { index, _ ->
        (index != action.index)
    })
    is TodoAction.ClearCompleted -> state.copy(todos = state.activeList())
    is TodoAction.ShowAll -> state.copy(mode = ALL)
    is TodoAction.ShowActive -> state.copy(mode = ACTIVE)
    is TodoAction.ShowCompleted -> state.copy(mode = COMPLETED)
}

class Todomvc : Application() {

    val todoStore = createReduxStore(::todoReducer, State(mutableListOf(), ALL))

    override fun start() {
        root("todomvc") {
            tag(SECTION, classes = setOf("todoapp")).bind(todoStore) { state ->
                tag(HEADER, classes = setOf("header")) {
                    tag(H1, "todos")
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
                tag(SECTION, classes = setOf("main")) {
                    visible = state.todos.isNotEmpty()
                    checkBoxInput(state.areAllCompleted(), classes = setOf("toggle-all")) {
                        id = "toggle-all"
                        onClick {
                            todoStore.dispatch(TodoAction.ToggleAll)
                        }
                    }
                    fieldLabel("toggle-all", "Mark all as complete")
                    tag(UL, classes = setOf("todo-list")) {
                        when (state.mode) {
                            ALL -> state.allListIndexed()
                            ACTIVE -> state.activeListIndexed()
                            COMPLETED -> state.completedListIndexed()
                        }.forEach { (index, todo) ->
                            tag(LI, classes = if (todo.completed) setOf("completed") else setOf()) li@{
                                lateinit var edit: TextInput
                                div(classes = setOf("view")) {
                                    checkBoxInput(todo.completed, classes = setOf("toggle")).onClick {
                                        todoStore.dispatch(TodoAction.ToggleActive(index))
                                    }
                                    tag(LABEL, todo.title) {
                                        setEventListener<Tag> {
                                            dblclick = {
                                                this@li.getElementJQuery()?.addClass("editing")
                                                edit.value = todo.title
                                                edit.getElementJQuery()?.focus()
                                            }
                                        }
                                    }
                                    button("", classes = setOf("destroy")).onClick {
                                        todoStore.dispatch(TodoAction.Delete(index))
                                    }
                                }
                                edit = textInput(classes = setOf("edit")) {
                                    setEventListener<TextInput> {
                                        blur = {
                                            if (this@li.getElementJQuery()?.hasClass("editing") == true) {
                                                this@li.getElementJQuery()?.removeClass("editing")
                                                editTodo(index, self.value)
                                            }
                                        }
                                        keydown = { e ->
                                            if (e.keyCode == ENTER_KEY) {
                                                editTodo(index, self.value)
                                                this@li.getElementJQuery()?.removeClass("editing")
                                            }
                                            if (e.keyCode == ESC_KEY) {
                                                this@li.getElementJQuery()?.removeClass("editing")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                tag(FOOTER, classes = setOf("footer")) {
                    visible = state.todos.isNotEmpty()
                    val itemsLeftString = if (state.activeList().size == 1) " item left" else " items left"
                    tag(SPAN, itemsLeftString, classes = setOf("todo-count")) {
                        tag(STRONG, "${state.activeList().size}")
                    }
                    listTag(ListType.UL, classes = setOf("filters")) {
                        link("All", "#!/", classes = if (state.mode == ALL) setOf("selected") else setOf())
                        link(
                            "Active", "#!/active",
                            classes = if (state.mode == ACTIVE) setOf("selected") else setOf()
                        )
                        link(
                            "Completed", "#!/completed",
                            classes = if (state.mode == COMPLETED) setOf("selected") else setOf()
                        )
                    }
                    if (state.completedList().isNotEmpty()) {
                        button("Clear completed", classes = setOf("clear-completed")).onClick {
                            todoStore.dispatch(TodoAction.ClearCompleted)
                        }
                    }
                }
            }
        }
        loadModel()
        routing.on("/", { _ -> todoStore.dispatch(TodoAction.ShowAll) })
            .on("/active", { _ -> todoStore.dispatch(TodoAction.ShowActive) })
            .on("/completed", { _ -> todoStore.dispatch(TodoAction.ShowCompleted) })
            .resolve()
        todoStore.subscribe {
            saveModel()
        }
    }

    private fun loadModel() {
        localStorage["todos-kvision"]?.let {
            todoStore.dispatch(TodoAction.Load(Json.decodeFromString(ListSerializer(Todo.serializer()), it)))
        }
    }

    private fun saveModel() {
        val jsonString = Json {
            prettyPrint = true
        }.encodeToString(ListSerializer(Todo.serializer()), todoStore.getState().todos)
        localStorage["todos-kvision"] = jsonString
    }

    private fun addTodo(value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            todoStore.dispatch(TodoAction.Add(Todo(false, v)))
        }
    }

    private fun editTodo(index: Int, value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            todoStore.dispatch(TodoAction.ChangeTitle(index, v))
        } else {
            todoStore.dispatch(TodoAction.Delete(index))
        }
    }
}

fun main() {
    startApplication(::Todomvc, module.hot)
}

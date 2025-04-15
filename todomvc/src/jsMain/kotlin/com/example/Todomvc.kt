package com.example

import com.example.MODE.*
import io.kvision.Application
import io.kvision.Hot
import io.kvision.core.onEvent
import io.kvision.form.check.checkBoxInput
import io.kvision.form.fieldLabel
import io.kvision.form.text.TextInput
import io.kvision.form.text.textInput
import io.kvision.html.ListType
import io.kvision.html.TAG.STRONG
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.footer
import io.kvision.html.h1
import io.kvision.html.header
import io.kvision.html.label
import io.kvision.html.li
import io.kvision.html.link
import io.kvision.html.listTag
import io.kvision.html.section
import io.kvision.html.span
import io.kvision.html.tag
import io.kvision.html.ul
import io.kvision.panel.root
import io.kvision.redux.RAction
import io.kvision.redux.createTypedReduxStore
import io.kvision.routing.Routing
import io.kvision.startApplication
import io.kvision.state.bind
import io.kvision.utils.ENTER_KEY
import io.kvision.utils.ESC_KEY
import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.set

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

    private val json = Json {
        prettyPrint = true
    }

    val todoStore = createTypedReduxStore(::todoReducer, State(mutableListOf(), ALL))

    override fun start() {
        val routing = Routing.init()
        root("todomvc") {
            section(className = "todoapp").bind(todoStore) { state ->
                header(className = "header") {
                    h1("todos")
                    textInput(className = "new-todo") {
                        placeholder = "What needs to be done?"
                        autofocus = true
                        onEvent {
                            keydown = { e ->
                                if (e.keyCode == ENTER_KEY) {
                                    addTodo(self.value)
                                    self.value = null
                                }
                            }
                        }
                    }
                }
                section(className = "main") {
                    visible = state.todos.isNotEmpty()
                    checkBoxInput(state.areAllCompleted(), className = "toggle-all") {
                        id = "toggle-all"
                        onClick {
                            todoStore.dispatch(TodoAction.ToggleAll)
                        }
                    }
                    fieldLabel("toggle-all", "Mark all as complete")
                    ul(className = "todo-list") {
                        when (state.mode) {
                            ALL -> state.allListIndexed()
                            ACTIVE -> state.activeListIndexed()
                            COMPLETED -> state.completedListIndexed()
                        }.forEach { (index, todo) ->
                            li(className = if (todo.completed) "completed" else null) li@{
                                lateinit var edit: TextInput
                                div(className = "view") {
                                    checkBoxInput(todo.completed, className = "toggle").onClick {
                                        todoStore.dispatch(TodoAction.ToggleActive(index))
                                    }
                                    label(todo.title) {
                                        onEvent {
                                            dblclick = {
                                                this@li.getElement()?.unsafeCast<Element>()?.classList?.add("editing")
                                                edit.value = todo.title
                                                edit.focus()
                                            }
                                        }
                                    }
                                    button("", className = "destroy").onClick {
                                        todoStore.dispatch(TodoAction.Delete(index))
                                    }
                                }
                                edit = textInput(className = "edit") {
                                    onEvent {
                                        blur = {
                                            if (this@li.getElement()
                                                    ?.unsafeCast<Element>()?.classList?.contains("editing") == true
                                            ) {
                                                this@li.getElement()
                                                    ?.unsafeCast<Element>()?.classList?.remove("editing")
                                                editTodo(index, self.value)
                                            }
                                        }
                                        keydown = { e ->
                                            if (e.keyCode == ENTER_KEY) {
                                                editTodo(index, self.value)
                                                this@li.getElement()
                                                    ?.unsafeCast<Element>()?.classList?.remove("editing")
                                            }
                                            if (e.keyCode == ESC_KEY) {
                                                this@li.getElement()
                                                    ?.unsafeCast<Element>()?.classList?.remove("editing")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                footer(className = "footer") {
                    visible = state.todos.isNotEmpty()
                    val itemsLeftString = if (state.activeList().size == 1) " item left" else " items left"
                    span(itemsLeftString, className = "todo-count") {
                        tag(STRONG, "${state.activeList().size}")
                    }
                    listTag(ListType.UL, className = "filters") {
                        link("All", "#/", className = if (state.mode == ALL) "selected" else null)
                        link(
                            "Active", "#/active",
                            className = if (state.mode == ACTIVE) "selected" else null
                        )
                        link(
                            "Completed", "#/completed",
                            className = if (state.mode == COMPLETED) "selected" else null
                        )
                    }
                    if (state.completedList().isNotEmpty()) {
                        button("Clear completed", className = "clear-completed").onClick {
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
        val jsonString = json.encodeToString(ListSerializer(Todo.serializer()), todoStore.getState().todos)
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
    startApplication(::Todomvc, js("import.meta.webpackHot").unsafeCast<Hot?>())
}

package com.example

import com.example.MODE.*
import com.example.TodoContract.Inputs.*
import io.kvision.Application
import io.kvision.Hot
import io.kvision.core.onClickLaunch
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
import io.kvision.routing.KVRouter
import io.kvision.startApplication
import io.kvision.state.bind
import io.kvision.utils.ENTER_KEY
import io.kvision.utils.ESC_KEY
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class Todomvc : Application(), KoinComponent {

    private val todoViewModel: TodoViewModel by inject()
    private val routing: KVRouter by inject()

    override fun start() {
        root("todomvc") {
            section(className = "todoapp").bind(todoViewModel) { state ->
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
                        onClickLaunch {
                            todoViewModel.send(ToggleAll)
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
                                    checkBoxInput(todo.completed, className = "toggle").onClickLaunch {
                                        todoViewModel.send(ToggleActive(index))
                                    }
                                    label(todo.title) {
                                        onEvent {
                                            dblclick = {
                                                this@li.getElement()?.classList?.add("editing")
                                                edit.value = todo.title
                                                edit.focus()
                                            }
                                        }
                                    }
                                    button("", className = "destroy").onClickLaunch {
                                        todoViewModel.send(Delete(index))
                                    }
                                }
                                edit = textInput(className = "edit") {
                                    onEvent {
                                        blur = {
                                            if (this@li.getElement()
                                                    ?.classList?.contains("editing") == true
                                            ) {
                                                this@li.getElement()
                                                    ?.classList?.remove("editing")
                                                editTodo(index, self.value)
                                            }
                                        }
                                        keydown = { e ->
                                            if (e.keyCode == ENTER_KEY) {
                                                editTodo(index, self.value)
                                                this@li.getElement()
                                                    ?.classList?.remove("editing")
                                            }
                                            if (e.keyCode == ESC_KEY) {
                                                this@li.getElement()
                                                    ?.classList?.remove("editing")
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
                        button("Clear completed", className = "clear-completed").onClickLaunch {
                            todoViewModel.send(ClearCompleted)
                        }
                    }
                }
            }
        }
        routing.kvOn("/") { todoViewModel.trySend(ShowAll) }
            .kvOn("/active") { todoViewModel.trySend(ShowActive) }
            .kvOn("/completed") { todoViewModel.trySend(ShowCompleted) }
    }

    private fun addTodo(value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            todoViewModel.trySend(Add(Todo(false, v)))
        }
    }

    private fun editTodo(index: Int, value: String?) {
        val v = value?.trim() ?: ""
        if (v.isNotEmpty()) {
            todoViewModel.trySend(ChangeTitle(index, v))
        } else {
            todoViewModel.trySend(Delete(index))
        }
    }
}

fun main() {
    startKoin {
        modules(todoModule)
    }
    startApplication(::Todomvc, js("import.meta.webpackHot").unsafeCast<Hot?>())
}

package com.example

object TodoContract {
    data class State(val todos: List<Todo>, val mode: MODE) {
        fun areAllCompleted() = todos.find { !it.completed } == null
        fun activeList() = todos.filter { !it.completed }
        fun completedList() = todos.filter { it.completed }
        fun allListIndexed() = todos.mapIndexed { index, todo -> index to todo }
        fun activeListIndexed() = allListIndexed().filter { !it.second.completed }
        fun completedListIndexed() = allListIndexed().filter { it.second.completed }
    }

    sealed class Inputs {
        data class Load(val todos: List<Todo>) : Inputs()
        data class Add(val todo: Todo) : Inputs()
        data class ChangeTitle(val index: Int, val title: String) : Inputs()
        data class ToggleActive(val index: Int) : Inputs()
        data class Delete(val index: Int) : Inputs()
        object ToggleAll : Inputs()
        object ClearCompleted : Inputs()
        object ShowAll : Inputs()
        object ShowActive : Inputs()
        object ShowCompleted : Inputs()
    }

    sealed class Events
}

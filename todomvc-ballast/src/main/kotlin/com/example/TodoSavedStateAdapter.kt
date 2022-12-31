package com.example

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.example.MODE.ALL
import io.kvision.routing.KVRouter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set

class TodoSavedStateAdapter(private val json: Json, private val storage: Storage, private val routing: KVRouter) :
    SavedStateAdapter<
            TodoContract.Inputs,
            TodoContract.Events,
            TodoContract.State> {

    override suspend fun SaveStateScope<
            TodoContract.Inputs,
            TodoContract.Events,
            TodoContract.State>.save() {
        saveAll { state ->
            val jsonString =
                json.encodeToString(ListSerializer(Todo.serializer()), state.todos)
            storage["todos-kvision"] = jsonString
        }
    }

    override suspend fun RestoreStateScope<
            TodoContract.Inputs,
            TodoContract.Events,
            TodoContract.State>.restore(): TodoContract.State {
        return TodoContract.State(
            todos = storage["todos-kvision"]?.let {
                json.decodeFromString(
                    ListSerializer(Todo.serializer()),
                    it
                )
            } ?: emptyList(), ALL
        )
    }

    override suspend fun onRestoreComplete(restoredState: TodoContract.State): TodoContract.Inputs? {
        routing.kvResolve()
        return null
    }
}

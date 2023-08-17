package com.example

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import io.kvision.routing.KVRouter

class TodoEventHandler(private val routing: KVRouter) :
    EventHandler<TodoContract.Inputs, TodoContract.Events, TodoContract.State> {
    override suspend fun EventHandlerScope<TodoContract.Inputs, TodoContract.Events, TodoContract.State>.handleEvent(
        event: TodoContract.Events
    ) = when (event) {
        is TodoContract.Events.StateRestored -> {
            routing.kvResolve()
        }
    }
}

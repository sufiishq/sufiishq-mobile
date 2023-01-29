package pk.sufiishq.app.core.event.dispatcher

import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler

object EventDispatcher {

    private val eventHandlerSet = mutableMapOf<String, EventHandler>()

    fun dispatch(event: Event, vararg events: Event) {
        arrayOf(event, *events).forEach { e ->
            eventHandlerSet[e.resolverClass.simpleName]?.onEvent(e)
                ?: throw NullPointerException("No resolver found for $e event")
        }
    }

    fun registerEventHandler(eventHandler: EventHandler) {
        eventHandlerSet[eventHandler.javaClass.simpleName] = eventHandler
    }

}
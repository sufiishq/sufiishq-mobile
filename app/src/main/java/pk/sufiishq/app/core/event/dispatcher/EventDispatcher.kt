package pk.sufiishq.app.core.event.dispatcher

import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler

class EventDispatcher private constructor() {

    private val eventHandlerSet = mutableSetOf<EventHandler>()

    fun dispatch(event: Event, vararg events: Event) {
        arrayOf(event, *events).forEach { e ->
            eventHandlerSet
                .firstOrNull { e.resolverClass.java == it.javaClass }
                ?.onEvent(e) ?: throw NullPointerException("No resolver found for $e event")
        }
    }

    fun registerEventHandler(eventHandler: EventHandler) {
        eventHandlerSet.add(eventHandler)
    }

    fun release() {
        instance = null
    }

    companion object {
        private var instance: EventDispatcher? = null

        fun getInstance(): EventDispatcher {
            if (instance == null) instance = EventDispatcher()
            return instance!!
        }
    }
}
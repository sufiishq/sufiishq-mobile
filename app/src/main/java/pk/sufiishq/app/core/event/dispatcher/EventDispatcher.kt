package pk.sufiishq.app.core.event.dispatcher

import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventDispatcher @Inject constructor() {

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
}
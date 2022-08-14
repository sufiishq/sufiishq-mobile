package pk.sufiishq.app.core.event.handler

import pk.sufiishq.app.core.event.events.Event

interface EventHandler {
    fun onEvent(event: Event)
}
package pk.sufiishq.app.core.event.exception

import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.handler.EventHandler
import timber.log.Timber

class UnhandledEventException(event: Event, resolver: EventHandler) : IllegalArgumentException(
    "event $event not handling in this resolver $resolver, handle this event or change resolver"
) {
    init {
        Timber.e("event $event not handling in this resolver $resolver, handle this event or change resolver")
    }
}
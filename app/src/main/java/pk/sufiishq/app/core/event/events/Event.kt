package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.core.event.handler.EventHandler
import kotlin.reflect.KClass

sealed class Event(val resolverClass: KClass<out EventHandler>)
package metron.event

import korlibs.event.Event
import korlibs.event.EventType
import korlibs.event.TEvent

class ResizedEvent : Event(), TEvent<ResizedEvent> {
    companion object : EventType<ResizedEvent>
    override val type: EventType<ResizedEvent> get() = ResizedEvent

    override fun toString(): String = "ResizedEvent()"
}

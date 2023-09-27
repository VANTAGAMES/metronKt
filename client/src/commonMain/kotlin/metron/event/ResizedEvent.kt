package metron.event

import korlibs.event.*

class ResizedEvent : Event(), TEvent<ResizedEvent> {
    companion object : EventType<ResizedEvent>
    override val type: EventType<ResizedEvent> get() = ResizedEvent

    override fun toString(): String = "ResizedEvent()"
}

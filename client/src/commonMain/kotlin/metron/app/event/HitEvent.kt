package event

import korlibs.event.*
import korlibs.time.*

class HitEvent(val delta: TimeSpan) : Event(), TEvent<HitEvent> {
    companion object : EventType<HitEvent>
    override val type: EventType<HitEvent> get() = HitEvent

    override fun toString(): String = "HitEvent()"
}

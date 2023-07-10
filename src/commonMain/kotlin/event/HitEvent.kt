package event

import Audit
import LivingStick
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class HitEvent : Event(), TEvent<HitEvent> {
    companion object : EventType<HitEvent>
    override val type: EventType<HitEvent> get() = HitEvent

    override fun toString(): String = "HitEvent()"
}

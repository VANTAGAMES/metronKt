package event

import LivingStick
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class StageStartEvent(
) : Event(), TEvent<StageStartEvent> {
    companion object : EventType<StageStartEvent>
    override val type: EventType<StageStartEvent> get() = StageStartEvent

    override fun toString(): String = "StageStartEvent()"
}

package event

import Audit
import LivingStick
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class GameEndEvent() : Event(), TEvent<GameEndEvent> {
    companion object : EventType<GameEndEvent>
    override val type: EventType<GameEndEvent> get() = GameEndEvent

    override fun toString(): String = "GameEndEvent()"
}

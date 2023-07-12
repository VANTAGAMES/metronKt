package event

import Audit
import LivingGhost
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class GameDisposeEvent() : Event(), TEvent<GameDisposeEvent> {
    companion object : EventType<GameDisposeEvent>
    override val type: EventType<GameDisposeEvent> get() = GameDisposeEvent

    override fun toString(): String = "GameDisposeEvent()"
}

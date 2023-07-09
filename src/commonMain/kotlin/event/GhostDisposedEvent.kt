package event

import LivingStick
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class GhostDisposedEvent(
    val isNaturally: Boolean
) : Event(), TEvent<GhostDisposedEvent> {
    companion object : EventType<GhostDisposedEvent>
    override val type: EventType<GhostDisposedEvent> get() = GhostDisposedEvent

    override fun toString(): String = "GhostSpawnEvent()"
}

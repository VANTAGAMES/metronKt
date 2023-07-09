package event

import Stick
import korlibs.event.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*

class GhostSpawnEvent(
    val angle: Angle,
    val lifeTime: TimeSpan,
    val view: Stick
) : Event(), TEvent<GhostSpawnEvent> {
    companion object : EventType<GhostSpawnEvent>
    override val type: EventType<GhostSpawnEvent> get() = GhostSpawnEvent

    override fun toString(): String = "GhostSpawnEvent()"
}

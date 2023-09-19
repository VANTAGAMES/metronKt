package event

import korlibs.event.*
import metron.app.components.*

class GhostSpawnEvent(
    val view: GhostStick
) : Event(), TEvent<GhostSpawnEvent> {
    companion object : EventType<GhostSpawnEvent>
    override val type: EventType<GhostSpawnEvent> get() = GhostSpawnEvent

    override fun toString(): String = "GhostSpawnEvent()"
}

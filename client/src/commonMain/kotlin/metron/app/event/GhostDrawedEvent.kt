package event

import korlibs.event.*
import metron.app.components.*

class GhostDrawedEvent(
    val ghostStick: GhostStick,
    val isNaturally: Boolean
) : Event(), TEvent<GhostDrawedEvent> {
    companion object : EventType<GhostDrawedEvent>
    override val type: EventType<GhostDrawedEvent> get() = GhostDrawedEvent

    override fun toString(): String = "GhostDisposedEvent()"
}

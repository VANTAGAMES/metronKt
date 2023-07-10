package event

import LivingGhost
import korlibs.event.*

class GhostDrawedEvent(
    val livingGhost: LivingGhost,
    val isNaturally: Boolean
) : Event(), TEvent<GhostDrawedEvent> {
    companion object : EventType<GhostDrawedEvent>
    override val type: EventType<GhostDrawedEvent> get() = GhostDrawedEvent

    override fun toString(): String = "GhostDisposedEvent()"
}

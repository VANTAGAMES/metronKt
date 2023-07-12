package event

import Audit
import LivingGhost
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class AuditEvent(
    val livingGhost: LivingGhost,
    val audit: Audit
) : Event(), TEvent<AuditEvent> {
    companion object : EventType<AuditEvent>
    override val type: EventType<AuditEvent> get() = AuditEvent

    override fun toString(): String = "AuditEvent()"
}

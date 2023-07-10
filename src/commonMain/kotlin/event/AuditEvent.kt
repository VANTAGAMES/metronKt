package event

import Audit
import LivingStick
import Stick
import korlibs.event.*
import korlibs.math.geom.*
import korlibs.time.*

class AuditEvent(
    val audit: Audit
) : Event(), TEvent<AuditEvent> {
    companion object : EventType<AuditEvent>
    override val type: EventType<AuditEvent> get() = AuditEvent

    override fun toString(): String = "AuditEvent()"
}

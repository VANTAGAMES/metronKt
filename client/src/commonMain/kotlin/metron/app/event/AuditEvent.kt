package event

import korlibs.event.*
import metron.app.*
import metron.app.components.*
import metron.app.systems.*

class AuditEvent(
    val ghostStick: GhostStick,
    val audit: AuditType
) : Event(), TEvent<AuditEvent> {
    companion object : EventType<AuditEvent>
    override val type: EventType<AuditEvent> get() = AuditEvent

    override fun toString(): String = "AuditEvent()"
}

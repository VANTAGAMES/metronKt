package metron

import korlibs.event.*

class SocketClosedEvent : Event(), TEvent<SocketClosedEvent> {
    companion object : EventType<SocketClosedEvent>
    override val type: EventType<SocketClosedEvent> get() = SocketClosedEvent
}

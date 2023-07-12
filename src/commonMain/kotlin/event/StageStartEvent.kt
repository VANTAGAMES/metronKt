package event

import korlibs.event.*

class StageStartEvent(
) : Event(), TEvent<StageStartEvent> {
    companion object : EventType<StageStartEvent>
    override val type: EventType<StageStartEvent> get() = StageStartEvent

    override fun toString(): String = "StageStartEvent()"
}

package event

import korlibs.event.*

class GameEndEvent() : Event(), TEvent<GameEndEvent> {
    companion object : EventType<GameEndEvent>
    override val type: EventType<GameEndEvent> get() = GameEndEvent

    override fun toString(): String = "GameEndEvent()"
}

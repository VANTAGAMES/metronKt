package event

import korlibs.event.*

class GameStartEvent : Event(), TEvent<GameStartEvent> {
    companion object : EventType<GameStartEvent>
    override val type: EventType<GameStartEvent> get() = GameStartEvent

    override fun toString(): String = "GameStartEvent()"
}

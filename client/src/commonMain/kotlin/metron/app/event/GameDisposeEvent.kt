package event

import korlibs.event.*

class GameDisposeEvent() : Event(), TEvent<GameDisposeEvent> {
    companion object : EventType<GameDisposeEvent>
    override val type: EventType<GameDisposeEvent> get() = GameDisposeEvent

    override fun toString(): String = "GameDisposeEvent()"
}

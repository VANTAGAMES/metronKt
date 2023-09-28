package event

import korlibs.event.*

class SettingsMenuToggleEvent : Event(), TEvent<SettingsMenuToggleEvent> {
    companion object : EventType<SettingsMenuToggleEvent>
    override val type: EventType<SettingsMenuToggleEvent> get() = SettingsMenuToggleEvent
    override fun toString(): String = "SettingsMenuToggleEvent()"
}

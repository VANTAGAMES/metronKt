package metron.app.systems

import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.World.Companion.family
import korlibs.io.async.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.time.*
import kotlinx.coroutines.*
import metron.*
import metron.app.Stage
import metron.app.components.*
import util.*

class PlayerStickSystem(val stage: Stage) : IteratingSystem(
    family { all(PlayerStick) }
){
    override fun onTickEntity(entity: Entity) {
        if (stage.isPaused) return
        entity[PlayerStick].body.rotation = stage.setTo(deltaTime.seconds)
    }
}

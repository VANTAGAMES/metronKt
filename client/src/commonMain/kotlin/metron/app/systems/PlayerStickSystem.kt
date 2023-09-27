package metron.app.systems

import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.World.Companion.family
import korlibs.time.*
import metron.app.*
import metron.app.components.*

class PlayerStickSystem(val stage: Stage) : IteratingSystem(
    family { all(PlayerStick) }
){
    override fun onTickEntity(entity: Entity) {
        if (stage.isPaused) return
        entity[PlayerStick].body.rotation = stage.setTo(deltaTime.seconds)
    }
}

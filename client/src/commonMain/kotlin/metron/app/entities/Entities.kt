package metron.app.entities

import korlibs.math.geom.*
import korlibs.time.*
import metron.app.*
import metron.app.components.*


fun Stage.createGhostNote(
    spawner: GhostSpawner, angle: Angle, lifeTime: TimeSpan, nextNote: TimeSpan
) = GhostStick(this, spawner, angle, lifeTime, nextNote).apply {
    world.entity {
        val note = this@apply
        it += note
    }
}
fun Stage.createPlayerStick() = world.entity {
    val stage = this@createPlayerStick
    it += PlayerStick(stage)
}


fun Stage.createGhostSpawner() = world.entity {
    it += GhostSpawner()
}

fun Stage.createAudit(ghostStick: GhostStick, audit: AuditType) = world.entity {
    it += Audit(ghostStick, audit)
}

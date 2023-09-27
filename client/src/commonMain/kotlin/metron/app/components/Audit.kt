package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*
import korlibs.image.text.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.handler.*
import metron.util.*
import kotlin.math.*

class Audit private constructor() : Component<Audit> {
    override fun type() = Companion

    companion object : ComponentHooks<Audit>() {
        operator fun invoke(ghostStick: GhostStick, audit: AuditType): Audit {
            val body = ghostStick.body
            val angle = body.rotation
            val offset = (-90).degrees
            body.noteHitEffect(stage = ghostStick.stage) {
                body.removeFromParent()
            }
            val stage = ghostStick.stage
            stage.lives.fastIterateRemove { it.body == body }
            camera.text(" ${audit.text} ", 45f, color = audit.color, font = stage.boldFont) {
                alignment = TextAlignment.CENTER
                globalPos = body.globalPos
                val auditHeight = stickHeight + 70
                pos = Point(pos.x + auditHeight * cos(angle + offset), pos.y + auditHeight * sin(angle + offset))
                setPositionRelativeTo(screen,
                    Point(
                        max(min(getPositionRelativeTo(screen).x, screen.width - width/2), width/2),
                        getPositionRelativeTo(screen).y
                    )
                )
                var elapsed = 0.milliseconds
                screen.onEvent(UpdateEvent) {
                    elapsed += it.deltaTime
                    zIndex = 2f
                    if (elapsed >= ghostStick.lifeTime) {
                        removeFromParent()
                    }
                }
            }
            return Audit()
        }
    }
}

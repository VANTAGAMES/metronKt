package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*
import korlibs.image.text.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.geom.max
import korlibs.math.geom.min
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.util.*

class Audit private constructor() : Component<Audit> {
    override fun type() = Companion

    companion object : ComponentHooks<Audit>() {
        operator fun invoke(ghostStick: GhostStick, audit: AuditType): Audit {
            val body = ghostStick.body
            val angle = body.rotation
            val offset = (-90).degrees
            body.noteHitEffect {
                body.removeFromParent()
            }
            val stage = ghostStick.stage
            stage.alives.fastIterateRemove { it.body == body }
            screen.text(" ${audit.text} ", 30f, color = audit.color, font = stage.boldFont) {
                filter = BlurFilter(0.5f)
                alignment = TextAlignment.CENTER
                globalPos = body.globalPos
                val auditHeight = stickHeight + 50
                pos = Point(pos.x + auditHeight * cos(angle + offset), pos.y + auditHeight * sin(angle + offset))
                var elapsed = 0.milliseconds
                onEvent(UpdateEvent) {
                    elapsed += it.deltaTime
                    zIndex = 2f
                    if (elapsed >= ghostStick.lifeTime/2) {
                        removeFromParent()
                    }
                }
            }
            return Audit()
        }
    }
}

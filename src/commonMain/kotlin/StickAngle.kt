import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*

class StickAngle(
    private val anglge: Angle,
    private val bpm: Double,
    private val easing: Easing,
    var elapsed: TimeSpan = 0.milliseconds,
    val offset: TimeSpan = 0.milliseconds
) {

    fun update(delta: TimeSpan) {
        elapsed += delta
    }

    fun setTo(delta: TimeSpan): Angle {
        update(delta)
        return performAngle()
    }

    fun performAngle(elapsed: TimeSpan = this.elapsed + this.offset): Angle {
        val durationInTween = (60.0 / bpm).seconds
        val elapsedInTween = run {
            val sum = elapsed % (durationInTween*2)
            if (sum > durationInTween) durationInTween*2 - sum else sum
        }
        val ratioInTween = elapsedInTween / durationInTween
        val easedRatioInTween = easing(ratioInTween)
        val ratio = easedRatioInTween.toRatio()
        return ratio.interpolateAngleNormalized(-anglge, anglge)
    }
}

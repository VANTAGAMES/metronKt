package util

import korlibs.math.geom.*


private val d180 = 180.degrees
private val d360 = 360.degrees
fun Angle.absBetween180degrees(other: Angle): Angle {
    val sum = abs(this - other)
    return if (sum >= d180) d360 - sum else sum
}

fun Angle.between180degrees(other: Angle): Angle {
    val angle = this - other
    val sum = abs(angle)
    return if (sum >= d180) (d360 - sum) * (if (angle < Angle.ZERO) 1 else -1)
    else angle
}


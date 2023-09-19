package metron.app

import korlibs.image.color.*

@Suppress("unused")
enum class AuditType(val text: String, val color: RGBA, val range: ClosedFloatingPointRange<Double>) {

    TOO_FAST("너무 빠름!", Colors.RED, -2.0..-1.0),
    TOO_SLOW("너무 느림!", Colors.RED, 1.0..2.0),
    FAST("빠름!", Colors.YELLOW, -1.0..-0.05),
    SLOW("느림!", Colors.YELLOW, 0.05..1.0),
    PERF("정확!", Colors.GREEN, -0.05..0.05),
}

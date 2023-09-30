package metron.app

import korlibs.image.color.*

@Suppress("unused")
enum class AuditType(val text: String, val color: RGBA, val range: ClosedFloatingPointRange<Double>) {
    TOO_FAST("너무 빠름!", Colors.RED, -0.4..-0.3),
    TOO_SLOW("너무 느림!", Colors.RED, 0.3..0.4),
    FAST("빠름!", Colors.YELLOW, -0.3..-0.1),
    SLOW("느림!", Colors.YELLOW, 0.1..0.3),
    PERF("정확!", Colors.GREEN, -0.1..0.1),
}

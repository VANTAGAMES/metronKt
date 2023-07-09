package util

import korlibs.image.color.*

object ColorUtil {
    fun hex2Rgb(colorStr: String): RGBA {
        var s = colorStr
        if (!s.startsWith("#")) s = "#$colorStr"
        return RGBA.invoke(
            s.substring(1, 3).toInt(16),
            s.substring(3, 5).toInt(16),
            s.substring(5, 7).toInt(16),
        )
    }

    fun String.hex() = hex2Rgb(this)

}

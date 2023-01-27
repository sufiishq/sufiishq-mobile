package pk.sufiishq.aurora.models

import androidx.compose.ui.graphics.Color

sealed class ColorPalette(val name: String, val color: Pair<Color, Color>) {
    object Clover : ColorPalette("Clover", Pair(Color(0xFF8BEE00), Color(0xFF68B300)))
    object Ocean : ColorPalette("Ocean", Pair(Color(0xFF0F76C4), Color(0xFF005493)))
    object Blackberry : ColorPalette("Blackberry", Pair(Color(0xFF742D53), Color(0xFF43182f)))
    object Blueberry : ColorPalette("Blueberry", Pair(Color(0xFF5E58BD), Color(0xFF464196)))
    object Grape : ColorPalette("Grape", Pair(Color(0xFF944584), Color(0xFF6c3461)))
}

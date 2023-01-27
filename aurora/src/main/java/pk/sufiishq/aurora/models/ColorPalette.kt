package pk.sufiishq.aurora.models

import androidx.compose.ui.graphics.Color

sealed class ColorPalette(val name: String, val color: Pair<Color, Color>) {
    object Clover : ColorPalette("Clover", Pair(Color(0xFF8BEE00), Color(0xFF68B300)))
    object Ocean : ColorPalette("Ocean", Pair(Color(0xFF0F76C4), Color(0xFF005493)))
    object Blackberry : ColorPalette("Blackberry", Pair(Color(0xFF742D53), Color(0xFF43182f)))
    object Blueberry : ColorPalette("Blueberry", Pair(Color(0xFF5E58BD), Color(0xFF464196)))
    object Grape : ColorPalette("Grape", Pair(Color(0xFF944584), Color(0xFF6c3461)))
    object Mud : ColorPalette("Mud", Pair(Color(0xFF9C7657), Color(0xFF70543e)))
    object Kiwi : ColorPalette("Kiwi", Pair(Color(0xFFA4E473), Color(0xFF7aab55)))
    object Mint : ColorPalette("Mint", Pair(Color(0xFFA2F5D1), Color(0xFF88ffcc)))
    object Mulberry : ColorPalette("Mulberry", Pair(Color(0xFFE2147B), Color(0xFF920a4e)))
    object Antler : ColorPalette("Antler", Pair(Color(0xFFCFA6A0), Color(0xFF957a76)))
    object Berry : ColorPalette("Berry", Pair(Color(0xFFAD3D6E), Color(0xFF990f4b)))
    object Camel : ColorPalette("Camel", Pair(Color(0xFFF89824), Color(0xFFbb6600)))
    object Lettuce : ColorPalette("Lettuce", Pair(Color(0xFFE3F7B6), Color(0xFFbed38e)))
    object Fox : ColorPalette("Fox", Pair(Color(0xFFF5BC7C), Color(0xFFc38743)))
    object Cherry : ColorPalette("Cherry", Pair(Color(0xFFF13B67), Color(0xFFcf0234)))
    object Koi : ColorPalette("Koi", Pair(Color(0xFFEC6D4A), Color(0xFFB6492C)))
    object Ice : ColorPalette("Ice", Pair(Color(0xFFd6fffa), Color(0xFF8BD1C9)))
    object Sand : ColorPalette("Sand", Pair(Color(0xFFe2ca76), Color(0xFFB39C51)))
    object Clementine : ColorPalette("Clementine", Pair(Color(0xFFe96e00), Color(0xFFB45703)))
    object Cashew : ColorPalette("Cashew", Pair(Color(0xFFedccb3), Color(0xFFA3856F)))
    object Coral : ColorPalette("Coral", Pair(Color(0xFFfc5a50), Color(0xFFC43830)))
    object Marigold : ColorPalette("Marigold", Pair(Color(0xFFfcc006), Color(0xFFD19F06)))
    object Carrot : ColorPalette("Carrot", Pair(Color(0xFFfd6f3b), Color(0xFFCA4E20)))
    object Canary : ColorPalette("Canary", Pair(Color(0xFFfdff63), Color(0xFFCFD13C)))
    object Papaya : ColorPalette("Papaya", Pair(Color(0xFFfea166), Color(0xFFBD7548)))
    object Royal : ColorPalette("Royal", Pair(Color(0xFF8B67E2), Color(0xFF6040AD)))
    object Olive : ColorPalette("Olive", Pair(Color(0xFF808000), Color(0xFF5F5F06)))
    object Forest : ColorPalette("Forest", Pair(Color(0xFF299E27), Color(0xFF187216)))
}

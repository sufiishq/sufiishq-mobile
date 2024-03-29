package pk.sufiishq.aurora.models

import androidx.compose.ui.graphics.Color

sealed class ColorPalette(val name: String, val color: Pair<Color, Color>) {
    object Ocean : ColorPalette("Ocean", Pair(Color(0xFF0F76C4), Color(0xFF005493)))
    object Clover : ColorPalette("Clover", Pair(Color(0xFF8BEE00), Color(0xFF68B300)))
    object Blackberry : ColorPalette("Blackberry", Pair(Color(0xFF742D53), Color(0xFF43182f)))
    object Blueberry : ColorPalette("Blueberry", Pair(Color(0xFF5E58BD), Color(0xFF464196)))
    object Grape : ColorPalette("Grape", Pair(Color(0xFF944584), Color(0xFF6c3461)))
    object Mud : ColorPalette("Mud", Pair(Color(0xFF9C7657), Color(0xFF70543e)))
    object Kiwi : ColorPalette("Kiwi", Pair(Color(0xFFA4E473), Color(0xFF7aab55)))
    object Mint : ColorPalette("Mint", Pair(Color(0xFFA4F1D1), Color(0xFF69C29C)))
    object Mulberry : ColorPalette("Mulberry", Pair(Color(0xFFE2147B), Color(0xFF920a4e)))
    object Antler : ColorPalette("Antler", Pair(Color(0xFFCFA6A0), Color(0xFF957a76)))
    object Berry : ColorPalette("Berry", Pair(Color(0xFFAD3D6E), Color(0xFF990f4b)))
    object Camel : ColorPalette("Camel", Pair(Color(0xFFF89824), Color(0xFFbb6600)))
    object Lettuce : ColorPalette("Lettuce", Pair(Color(0xFFE3F7B6), Color(0xFFbed38e)))
    object Fox : ColorPalette("Fox", Pair(Color(0xFFF5BC7C), Color(0xFFc38743)))
    object Cherry : ColorPalette("Cherry", Pair(Color(0xFF990F03), Color(0xFF720B02)))
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
    object Dolphin : ColorPalette("Dolphin", Pair(Color(0xFF524E5C), Color(0xFF35323C)))
    object Granite : ColorPalette("Granite", Pair(Color(0xFF2F3030), Color(0xFF232424)))
    object Pigeons : ColorPalette("Pigeons", Pair(Color(0xFF444444), Color(0xFF313131)))
    object Slate : ColorPalette("Slate", Pair(Color(0xFF222222), Color(0xFF131313)))
    object Pearl : ColorPalette("Pearl", Pair(Color(0xFFFBFCF7), Color(0xFFE9ECD9)))
    object Alabaster : ColorPalette("Alabaster", Pair(Color(0xFFFEFAF1), Color(0xFFEBE1CB)))
    object Snow : ColorPalette("Snow", Pair(Color(0xFFFBFCF7), Color(0xFFCEE4E7)))
    object Ivory : ColorPalette("Ivory", Pair(Color(0xFFFDF6E4), Color(0xFFEEE3C6)))
    object Cream : ColorPalette("Cream", Pair(Color(0xFFFFFADA), Color(0xFFE7DEA3)))
    object Salt : ColorPalette("Salt", Pair(Color(0xFFE6DECC), Color(0xFFCFC09E)))
    object Tan : ColorPalette("Tan", Pair(Color(0xFFE6DBAD), Color(0xFFD8CA90)))
    object Beige : ColorPalette("Beige", Pair(Color(0xFFDDC98A), Color(0xFFD1BA70)))
    object Macaroon : ColorPalette("Macaroon", Pair(Color(0xFFF8E076), Color(0xFFE4C951)))
    object Sepia : ColorPalette("Sepia", Pair(Color(0xFFE3B778), Color(0xFFBE9151)))
    object Yellow : ColorPalette("Yellow", Pair(Color(0xFFFDE64B), Color(0xFFCAB737)))
    object Gold : ColorPalette("Gold", Pair(Color(0xFFF9A600), Color(0xFFC08001)))
    object Lemon : ColorPalette("Lemon", Pair(Color(0xFFEFFD5F), Color(0xFFBFCA4B)))
    object Orange : ColorPalette("Orange", Pair(Color(0xFFED7015), Color(0xFFCC6012)))
    object Tangerine : ColorPalette("Tangerine", Pair(Color(0xFFFA8228), Color(0xFFCC6A21)))
    object Cider : ColorPalette("Cider", Pair(Color(0xFFB46726), Color(0xFF864D1D)))
    object Rust : ColorPalette("Rust", Pair(Color(0xFF8C4004), Color(0xFF642E03)))
    object Tiger : ColorPalette("Tiger", Pair(Color(0xFFFD6B00), Color(0xFFCA5600)))
    object Fire : ColorPalette("Fire", Pair(Color(0xFFDD561C), Color(0xFFBB4918)))
    object Cantaloupe : ColorPalette("Cantaloupe", Pair(Color(0xFFFCA172), Color(0xFFCA815B)))
    object Apricot : ColorPalette("Apricot", Pair(Color(0xFFED810D), Color(0xFFC26A0C)))
    object Honey : ColorPalette("Honey", Pair(Color(0xFFEC9705), Color(0xFFBB7804)))
    object Amber : ColorPalette("Amber", Pair(Color(0xFF893100), Color(0xFF662400)))
    object Red : ColorPalette("Red", Pair(Color(0xFFCF322D), Color(0xFFAA2824)))
    object Rose : ColorPalette("Rose", Pair(Color(0xFFE2262C), Color(0xFFC01D22)))
    object Jam : ColorPalette("Jam", Pair(Color(0xFF600F0A), Color(0xFF3C0906)))
    object Merlot : ColorPalette("Merlot", Pair(Color(0xFF541F1B), Color(0xFF331310)))
    object Crimson : ColorPalette("Crimson", Pair(Color(0xFFB80E0B), Color(0xFF940B08)))
    object Ruby : ColorPalette("Ruby", Pair(Color(0xFF900602), Color(0xFF690502)))
    object Wine : ColorPalette("Wine", Pair(Color(0xFF710C03), Color(0xFF460802)))
    object Blush : ColorPalette("Blush", Pair(Color(0xFFBC5449), Color(0xFF924038)))
    object Candy : ColorPalette("Candy", Pair(Color(0xFFD21403), Color(0xFFAA0E00)))
    object Pink : ColorPalette("Pink", Pair(Color(0xFFF69ACD), Color(0xFFC77AA5)))
    object Fuchsia : ColorPalette("Fuchsia", Pair(Color(0xFFFC45AA), Color(0xFFCF378B)))
    object Punch : ColorPalette("Punch", Pair(Color(0xFFF15378), Color(0xFFCA4766)))
    object Magenta : ColorPalette("Magenta", Pair(Color(0xFFE21484), Color(0xFFBB116D)))
    object HotPink : ColorPalette("Hot Pink", Pair(Color(0xFFFF1695), Color(0xFFCE1178)))
    object Purple : ColorPalette("Purple", Pair(Color(0xFFA32CC4), Color(0xFF83239E)))
    object Mauve : ColorPalette("Mauve", Pair(Color(0xFF7A4A88), Color(0xFF583663)))
    object Violet : ColorPalette("Violet", Pair(Color(0xFF710093), Color(0xFF560070)))
    object Plum : ColorPalette("Plum", Pair(Color(0xFF601A36), Color(0xFF461227)))
    object Raisin : ColorPalette("Raisin", Pair(Color(0xFF290916), Color(0xFF14030A)))
    object Blue : ColorPalette("Blue", Pair(Color(0xFF3A43BA), Color(0xFF2B3291)))
    object Sky : ColorPalette("Sky", Pair(Color(0xFF61C5DA), Color(0xFF4EA2B4)))
    object Navy : ColorPalette("Navy", Pair(Color(0xFF0B1171), Color(0xFF070B4D)))
    object Teal : ColorPalette("Teal", Pair(Color(0xFF48AAAD), Color(0xFF378183)))
    object Spruce : ColorPalette("Spruce", Pair(Color(0xFF2C3D4C), Color(0xFF1A2530)))
    object Stone : ColorPalette("Stone", Pair(Color(0xFF58788D), Color(0xFF425B6B)))
    object Aegean : ColorPalette("Aegean", Pair(Color(0xFF1E456E), Color(0xFF142E49)))
    object Denim : ColorPalette("Denim", Pair(Color(0xFF151E3D), Color(0xFF090E1F)))
    object Green : ColorPalette("Green", Pair(Color(0xFF3BB143), Color(0xFF2B8531)))
    object Lime : ColorPalette("Lime", Pair(Color(0xFFAFFC38), Color(0xFF8FCF2C)))
    object Juniper : ColorPalette("Juniper", Pair(Color(0xFF3A5312), Color(0xFF25350B)))
    object Sage : ColorPalette("Sage", Pair(Color(0xFF728C69), Color(0xFF51644A)))
    object Fern : ColorPalette("Fern", Pair(Color(0xFF5CBC63), Color(0xFF47924C)))
    object SeaFoam : ColorPalette("Sea foam", Pair(Color(0xFF3DEC97), Color(0xFF31C47C)))
    object Brown : ColorPalette("Brown", Pair(Color(0xFF231609), Color(0xFF0F0903)))
    object Coffee : ColorPalette("Coffee", Pair(Color(0xFF4B371C), Color(0xFF2E2110)))
    object Peanut : ColorPalette("Peanut", Pair(Color(0xFF785C34), Color(0xFF554124)))
    object Caramel : ColorPalette("Caramel", Pair(Color(0xFF65340F), Color(0xFF49250A)))
    object Shadow : ColorPalette("Shadow", Pair(Color(0xFF373737), Color(0xFF201F1F)))
    object Flint : ColorPalette("Flint", Pair(Color(0xFF7E7D9C), Color(0xFF62617A)))
    object Pebble : ColorPalette("Pebble", Pair(Color(0xFF333333), Color(0xFF222222)))
    object Azure : ColorPalette("Azure", Pair(Color(0xFF007FFF), Color(0xFF016BD6)))
}
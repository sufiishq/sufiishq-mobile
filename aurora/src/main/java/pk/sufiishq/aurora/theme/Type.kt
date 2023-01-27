package pk.sufiishq.aurora.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import pk.sufiishq.aurora.R

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = FontFamily(
        Font(R.font.ubuntu_th, FontWeight.Thin),
        Font(R.font.ubuntu_l, FontWeight.Light),
        Font(R.font.ubuntu_r, FontWeight.Normal),
        Font(R.font.ubuntu_m, FontWeight.Medium),
        Font(R.font.ubuntu_b, FontWeight.Bold)
    )
)
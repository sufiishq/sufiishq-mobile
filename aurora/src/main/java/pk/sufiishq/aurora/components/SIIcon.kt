package pk.sufiishq.aurora.components

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIIcon(
    @DrawableRes resId: Int,
    tint: AuroraColor? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {

    val tintColor = tint?.color?.invoke()
        ?: Color.Unspecified

    onClick?.apply {
        IconButton(onClick = this) {
            SITweakIcon(
                resId = resId,
                tint = tintColor,
                modifier = modifier
            )
        }
    } ?: SITweakIcon(
        resId = resId,
        tint = tintColor,
        modifier = modifier
    )
}

@Composable
internal fun SITweakIcon(
    @DrawableRes resId: Int,
    tint: Color,
    modifier: Modifier
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = resId),
        tint = tint,
        contentDescription = null
    )
}

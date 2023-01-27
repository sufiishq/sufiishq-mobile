package pk.sufiishq.aurora.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun SIImage(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    tintColor: AuroraColor? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val colorFilter = tintColor?.let {
        ColorFilter.tint(it.color())
    }

    Image(
        modifier = modifier,
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}

@Composable
fun SIImage(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        modifier = modifier,
        contentScale = contentScale,
        bitmap = bitmap,
        alignment = alignment,
        contentDescription = null
    )
}
package pk.sufiishq.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import pk.sufiishq.app.utils.assetsToBitmap
import pk.sufiishq.app.utils.rem

@Composable
fun AssetImage(
    path: String,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    modifier: Modifier
) {

    val context = LocalContext.current
    val bitmap by rem(context.assetsToBitmap(path))

    bitmap?.asImageBitmap()?.apply {
        Image(
            modifier = modifier,
            contentScale = contentScale,
            bitmap = this,
            alignment = alignment,
            contentDescription = null
        )
    }
}
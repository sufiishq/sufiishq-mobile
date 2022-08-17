package pk.sufiishq.app.ui.components

import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TileAndroidImage(
    @DrawableRes drawableId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val drawable = remember(drawableId) {
        BitmapDrawable(
            context.resources,
            BitmapFactory.decodeResource(
                context.resources,
                drawableId
            )
        ).apply {
            tileModeX = Shader.TileMode.REPEAT
            tileModeY = Shader.TileMode.REPEAT
        }
    }
    AndroidView(
        factory = {
            ImageView(it)
        },
        update = { imageView ->
            imageView.background = drawable
        },
        modifier = modifier
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Image
            }
    )
}
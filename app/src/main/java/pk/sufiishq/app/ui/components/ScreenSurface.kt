package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import pk.sufiishq.app.R
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun ContentBackground(
    content: @Composable () -> Unit
) {
    SIBox (
        bgColor = AuroraColor.Surface,
        modifier = Modifier.fillMaxSize()
    ) {
        TileAndroidImage(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
            drawableId = R.drawable.pattern,
            contentDescription = ""
        )

        content()
    }
}
package pk.sufiishq.app.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.IconProvider
import pk.sufiishq.app.utils.MenuIconColors

@Composable
fun PopupMenuLabel(label: String, drawableId: Int? = null, iconTint: Color? = null) {
    Row(
        modifier = Modifier.width(150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = drawableId ?: IconProvider.getIcon(label).drawableId),
            tint = iconTint ?: IconProvider.getIcon(label).color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 14.sp
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PopupMenuLabelPreviewLight() {
    SufiIshqTheme {
        PopupMenuLabel(
            label = "Mark as Favorite",
            drawableId = R.drawable.ic_round_favorite_24,
            iconTint = MenuIconColors.FAVORITE
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PopupMenuLabelPreviewDark() {
    SufiIshqTheme(darkTheme = true) {
        PopupMenuLabel(
            label = "Mark as Favorite",
            drawableId = R.drawable.ic_round_favorite_24,
            iconTint = MenuIconColors.FAVORITE
        )
    }
}

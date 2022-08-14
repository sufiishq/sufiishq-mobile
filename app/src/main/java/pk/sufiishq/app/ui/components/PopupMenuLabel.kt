package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.utils.IconProvider

@Composable
fun PopupMenuLabel(label: String) {
    Row(
        modifier = Modifier.width(150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val menuIcon = IconProvider.getIcon(label)
        Icon(
            painter = painterResource(id = menuIcon.drawableId),
            tint = menuIcon.color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 14.sp
        )
    }
}
package pk.sufiishq.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R

@Composable
fun MainNavigationButton(
    backgroundColor: Color,
    bgColor: Color,
    fgColor: Color
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        backgroundColor = backgroundColor
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TileAndroidImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                drawableId = R.drawable.pattern,
                contentDescription = "",
            )

            Card(
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .clip(CircleShape)
                    .clickable {

                    },
                backgroundColor = bgColor
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_round_menu_24),
                    tint = fgColor,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun MainNavigationMenu(
    expanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {


    }
}
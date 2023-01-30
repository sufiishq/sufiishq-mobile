package pk.sufiishq.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.layout.SICard
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun MainNavigationButton(
    scaffoldState: ScaffoldState
) {

    val coroutineScope = rememberCoroutineScope()

    SICard(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
    ) {

        ContentBackground {
            SICard(
                modifier = Modifier
                    .fillMaxSize(0.73f)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                bgColor = AuroraColor.SecondaryVariant
            ) { contentColor ->
                SIIcon(
                    modifier = Modifier.padding(8.dp),
                    resId = R.drawable.ic_round_menu_24,
                    tint = contentColor,
                )
            }
        }
    }
}


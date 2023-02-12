package pk.sufiishq.aurora.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SIExpandableCard(
    header: @Composable (RowScope.(onColor: AuroraColor) -> Unit)? = null,
    isExpanded: Boolean = true,
    onHeaderClick: () -> Unit = {},
    body: @Composable ColumnScope.(onColor: AuroraColor) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Card {
            Column {

                // card header
                header?.apply {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AuroraColor.SecondaryVariant.color())
                            .clickable(onClick = onHeaderClick)
                            .padding(12.dp, 12.dp)
                    ) {
                        header(AuroraColor.SecondaryVariant.getForegroundColor())
                    }
                }

                // card body
                AnimatedVisibility(
                    visible = isExpanded
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AuroraColor.Background.color())
                            .padding(12.dp)
                    ) {
                        body(AuroraColor.Background.getForegroundColor())
                    }
                }
            }
        }
    }
}
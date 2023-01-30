package pk.sufiishq.aurora.layout

import androidx.annotation.IntRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.theme.AuroraColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SIDialog(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Surface,
    borderColor: AuroraColor = AuroraColor.Background,
    onNoText: String? = null,
    onNoClick: () -> Unit = {},
    onYesText: String? = null,
    onYesClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    @IntRange(from = 0, to = 12) innerPadding: Int = 12,
    content: @Composable ColumnScope.(fgColor: AuroraColor) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Surface(
            modifier = modifier.padding(36.dp, 0.dp),
            shape = RoundedCornerShape(8.dp),
            color = bgColor.color(),
            border = BorderStroke(1.dp, borderColor.color())
        ) {
            SIColumn(
                bgColor = bgColor,
                padding = innerPadding
            ) { fgColor ->

                content(fgColor)

                if (onNoText != null || onYesText != null) {
                    SIHeightSpace(value = 8)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    onNoText?.apply {
                        SIText(
                            text = this,
                            textColor = fgColor,
                            textSize = TextSize.Small,
                            onClick = {
                                onNoClick()
                            }
                        )
                    }

                    onYesText?.apply {
                        SIText(
                            text = this,
                            textColor = fgColor,
                            textSize = TextSize.Small,
                            onClick = {
                                onYesClick()
                            }
                        )
                    }
                }
            }
        }
    }
}
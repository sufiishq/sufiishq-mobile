package pk.sufiishq.aurora.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SISpace(
    modifier: Modifier
) {
    Spacer(modifier = modifier)
}

@Composable
fun SIWidthSpace(
    value: Int
) {
    SISpace(
        modifier = Modifier.width(value.dp)
    )
}

@Composable
fun SIHeightSpace(
    value: Int
) {
    SISpace(
        modifier = Modifier.height(value.dp)
    )
}
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
    modifier: Modifier = Modifier,
    value: Int
) {
    SISpace(
        modifier = modifier.width(value.dp)
    )
}

@Composable
fun SIHeightSpace(
    modifier: Modifier = Modifier,
    value: Int
) {
    SISpace(
        modifier = modifier.height(value.dp)
    )
}
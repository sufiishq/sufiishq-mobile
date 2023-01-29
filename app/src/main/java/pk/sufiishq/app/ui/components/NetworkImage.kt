package pk.sufiishq.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox

private enum class State {
    Loading, Done
}

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String
) {

    val state = rem(State.Loading)

    SIBox {

        AsyncImage(
            modifier = modifier,
            model = url,
            contentDescription = null,
            onLoading = {
                state.value = State.Loading
            },
            onError = {
                state.value = State.Done
            },
            onSuccess = {
                state.value = State.Done
            }
        )

        if (state.value == State.Loading) {
            SIBox(padding = 12) {
                SICircularProgressIndicator(
                    strokeWidth = 2
                )
            }
        }
    }
}
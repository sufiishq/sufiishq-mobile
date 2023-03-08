package pk.sufiishq.aurora.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun <T> rem(value: T): MutableState<T> {
    return remember { mutableStateOf(value) }
}

@Composable
internal fun LazyListState.isScrollingUp(): Boolean {
    val previousIndex = remember(this) { mutableStateOf(firstVisibleItemIndex) }
    val previousScrollOffset = remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex.value != firstVisibleItemIndex) {
                previousIndex.value > firstVisibleItemIndex
            } else {
                previousScrollOffset.value >= firstVisibleItemScrollOffset
            }
                .also {
                    previousIndex.value = firstVisibleItemIndex
                    previousScrollOffset.value = firstVisibleItemScrollOffset
                }
        }
    }
        .value
}
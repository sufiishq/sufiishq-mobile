package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyKalamDataProvider

@Composable
fun SearchTextField(
    searchText: MutableState<String>,
    eventDispatcher: EventDispatcher,
    matColors: Colors,
    lazyKalamItems: LazyPagingItems<Kalam>,
    trackListType: TrackListType
) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = searchText.value,
        onValueChange = {
            searchText.value = it
            eventDispatcher.dispatch(KalamEvents.SearchKalam(it, trackListType))
            lazyKalamItems.refresh()
        },
        placeholder = {
            Text("Search in ${trackListType.title}", color = matColors.primary)
        },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = matColors.primary,
            backgroundColor = matColors.primaryVariant,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SearchTextFieldPreviewLight() {
    SufiIshqTheme(darkTheme = false) {
        val data = dummyKalamDataProvider()
        SearchTextField(
            remember { mutableStateOf("") },
            EventDispatcher(),
            MaterialTheme.colors,
            data.getKalamDataFlow().collectAsLazyPagingItems(),
            TrackListType.All()
        )
    }
}
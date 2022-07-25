package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.data.providers.PlaylistDataProvider
import pk.sufiishq.app.models.Playlist
import pk.sufiishq.app.ui.components.PlaylistItem
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.dummyPlaylistDataProvider

@Composable
fun PlaylistView(playlistDataProvider: PlaylistDataProvider, navController: NavController) {
    val matColors = MaterialTheme.colors
    val openDialog = remember { mutableStateOf(false) }
    val playlist = remember { mutableStateOf(Playlist(0, "")) }
    val allPlaylist = playlistDataProvider.getAll().toMutableStateList()

    Surface {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    playlist.value = Playlist(0, "")
                    openDialog.value = true
                }) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .background(matColors.secondaryVariant)) {
                if (allPlaylist.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        itemsIndexed(allPlaylist) { index, pl ->
                            PlaylistItem(
                                matColors,
                                pl,
                                allPlaylist,
                                playlistDataProvider,
                                navController
                            ) {
                                playlist.value = it
                                openDialog.value = true
                            }

                            if (index < allPlaylist.lastIndex) {
                                Divider(color = matColors.background)
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No playlist found"
                        )
                    }
                }
            }
        }
    }

    if (openDialog.value) {
        AddPlaylistDialog(
            playlist = playlist.value,
            onDismiss = { openDialog.value = false }
        ) {
            if (it.id == 0) playlistDataProvider.add(it) else playlistDataProvider.update(it)
            openDialog.value = false
        }
    }


}

@Composable
fun AddPlaylistDialog(
    playlist: Playlist,
    onDismiss: () -> Unit,
    onConfirmClicked: (playlist: Playlist) -> Unit
) {

    val playlistTitle = remember { mutableStateOf(playlist.title) }
    val error =
        remember { mutableStateOf(if (playlist.title.isEmpty()) "Title cannot be empty" else "") }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // TITLE
                Text(text = "Playlist", style = MaterialTheme.typography.subtitle1)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    OutlinedTextFieldValidation(
                        value = playlistTitle.value,
                        onValueChange = {
                            playlistTitle.value = it
                            if (playlistTitle.value.isEmpty()) {
                                error.value = "Title cannot be empty"
                            } else {
                                error.value = ""
                            }
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            if (playlistTitle.value.trim().isNotEmpty()) {
                                onConfirmClicked(Playlist(playlist.id, playlistTitle.value.trim()))
                            }
                        }),
                        modifier = Modifier.padding(top = 8.dp),
                        label = {
                            Text(text = "Title")
                        },
                        error = error.value
                    )
                }

                // BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        if (playlistTitle.value.trim().isNotEmpty()) {
                            onConfirmClicked(Playlist(playlist.id, playlistTitle.value.trim()))
                        }
                    }) {
                        Text(text = if (playlist.title.isEmpty()) "Add" else "Update")
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedTextFieldValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(0.8f),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    error: String = "",
    isError: Boolean = error.isNotEmpty(),
    trailingIcon: @Composable (() -> Unit)? = {
        if (error.isNotEmpty())
            Icon(Icons.Filled.Close, "error", tint = MaterialTheme.colors.error)
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.Black
    )

) {

    Column {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = keyboardActions,
            maxLines = 1,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLightPlaylistView() {
    SufiIshqTheme(darkTheme = false) {
        PlaylistView(
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkPlaylistView() {
    SufiIshqTheme(darkTheme = true) {
        PlaylistView(
            playlistDataProvider = dummyPlaylistDataProvider(),
            navController = rememberNavController()
        )
    }
}

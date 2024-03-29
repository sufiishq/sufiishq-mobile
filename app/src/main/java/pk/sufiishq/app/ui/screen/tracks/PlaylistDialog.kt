/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.feature.kalam.controller.KalamController
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.aurora.components.SIDivider
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.widgets.SIDataRow

@PackagePrivate
@Composable
fun PlaylistDialog(
    kalamController: KalamController,
) {
    kalamController.showPlaylistDialog().observeAsState().value?.apply {
        val kalam = first
        val playlistItems = second

        SIDialog(
            title = optString(TextRes.title_playlist),
            onDismissRequest = { kalamController.dismissPlaylistDialog() },
        ) {
            SILazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(0.dp, 300.dp),
                contentPadding = PaddingValues(0.dp),
                content = {
                    itemsIndexed(playlistItems) { index, item ->
                        SIDataRow(
                            title = item.title,
                            rowHeight = 50,
                            onClick = { kalamController.addToPlaylist(kalam, item) },
                        )

                        if (index < playlistItems.lastIndex) {
                            SIDivider()
                        }
                    }
                },
            )
        }
    }
}

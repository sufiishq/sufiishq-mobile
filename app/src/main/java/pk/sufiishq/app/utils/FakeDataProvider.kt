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

package pk.sufiishq.app.utils

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.app.controller.DashboardController
import pk.sufiishq.app.feature.app.controller.MainController
import pk.sufiishq.app.feature.app.model.NavigationItem
import pk.sufiishq.app.feature.events.model.Event
import pk.sufiishq.app.feature.help.HelpDataType
import pk.sufiishq.app.feature.help.controller.HelpController
import pk.sufiishq.app.feature.help.model.HelpContent
import pk.sufiishq.app.feature.hijridate.model.HijriDate
import pk.sufiishq.app.feature.kalam.downloader.KalamDownloadState
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.feature.kalam.model.KalamDeleteItem
import pk.sufiishq.app.feature.kalam.model.KalamInfo
import pk.sufiishq.app.feature.kalam.splitter.SplitKalamInfo
import pk.sufiishq.app.feature.kalam.splitter.SplitStatus
import pk.sufiishq.app.feature.player.controller.PlayerController
import pk.sufiishq.app.feature.playlist.controller.PlaylistController
import pk.sufiishq.app.feature.playlist.model.Playlist
import pk.sufiishq.aurora.models.DataMenuItem

// ---------------------------------------------------------------------------------- //
// DASHBOARD DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeDashboardController() =
    object : DashboardController {
        override fun getMainNavigationItems(): List<NavigationItem> = listOf()
        override fun countAll(): LiveData<Int> = MutableLiveData(150)
        override fun countFavorites(): LiveData<Int> = MutableLiveData(15)
        override fun countDownloads(): LiveData<Int> = MutableLiveData(35)
        override fun countPlaylist(): LiveData<Int> = MutableLiveData(5)
        override fun getHighlightAvailable(): LiveData<Highlight?> = MutableLiveData(null)
    }

// ---------------------------------------------------------------------------------- //
// PLAYER DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakePlayerController() =
    object : PlayerController {

        override fun getKalamInfo(): LiveData<KalamInfo?> = MutableLiveData(null)
        override fun updateSeekbarValue(value: Float) = Unit
        override fun onSeekbarChanged(value: Int) = Unit
        override fun doPlayOrPause() = Unit
    }

// ---------------------------------------------------------------------------------- //
// KALAM DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeKalamController() =
    object : pk.sufiishq.app.feature.kalam.controller.KalamController {

        override fun getKalamDataFlow(): Flow<PagingData<Kalam>> = flow {
            PagingData.from(
                listOf(
                    fakeKalam(),
                ),
            )
        }

        override fun searchKalam(keyword: String, trackListType: TrackListType) = Unit
        override fun popupMenuItems(kalam: Kalam, trackType: String): List<DataMenuItem> = listOf()

        override fun changeTrack(kalam: Kalam, trackListType: TrackListType) = Unit

        override fun shareKalam(kalam: Kalam, componentActivity: ComponentActivity) = Unit

        override fun markAsFavorite(kalam: Kalam) = Unit
        override fun removeFavorite(kalam: Kalam) = Unit

        override fun delete(kalamDeleteItem: KalamDeleteItem) = Unit
        override fun showKalamConfirmDeleteDialog(): LiveData<KalamDeleteItem?> =
            MutableLiveData(null)

        override fun showKalamConfirmDeleteDialog(kalamDeleteItem: KalamDeleteItem?) = Unit

        override fun addToPlaylist(kalam: Kalam, playlist: Playlist) = Unit
        override fun showPlaylistDialog(): LiveData<Pair<Kalam, List<Playlist>>?> =
            MutableLiveData(null)

        override fun showPlaylistDialog(kalam: Kalam) = Unit
        override fun dismissPlaylistDialog() = Unit

        override fun getKalamDownloadState(): LiveData<KalamDownloadState> =
            MutableLiveData(KalamDownloadState.Idle)

        override fun startDownload(kalam: Kalam) = Unit
        override fun dismissDownload() = Unit

        override fun showKalamSplitDialog(kalam: Kalam) = Unit
        override fun showKalamSplitDialog(): LiveData<SplitKalamInfo?> = MutableLiveData(null)
        override fun startSplitting() = Unit
        override fun playSplitKalamPreview() = Unit
        override fun setSplitStart(start: Int) = Unit
        override fun setSplitEnd(end: Int) = Unit
        override fun setSplitStatus(status: SplitStatus) = Unit
        override fun updateSplitSeekbarValue(value: Float) = Unit
        override fun saveSplitKalam(sourceKalam: Kalam, kalamTitle: String) = Unit
        override fun dismissKalamSplitDialog() = Unit
    }

@ExcludeFromJacocoGeneratedReport
fun fakeKalam() = Kalam(1, "Kalam Title", 2, "1993", "Karachi", "", "", 0, 0)

// ---------------------------------------------------------------------------------- //
// PLAYLIST DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakePlaylistController() =
    object : PlaylistController {

        override fun getPopupMenuItems(): List<DataMenuItem> = listOf()
        override fun showAddUpdatePlaylistDialog(): LiveData<Playlist?> = MutableLiveData(null)
        override fun showConfirmDeletePlaylistDialog(): LiveData<Playlist?> = MutableLiveData(null)
        override fun getAll(): LiveData<List<Playlist>> =
            MutableLiveData(
                listOf(
                    Playlist(1, "Karachi"),
                    Playlist(2, "Lahore"),
                ),
            )

        override fun get(id: Int) = MutableLiveData(Playlist(1, "Karachi"))
        override fun add(playlist: Playlist) = Unit
        override fun showAddUpdatePlaylistDialog(playlist: Playlist?) = Unit
        override fun showConfirmDeletePlaylistDialog(playlist: Playlist?) = Unit
        override fun update(playlist: Playlist) = Unit
        override fun delete(playlist: Playlist) = Unit
    }

@ExcludeFromJacocoGeneratedReport
fun fakePlaylist() = Playlist(1, "Karachi")

// ---------------------------------------------------------------------------------- //
// GLOBAL DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeMainController() =
    object : MainController {

        override fun popupMenuItems(): List<DataMenuItem> = listOf()
        override fun showUpdateButton(): LiveData<Boolean> = MutableLiveData(false)
        override fun handleUpdate() = Unit
        override fun openFacebookGroup(context: Context, groupUrl: String) = Unit
        override fun shareApp(activity: ComponentActivity) = Unit
        override fun getUpcomingEvents(): LiveData<List<Event>> = MutableLiveData(listOf())
        override fun checkUpdate(activity: ComponentActivity) = Unit
        override fun showUpdateButton(value: Boolean) = Unit
        override fun unregisterListener(activity: ComponentActivity) = Unit
        override fun getHijriDate(): LiveData<HijriDate?> = MutableLiveData(null)
    }

// ---------------------------------------------------------------------------------- //
// HELP DATA PROVIDER
// ---------------------------------------------------------------------------------- //

@ExcludeFromJacocoGeneratedReport
fun fakeHelpController() =
    object : HelpController {

        override fun getHelpContent(): Flow<List<HelpContent>> =
            MutableStateFlow(
                listOf(
                    HelpContent(
                        "First Title",
                        listOf(
                            HelpDataType.Paragraph(buildAnnotatedString { append("some paragraph") }),
                            HelpDataType.Photo("Path"),
                            HelpDataType.Paragraph(buildAnnotatedString { append("another paragraph") }),
                            HelpDataType.Divider(2),
                            HelpDataType.Paragraph(buildAnnotatedString { append("another paragraph") }),
                            HelpDataType.Spacer(10),
                            HelpDataType.Paragraph(
                                buildAnnotatedString {
                                    append("another paragraph with some **bold** and __italic__ words")
                                },
                            ),
                        ),
                    ),
                    HelpContent(
                        "Second Title",
                        listOf(HelpDataType.Paragraph(buildAnnotatedString { append("Test 2") })),
                    ),
                ),
            )
    }

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

package pk.sufiishq.app.helpers

import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString

sealed class TrackListType(val type: String, val title: String) {
    class All(title: String = getString(R.string.title_all_kalam)) :
        TrackListType(ScreenType.Tracks.ALL, title)

    class Downloads(title: String = getString(R.string.title_downloads)) :
        TrackListType(ScreenType.Tracks.DOWNLOADS, title)

    class Favorites(title: String = getString(R.string.title_favorites)) :
        TrackListType(ScreenType.Tracks.FAVORITES, title)

    class Playlist(title: String, val playlistId: Int) :
        TrackListType(ScreenType.Tracks.PLAYLIST, title)
}

fun String.createTrackListType(
    playlistTitle: String? = null,
    playlistId: Int? = null,
): TrackListType {
    return when (this) {
        ScreenType.Tracks.ALL -> TrackListType.All()
        ScreenType.Tracks.DOWNLOADS -> TrackListType.Downloads()
        ScreenType.Tracks.FAVORITES -> TrackListType.Favorites()
        ScreenType.Tracks.PLAYLIST -> TrackListType.Playlist(playlistTitle!!, playlistId!!)
        else -> throw IllegalArgumentException("type: $this is not a valid argument")
    }
}

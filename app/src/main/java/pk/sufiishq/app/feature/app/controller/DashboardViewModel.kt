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

package pk.sufiishq.app.feature.app.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pk.sufiishq.app.feature.admin.highlight.HighlightManager
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.app.model.NavigationItem
import pk.sufiishq.app.feature.kalam.data.repository.KalamRepository
import pk.sufiishq.app.feature.playlist.data.repository.PlaylistRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject
constructor(
    private val kalamRepository: KalamRepository,
    private val highlightManager: HighlightManager,
    private val playlistRepository: PlaylistRepository,
    private val mainNavigationItems: List<NavigationItem>,
) : ViewModel(), DashboardController {

    override fun getMainNavigationItems(): List<NavigationItem> = mainNavigationItems
    override fun countAll(): LiveData<Int> = kalamRepository.countAll()
    override fun countFavorites(): LiveData<Int> = kalamRepository.countFavorites()
    override fun countDownloads(): LiveData<Int> = kalamRepository.countDownloads()
    override fun countPlaylist(): LiveData<Int> = playlistRepository.countAll()

    // -------------------------------------------------------------------- //
    // highlight available check functionality
    // -------------------------------------------------------------------- //

    override fun getHighlightAvailable(): LiveData<Highlight?> {
        return highlightManager.getHighlightAvailable()
    }
}

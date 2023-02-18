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

package pk.sufiishq.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pk.sufiishq.app.feature.app.popupmenu.AppBarPopupMenu
import pk.sufiishq.app.feature.kalam.popupmenu.KalamItemPopupMenu
import pk.sufiishq.app.feature.playlist.popupmenu.PlaylistItemPopupMenu
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.di.qualifier.KalamItemPopupMenuItems
import pk.sufiishq.app.di.qualifier.PlaylistItemPopupMenuItems
import pk.sufiishq.app.helpers.popupmenu.PopupMenu
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppBarPopupMenuModule {

    @AppBarPopupMenuItems
    @Singleton
    @Binds
    fun provideAppBarPopupMenu(appBarPopupMenu: AppBarPopupMenu): PopupMenu
}

@Module
@InstallIn(SingletonComponent::class)
interface KalamItemPopupMenuModule {

    @KalamItemPopupMenuItems
    @Singleton
    @Binds
    fun provideKalamItemPopupMenu(kalamItemPopupMenu: KalamItemPopupMenu): PopupMenu
}

@Module
@InstallIn(SingletonComponent::class)
interface PlaylistItemPopupMenuModule {

    @PlaylistItemPopupMenuItems
    @Singleton
    @Binds
    fun providePlaylistItemPopupMenu(playlistItemPopupMenu: PlaylistItemPopupMenu): PopupMenu
}

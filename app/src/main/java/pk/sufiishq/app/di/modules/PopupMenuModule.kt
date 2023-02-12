package pk.sufiishq.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import pk.sufiishq.app.di.qualifier.AppBarPopupMenuItems
import pk.sufiishq.app.di.qualifier.KalamItemPopupMenuItems
import pk.sufiishq.app.di.qualifier.PlaylistItemPopupMenuItems
import pk.sufiishq.app.helpers.popupmenu.AppBarPopupMenu
import pk.sufiishq.app.helpers.popupmenu.KalamItemPopupMenu
import pk.sufiishq.app.helpers.popupmenu.PlaylistItemPopupMenu
import pk.sufiishq.app.helpers.popupmenu.PopupMenu

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
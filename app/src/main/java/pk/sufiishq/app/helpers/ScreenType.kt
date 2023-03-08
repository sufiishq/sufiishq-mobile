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

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import pk.sufiishq.app.feature.app.model.Media
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.ui.screen.admin.AdminSettingsScreen
import pk.sufiishq.app.ui.screen.applock.AppLockScreen
import pk.sufiishq.app.ui.screen.dashboard.DashboardScreen
import pk.sufiishq.app.ui.screen.gallery.GalleryScreen
import pk.sufiishq.app.ui.screen.help.HelpScreen
import pk.sufiishq.app.ui.screen.location.DarbarLocationScreen
import pk.sufiishq.app.ui.screen.occasion.OccasionScreen
import pk.sufiishq.app.ui.screen.occasionlist.OccasionListScreen
import pk.sufiishq.app.ui.screen.photo.PhotoScreen
import pk.sufiishq.app.ui.screen.photolist.PhotoListScreen
import pk.sufiishq.app.ui.screen.playlist.PlaylistScreen
import pk.sufiishq.app.ui.screen.theme.ThemeScreen
import pk.sufiishq.app.ui.screen.tracks.TracksScreen
import pk.sufiishq.app.ui.screen.videolist.VideoListScreen
import pk.sufiishq.app.ui.screen.videoplay.VideoPlayScreen
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getApp
import pk.sufiishq.app.feature.occasions.model.Occasion as OccasionModel

val MainScreens: List<ScreenType> =
    ScreenType::class.nestedClasses.toList().map { it.objectInstance as ScreenType }

sealed interface ScreenType {

    val route: String
    fun buildRoute(): String
    fun arguments(): List<NamedNavArgument> = emptyList()
    fun deepLinks(): List<NavDeepLink> = emptyList()

    fun buildRoute(vararg args: Any): String {
        return buildString {
            append(route)

            args.forEach { arg -> append("/").append(arg) }
        }
    }

    @Composable
    fun Compose(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry,
        scaffoldState: ScaffoldState,
    )

    object Dashboard : ScreenType {

        override val route: String
            get() = "screen_dashboard"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            DashboardScreen(
                navController,
            )
        }
    }

    object Playlist : ScreenType {

        override val route: String
            get() = "screen_playlist"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            PlaylistScreen(
                navController = navController,
            )
        }
    }

    object Help : ScreenType {

        override val route: String
            get() = "screen_help"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            HelpScreen()
        }
    }

    object Tracks : ScreenType {

        // params
        private const val PARAM_TRACK_TYPE = "trackType"
        private const val PARAM_TITLE = "title"
        private const val PARAM_PLAYLIST_ID = "playlistId"

        // track list types
        const val ALL = "all"
        const val DOWNLOADS = "downloads"
        const val FAVORITES = "favorites"
        const val PLAYLIST = "playlist"

        override val route: String
            get() = "screen_tracks"

        override fun buildRoute(): String {
            return "$route/{$PARAM_TRACK_TYPE}/{$PARAM_TITLE}/{$PARAM_PLAYLIST_ID}"
        }

        private fun getTrackType(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_TRACK_TYPE) ?: ALL
        }

        private fun getTitle(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_TITLE)
                ?: getApp().getString(TextRes.title_all_kalam)
        }

        private fun getPlaylistId(navBackStackEntry: NavBackStackEntry): Int {
            return navBackStackEntry.arguments?.getInt(PARAM_PLAYLIST_ID) ?: 0
        }

        private fun getTrackListType(navBackStackEntry: NavBackStackEntry): TrackListType {
            val trackType = getTrackType(navBackStackEntry)
            val title = getTitle(navBackStackEntry)
            val playlistId = getPlaylistId(navBackStackEntry)

            return when (trackType) {
                DOWNLOADS -> TrackListType.Downloads()
                FAVORITES -> TrackListType.Favorites()
                PLAYLIST -> TrackListType.Playlist(title, playlistId)
                else -> TrackListType.All()
            }
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_TRACK_TYPE) { type = NavType.StringType },
                navArgument(PARAM_TITLE) { type = NavType.StringType },
                navArgument(PARAM_PLAYLIST_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            TracksScreen(
                trackListType = getTrackListType(navBackStackEntry),
            )
        }
    }

    object Theme : ScreenType {

        override val route: String
            get() = "screen_photo"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            ThemeScreen()
        }
    }

    object DarbarLocation : ScreenType {

        override val route: String
            get() = "screen_darbar_location"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            DarbarLocationScreen()
        }
    }

    object Photo : ScreenType {

        private const val PARAM_PHOTO_ID = "photoId"

        override val route: String
            get() = "screen_photo"

        override fun buildRoute(): String {
            return "$route/{$PARAM_PHOTO_ID}"
        }

        private fun getPhotoId(navBackStackEntry: NavBackStackEntry): Int {
            return navBackStackEntry.arguments?.getInt(PARAM_PHOTO_ID)!!
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_PHOTO_ID) { type = NavType.IntType },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            PhotoScreen(
                photoId = getPhotoId(navBackStackEntry),
            )
        }
    }

    object AppLock : ScreenType {

        override val route: String
            get() = "screen_app_lock"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            AppLockScreen(
                scaffoldState = scaffoldState,
            )
        }
    }

    object AdminSettings : ScreenType {

        override val route: String
            get() = "screen_admin_settings"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            AdminSettingsScreen(
                scaffoldState = scaffoldState,
            )
        }
    }

    object Gallery : ScreenType {

        override val route: String
            get() = "gallery_screen"

        override fun buildRoute() = route

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            GalleryScreen(navController)
        }
    }

    object OccasionList : ScreenType {

        private const val PARAM_LIST_TYPE = "list_type"

        override val route: String
            get() = "occasion_list_screen"

        override fun buildRoute(): String {
            return "$route/{$PARAM_LIST_TYPE}"
        }

        private fun getListType(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_LIST_TYPE)!!
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_LIST_TYPE) { type = NavType.StringType },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            OccasionListScreen(navController, OccasionType.valueOf(getListType(navBackStackEntry)))
        }
    }

    object Occasion : ScreenType {

        private const val PARAM_OCCASION = "list_type"

        override val route: String
            get() = "occasion_screen"

        override fun buildRoute(): String {
            return "$route/{$PARAM_OCCASION}"
        }

        private fun getOccasion(navBackStackEntry: NavBackStackEntry): OccasionModel {
            return navBackStackEntry.arguments?.getString(PARAM_OCCASION)!!.let {
                Gson().fromJson(it, OccasionModel::class.java)
            }
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_OCCASION) { type = JsonNavType(OccasionModel::class.java) },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            OccasionScreen(navController, getOccasion(navBackStackEntry))
        }
    }

    object PhotoList : ScreenType {

        private const val PARAM_MEDIA = "referenceId"

        override val route: String
            get() = "photo_list_screen"

        override fun buildRoute(): String {
            return "$route/{$PARAM_MEDIA}"
        }

        private fun getReferenceId(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_MEDIA)!!
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_MEDIA) { type = NavType.StringType },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            PhotoListScreen(getReferenceId(navBackStackEntry))
        }
    }

    object VideoList : ScreenType {

        private const val PARAM_MEDIA = "referenceId"

        override val route: String
            get() = "video_list_screen"

        override fun buildRoute(): String {
            return "$route/{$PARAM_MEDIA}"
        }

        private fun getReferenceId(navBackStackEntry: NavBackStackEntry): String {
            return navBackStackEntry.arguments?.getString(PARAM_MEDIA)!!
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_MEDIA) { type = NavType.StringType },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            VideoListScreen(navController, getReferenceId(navBackStackEntry))
        }
    }

    object VideoPlay : ScreenType {

        private const val PARAM_MEDIA = "media"

        override val route: String
            get() = "video_play_screen"

        override fun buildRoute(): String {
            return "$route/{$PARAM_MEDIA}"
        }

        private fun getMedia(navBackStackEntry: NavBackStackEntry): Media {
            return navBackStackEntry.arguments?.getString(PARAM_MEDIA)!!.let {
                Gson().fromJson(it, Media::class.java)
            }
        }

        override fun arguments(): List<NamedNavArgument> {
            return listOf(
                navArgument(PARAM_MEDIA) { type = JsonNavType(Media::class.java) },
            )
        }

        @Composable
        override fun Compose(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry,
            scaffoldState: ScaffoldState,
        ) {
            VideoPlayScreen(navController, getMedia(navBackStackEntry))
        }
    }
}

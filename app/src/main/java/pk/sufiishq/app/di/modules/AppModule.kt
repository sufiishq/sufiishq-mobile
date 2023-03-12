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

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.feature.app.model.NavigationItem
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.helper.TrackListTypeDeserializer
import pk.sufiishq.app.feature.storage.KeyValueStorage
import pk.sufiishq.app.feature.storage.SecureSharedPreferencesStorage
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.getString
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    @Provides
    @Singleton
    @SecureSharedPreferences
    fun providesKeyValueStorage(@ApplicationContext appContext: Context): KeyValueStorage {
        return SecureSharedPreferencesStorage(appContext)
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(TrackListType::class.java, TrackListTypeDeserializer())
            .create()
    }

    @Provides
    @Singleton
    @HelpJson
    fun helpContentJson(@ApplicationContext context: Context): JSONObject {
        return JSONObject(
            context.assets.open("help/help.json").bufferedReader().use { it.readText() },
        )
    }

    @Singleton
    @Provides
    fun provideMainNavigation(): List<NavigationItem> {
        return listOf(
            // NavigationItem(getString(TextRes.menu_item_qibla_direction), ImageRes.compass,
            // ""),
            NavigationItem(
                getString(TextRes.menu_item_tasbeeh_sarkar),
                ImageRes.tasbih,
                ScreenType.Photo.buildRoute(ImageRes.photo_tasbeeh.toString()),
            ),
            NavigationItem(getString(TextRes.menu_item_events), ImageRes.events, ScreenType.EventList.buildRoute()),
            NavigationItem(getString(TextRes.menu_item_gallery), ImageRes.gallery, ScreenType.Gallery.buildRoute()),
            // NavigationItem(getString(TextRes.menu_item_niaz), ImageRes.niaz, ""),
            // NavigationItem(getString(TextRes.menu_item_remedies), ImageRes.remedy, ""),
            // NavigationItem(getString(TextRes.menu_item_rubai), ImageRes.rubai, ""),
            NavigationItem(
                getString(TextRes.menu_item_location),
                ImageRes.location,
                ScreenType.DarbarLocation.buildRoute(),
            ),
            NavigationItem(
                getString(TextRes.menu_item_shijra),
                ImageRes.shijra,
                ScreenType.Photo.buildRoute(ImageRes.photo_shijra.toString()),
            ),
        )
    }
}

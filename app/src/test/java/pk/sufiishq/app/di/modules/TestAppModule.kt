package pk.sufiishq.app.di.modules

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import org.json.JSONObject
import pk.sufiishq.app.R
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.fake.TestSharedPreferences
import pk.sufiishq.app.feature.app.model.NavigationItem
import pk.sufiishq.app.feature.help.di.qualifier.HelpJson
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.helper.TrackListTypeDeserializer
import pk.sufiishq.app.feature.storage.KeyValueStorage
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.utils.getString

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class TestAppModule {
    @Provides
    fun providesHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    @Provides
    @Singleton
    @SecureSharedPreferences
    fun providesKeyValueStorage(@ApplicationContext appContext: Context): KeyValueStorage {
        return TestSharedPreferences(appContext)
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
            javaClass.classLoader.getResourceAsStream("help/help.json").bufferedReader().readText(),
        )
    }

    @Singleton
    @Provides
    fun provideMainNavigation(): List<NavigationItem> {
        return listOf(
            // NavigationItem(getString(R.string.menu_item_qibla_direction), R.drawable.compass,
            // ""),
            NavigationItem(
                getString(R.string.menu_item_tasbeeh_sarkar),
                R.drawable.tasbih,
                ScreenType.Photo.buildRoute(R.drawable.photo_tasbeeh.toString()),
            ),
            // NavigationItem(getString(R.string.menu_item_events), R.drawable.events, ""),
            // NavigationItem(getString(R.string.menu_item_gallery), R.drawable.gallery, ""),
            // NavigationItem(getString(R.string.menu_item_niaz), R.drawable.niaz, ""),
            // NavigationItem(getString(R.string.menu_item_remedies), R.drawable.remedy, ""),
            // NavigationItem(getString(R.string.menu_item_rubai), R.drawable.rubai, ""),
            NavigationItem(
                getString(R.string.menu_item_location),
                R.drawable.location,
                ScreenType.DarbarLocation.buildRoute(),
            ),
            NavigationItem(
                getString(R.string.menu_item_shijra),
                R.drawable.shijra,
                ScreenType.Photo.buildRoute(R.drawable.photo_shijra.toString()),
            ),
        )
    }
}
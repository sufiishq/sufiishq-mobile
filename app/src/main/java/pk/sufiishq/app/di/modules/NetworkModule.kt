package pk.sufiishq.app.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import pk.sufiishq.app.core.help.HelpContentService
import pk.sufiishq.app.core.kalam.downloader.DownloadFileService
import pk.sufiishq.app.core.hijridate.HijriDateService
import pk.sufiishq.app.utils.SUFI_ISHQ_HOST
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): String = SUFI_ISHQ_HOST

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun providesRequestBuilder(): Request.Builder {
        return Request.Builder()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideHelpContentService(retrofit: Retrofit): HelpContentService {
        return retrofit.create(HelpContentService::class.java)
    }

    @Provides
    fun provideDownloadFileService(retrofit: Retrofit): DownloadFileService {
        return retrofit.create(DownloadFileService::class.java)
    }

    @Provides
    fun provideHijriDateService(retrofit: Retrofit): HijriDateService {
        return retrofit.create(HijriDateService::class.java)
    }
}
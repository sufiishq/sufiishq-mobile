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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import pk.sufiishq.app.core.help.api.HelpContentService
import pk.sufiishq.app.core.hijridate.api.HijriDateService
import pk.sufiishq.app.core.kalam.downloader.DownloadFileService
import pk.sufiishq.app.utils.SUFI_ISHQ_HOST
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton @Provides
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
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).build()
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

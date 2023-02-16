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
import kotlinx.coroutines.Dispatchers
import pk.sufiishq.app.di.qualifier.DefaultDispatcher
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.di.qualifier.MainDispatcher
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DefaultDispatcherModule {

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineContext = Dispatchers.Default
}

@Module
@InstallIn(SingletonComponent::class)
class IODispatcherModule {

    @IoDispatcher @Provides
    fun provideIoDispatcher(): CoroutineContext = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
class MainDispatcherModule {

    @MainDispatcher @Provides
    fun provideMainDispatcher(): CoroutineContext = Dispatchers.Main
}

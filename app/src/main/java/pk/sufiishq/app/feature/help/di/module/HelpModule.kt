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

package pk.sufiishq.app.feature.help.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pk.sufiishq.app.feature.help.di.qualifier.OfflineResolver
import pk.sufiishq.app.feature.help.di.qualifier.OnlineResolver
import pk.sufiishq.app.feature.help.resolver.HelpContentResolver
import pk.sufiishq.app.feature.help.resolver.OfflineHelpContentResolver
import pk.sufiishq.app.feature.help.resolver.OnlineHelpContentResolver

@Module
@InstallIn(SingletonComponent::class)
interface OfflineHelpModule {

    @OfflineResolver
    @Binds
    fun bindLocalHelpContentResolver(
        offlineHelpContentResolver: OfflineHelpContentResolver,
    ): HelpContentResolver
}

@Module
@InstallIn(SingletonComponent::class)
interface OnlineHelpModule {

    @OnlineResolver
    @Binds
    fun bindOnlineHelpContentResolver(
        onlineHelpContentResolver: OnlineHelpContentResolver,
    ): HelpContentResolver
}

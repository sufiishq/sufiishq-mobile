package pk.sufiishq.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pk.sufiishq.app.core.help.HelpContentResolver
import pk.sufiishq.app.core.help.LocalHelpContentResolver
import pk.sufiishq.app.core.help.OnlineHelpContentResolver
import pk.sufiishq.app.di.qualifier.OfflineResolver
import pk.sufiishq.app.di.qualifier.OnlineResolver

@Module
@InstallIn(SingletonComponent::class)
interface OfflineHelpModule {

    @OfflineResolver
    @Binds
    fun bindLocalHelpContentResolver(localHelpContentResolver: LocalHelpContentResolver): HelpContentResolver
}

@Module
@InstallIn(SingletonComponent::class)
interface OnlineHelpModule {

    @OnlineResolver
    @Binds
    fun bindOnlineHelpContentResolver(onlineHelpContentResolver: OnlineHelpContentResolver): HelpContentResolver
}
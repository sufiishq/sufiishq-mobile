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

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineContext = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
class MainDispatcherModule {

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineContext = Dispatchers.Main
}
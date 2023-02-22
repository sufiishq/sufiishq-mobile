package pk.sufiishq.app.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import pk.sufiishq.app.di.qualifier.DefaultDispatcher
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.di.qualifier.MainDispatcher
import kotlin.coroutines.CoroutineContext

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MainDispatcherModule::class]
)
class TestMainDispatcherModule {

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineContext = Dispatchers.Main.immediate
}
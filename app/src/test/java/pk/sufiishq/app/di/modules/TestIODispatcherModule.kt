package pk.sufiishq.app.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import pk.sufiishq.app.di.qualifier.DefaultDispatcher
import pk.sufiishq.app.di.qualifier.IoDispatcher
import kotlin.coroutines.CoroutineContext

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [IODispatcherModule::class]
)
class TestIODispatcherModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineContext = Dispatchers.Main.immediate
}
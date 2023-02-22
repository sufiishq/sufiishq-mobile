package pk.sufiishq.app.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import pk.sufiishq.app.di.qualifier.DefaultDispatcher
import kotlin.coroutines.CoroutineContext

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DefaultDispatcherModule::class]
)
class TestDefaultDispatcherModule {

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineContext = Dispatchers.Main.immediate
}
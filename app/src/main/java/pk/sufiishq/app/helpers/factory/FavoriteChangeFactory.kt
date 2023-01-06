package pk.sufiishq.app.helpers.factory

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.strategies.kalam.favorite.AddToFavoriteStrategy
import pk.sufiishq.app.helpers.strategies.kalam.favorite.FavoriteChangeStrategy
import pk.sufiishq.app.helpers.strategies.kalam.favorite.RemoveFromFavoriteStrategy
import kotlin.reflect.KClass

@Singleton
class FavoriteChangeFactory @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val kalamRepository: KalamRepository
) {

    fun <T : FavoriteChangeStrategy> create(clazz: KClass<T>): FavoriteChangeStrategy {
        return when (clazz) {
            AddToFavoriteStrategy::class -> AddToFavoriteStrategy(appContext, kalamRepository)
            RemoveFromFavoriteStrategy::class -> RemoveFromFavoriteStrategy(
                appContext,
                kalamRepository
            )
            else -> throw IllegalArgumentException()
        }
    }
}
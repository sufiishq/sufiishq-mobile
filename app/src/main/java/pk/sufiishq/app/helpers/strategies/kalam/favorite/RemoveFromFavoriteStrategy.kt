package pk.sufiishq.app.helpers.strategies.kalam.favorite

import android.content.Context
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.toastShort

class RemoveFromFavoriteStrategy(
    private val appContext: Context,
    private val kalamRepository: KalamRepository
) : FavoriteChangeStrategy {

    override suspend fun change(kalam: Kalam) {

        kalam.apply {
            isFavorite = 0
        }.also {
            kalamRepository.update(kalam)
        }.also {
            appContext.toastShort(
                appContext.getString(R.string.favorite_removed).format(kalam.title)
            )
        }
    }
}
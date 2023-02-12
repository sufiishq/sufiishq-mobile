package pk.sufiishq.app.core.kalam.favorite

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.quickToast
import kotlin.coroutines.CoroutineContext

class FavoriteManager @Inject constructor(
    private val kalamRepository: KalamRepository,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    fun markAsFavorite(kalam: Kalam) {
        updateFavorite(
            kalam.apply {
                isFavorite = 1
            },
            R.string.dynamic_favorite_added
        )
    }

    fun removeFavorite(kalam: Kalam) {
        updateFavorite(
            kalam.apply {
                isFavorite = 0
            },
            R.string.dynamic_favorite_removed
        )
    }

    private fun updateFavorite(kalam: Kalam, toastMessageResId: Int) {
        CoroutineScope(dispatcher).launch {
            kalamRepository.update(kalam).also {
                quickToast(toastMessageResId, kalam.title)
            }
        }
    }
}
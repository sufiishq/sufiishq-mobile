package pk.sufiishq.app.helpers.factory

import javax.inject.Inject
import javax.inject.Singleton
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.helpers.ScreenType.Tracks.PLAYLIST
import pk.sufiishq.app.helpers.strategies.kalam.delete.DeleteFromDownloadsStrategy
import pk.sufiishq.app.helpers.strategies.kalam.delete.DeleteFromPlaylistStrategy
import pk.sufiishq.app.helpers.strategies.kalam.delete.KalamDeleteStrategy

@Singleton
class KalamDeleteStrategyFactory @Inject constructor(private val kalamRepository: KalamRepository) {

    fun create(trackType: String): KalamDeleteStrategy {
        return when (trackType) {
            PLAYLIST -> DeleteFromPlaylistStrategy(kalamRepository)
            else -> DeleteFromDownloadsStrategy(kalamRepository)
        }
    }
}
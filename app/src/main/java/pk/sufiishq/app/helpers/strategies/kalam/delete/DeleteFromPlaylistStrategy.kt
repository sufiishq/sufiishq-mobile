package pk.sufiishq.app.helpers.strategies.kalam.delete

import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.models.Kalam

class DeleteFromPlaylistStrategy(private val kalamRepository: KalamRepository) :
    KalamDeleteStrategy {

    override suspend fun delete(kalam: Kalam) {
        kalam.playlistId = 0
        kalamRepository.update(kalam)
    }
}
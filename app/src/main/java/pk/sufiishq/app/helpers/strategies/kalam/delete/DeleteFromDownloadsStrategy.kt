package pk.sufiishq.app.helpers.strategies.kalam.delete

import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.utils.offlineFile

class DeleteFromDownloadsStrategy(private val kalamRepository: KalamRepository) :
    KalamDeleteStrategy {

    override suspend fun delete(kalam: Kalam) {
        kalam.apply {
            offlineFile()?.delete()
            offlineSource = ""
            kalamRepository.update(this)

            if (onlineSource.isEmpty()) {
                kalamRepository.delete(this)
            }
        }
    }
}
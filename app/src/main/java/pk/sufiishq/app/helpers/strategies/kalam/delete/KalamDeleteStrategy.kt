package pk.sufiishq.app.helpers.strategies.kalam.delete

import pk.sufiishq.app.models.Kalam

interface KalamDeleteStrategy {
    suspend fun delete(kalam: Kalam)
}
package pk.sufiishq.app.helpers.strategies.kalam.favorite

import pk.sufiishq.app.models.Kalam

interface FavoriteChangeStrategy {
    suspend fun change(kalam: Kalam)
}
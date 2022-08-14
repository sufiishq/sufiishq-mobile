package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamDeleteItem
import pk.sufiishq.app.viewmodels.KalamViewModel
import java.io.File

abstract class KalamEvents : Event(KalamViewModel::class) {
    class UpdateTrackListType(val trackListType: TrackListType) : Event(KalamViewModel::class)
    class SearchKalam(val keyword: String, val trackListType: TrackListType) :
        Event(KalamViewModel::class)

    class UpdateKalam(val kalam: Kalam) : Event(KalamViewModel::class)
    class ShowKalamConfirmDeleteDialog(val kalamDeleteItem: KalamDeleteItem?) :
        Event(KalamViewModel::class)

    class DeleteKalam(val kalamDeleteItem: KalamDeleteItem) : Event(KalamViewModel::class)
    class SaveSplitKalam(val sourceKalam: Kalam, val splitFile: File, val kalamTitle: String) :
        Event(KalamViewModel::class)

    class MarkAsFavoriteKalam(val kalam: Kalam) : Event(KalamViewModel::class)
    class RemoveFavoriteKalam(val kalam: Kalam) : Event(KalamViewModel::class)
    class ShowKalamSplitManagerDialog(val kalam: Kalam?) : Event(KalamViewModel::class)
    class ShowKalamRenameDialog(val kalam: Kalam?) : Event(KalamViewModel::class)
}
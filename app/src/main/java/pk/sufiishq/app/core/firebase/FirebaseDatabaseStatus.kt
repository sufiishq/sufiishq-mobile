package pk.sufiishq.app.core.firebase

import pk.sufiishq.app.R
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.getString

sealed class FirebaseDatabaseStatus(val message: String?) {
    class Write(message: String = getString(R.string.msg_record_updated)) :
        FirebaseDatabaseStatus(message)

    class ReadHighlight(val highlight: Highlight) : FirebaseDatabaseStatus(null)
    class ReadMaintenance(val activeStatus: Boolean, val strictMode: Boolean) :
        FirebaseDatabaseStatus(null)

    class Delete(message: String = getString(R.string.msg_highlight_deleted)) :
        FirebaseDatabaseStatus(message)

    class Failed(val ex: Exception) : FirebaseDatabaseStatus(ex.message ?: ex.localizedMessage)
}
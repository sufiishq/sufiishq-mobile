package pk.sufiishq.app.models


data class Highlight(
    val startDateTime: Long,
    val endDateTime: Long,
    val title: String?,
    val detail: String,
    val contacts: Map<String, Map<String, String>>? = null
) {
    companion object {
        const val NAME = "name"
        const val NUMBER = "number"
    }
}

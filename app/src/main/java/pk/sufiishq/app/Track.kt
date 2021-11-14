package pk.sufiishq.app

data class Track(
    val id: Int,
    val title: String,
    val code: Int,
    val year: String,
    val location: String,
    val src: String,
    val startFrom: Int
    )
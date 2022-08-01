package pk.sufiishq.app.utils

val Int.formatTime: String
    get() {
        return if (this > 999) {
            val totalSeconds = this / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            "00:00:00"
        }
    }
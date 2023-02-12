package pk.sufiishq.app.utils


fun <T : Any> String.getFromStorage(default: T): T {
    return getApp().keyValueStorage.get(this, default)
}

fun <T : Any> String.putInStorage(value: T) {
    getApp().keyValueStorage.put(this, value)
}
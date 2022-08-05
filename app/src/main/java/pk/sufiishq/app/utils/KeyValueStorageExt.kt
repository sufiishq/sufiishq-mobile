package pk.sufiishq.app.utils



fun <T: Any> String.getFromStorage(default: T): T {
    return app.keyValueStorage.get(this, default)
}

fun <T: Any> String.putInStorage(value: T) {
    app.keyValueStorage.put(this, value)
}
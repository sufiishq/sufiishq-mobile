package pk.sufiishq.app.storage


interface KeyValueStorage {

    fun contains(key: String): Boolean
    fun <T> get(key: String, default: T): T
    fun <T> put(key: String, value: T)
}
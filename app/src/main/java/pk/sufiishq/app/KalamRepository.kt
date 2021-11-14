package pk.sufiishq.app

import kotlinx.coroutines.delay

class KalamRepository(private val db: SufiishqDBHelper) {

    fun loadKalam(page: Int, searchKeyword: String): List<Track> {
        return db.getKalams(page, searchKeyword)
    }

    fun getDefaultKalam(): Track {
        return db.getFirstKalam()
    }
}
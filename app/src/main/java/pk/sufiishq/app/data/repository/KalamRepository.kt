package pk.sufiishq.app.data.repository

import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.models.Kalam
import javax.inject.Inject

class KalamRepository @Inject constructor(private val kalamDao: KalamDao) {

    fun insert(kalam: Kalam) = kalamDao.insert(kalam)

    fun loadAllKalam(offset: Int, searchKeyword: String): List<Kalam> {
        return kalamDao.getAllKalam("%${searchKeyword}%", offset * 10)
    }

    fun loadDownloadsKalam(offset: Int, searchKeyword: String): List<Kalam> {
        return kalamDao.getDownloadsKalam("%${searchKeyword}%", offset * 10)
    }

    fun loadFavoritesKalam(offset: Int, searchKeyword: String): List<Kalam> {
        return kalamDao.getFavoritesKalam("%${searchKeyword}%", offset * 10)
    }

    fun loadPlaylistKalam(playlistId: Int, offset: Int, searchKeyword: String): List<Kalam> {
        return kalamDao.getPlaylistKalam(playlistId, "%${searchKeyword}%", offset * 10)
    }

    fun loadAllPlaylistKalam(playlistId: Int): List<Kalam> {
        return kalamDao.getAllPlaylistKalam(playlistId)
    }

    fun getDefaultKalam(): Kalam {
        return kalamDao.getFirstKalam()
    }

    fun countAll(): Int {
        return kalamDao.countAll()
    }


    fun countDownloads(): Int {
        return kalamDao.countDownloads()
    }

    fun countFavorites(): Int {
        return kalamDao.countFavorites()
    }

    fun update(kalam: Kalam) {
        kalamDao.update(kalam)
    }

    fun delete(kalam: Kalam) {
        kalamDao.delete(kalam)
    }
}
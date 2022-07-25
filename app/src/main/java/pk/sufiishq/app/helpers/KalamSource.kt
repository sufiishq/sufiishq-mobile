package pk.sufiishq.app.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.models.Kalam
import java.lang.Exception

class KalamSource(
    private val kalamRepository: KalamRepository,
    private val trackType: String,
    private val playlistId: Int = 0,
    private val searchKeyword: String = "",
    currentPage: Int = 0
) : PagingSource<Int, Kalam>() {

    private var nextPage = currentPage

    override fun getRefreshKey(state: PagingState<Int, Kalam>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Kalam> {
        return try {

            val result = when (trackType) {
                Screen.Tracks.DOWNLOADS -> kalamRepository.loadDownloadsKalam(
                    nextPage,
                    searchKeyword
                )
                Screen.Tracks.FAVORITES -> kalamRepository.loadFavoritesKalam(
                    nextPage,
                    searchKeyword
                )
                Screen.Tracks.PLAYLIST -> kalamRepository.loadPlaylistKalam(
                    playlistId,
                    nextPage,
                    searchKeyword
                )
                else -> kalamRepository.loadAllKalam(nextPage, searchKeyword)
            }

            nextPage = nextPage.plus(1)

            LoadResult.Page(
                result,
                prevKey = null,
                nextKey = if (result.isNotEmpty()) nextPage else null
            )

        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
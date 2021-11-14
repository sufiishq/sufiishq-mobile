package pk.sufiishq.app

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception

class KalamSource(private val kalamRepository: KalamRepository, private val searchKeyword: String = "", private val currentPage: Int = 1): PagingSource<Int, Track>() {

    private var nextPage = currentPage

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        return try {



            val result = kalamRepository.loadKalam(nextPage, searchKeyword)
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
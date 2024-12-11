import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.data.retrofit.ApiService

class FoodPagingSource(private val apiService: ApiService) : PagingSource<Int, ListDataFood>() {

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListDataFood> {
        return try {
            val currentPage = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getFood(currentPage, params.loadSize)

            LoadResult.Page(
                data = response.data,
                prevKey = if (response.pagination.hasPreviousPage) currentPage - 1 else null,
                nextKey = if (response.pagination.hasNextPage) currentPage + 1 else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListDataFood>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

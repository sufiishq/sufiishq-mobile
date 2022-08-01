package pk.sufiishq.app.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import pk.sufiishq.app.data.repository.KalamRepository
import javax.inject.Inject

@HiltViewModel
class AssetKalamLoaderViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val kalamRepository: KalamRepository
) : ViewModel() {

    private var disposable = Disposables.disposed()

    fun countAll(): LiveData<Int> = kalamRepository.countAll()

    fun loadAllKalam(count: Int, block: (dataInserted: Boolean) -> Unit) {
        if (count <= 0) {
            disposable = kalamRepository.loadAllFromAssets(appContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { kalamList ->
                    viewModelScope.launch {
                        kalamRepository.insertAll(kalamList)
                        block(true)
                    }
                }
        } else {
            block(false)
        }
    }

    fun release() {
        disposable.dispose()
    }
}


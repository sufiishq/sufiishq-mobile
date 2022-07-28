package pk.sufiishq.app.utils

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import io.reactivex.Completable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.File

fun File.moveTo(destination: File): Completable {
    return Completable.fromAction {
        IOUtils.copy(this.inputStream(), destination.outputStream())
        this.delete()
    }
}

fun File.deleteContent() {

    list { _, name ->
        name.endsWith("mp3")
    }?.forEach {
        try {
            File("$absolutePath/$it").delete()
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }
}

fun File.split(
    output: File,
    start: String,
    end: String,
    onComplete: (returnCode: Int) -> Unit
) {

    EpEditor.execCmd(
        "-y -i $absolutePath -ss $start -codec copy -t $end ${output.absolutePath}",
        0,
        object : OnEditorListener {
            override fun onSuccess() {
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(SPLIT_SUCCESS)
                }
            }

            override fun onFailure() {
                CoroutineScope(Dispatchers.Main).launch {
                    onComplete(SPLIT_CANCEL)
                }
            }

            override fun onProgress(progress: Float) { /* no comment */ }
        }
    )
}
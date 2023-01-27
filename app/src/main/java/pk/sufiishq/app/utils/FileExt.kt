package pk.sufiishq.app.utils

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import io.reactivex.Completable
import java.io.File
import org.apache.commons.io.IOUtils
import pk.sufiishq.app.core.splitter.SplitStatus
import timber.log.Timber

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
            absolutePath.toFile(it).delete()
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }
}

fun String.toFile(filename: String): File {
    return File(this, filename)
}

fun File.split(
    output: File,
    start: String,
    end: String,
    onComplete: (splitStatus: SplitStatus) -> Unit
) {

    EpEditor.execCmd(
        "-y -i $absolutePath -ss $start -codec copy -t $end ${output.absolutePath}",
        0,
        object : OnEditorListener {
            override fun onSuccess() {
                onComplete(SplitStatus.Done)
            }

            override fun onFailure() {
                onComplete(SplitStatus.Error("Execution failed"))
            }

            override fun onProgress(progress: Float) {}
        }
    )
}
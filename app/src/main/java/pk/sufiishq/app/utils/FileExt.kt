package pk.sufiishq.app.utils

import com.arthenica.mobileffmpeg.FFmpeg
import io.reactivex.Completable
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

    list { dir, name ->
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

    FFmpeg.executeAsync("-y -i $absolutePath -ss $start -codec copy -t $end ${output.absolutePath}") { executionId, returnCode ->
        onComplete(returnCode)
    }
}
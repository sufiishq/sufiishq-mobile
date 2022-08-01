package pk.sufiishq.app.helpers

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FileDownloader @Inject constructor(okHttpClient: OkHttpClient) {

    companion object {
        const val BUFFER_LENGTH_BYTES = 1024 * 8
        const val HTTP_TIMEOUT = 30
    }

    private var okHttpClient: OkHttpClient

    init {
        val okHttpBuilder = okHttpClient.newBuilder()
            .connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
        this.okHttpClient = okHttpBuilder.build()
    }

    fun download(url: String, file: File): Observable<Int> {

        return Observable.create { emitter ->
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            val body = response.body
            val responseCode = response.code
            if (responseCode >= HttpURLConnection.HTTP_OK &&
                responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                body != null
            ) {
                val length = body.contentLength().toDouble()
                body.byteStream().apply {
                    file.outputStream().use { fileOut ->
                        var bytesCopied = 0.toDouble()
                        val buffer = ByteArray(BUFFER_LENGTH_BYTES)
                        var bytes = read(buffer)
                        while (bytes >= 0) {
                            fileOut.write(buffer, 0, bytes)
                            bytesCopied += bytes.toDouble()
                            bytes = read(buffer)
                            emitter.onNext((bytesCopied / length * 100.toDouble()).toInt())
                        }
                    }
                    emitter.onComplete()
                }
            } else {
                throw IllegalArgumentException("Error occurred when do http get $url")
            }
        }
    }
}
package pk.sufiishq.app.core.hijridate

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface HijriDateService {

    @GET
    suspend fun getHijriDate(@Url url: String): ResponseBody
}
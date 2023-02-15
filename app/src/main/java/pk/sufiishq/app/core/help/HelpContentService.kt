package pk.sufiishq.app.core.help

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface HelpContentService {

    @GET
    fun getHelp(@Url url: String): Call<ResponseBody>
}
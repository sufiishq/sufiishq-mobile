package pk.sufiishq.app.core.help

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface HelpContentService {

    @GET("media/help/help.json")
    fun getHelp(): Call<ResponseBody>
}
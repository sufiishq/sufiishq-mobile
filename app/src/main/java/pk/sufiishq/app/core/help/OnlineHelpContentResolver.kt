package pk.sufiishq.app.core.help

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.HelpJson
import pk.sufiishq.app.models.HelpContent

private const val HELP_URL = "https://raw.githubusercontent.com/sufiishq/sufiishq-mobile/master/app/src/main/assets/help/help.json"

class OnlineHelpContentResolver @Inject constructor(
    @HelpJson private val helpJson: JSONObject,
    private val helpContentService: HelpContentService,
    private val transformer: HelpContentTransformer
) : HelpContentResolver {

    override fun resolve(): Flow<List<HelpContent>> {

        return flow {

            try {
                val result = helpContentService.getHelp(HELP_URL).execute()
                if (result.isSuccessful) {
                    emit(transformer.transform(JSONObject(result.body()!!.string())))
                } else {
                    emitLocal(this)
                }
            } catch (ex: Exception) {
                emitLocal(this)
            }

        }.flowOn(Dispatchers.IO)
    }

    private suspend fun emitLocal(flow: FlowCollector<List<HelpContent>>) {
        flow.emit(transformer.transform(helpJson))
    }
}
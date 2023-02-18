package pk.sufiishq.app.core.help.resolver

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import pk.sufiishq.app.core.help.HelpContentTransformer
import pk.sufiishq.app.core.help.api.HelpContentService
import pk.sufiishq.app.core.help.di.qualifier.HelpJson
import pk.sufiishq.app.core.help.model.HelpContent
import timber.log.Timber

private const val HELP_URL =
    "https://raw.githubusercontent.com/sufiishq/sufiishq-mobile/master/app/src/main/assets/help/help.json"

class OnlineHelpContentResolver
@Inject
constructor(
    @HelpJson private val helpJson: JSONObject,
    private val helpContentService: HelpContentService,
    private val transformer: HelpContentTransformer,
) : HelpContentResolver {

    override fun resolve(): Flow<List<HelpContent>> {
        return flow {

            try {
                helpContentService
                    .getHelp(HELP_URL)
                    .execute()
                    .takeIf { it.isSuccessful }
                    ?.let {
                        emit(transformer.transform(JSONObject(it.body()!!.string())))
                    } ?: emit(transformer.transform(helpJson))
            } catch (ex: Exception) {
                Timber.e(ex)
                emit(transformer.transform(helpJson))
            }
        }
            .flowOn(Dispatchers.IO)
    }

}
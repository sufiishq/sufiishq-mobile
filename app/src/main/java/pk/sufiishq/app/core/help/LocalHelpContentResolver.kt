package pk.sufiishq.app.core.help

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import pk.sufiishq.app.di.qualifier.HelpJson
import pk.sufiishq.app.models.HelpContent

class LocalHelpContentResolver @Inject constructor(
    @HelpJson private val helpJson: JSONObject,
    private val transformer: HelpContentTransformer
) : HelpContentResolver {

    override fun resolve(): Flow<List<HelpContent>> {

        return flow {
            emit(transformer.transform(helpJson))

        }.flowOn(Dispatchers.IO)
    }
}
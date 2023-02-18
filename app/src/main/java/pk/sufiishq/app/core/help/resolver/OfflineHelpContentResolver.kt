package pk.sufiishq.app.core.help.resolver

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import pk.sufiishq.app.core.help.HelpContentTransformer
import pk.sufiishq.app.core.help.di.qualifier.HelpJson
import pk.sufiishq.app.core.help.model.HelpContent

class OfflineHelpContentResolver
@Inject
constructor(
    @HelpJson private val helpJson: JSONObject,
    private val transformer: HelpContentTransformer,
) : HelpContentResolver {

    override fun resolve(): Flow<List<HelpContent>> {
        return flow { emit(transformer.transform(helpJson)) }.flowOn(Dispatchers.IO)
    }
}
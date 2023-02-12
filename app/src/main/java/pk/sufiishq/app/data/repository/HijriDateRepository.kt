package pk.sufiishq.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pk.sufiishq.app.core.hijridate.HijriDateResolver
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.HijriDate
import kotlin.coroutines.CoroutineContext


class HijriDateRepository @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineContext,
    private val hijriDateResolver: HijriDateResolver
) {
    fun getHijriDate(): Flow<HijriDate?> = flow {
        emit(hijriDateResolver.resolve())
    }.flowOn(dispatcher)

}
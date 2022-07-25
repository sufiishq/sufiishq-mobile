package pk.sufiishq.app.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import pk.sufiishq.app.utils.deleteContent

class CacheRemoveWorker(context: Context, workerParam: WorkerParameters) :
    Worker(context, workerParam) {

    companion object {
        val TAG = CacheRemoveWorker::class.simpleName!!
    }

    override fun doWork(): Result {
        applicationContext.cacheDir.deleteContent()
        return Result.success()
    }
}
package com.example.datingapp.Workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.datingapp.API.ApiClient
import com.example.datingapp.Database.AppDatabase
import com.example.datingapp.Utils.NetworkUtils
import com.example.datingapp.Utils.toApiMessage
import com.example.datingapp.Utils.toMessage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageSyncWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val messageDao = AppDatabase.getDatabase(applicationContext).messageDao()

        if (NetworkUtils.isConnected(applicationContext)) {
            // Executar operações de base de dados dentro de uma coroutine
            runBlocking {
                val unsentMessages = withContext(Dispatchers.IO) {
                    messageDao.getUnsentMessages()
                }

                for (msg in unsentMessages) {
                    val apiMessage = msg.toApiMessage()

                    ApiClient.sendMessage(apiMessage.toMessage()) { response, error ->
                        if (error == null) {
                            runBlocking {
                                withContext(Dispatchers.IO) {
                                    messageDao.markAsSent(msg.id)
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success()
    }
}

package com.pravda.offlineai.repository

import com.pravda.offlineai.data.AuditEntry
import com.pravda.offlineai.data.PravdaDatabase
import com.pravda.offlineai.data.TaskRecord
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.UUID

class PravdaRepository(private val db: PravdaDatabase) {
    val tasks: Flow<List<TaskRecord>> = db.dao().observeTasks()
    val audit: Flow<List<AuditEntry>> = db.dao().observeAudit()

    suspend fun saveTask(description: String, requirements: String, result: String?) {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        db.dao().insertTask(TaskRecord(id, "user", description, requirements, "CREATED", result, now))
        val details = "Створено задачу; вимоги=$requirements"
        db.dao().insertAudit(AuditEntry(UUID.randomUUID().toString(), "user", "TASK_CREATED", id, now, details, sha256("$id|$details|$now")))
    }

    private fun sha256(value: String) = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray()).joinToString("") { "%02x".format(it) }
}

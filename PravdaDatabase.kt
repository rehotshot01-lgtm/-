package com.pravda.offlineai.data

import androidx.room.Database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [Universe::class, TruthRecord::class, Agent::class, AgentVersion::class, SkillProfile::class,
        TaskRecord::class, Evaluation::class, Benchmark::class, Experiment::class, Discovery::class,
        AccessGrant::class, RollbackSnapshot::class, Artifact::class, MemoryRecord::class, AuditEntry::class],
    version = 1, exportSchema = true
)
abstract class PravdaDatabase : RoomDatabase() {
    abstract fun dao(): PravdaDao
}

@Dao
interface PravdaDao {
    @Query("SELECT * FROM TaskRecord ORDER BY createdAt ASC") fun observeTasks(): Flow<List<TaskRecord>>
    @Query("SELECT * FROM AuditEntry ORDER BY timestamp ASC") fun observeAudit(): Flow<List<AuditEntry>>
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertTask(task: TaskRecord)
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertAudit(entry: AuditEntry)
}

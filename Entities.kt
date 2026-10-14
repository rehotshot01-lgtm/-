package com.pravda.offlineai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity data class Universe(@PrimaryKey val id: String, val name: String, val truthId: String?, val status: String, val createdAt: Long, val activeVersionId: String?)
@Entity data class TruthRecord(@PrimaryKey val id: String, val universeId: String, val name: String, val version: Int, val status: String)
@Entity data class Agent(@PrimaryKey val id: String, val universeId: String, val name: String, val role: String, val status: String, val skillProfileId: String?, val activeVersionId: String?)
@Entity data class AgentVersion(@PrimaryKey val id: String, val agentId: String, val parentVersionId: String?, val modelProfileId: String?, val promptHash: String, val memoryVersion: Int, val strategyVersion: Int, val status: String, val createdAt: Long)
@Entity data class SkillProfile(@PrimaryKey val id: String, val agentId: String, val skill: String, val score: Double, val confidence: Double, val sampleCount: Int)
@Entity data class TaskRecord(@PrimaryKey val id: String, val source: String, val description: String, val requirements: String, val status: String, val result: String?, val createdAt: Long)
@Entity data class Evaluation(@PrimaryKey val id: String, val agentVersionId: String, val benchmarkId: String, val score: Double, val correctness: Double, val efficiency: Double, val stability: Double, val regression: Double, val safety: Double)
@Entity data class Benchmark(@PrimaryKey val id: String, val name: String, val immutable: Boolean, val secret: Boolean, val version: Int)
@Entity data class Experiment(@PrimaryKey val id: String, val baselineVersion: String, val candidateVersion: String, val status: String, val metrics: String)
@Entity data class Discovery(@PrimaryKey val id: String, val taskId: String, val sourceAgent: String, val sourceUniverse: String, val oldSolution: String, val newSolution: String, val proofData: String, val benchmarkDelta: Double, val accessPolicy: String, val createdAt: Long, val status: String)
@Entity data class AccessGrant(@PrimaryKey val id: String, val discoveryId: String, val recipientType: String, val recipientId: String, val grantedAt: Long, val revokedAt: Long?)
@Entity data class RollbackSnapshot(@PrimaryKey val id: String, val universeId: String, val createdAt: Long, val stateHash: String, val snapshotJson: String)
@Entity data class Artifact(@PrimaryKey val id: String, val taskId: String, val type: String, val path: String, val hash: String)
@Entity data class MemoryRecord(@PrimaryKey val id: String, val scope: String, val type: String, val content: String, val confidence: Double, val source: String, val createdAt: Long)
@Entity data class AuditEntry(@PrimaryKey val id: String, val actor: String, val action: String, val objectId: String, val timestamp: Long, val details: String, val hash: String)

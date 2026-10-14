package com.pravda.offlineai.engine

data class CandidateMetrics(
    val correctness: Double,
    val efficiency: Double,
    val stability: Double,
    val safety: Double,
    val regression: Double
)

data class GateDecision(val accepted: Boolean, val reasons: List<String>)

/**
 * Conservative starter gate. Production thresholds and benchmark corpus must be specified,
 * independently tested and protected from agent access. This does not replace CORE/SECRET benchmarks.
 */
object VerificationGate {
    fun evaluate(candidate: CandidateMetrics, baseline: CandidateMetrics): GateDecision {
        val reasons = mutableListOf<String>()
        if (candidate.correctness < baseline.correctness) reasons += "Correctness нижча за baseline"
        if (candidate.stability < baseline.stability) reasons += "Stability нижча за baseline"
        if (candidate.safety < baseline.safety) reasons += "Safety нижча за baseline"
        if (candidate.regression > baseline.regression) reasons += "Регресія зросла"
        if (candidate.correctness < 0.95) reasons += "Correctness нижча за мінімальний поріг 0.95"
        if (candidate.safety < 0.99) reasons += "Safety нижча за мінімальний поріг 0.99"
        return GateDecision(reasons.isEmpty(), reasons)
    }
}

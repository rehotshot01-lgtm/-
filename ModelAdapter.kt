package com.pravda.offlineai.engine

interface ModelAdapter {
    val modelName: String
    suspend fun generate(prompt: String): String
}

/**
 * Deterministic placeholder only. It is NOT an LLM and must be replaced by a real local
 * inference backend (e.g. a compatible on-device runtime) before claiming AI functionality.
 */
class DemoModelAdapter : ModelAdapter {
    override val modelName = "DEMO-NOT-A-REAL-LLM"
    override suspend fun generate(prompt: String): String =
        "Задачу прийнято. Це демонстраційна відповідь без локальної LLM. Потрібно реалізувати: $prompt"
}

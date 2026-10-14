package com.pravda.offlineai.engine

import com.pravda.offlineai.repository.PravdaRepository

class TaskEngine(private val model: ModelAdapter) {
    suspend fun createTask(text: String, repository: PravdaRepository) {
        val requirements = listOf("Визначити критерії успіху", "Перевірити результат", "Зберегти журнал дій")
            .joinToString("; ")
        val plan = model.generate("Сформуй план виконання: $text")
        repository.saveTask(text, requirements, plan)
    }
}

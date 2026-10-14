package com.pravda.offlineai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.pravda.offlineai.data.PravdaDatabase
import com.pravda.offlineai.data.TaskRecord
import com.pravda.offlineai.engine.DemoModelAdapter
import com.pravda.offlineai.engine.TaskEngine
import com.pravda.offlineai.repository.PravdaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val vm: PravdaViewModel by viewModels {
        val db = Room.databaseBuilder(applicationContext, PravdaDatabase::class.java, "pravda.db").build()
        PravdaViewModel.Factory(PravdaRepository(db), TaskEngine(DemoModelAdapter()))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PravdaApp(vm) }
    }
}

class PravdaViewModel(
    private val repository: PravdaRepository,
    private val taskEngine: TaskEngine
) : ViewModel() {
    val tasks = repository.tasks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val audit = repository.audit.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun submit(text: String) = viewModelScope.launch { taskEngine.createTask(text, repository) }
    class Factory(private val repository: PravdaRepository, private val engine: TaskEngine) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PravdaViewModel(repository, engine) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PravdaApp(vm: PravdaViewModel) {
    var section by remember { mutableStateOf("Чат") }
    var prompt by remember { mutableStateOf("") }
    val tasks by vm.tasks.collectAsStateWithLifecycle()
    val audit by vm.audit.collectAsStateWithLifecycle()
    val sections = listOf("Чат", "Всесвіти", "ІСТИНИ", "Еволюція", "Армагедон", "Vault", "Спеціалісти", "Задачі", "Аудит", "Налаштування")
    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("ПРАВДА", style = MaterialTheme.typography.headlineSmall) },
                actions = { Text("OFFLINE", modifier = Modifier.padding(end = 16.dp), color = MaterialTheme.colorScheme.primary) }) }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                ScrollableTabRow(selectedTabIndex = sections.indexOf(section).coerceAtLeast(0)) {
                    sections.forEach { name ->
                        Tab(selected = section == name, onClick = { section = name }, text = { Text(name) })
                    }
                }
                Spacer(Modifier.height(12.dp))
                when (section) {
                    "Чат" -> {
                        Text("Глобальний координатор задач", style = MaterialTheme.typography.titleMedium)
                        Text("Основа MVP. Модель наразі демонстраційна; реальний локальний LLM ще не підключено.",
                            style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = prompt, onValueChange = { prompt = it },
                            modifier = Modifier.fillMaxWidth(), label = { Text("Напиши задачу для ПРАВДИ") },
                            minLines = 3)
                        Button(onClick = { if (prompt.isNotBlank()) { vm.submit(prompt.trim()); prompt = "" } },
                            modifier = Modifier.padding(top = 8.dp)) { Icon(Icons.Default.Send, null); Spacer(Modifier.width(8.dp)); Text("Створити задачу") }
                        Spacer(Modifier.height(16.dp))
                        Text("Останні задачі", style = MaterialTheme.typography.titleMedium)
                        LazyColumn { items(tasks.takeLast(30).reversed()) { task ->
                            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(task.description, style = MaterialTheme.typography.bodyLarge)
                                    Text("Статус: ${task.status} • ${task.createdAt}", style = MaterialTheme.typography.bodySmall)
                                    task.result?.let { Text(it, modifier = Modifier.padding(top = 4.dp)) }
                                }
                            }
                        } }
                    }
                    "Задачі" -> LazyColumn { items(tasks.reversed()) { Text("${it.status}: ${it.description}", Modifier.padding(8.dp)) } }
                    "Аудит" -> LazyColumn { items(audit.reversed()) { Text("${it.action} — ${it.details}", Modifier.padding(8.dp)) } }
                    else -> {
                        val detail = when (section) {
                            "Всесвіти" -> "TODO: створення, статус і активна версія Всесвітів."
                            "ІСТИНИ" -> "TODO: сховище перевірених рішень зі статусами доказів."
                            "Еволюція" -> "TODO: baseline → candidate → тести → benchmark gate → accept/reject → rollback."
                            "Армагедон" -> "TODO: ізольовані експерименти, fuzzing, stress tests і відновлення snapshot."
                            "Vault" -> "TODO: Discovery Vault, правила доступу та журнал видачі."
                            "Спеціалісти" -> "TODO: AgentProfile, навички, оцінювання та Super-Specialists."
                            else -> "Строгий offline режим. TODO: вибір локальної моделі, резервні копії та дозволи."
                        }
                        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
                            Text(section, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(detail)
                            Spacer(Modifier.height(12.dp))
                            Text("Стан: NOT_IMPLEMENTED", color = MaterialTheme.colorScheme.error)
                        } }
                    }
                }
            }
        }
    }
}

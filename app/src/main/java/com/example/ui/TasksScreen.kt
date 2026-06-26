package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksScreen(viewModel: TaskBinViewModel, onAddTask: () -> Unit) {
    val activeTasks by viewModel.activeTasks.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tugas Saya") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Tugas")
            }
        }
    ) { padding ->
        if (activeTasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Tidak ada tugas aktif. Tekan + untuk menambah.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(activeTasks) { task ->
                    TaskCard(task = task, onComplete = { viewModel.completeTask(task) })
                }
                item { Spacer(modifier = Modifier.height(88.dp)) }
            }
        }
    }
}

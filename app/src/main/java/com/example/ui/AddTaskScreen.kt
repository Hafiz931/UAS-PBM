package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: TaskBinViewModel, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimatedHours by remember { mutableStateOf("") }
    var difficulty by remember { mutableIntStateOf(2) }
    
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Tambah Tugas") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxWidth()) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Tugas") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = estimatedHours,
                onValueChange = { estimatedHours = it },
                label = { Text("Estimasi Waktu (Jam)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tingkat Kesulitan", style = MaterialTheme.typography.titleSmall)
            SegmentedButtons(selectedValue = difficulty, onSelectionChanged = { difficulty = it })
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    if(title.isNotBlank() && estimatedHours.isNotBlank()) {
                        val hours = estimatedHours.toFloatOrNull() ?: 1.0f
                        // Set deadline to 3 days from now for prototype
                        val deadline = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000)
                        viewModel.addTask(title, 0, description, deadline, difficulty, hours)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Tugas")
            }
        }
    }
}

@Composable
fun SegmentedButtons(selectedValue: Int, onSelectionChanged: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(1 to "Mudah", 2 to "Sedang", 3 to "Sulit").forEach { (value, label) ->
            FilterChip(
                selected = selectedValue == value,
                onClick = { onSelectionChanged(value) },
                label = { Text(label) }
            )
        }
    }
}

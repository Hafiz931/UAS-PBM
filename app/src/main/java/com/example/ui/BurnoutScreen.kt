package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.BurnoutHistory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BurnoutScreen(viewModel: TaskBinViewModel) {
    val burnoutHistory by viewModel.burnoutHistory.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Barometer Burnout") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            val current = burnoutHistory.firstOrNull()
            
            Text("Status Saat Ini", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val score = current?.score ?: 0
                    val level = current?.level ?: "Green"
                    
                    val gaugeColor = when (level) {
                        "Red" -> MaterialTheme.colorScheme.error
                        "Yellow" -> com.example.ui.theme.Warning
                        else -> com.example.ui.theme.Success
                    }
                    
                    Text("Skor: $score / 100", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = gaugeColor)
                    
                    val statusText = when(level) {
                        "Red" -> "Bahaya"
                        "Yellow" -> "Waspada"
                        else -> "Aman"
                    }
                    Text("Tingkat: $statusText", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val recommendation = when (level) {
                        "Red" -> "Risiko Burnout Tinggi! Atur ulang jadwal, istirahatlah, dan utamakan kesehatan mental."
                        "Yellow" -> "Hati-hati. Beban kerjamu berat. Coba bagi tugas menjadi bagian-bagian kecil."
                        else -> "Beban kerja sehat. Pertahankan produktivitas yang mindful!"
                    }
                    Text(recommendation, style = MaterialTheme.typography.bodyMedium)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Riwayat Terbaru", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(burnoutHistory.drop(1)) { historyItem ->
                    ListTile(historyItem)
                }
            }
        }
    }
}

@Composable
fun ListTile(history: BurnoutHistory) {
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val dateString = format.format(Date(history.timestamp))
    val statusText = when(history.level) {
        "Red" -> "Bahaya"
        "Yellow" -> "Waspada"
        else -> "Aman"
    }
    ListItem(
        headlineContent = { Text("Skor: ${history.score}") },
        supportingContent = { Text(dateString) },
        trailingContent = { Text(statusText, fontWeight = FontWeight.Bold) }
    )
    HorizontalDivider()
}

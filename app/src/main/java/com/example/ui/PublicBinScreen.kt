package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.PublicKnowledge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicBinScreen(viewModel: TaskBinViewModel) {
    val publicKnowledge by viewModel.publicKnowledge.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.seedPublicKnowledge()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pustaka Publik") }) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Daur Ulang Ilmu", style = MaterialTheme.typography.titleLarge)
                Text("Bantu orang lain dengan berbagi sumber daya.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(publicKnowledge) { knowledge ->
                KnowledgeCard(knowledge)
            }
        }
    }
}

@Composable
fun KnowledgeCard(knowledge: PublicKnowledge) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(knowledge.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Peringkat", tint = com.example.ui.theme.Warning, modifier = Modifier.size(16.dp))
                    Text("${knowledge.rating}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(knowledge.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Mata Kuliah: ${knowledge.courseName} | Oleh: ${knowledge.uploaderName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                IconButton(onClick = { /* Mock Download */ }) {
                    Icon(Icons.Default.Download, contentDescription = "Unduh")
                }
            }
        }
    }
}

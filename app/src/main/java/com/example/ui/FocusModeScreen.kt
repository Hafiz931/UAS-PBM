package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusModeScreen(viewModel: TaskBinViewModel) {
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(25 * 60) } // 25 mins

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            if (timeLeft == 0) {
                viewModel.addFocusSession(25)
                isRunning = false
                timeLeft = 5 * 60 // 5 min break
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mode Fokus") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            Text(
                String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { isRunning = true }, enabled = !isRunning) {
                    Text("Mulai Fokus")
                }
                OutlinedButton(onClick = { isRunning = false }) {
                    Text("Jeda")
                }
                TextButton(onClick = { 
                    isRunning = false
                    timeLeft = 25 * 60
                }) {
                    Text("Ulangi")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Tetap terhidrasi! Ambil jeda untuk mencegah burnout.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

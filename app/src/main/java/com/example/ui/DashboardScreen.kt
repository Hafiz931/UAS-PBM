package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.Task
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: TaskBinViewModel, onNavigateToTasks: () -> Unit) {
    val activeTasks by viewModel.activeTasks.collectAsState()
    val burnoutHistory by viewModel.burnoutHistory.collectAsState()
    val todayFocusTime by viewModel.todayFocusTime.collectAsState()

    val currentBurnout = burnoutHistory.firstOrNull()
    val score = currentBurnout?.score ?: 0
    val level = currentBurnout?.level ?: "Green"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding() + 80.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    val resourceId = LocalContext.current.resources.getIdentifier("dashboard_hero_light_1782284713167", "drawable", LocalContext.current.packageName)
                    if (resourceId != 0) {
                        Image(
                            painter = painterResource(id = resourceId),
                            contentDescription = "Hero Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Primary))
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                    startY = 200f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            "Selamat Datang,",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Text(
                            "Siap untuk fokus?",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    val gaugeColor = when (level) {
                        "Red" -> Danger
                        "Yellow" -> Warning
                        else -> Success
                    }
                    val containerColor = when (level) {
                        "Red" -> DangerContainer
                        "Yellow" -> WarningContainer
                        else -> SuccessContainer
                    }
                    
                    Card(
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Tingkat Burnout", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = gaugeColor)
                                Text("$score / 100", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = gaugeColor)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            LinearProgressIndicator(
                                progress = { score / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = gaugeColor,
                                trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            val statusText = when(level) {
                                "Red" -> "Bahaya"
                                "Yellow" -> "Waspada"
                                else -> "Aman"
                            }
                            Text(
                                text = "Status: $statusText.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = gaugeColor.copy(alpha = 0.8f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Tugas Aktif",
                        value = "${activeTasks.size}",
                        icon = Icons.AutoMirrored.Outlined.Assignment,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Waktu Fokus",
                        value = "${todayFocusTime ?: 0}m",
                        icon = Icons.Outlined.Timer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tugas Mendatang", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onNavigateToTasks) {
                        Text("Lihat Semua")
                    }
                }
            }

            if (activeTasks.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Semua tugas selesai! Kerja bagus.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            items(activeTasks.take(3)) { task ->
                TaskCard(task = task, onComplete = { viewModel.completeTask(task) })
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TaskCard(task: Task, onComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                Text("Tenggat ${format.format(Date(task.deadline))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val diffColor = when(task.difficulty) {
                        1 -> Success
                        2 -> Warning
                        else -> Danger
                    }
                    val diffContainerColor = when(task.difficulty) {
                        1 -> SuccessContainer
                        2 -> WarningContainer
                        else -> DangerContainer
                    }
                    val diffText = when(task.difficulty) {
                        1 -> "Mudah"
                        2 -> "Sedang"
                        else -> "Sulit"
                    }
                    Surface(
                        color = diffContainerColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            diffText, 
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), 
                            style = MaterialTheme.typography.labelSmall, 
                            color = diffColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "${task.estimatedHours}h", 
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), 
                            style = MaterialTheme.typography.labelSmall, 
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            IconButton(
                onClick = onComplete,
                modifier = Modifier
                    .size(48.dp)
                    .background(SuccessContainer, RoundedCornerShape(24.dp))
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Selesaikan Tugas", tint = Success, modifier = Modifier.size(28.dp))
            }
        }
    }
}

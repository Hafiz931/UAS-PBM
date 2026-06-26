package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.BurnoutHistory
import com.example.data.Course
import com.example.data.FocusSession
import com.example.data.PublicKnowledge
import com.example.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskBinViewModel(private val repository: AppRepository) : ViewModel() {
    val allTasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val activeTasks: StateFlow<List<Task>> = repository.activeTasks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val allCourses: StateFlow<List<Course>> = repository.allCourses.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val burnoutHistory: StateFlow<List<BurnoutHistory>> = repository.burnoutHistory.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val publicKnowledge: StateFlow<List<PublicKnowledge>> = repository.publicKnowledge.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val todayFocusTime: StateFlow<Int?> = repository.getTodayFocusTime().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    fun addTask(title: String, courseId: Int, description: String, deadline: Long, difficulty: Int, estimatedHours: Float) {
        viewModelScope.launch {
            repository.insertTask(Task(
                title = title,
                courseId = courseId,
                description = description,
                deadline = deadline,
                difficulty = difficulty,
                estimatedHours = estimatedHours,
                status = "Pending"
            ))
            updateBurnoutScore()
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = "Completed"))
            updateBurnoutScore()
        }
    }

    fun addCourse(name: String) {
        viewModelScope.launch { repository.insertCourse(Course(name = name)) }
    }

    fun addFocusSession(durationMinutes: Int) {
        viewModelScope.launch {
            repository.insertFocusSession(FocusSession(durationMinutes = durationMinutes, timestamp = System.currentTimeMillis()))
        }
    }

    fun seedPublicKnowledge() {
        viewModelScope.launch {
            // Check if empty
            if (publicKnowledge.value.isEmpty()) {
                repository.insertPublicKnowledge(PublicKnowledge(title = "Mobile App Architecture", description = "PDF containing MVVM best practices", courseName = "Mobile Programming", semester = 6, topic = "Architecture", uploaderName = "Anonymous", rating = 4.8f))
                repository.insertPublicKnowledge(PublicKnowledge(title = "Cryptography Cheat Sheet", description = "Quick formulae for RSA and AES", courseName = "Cryptography", semester = 6, topic = "Algorithms", uploaderName = "Jane Doe", rating = 5.0f))
            }
        }
    }

    private suspend fun updateBurnoutScore() {
        val tasks = activeTasks.value
        if (tasks.isEmpty()) return
        
        var score = 0f
        tasks.forEach { task ->
            score += task.estimatedHours * 2
            score += task.difficulty * 3
            val daysUntilDeadline = ((task.deadline - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toFloat()
            if (daysUntilDeadline < 3) {
                score += 15 // High penalty for approaching deadlines
            }
        }
        
        val normalizedScore = (score).toInt().coerceIn(0, 100)
        val level = when {
            normalizedScore <= 40 -> "Green"
            normalizedScore <= 70 -> "Yellow"
            else -> "Red"
        }
        
        repository.insertBurnoutHistory(BurnoutHistory(score = normalizedScore, level = level, timestamp = System.currentTimeMillis()))
    }
}

class TaskBinViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskBinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskBinViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

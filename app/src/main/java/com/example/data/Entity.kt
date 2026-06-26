package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: Int, // Can be 0 if no course
    val title: String,
    val description: String,
    val deadline: Long, // timestamp
    val difficulty: Int, // 1: Easy, 2: Medium, 3: Hard
    val estimatedHours: Float,
    val status: String, // "Pending", "In Progress", "Completed"
    val priorityScore: Float = 0f
)

@Entity(tableName = "burnout_history")
data class BurnoutHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val level: String, // "Green", "Yellow", "Red"
    val timestamp: Long
)

@Entity(tableName = "public_knowledge")
data class PublicKnowledge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val courseName: String,
    val semester: Int,
    val topic: String,
    val uploaderName: String, // Mock anonymous or real name
    val rating: Float = 0f
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val durationMinutes: Int,
    val timestamp: Long
)

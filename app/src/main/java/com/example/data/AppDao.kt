package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Tasks
    @Query("SELECT * FROM tasks ORDER BY priorityScore DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status != 'Completed' ORDER BY deadline ASC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE courseId = :courseId ORDER BY deadline ASC")
    fun getTasksByCourseId(courseId: Int): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // Courses
    @Query("SELECT * FROM courses ORDER BY name ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    // Burnout
    @Query("SELECT * FROM burnout_history ORDER BY timestamp DESC LIMIT 7")
    fun getBurnoutHistory(): Flow<List<BurnoutHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBurnoutHistory(history: BurnoutHistory)
    
    // Focus Sessions
    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE timestamp >= :startOfDay")
    fun getTodayFocusTime(startOfDay: Long): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSession)

    // Public Knowledge
    @Query("SELECT * FROM public_knowledge ORDER BY id DESC")
    fun getAllPublicKnowledge(): Flow<List<PublicKnowledge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublicKnowledge(knowledge: PublicKnowledge)
}

package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AppRepository(private val appDao: AppDao) {
    val allTasks: Flow<List<Task>> = appDao.getAllTasks()
    val activeTasks: Flow<List<Task>> = appDao.getActiveTasks()
    val allCourses: Flow<List<Course>> = appDao.getAllCourses()
    val burnoutHistory: Flow<List<BurnoutHistory>> = appDao.getBurnoutHistory()
    val publicKnowledge: Flow<List<PublicKnowledge>> = appDao.getAllPublicKnowledge()

    fun getTasksByCourseId(courseId: Int) = appDao.getTasksByCourseId(courseId)
    
    fun getTodayFocusTime(): Flow<Int?> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return appDao.getTodayFocusTime(calendar.timeInMillis)
    }

    suspend fun insertTask(task: Task) {
        val calculatedPriority = calculatePriority(task)
        appDao.insertTask(task.copy(priorityScore = calculatedPriority))
    }
    
    suspend fun updateTask(task: Task) {
        val calculatedPriority = calculatePriority(task)
        appDao.updateTask(task.copy(priorityScore = calculatedPriority))
    }
    suspend fun deleteTask(task: Task) = appDao.deleteTask(task)

    suspend fun insertCourse(course: Course) = appDao.insertCourse(course)
    suspend fun deleteCourse(course: Course) = appDao.deleteCourse(course)

    suspend fun insertBurnoutHistory(history: BurnoutHistory) = appDao.insertBurnoutHistory(history)
    suspend fun insertFocusSession(session: FocusSession) = appDao.insertFocusSession(session)
    suspend fun insertPublicKnowledge(knowledge: PublicKnowledge) = appDao.insertPublicKnowledge(knowledge)

    private fun calculatePriority(task: Task): Float {
        val daysUntilDeadline = ((task.deadline - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toFloat()
        val deadlineWeight = if (daysUntilDeadline > 0) 10f / daysUntilDeadline else 20f
        val difficultyWeight = task.difficulty * 2f
        val timeWeight = task.estimatedHours * 0.5f
        return deadlineWeight + difficultyWeight + timeWeight
    }
}

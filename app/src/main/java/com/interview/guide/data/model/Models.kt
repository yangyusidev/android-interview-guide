package com.interview.guide.data.model

import androidx.compose.ui.graphics.Color
import com.interview.guide.ui.theme.*

/**
 * 面试题分类
 */
enum class Category(
    val displayName: String,
    val color: Color,
    val icon: String
) {
    JAVA("Java基础", CategoryJava, "J"),
    KOTLIN("Kotlin", CategoryKotlin, "K"),
    ANDROID("Android基础", CategoryAndroid, "A"),
    JETPACK("Jetpack组件", CategoryJetpack, "JP"),
    ARCHITECTURE("架构设计", CategoryArchitecture, "AR"),
    PERFORMANCE("性能优化", CategoryPerformance, "PF")
}

/**
 * 难度等级
 */
enum class Difficulty(val displayName: String, val level: Int) {
    EASY("初级", 1),
    MEDIUM("中级", 2),
    HARD("高级", 3)
}

/**
 * 面试题
 */
data class Question(
    val id: Int,
    val title: String,
    val category: Category,
    val difficulty: Difficulty,
    val question: String,
    val answer: String,
    val codeExample: String? = null,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val isLearned: Boolean = false
)

/**
 * 学习进度
 */
data class LearningProgress(
    val category: Category,
    val totalQuestions: Int,
    val learnedQuestions: Int
) {
    val progress: Float
        get() = if (totalQuestions > 0) learnedQuestions.toFloat() / totalQuestions else 0f
}

/**
 * 用户统计
 */
data class UserStats(
    val totalQuestions: Int,
    val learnedQuestions: Int,
    val favoriteQuestions: Int,
    val streakDays: Int
)

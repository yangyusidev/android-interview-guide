package com.interview.guide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.interview.guide.data.SampleData
import com.interview.guide.data.model.Question
import com.interview.guide.ui.components.EmptyState
import com.interview.guide.ui.components.QuestionCard
import com.interview.guide.ui.components.SearchBar
import com.interview.guide.ui.theme.AndroidInterviewGuideTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onQuestionClick: (Question) -> Unit,
    bottomInset: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val favoriteQuestions = SampleData.questions.filter { it.isFavorite }
    var searchQuery by remember { mutableStateOf("") }

    val filteredQuestions = favoriteQuestions.filter { question ->
        searchQuery.isEmpty() || question.title.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "我的收藏",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = bottomInset + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 搜索栏
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "在收藏中搜索..."
                )
            }

            // 统计信息
            item {
                Text(
                    text = "共收藏 ${filteredQuestions.size} 道题目",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 收藏列表
            if (filteredQuestions.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Outlined.FavoriteBorder,
                        title = "暂无收藏",
                        description = "浏览题目时点击收藏按钮即可添加到这里"
                    )
                }
            } else {
                items(filteredQuestions) { question ->
                    QuestionCard(
                        question = question,
                        onClick = { onQuestionClick(question) },
                        onFavoriteClick = { /* 取消收藏 */ }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Favorite - Light")
@Composable
private fun FavoriteScreenPreview() {
    AndroidInterviewGuideTheme {
        FavoriteScreen(
            onQuestionClick = {},
            bottomInset = 80.dp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Favorite - Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteScreenDarkPreview() {
    AndroidInterviewGuideTheme {
        FavoriteScreen(
            onQuestionClick = {},
            bottomInset = 80.dp
        )
    }
}

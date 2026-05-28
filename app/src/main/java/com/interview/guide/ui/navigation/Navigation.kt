package com.interview.guide.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.interview.guide.data.SampleData
import com.interview.guide.data.model.Category
import com.interview.guide.ui.screens.*

/**
 * 导航路由
 */
sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen(
        route = "home",
        title = "首页",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Favorite : Screen(
        route = "favorite",
        title = "收藏",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    data object Profile : Screen(
        route = "profile",
        title = "我的",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    data object Category : Screen(
        route = "category/{categoryName}",
        title = "分类",
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category
    ) {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }

    data object QuestionDetail : Screen(
        route = "question/{questionId}",
        title = "题目详情",
        selectedIcon = Icons.Filled.Article,
        unselectedIcon = Icons.Outlined.Article
    ) {
        fun createRoute(questionId: Int) = "question/$questionId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Favorite,
    Screen.Profile
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        // 把底部导航栏高度单独取出来，传给主页面，让它们的 LazyColumn 自己处理底部沉浸
        val bottomBarHeight = paddingValues.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            // 只 padding 顶部状态栏，底部不 padding，让内容可以延伸到 NavigationBar 下方
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            // 首页
            composable(Screen.Home.route) {
                HomeScreen(
                    bottomInset = bottomBarHeight,
                    onCategoryClick = { category ->
                        navController.navigate(Screen.Category.createRoute(category.name))
                    },
                    onQuestionClick = { question ->
                        navController.navigate(Screen.QuestionDetail.createRoute(question.id))
                    }
                )
            }

            // 收藏页
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    bottomInset = bottomBarHeight,
                    onQuestionClick = { question ->
                        navController.navigate(Screen.QuestionDetail.createRoute(question.id))
                    }
                )
            }

            // 个人中心
            composable(Screen.Profile.route) {
                ProfileScreen(
                    bottomInset = bottomBarHeight
                )
            }

            // 分类详情
            composable(
                route = Screen.Category.route,
                arguments = listOf(
                    navArgument("categoryName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                val category = Category.entries.find { it.name == categoryName } ?: Category.JAVA

                CategoryScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() },
                    onQuestionClick = { question ->
                        navController.navigate(Screen.QuestionDetail.createRoute(question.id))
                    }
                )
            }

            // 题目详情
            composable(
                route = Screen.QuestionDetail.route,
                arguments = listOf(
                    navArgument("questionId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val questionId = backStackEntry.arguments?.getInt("questionId") ?: 0
                val question = SampleData.questions.find { it.id == questionId }

                question?.let {
                    QuestionDetailScreen(
                        question = it,
                        onBackClick = { navController.popBackStack() },
                        onMarkAsLearned = { /* 标记已学 */ },
                        onToggleFavorite = { /* 切换收藏 */ }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 只在主页面显示底部导航
    val showBottomBar = currentDestination?.hierarchy?.any { destination ->
        bottomNavItems.any { it.route == destination.route }
    } == true

    if (showBottomBar) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            bottomNavItems.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                            contentDescription = screen.title
                        )
                    },
                    label = { Text(screen.title) },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    }
}

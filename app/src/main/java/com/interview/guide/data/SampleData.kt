package com.interview.guide.data

import com.interview.guide.data.model.Category
import com.interview.guide.data.model.Difficulty
import com.interview.guide.data.model.LearningProgress
import com.interview.guide.data.model.Question
import com.interview.guide.data.model.UserStats

object SampleData {

    val questions = listOf(
        Question(
            id = 1,
            title = "Java中的四种引用类型",
            category = Category.JAVA,
            difficulty = Difficulty.MEDIUM,
            question = "请说明Java中的四种引用类型及其区别？",
            answer = """
## 四种引用类型

### 1. 强引用 (Strong Reference)
- 最常见的引用类型
- 只要强引用存在，垃圾回收器永远不会回收被引用的对象
- 例如：`Object obj = new Object();`

### 2. 软引用 (Soft Reference)
- 在内存不足时会被回收
- 适合用于实现内存敏感的缓存
- 使用 `SoftReference<T>` 类

### 3. 弱引用 (Weak Reference)
- 只要发生GC就会被回收
- 适合用于实现规范化映射
- 使用 `WeakReference<T>` 类

### 4. 虚引用 (Phantom Reference)
- 不会影响对象的生命周期
- 主要用于跟踪对象被垃圾回收的状态
- 使用 `PhantomReference<T>` 类
            """.trimIndent(),
            codeExample = """
// 强引用
Object strongRef = new Object();

// 软引用
SoftReference<Object> softRef = new SoftReference<>(new Object());

// 弱引用
WeakReference<Object> weakRef = new WeakReference<>(new Object());

// 虚引用
ReferenceQueue<Object> queue = new ReferenceQueue<>();
PhantomReference<Object> phantomRef = new PhantomReference<>(new Object(), queue);
            """.trimIndent(),
            tags = listOf("GC", "内存管理", "引用")
        ),
        Question(
            id = 2,
            title = "Kotlin协程原理",
            category = Category.KOTLIN,
            difficulty = Difficulty.HARD,
            question = "请解释Kotlin协程的工作原理，以及suspend关键字的作用？",
            answer = """
## Kotlin协程原理

### 协程是什么
协程是一种轻量级的线程，可以挂起和恢复执行，而不会阻塞线程。

### suspend关键字
- 标记函数为可挂起函数
- 只能在协程作用域或其他suspend函数中调用
- 编译器会将suspend函数转换为状态机

### 核心组件
1. **CoroutineScope** - 协程作用域，管理协程生命周期
2. **CoroutineContext** - 协程上下文，包含Job、Dispatcher等
3. **Dispatcher** - 调度器，决定协程在哪个线程执行

### 工作原理
1. 编译器将suspend函数转换为状态机
2. 每个挂起点对应一个状态
3. Continuation对象保存当前状态和局部变量
4. 恢复时从上次挂起点继续执行
            """.trimIndent(),
            codeExample = """
// 定义suspend函数
suspend fun fetchData(): String {
    delay(1000) // 挂起点
    return "Data"
}

// 在协程中调用
viewModelScope.launch {
    val data = fetchData()
    // 处理数据
}

// 不同的调度器
launch(Dispatchers.IO) { /* IO操作 */ }
launch(Dispatchers.Main) { /* UI操作 */ }
launch(Dispatchers.Default) { /* CPU密集型 */ }
            """.trimIndent(),
            tags = listOf("协程", "异步", "并发")
        ),
        Question(
            id = 3,
            title = "Activity生命周期",
            category = Category.ANDROID,
            difficulty = Difficulty.EASY,
            question = "请详细描述Activity的生命周期及各个回调方法的作用？",
            answer = """
## Activity生命周期

### 完整生命周期
`onCreate()` → `onStart()` → `onResume()` → `onPause()` → `onStop()` → `onDestroy()`

### 各回调方法说明

#### onCreate()
- Activity首次创建时调用
- 进行初始化工作：setContentView、初始化变量等

#### onStart()
- Activity变为可见状态
- 但还不能与用户交互

#### onResume()
- Activity位于前台，可以与用户交互
- 此时Activity处于运行状态

#### onPause()
- 即将失去焦点
- 适合保存轻量级数据，释放系统资源

#### onStop()
- Activity完全不可见
- 可能被系统回收

#### onDestroy()
- Activity即将被销毁
- 释放所有资源

### 特殊情况
- 配置变更（如屏幕旋转）会导致Activity重建
- 可使用 `onSaveInstanceState()` 保存状态
            """.trimIndent(),
            tags = listOf("生命周期", "Activity")
        ),
        Question(
            id = 4,
            title = "Jetpack Compose重组机制",
            category = Category.JETPACK,
            difficulty = Difficulty.HARD,
            question = "请解释Jetpack Compose的重组(Recomposition)机制？",
            answer = """
## Compose重组机制

### 什么是重组
重组是Compose在状态变化时重新调用Composable函数来更新UI的过程。

### 智能重组
- Compose只会重组受状态变化影响的部分
- 跳过输入未变化的Composable函数
- 使用稳定类型可以帮助优化重组

### 重组触发条件
1. 读取的State对象发生变化
2. Composable函数的参数发生变化

### 优化重组的方法
1. **使用remember** - 缓存计算结果
2. **使用key** - 帮助Compose识别列表项
3. **提取无状态组件** - 将状态上提
4. **使用derivedStateOf** - 避免不必要的重组
5. **使用Stable/Immutable** - 标记稳定类型
            """.trimIndent(),
            codeExample = """
@Composable
fun Counter() {
    // 状态变化会触发重组
    var count by remember { mutableStateOf(0) }
    
    Column {
        // 只有依赖count的部分会重组
        Text("Count: ${'$'}count")
        
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}

// 使用derivedStateOf优化
val isEnabled by remember {
    derivedStateOf { count > 0 }
}
            """.trimIndent(),
            tags = listOf("Compose", "重组", "性能")
        ),
        Question(
            id = 5,
            title = "MVVM架构模式",
            category = Category.ARCHITECTURE,
            difficulty = Difficulty.MEDIUM,
            question = "请解释MVVM架构模式在Android中的应用？",
            answer = """
## MVVM架构模式

### 三层结构

#### Model（数据层）
- Repository：数据仓库，协调不同数据源
- DataSource：本地数据库、网络API等

#### View（视图层）
- Activity/Fragment/Composable
- 只负责UI展示和用户交互
- 观察ViewModel中的数据变化

#### ViewModel（逻辑层）
- 持有UI状态和业务逻辑
- 通过LiveData/StateFlow暴露数据
- 生命周期感知，配置变更时不会销毁

### 优势
1. 关注点分离，代码更清晰
2. 便于单元测试
3. UI和业务逻辑解耦
4. ViewModel可被多个View共享
            """.trimIndent(),
            codeExample = """
// ViewModel
class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()
    
    fun loadUser(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = repository.getUser(id)
            _uiState.update { it.copy(user = user, isLoading = false) }
        }
    }
}

// Composable
@Composable
fun UserScreen(viewModel: UserViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        Text(uiState.user?.name ?: "")
    }
}
            """.trimIndent(),
            tags = listOf("MVVM", "架构", "ViewModel")
        ),
        Question(
            id = 6,
            title = "内存泄漏排查",
            category = Category.PERFORMANCE,
            difficulty = Difficulty.MEDIUM,
            question = "Android中常见的内存泄漏场景有哪些？如何排查和解决？",
            answer = """
## 常见内存泄漏场景

### 1. 静态变量持有Context
```kotlin
// 错误示例
companion object {
    var context: Context? = null
}
```

### 2. 非静态内部类持有外部类引用
- Handler、Thread、AsyncTask等
- 解决：使用静态内部类 + 弱引用

### 3. 未取消的监听器/回调
- 注册广播未注销
- 添加监听器未移除

### 4. 资源未关闭
- Cursor、Stream、Bitmap等

### 5. 单例持有Activity引用

## 排查工具
1. **LeakCanary** - 自动检测内存泄漏
2. **Android Profiler** - 分析内存使用
3. **MAT** - 分析堆转储文件

## 解决方案
- 使用ApplicationContext代替Activity Context
- 及时取消异步任务
- 使用弱引用
- 在onDestroy中释放资源
            """.trimIndent(),
            tags = listOf("内存泄漏", "性能优化", "LeakCanary")
        )
    )

    fun getQuestionsByCategory(category: Category): List<Question> {
        return questions.filter { it.category == category }
    }

    fun getProgress(): List<LearningProgress> {
        return Category.entries.map { category ->
            val categoryQuestions = questions.filter { it.category == category }
            LearningProgress(
                category = category,
                totalQuestions = categoryQuestions.size,
                learnedQuestions = categoryQuestions.count { it.isLearned }
            )
        }
    }

    fun getUserStats(): UserStats {
        return UserStats(
            totalQuestions = questions.size,
            learnedQuestions = questions.count { it.isLearned },
            favoriteQuestions = questions.count { it.isFavorite },
            streakDays = 7
        )
    }
}

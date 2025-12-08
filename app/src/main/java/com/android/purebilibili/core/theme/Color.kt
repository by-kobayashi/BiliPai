package com.android.purebilibili.core.theme

import androidx.compose.ui.graphics.Color

// --- B站核心品牌色 ---
val BiliPink = Color(0xFFFA7298)
val BiliPinkDim = Color(0xFFE6688C) // 按压态
val BiliPinkLight = Color(0xFFFFEBF0) // 浅粉色背景 (用于高亮区域)

// --- 背景色 ---
val BiliBackground = Color(0xFFF1F2F3) // 经典淡灰背景 (APP底色)
val SurfaceCard = Color(0xFFFFFFFF)    // 卡片背景 (纯白)

// --- 文字颜色 ---
val TextPrimary = Color(0xFF18191C)   // 主要文字 (接近黑)
val TextSecondary = Color(0xFF61666D) // 次要文字 (深灰)
val TextTertiary = Color(0xFF9499A0)  // 辅助文字 (浅灰)

// --- 基础色 ---
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

// --- 深色模式适配 ---
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF222222)
val BiliPinkDark = Color(0xFFD46282)
val TextPrimaryDark = Color(0xFFE1E1E1)
val TextSecondaryDark = Color(0xFFAAAAAA)

// 🔥🔥 [新增] --- 预设主题色 (用于自定义主题) ---
val ThemeColors = listOf(
    Color(0xFFFA7298),  // 0: 粉色 (默认 BiliPink)
    Color(0xFF00A1D6),  // 1: 蓝色 (Bilibili Blue)
    Color(0xFF4CAF50),  // 2: 绿色 (Material Green)
    Color(0xFF9C27B0),  // 3: 紫色 (Material Purple)
    Color(0xFFFF5722),  // 4: 橙色 (Material Deep Orange)
    Color(0xFF607D8B),  // 5: 蓝灰色 (Material Blue Grey)
)

val ThemeColorNames = listOf("粉色", "蓝色", "绿色", "紫色", "橙色", "蓝灰")
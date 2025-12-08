// 文件路径: core/store/SettingsManager.kt
package com.android.purebilibili.core.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.purebilibili.feature.settings.AppThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 声明 DataStore 扩展属性
private val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

object SettingsManager {
    // 键定义
    private val KEY_AUTO_PLAY = booleanPreferencesKey("auto_play")
    private val KEY_HW_DECODE = booleanPreferencesKey("hw_decode")
    private val KEY_THEME_MODE = intPreferencesKey("theme_mode_v2")
    private val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    private val KEY_BG_PLAY = booleanPreferencesKey("bg_play")
    // 🔥🔥 [新增] 手势灵敏度和主题色
    private val KEY_GESTURE_SENSITIVITY = floatPreferencesKey("gesture_sensitivity")
    private val KEY_THEME_COLOR_INDEX = intPreferencesKey("theme_color_index")

    // --- Auto Play ---
    fun getAutoPlay(context: Context): Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_AUTO_PLAY] ?: true }

    suspend fun setAutoPlay(context: Context, value: Boolean) {
        context.settingsDataStore.edit { preferences -> preferences[KEY_AUTO_PLAY] = value }
    }

    // --- HW Decode ---
    fun getHwDecode(context: Context): Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_HW_DECODE] ?: true }

    suspend fun setHwDecode(context: Context, value: Boolean) {
        context.settingsDataStore.edit { preferences -> preferences[KEY_HW_DECODE] = value }
    }

    // --- Theme Mode ---
    fun getThemeMode(context: Context): Flow<AppThemeMode> = context.settingsDataStore.data
        .map { preferences ->
            val modeInt = preferences[KEY_THEME_MODE] ?: AppThemeMode.FOLLOW_SYSTEM.value
            AppThemeMode.fromValue(modeInt)
        }

    suspend fun setThemeMode(context: Context, mode: AppThemeMode) {
        context.settingsDataStore.edit { preferences -> preferences[KEY_THEME_MODE] = mode.value }
    }

    // --- Dynamic Color ---
    fun getDynamicColor(context: Context): Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_DYNAMIC_COLOR] ?: true }

    suspend fun setDynamicColor(context: Context, value: Boolean) {
        context.settingsDataStore.edit { preferences -> preferences[KEY_DYNAMIC_COLOR] = value }
    }

    // --- 后台/画中画播放 ---
    fun getBgPlay(context: Context): Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_BG_PLAY] ?: false }

    suspend fun setBgPlay(context: Context, value: Boolean) {
        context.settingsDataStore.edit { preferences -> preferences[KEY_BG_PLAY] = value }
    }

    // 🔥🔥 [新增] --- 手势灵敏度 (0.5 ~ 2.0, 默认 1.0) ---
    fun getGestureSensitivity(context: Context): Flow<Float> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_GESTURE_SENSITIVITY] ?: 1.0f }

    suspend fun setGestureSensitivity(context: Context, value: Float) {
        context.settingsDataStore.edit { preferences -> 
            preferences[KEY_GESTURE_SENSITIVITY] = value.coerceIn(0.5f, 2.0f) 
        }
    }

    // 🔥🔥 [新增] --- 主题色索引 (0-5, 默认 0 = BiliPink) ---
    fun getThemeColorIndex(context: Context): Flow<Int> = context.settingsDataStore.data
        .map { preferences -> preferences[KEY_THEME_COLOR_INDEX] ?: 0 }

    suspend fun setThemeColorIndex(context: Context, index: Int) {
        context.settingsDataStore.edit { preferences -> 
            preferences[KEY_THEME_COLOR_INDEX] = index.coerceIn(0, 5)
        }
    }
}
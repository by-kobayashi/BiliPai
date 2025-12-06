// 文件路径: feature/settings/SettingsViewModel.kt
package com.android.purebilibili.feature.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.purebilibili.core.store.SettingsManager
import com.android.purebilibili.core.util.CacheUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val autoPlay: Boolean = true,
    val hwDecode: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = true,
    val bgPlay: Boolean = false,
    val cacheSize: String = "计算中..."
)

// 内部数据类，用于分批合并流
private data class BaseSettings(
    val autoPlay: Boolean,
    val hwDecode: Boolean,
    val themeMode: AppThemeMode,
    val dynamicColor: Boolean,
    val bgPlay: Boolean
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    // 本地状态流：缓存大小
    private val _cacheSize = MutableStateFlow("计算中...")

    // 🔥🔥 [核心修复] 分两步合并，解决 combine 参数限制报错
    // 第 1 步：合并 DataStore 的 5 个设置
    private val baseSettingsFlow = combine(
        SettingsManager.getAutoPlay(context),
        SettingsManager.getHwDecode(context),
        SettingsManager.getThemeMode(context),
        SettingsManager.getDynamicColor(context),
        SettingsManager.getBgPlay(context)
    ) { autoPlay, hwDecode, themeMode, dynamicColor, bgPlay ->
        BaseSettings(autoPlay, hwDecode, themeMode, dynamicColor, bgPlay)
    }

    // 第 2 步：与缓存大小合并
    val state: StateFlow<SettingsUiState> = combine(
        baseSettingsFlow,
        _cacheSize
    ) { settings, cacheSize ->
        SettingsUiState(
            autoPlay = settings.autoPlay,
            hwDecode = settings.hwDecode,
            themeMode = settings.themeMode,
            dynamicColor = settings.dynamicColor,
            bgPlay = settings.bgPlay,
            cacheSize = cacheSize
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    init {
        refreshCacheSize()
    }

    // --- 功能方法 ---

    fun refreshCacheSize() {
        viewModelScope.launch { _cacheSize.value = CacheUtils.getTotalCacheSize(context) }
    }

    fun clearCache() {
        viewModelScope.launch {
            CacheUtils.clearAllCache(context)
            _cacheSize.value = CacheUtils.getTotalCacheSize(context)
        }
    }

    fun toggleAutoPlay(value: Boolean) { viewModelScope.launch { SettingsManager.setAutoPlay(context, value) } }
    fun toggleHwDecode(value: Boolean) { viewModelScope.launch { SettingsManager.setHwDecode(context, value) } }
    fun setThemeMode(mode: AppThemeMode) { viewModelScope.launch { SettingsManager.setThemeMode(context, mode) } }
    fun toggleDynamicColor(value: Boolean) { viewModelScope.launch { SettingsManager.setDynamicColor(context, value) } }
    fun toggleBgPlay(value: Boolean) { viewModelScope.launch { SettingsManager.setBgPlay(context, value) } }

    // --- App Icon Switching ---

    private val _currentIcon = MutableStateFlow(".MainActivityDefault")
    val currentIcon: StateFlow<String> = _currentIcon

    init {
        refreshCacheSize()
        viewModelScope.launch {
            _currentIcon.value = getCurrentIconAlias()
        }
    }

    fun getCurrentIconAlias(): String {
        val pm = context.packageManager
        val packageName = context.packageName
        
        val aliases = listOf(
            ".MainActivityDefault", // New default alias
            ".MainActivityMinimalist",
            ".MainActivityGlass",
            ".MainActivityMascot",
            ".MainActivityMascotBlue",
            ".MainActivityAbstract"
        )

        for (alias in aliases) {
            val componentName = android.content.ComponentName(packageName, "$packageName$alias")
            if (pm.getComponentEnabledSetting(componentName) == android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                return alias
            }
        }
        // If nothing is explicitly enabled or we are in a weird state, return current expectation
        return ".MainActivityDefault"
    }

    fun changeAppIcon(aliasName: String) {
        viewModelScope.launch {
            val pm = context.packageManager
            val packageName = context.packageName
            val currentAlias = getCurrentIconAlias()

            if (currentAlias == aliasName) return@launch

            // Disable current
            val disableComponent = android.content.ComponentName(packageName, "$packageName$currentAlias")
            pm.setComponentEnabledSetting(
                disableComponent,
                android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                android.content.pm.PackageManager.DONT_KILL_APP
            )

            // Enable new
            val enableComponent = android.content.ComponentName(packageName, "$packageName$aliasName")
            pm.setComponentEnabledSetting(
                enableComponent,
                android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                android.content.pm.PackageManager.DONT_KILL_APP
            )
            
            // Update state (though app might restart)
            _currentIcon.value = aliasName
        }
    }
}
package com.android.purebilibili.core.plugin

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.StateFlow

data class CastPluginRoute(
    val routeId: String,
    val name: String,
    val description: String? = null,
    val icon: ImageVector? = null
)

data class CastPluginMediaRequest(
    val url: String,
    val title: String,
    val creator: String = "",
    val contentType: String = "video/mp4"
)

interface CastPluginApi : Plugin {
    val routes: StateFlow<List<CastPluginRoute>>
    fun startRouteDiscovery(context: Context)
    fun stopRouteDiscovery()
    suspend fun cast(
        context: Context,
        route: CastPluginRoute,
        media: CastPluginMediaRequest
    ): Result<Unit>
}

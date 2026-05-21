package com.android.purebilibili.navigation3

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

private const val BILI_PAI_NAV_ROUTE_BASE_METADATA_KEY = "biliPaiNavRouteBase"
private const val VIDEO_ROUTE_BASE = "video"
private val CARD_RETURN_TARGET_ROUTE_BASES = setOf(
    "home",
    "dynamic",
    "search",
    "history",
    "favorite",
    "watch_later",
    "partition",
    "dynamic_detail",
    "space",
    "category"
)

internal fun biliPaiNavEntryProvider(
    sourceMetadata: BiliPaiNavSourceMetadata,
    cardTransitionEnabled: Boolean = true,
    visibleBottomBarRoutes: Set<String> = emptySet(),
    content: @Composable (BiliPaiNavKey) -> Unit
): (BiliPaiNavKey) -> NavEntry<BiliPaiNavKey> {
    val entryMetadata: (BiliPaiNavKey) -> Map<String, Any> = { key ->
        biliPaiNavEntryMetadata(
            key = key,
            sourceMetadata = sourceMetadata,
            cardTransitionEnabled = cardTransitionEnabled,
            visibleBottomBarRoutes = visibleBottomBarRoutes
        )
    }
    return entryProvider(
        fallback = { key ->
            NavEntry(
                key = key,
                metadata = entryMetadata(key),
                content = content
            )
        }
    ) {
        entry<BiliPaiNavKey.MainHost>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Home>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Dynamic>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Search>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.SearchTrending>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.TopicDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Settings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.OpenSourceLicenses>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.AppearanceSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.IconSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.AnimationSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.PlaybackSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.PermissionSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.PluginsSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.BottomBarSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.SettingsShare>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.WebDavBackup>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.TipsSettings>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Login>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Profile>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.History>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Favorite>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.WatchLater>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Onboarding>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Following>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.DownloadList>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.OfflineVideoPlayer>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LiveList>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LiveSearch>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LiveArea>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LiveAreaDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LiveFollowing>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Inbox>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.ReplyMe>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.AtMe>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.LikeMe>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.SystemNotice>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Chat>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Partition>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Story>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.AudioMode>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.SeasonSeriesDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Bangumi>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.BangumiPlayer>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.MusicDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.NativeMusic>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.VideoDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.ArticleDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.DynamicDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Space>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Category>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Live>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.BangumiDetail>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Web>(metadata = entryMetadata, content = content)
        entry<BiliPaiNavKey.Unknown>(metadata = entryMetadata, content = content)
    }
}

internal fun biliPaiNavEntryMetadata(
    key: BiliPaiNavKey,
    sourceMetadata: BiliPaiNavSourceMetadata,
    cardTransitionEnabled: Boolean = true,
    visibleBottomBarRoutes: Set<String> = emptySet()
): Map<String, Any> {
    val transitions = resolveBiliPaiNavEntryRouteTransitions(
        key = key,
        cardTransitionEnabled = cardTransitionEnabled,
        sourceMetadata = sourceMetadata
    )
    return mapOf(
        BILI_PAI_NAV_ROUTE_BASE_METADATA_KEY to key.routeBase
    ) + NavDisplay.transitionSpec {
        val transition = resolveBiliPaiNavEntryForwardRouteTransition(
            defaultTransition = transitions.forward,
            fromRoute = initialState.biliPaiRouteBase(),
            toRoute = targetState.biliPaiRouteBase(),
            visibleBottomBarRoutes = visibleBottomBarRoutes
        )
        resolveBiliPaiNavContentTransform(transition)
    } + NavDisplay.popTransitionSpec {
        val transition = resolveBiliPaiNavEntryPopRouteTransition(
            defaultTransition = transitions.pop,
            fromRoute = initialState.biliPaiRouteBase(),
            toRoute = targetState.biliPaiRouteBase(),
            cardTransitionEnabled = cardTransitionEnabled,
            sourceMetadata = sourceMetadata
        )
        resolveBiliPaiNavContentTransform(transition)
    }
}

internal fun resolveBiliPaiNavEntryForwardRouteTransition(
    defaultTransition: BiliPaiNavRouteTransition,
    fromRoute: String?,
    toRoute: String?,
    visibleBottomBarRoutes: Set<String>
): BiliPaiNavRouteTransition {
    return defaultTransition
}

internal fun resolveBiliPaiNavEntryPopRouteTransition(
    defaultTransition: BiliPaiNavRouteTransition,
    fromRoute: String?,
    toRoute: String?,
    cardTransitionEnabled: Boolean = true,
    sourceMetadata: BiliPaiNavSourceMetadata
): BiliPaiNavRouteTransition {
    val normalizedFromRoute = normalizeBiliPaiNavEntryRouteBase(fromRoute)
    val normalizedToRoute = normalizeBiliPaiNavEntryRouteBase(toRoute)
    val normalizedSourceRoute = normalizeBiliPaiNavEntryRouteBase(sourceMetadata.sourceRoute)
    val sharedReadyVideoToSourceCard = sourceMetadata.sharedTransitionReady &&
        normalizedFromRoute == VIDEO_ROUTE_BASE &&
        normalizedToRoute != null &&
        normalizedToRoute == normalizedSourceRoute &&
        isCardReturnTargetRouteBase(normalizedToRoute)

    if (cardTransitionEnabled && sharedReadyVideoToSourceCard) {
        return BiliPaiNavRouteTransition.NO_OP_SHARED_ELEMENT
    }
    if (!cardTransitionEnabled && sharedReadyVideoToSourceCard) {
        resolveCardDisabledVideoReturnTransition(sourceMetadata.cardSourceDirection)?.let {
            return it
        }
    }

    return if (defaultTransition == BiliPaiNavRouteTransition.NO_OP_SHARED_ELEMENT) {
        BiliPaiNavRouteTransition.FALLBACK
    } else {
        defaultTransition
    }
}

private fun androidx.navigation3.scene.Scene<*>.biliPaiRouteBase(): String? {
    return entries
        .lastOrNull()
        ?.metadata
        ?.get(BILI_PAI_NAV_ROUTE_BASE_METADATA_KEY) as? String
}

internal data class BiliPaiNavEntryRouteTransitions(
    val forward: BiliPaiNavRouteTransition,
    val pop: BiliPaiNavRouteTransition,
    val predictivePop: BiliPaiNavRouteTransition
)

internal fun resolveBiliPaiNavEntryRouteTransitions(
    key: BiliPaiNavKey,
    cardTransitionEnabled: Boolean = true,
    sourceMetadata: BiliPaiNavSourceMetadata
): BiliPaiNavEntryRouteTransitions {
    val recordedMatchingVideoSource = key is BiliPaiNavKey.VideoDetail &&
        sourceMetadata.clickedBoundsRecorded &&
        sourceMetadata.sourceRoute != null &&
        sourceMetadata.sourceRoute == key.sourceRoute &&
        sourceMetadata.sourceKey == "${sourceMetadata.sourceRoute}:${key.bvid}"
    val sharedReadyVideoPush = recordedMatchingVideoSource &&
        sourceMetadata.sharedTransitionReady
    val forward = when {
        cardTransitionEnabled && sharedReadyVideoPush -> BiliPaiNavRouteTransition.NO_OP_SHARED_ELEMENT
        !cardTransitionEnabled && sharedReadyVideoPush ->
            resolveCardDisabledVideoForwardTransition(sourceMetadata.cardSourceDirection)
                ?: BiliPaiNavRouteTransition.FALLBACK
        else -> BiliPaiNavRouteTransition.FALLBACK
    }
    return BiliPaiNavEntryRouteTransitions(
        forward = forward,
        pop = BiliPaiNavRouteTransition.FALLBACK,
        predictivePop = BiliPaiNavRouteTransition.FALLBACK
    )
}

private fun normalizeBiliPaiNavEntryRouteBase(route: String?): String? {
    return route
        ?.substringBefore("?")
        ?.takeIf { it.isNotBlank() }
}

private fun isCardReturnTargetRouteBase(routeBase: String): Boolean {
    return routeBase in CARD_RETURN_TARGET_ROUTE_BASES
}

private fun resolveCardDisabledVideoForwardTransition(
    sourceDirection: BiliPaiNavCardSourceDirection
): BiliPaiNavRouteTransition? {
    return when (sourceDirection) {
        BiliPaiNavCardSourceDirection.SOURCE_LEFT ->
            BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_FORWARD_FROM_LEFT
        BiliPaiNavCardSourceDirection.SOURCE_RIGHT ->
            BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_FORWARD_FROM_RIGHT
        BiliPaiNavCardSourceDirection.NONE -> null
    }
}

private fun resolveCardDisabledVideoReturnTransition(
    sourceDirection: BiliPaiNavCardSourceDirection
): BiliPaiNavRouteTransition? {
    return when (sourceDirection) {
        BiliPaiNavCardSourceDirection.SOURCE_LEFT ->
            BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_RETURN_TO_LEFT
        BiliPaiNavCardSourceDirection.SOURCE_RIGHT ->
            BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_RETURN_TO_RIGHT
        BiliPaiNavCardSourceDirection.NONE -> null
    }
}

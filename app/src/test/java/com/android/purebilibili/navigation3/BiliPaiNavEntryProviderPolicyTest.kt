package com.android.purebilibili.navigation3

import com.android.purebilibili.navigation.ScreenRoutes
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BiliPaiNavEntryProviderPolicyTest {

    @Test
    fun sharedReadyMetadataAloneDoesNotDisableRouteLayerForReturnTarget() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.Home,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true
            )
        )

        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun homeVideoPushUsesNoOpRouteLayerWithRecordedBounds() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "home"),
            cardTransitionEnabled = true,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true
            )
        )

        assertEquals(BiliPaiNavRouteTransition.NO_OP_SHARED_ELEMENT, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun homeVideoPushWithDisabledSharedTransitionUsesLeftSourceFallback() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "home"),
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_LEFT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_FORWARD_FROM_LEFT, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun homeVideoPushWithDisabledSharedTransitionUsesRightSourceFallback() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "home"),
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_RIGHT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_FORWARD_FROM_RIGHT, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun homeVideoPushWithInvisibleSourceKeepsFallbackRouteLayer() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "home"),
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = false,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_LEFT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun bottomTabForwardNavigationKeepsFallbackBecausePagerOwnsTabMotion() {
        val visibleRoutes = setOf(
            ScreenRoutes.Home.route,
            ScreenRoutes.Dynamic.route,
            ScreenRoutes.History.route,
            ScreenRoutes.Profile.route
        )

        assertEquals(
            BiliPaiNavRouteTransition.FALLBACK,
            resolveBiliPaiNavEntryForwardRouteTransition(
                defaultTransition = BiliPaiNavRouteTransition.FALLBACK,
                fromRoute = ScreenRoutes.Home.route,
                toRoute = ScreenRoutes.Profile.route,
                visibleBottomBarRoutes = visibleRoutes
            )
        )
        assertEquals(
            BiliPaiNavRouteTransition.FALLBACK,
            resolveBiliPaiNavEntryForwardRouteTransition(
                defaultTransition = BiliPaiNavRouteTransition.FALLBACK,
                fromRoute = ScreenRoutes.Search.route,
                toRoute = ScreenRoutes.Profile.route,
                visibleBottomBarRoutes = visibleRoutes
            )
        )
    }

    @Test
    fun homeVideoPushWithoutRecordedBoundsKeepsForwardFallback() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "home"),
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = false,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_LEFT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun videoPushWithStaleSharedSourceKeepsForwardFallback() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV2", sourceRoute = "home"),
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_LEFT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun nonHomeVideoPushUsesNoOpRouteLayerWithMatchingVisibleSourceCard() {
        val transitions = resolveBiliPaiNavEntryRouteTransitions(
            key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "history"),
            cardTransitionEnabled = true,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "history:BV1",
                sourceRoute = "history",
                clickedBoundsRecorded = true,
                cardFullyVisible = true
            )
        )

        assertEquals(BiliPaiNavRouteTransition.NO_OP_SHARED_ELEMENT, transitions.forward)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.pop)
        assertEquals(BiliPaiNavRouteTransition.FALLBACK, transitions.predictivePop)
    }

    @Test
    fun entryPopWithDisabledSharedTransitionUsesSourceDirectionFallback() {
        val leftTransition = resolveBiliPaiNavEntryPopRouteTransition(
            defaultTransition = BiliPaiNavRouteTransition.FALLBACK,
            fromRoute = "video",
            toRoute = "home",
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_LEFT
            )
        )
        val rightTransition = resolveBiliPaiNavEntryPopRouteTransition(
            defaultTransition = BiliPaiNavRouteTransition.FALLBACK,
            fromRoute = "video",
            toRoute = "home",
            cardTransitionEnabled = false,
            sourceMetadata = BiliPaiNavSourceMetadata(
                sourceKey = "home:BV1",
                sourceRoute = "home",
                clickedBoundsRecorded = true,
                cardFullyVisible = true,
                cardSourceDirection = BiliPaiNavCardSourceDirection.SOURCE_RIGHT
            )
        )

        assertEquals(BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_RETURN_TO_LEFT, leftTransition)
        assertEquals(BiliPaiNavRouteTransition.CARD_DISABLED_VIDEO_RETURN_TO_RIGHT, rightTransition)
    }

    @Test
    fun providerUsesTypedVideoEntryContentKey() {
        val provider = biliPaiNavEntryProvider(
            sourceMetadata = BiliPaiNavSourceMetadata(),
            content = {}
        )
        val key = BiliPaiNavKey.VideoDetail(bvid = "BV1", sourceRoute = "search")
        val entry = provider(key)

        assertEquals(key.toString(), entry.contentKey)
        assertTrue(entry.metadata.isNotEmpty())
    }

    @Test
    fun providerDoesNotOwnPredictivePopTransition() {
        val source = listOf(
            File("app/src/main/java/com/android/purebilibili/navigation3/BiliPaiNavEntryProvider.kt"),
            File("src/main/java/com/android/purebilibili/navigation3/BiliPaiNavEntryProvider.kt")
        ).first { it.exists() }.readText()

        assertFalse(source.contains("NavDisplay.predictivePopTransitionSpec"))
    }
}

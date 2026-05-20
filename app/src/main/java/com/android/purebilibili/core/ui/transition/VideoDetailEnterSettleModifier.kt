package com.android.purebilibili.core.ui.transition

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun Modifier.videoDetailEnterSettle(
    settleSpec: VideoDetailEnterSettleSpec,
    settleDelayMillis: Int
): Modifier {
    val density = LocalDensity.current
    val settleScale = remember { Animatable(1f) }
    val settleTranslationY = remember { Animatable(0f) }

    LaunchedEffect(settleSpec, settleDelayMillis) {
        if (!settleSpec.enabled) {
            settleScale.snapTo(1f)
            settleTranslationY.snapTo(0f)
            return@LaunchedEffect
        }

        delay(settleDelayMillis.coerceAtLeast(0).toLong())
        settleScale.snapTo(settleSpec.startScale)
        settleTranslationY.snapTo(settleSpec.startTranslationYDp)
        val settleSpring = spring<Float>(
            dampingRatio = settleSpec.dampingRatio,
            stiffness = settleSpec.stiffness
        )
        launch {
            settleScale.animateTo(
                targetValue = 1f,
                animationSpec = settleSpring
            )
        }
        launch {
            settleTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = settleSpring
            )
        }
    }

    return graphicsLayer {
        scaleX = settleScale.value
        scaleY = settleScale.value
        translationY = settleTranslationY.value * density.density
    }
}

package com.android.purebilibili.feature.list

import com.android.purebilibili.core.store.CommonListHeaderCollapseMode
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonListHeaderCollapsePolicyTest {

    @Test
    fun `collapse mode falls back to reverse scroll behavior`() {
        assertEquals(
            CommonListHeaderCollapseMode.SHOW_ON_REVERSE_SCROLL,
            CommonListHeaderCollapseMode.fromValue(999)
        )
    }

    @Test
    fun `always visible mode ignores scroll deltas`() {
        assertEquals(
            0f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = -80f,
                scrollDeltaYPx = -40f,
                maxCollapsePx = 160f,
                isAtTop = false,
                mode = CommonListHeaderCollapseMode.ALWAYS_VISIBLE
            )
        )
    }

    @Test
    fun `reverse scroll mode collapses and restores within bounds`() {
        assertEquals(
            -32f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = 0f,
                scrollDeltaYPx = -32f,
                maxCollapsePx = 160f,
                isAtTop = true,
                mode = CommonListHeaderCollapseMode.SHOW_ON_REVERSE_SCROLL
            )
        )
        assertEquals(
            -100f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = -60f,
                scrollDeltaYPx = -40f,
                maxCollapsePx = 160f,
                isAtTop = false,
                mode = CommonListHeaderCollapseMode.SHOW_ON_REVERSE_SCROLL
            )
        )
        assertEquals(
            -20f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = -60f,
                scrollDeltaYPx = 40f,
                maxCollapsePx = 160f,
                isAtTop = false,
                mode = CommonListHeaderCollapseMode.SHOW_ON_REVERSE_SCROLL
            )
        )
    }

    @Test
    fun `top only mode stays collapsed until list reaches top`() {
        assertEquals(
            -160f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = -160f,
                scrollDeltaYPx = 48f,
                maxCollapsePx = 160f,
                isAtTop = false,
                mode = CommonListHeaderCollapseMode.SHOW_AT_TOP_ONLY
            )
        )
        assertEquals(
            0f,
            resolveCommonListHeaderOffsetPx(
                currentOffsetPx = -160f,
                scrollDeltaYPx = 0f,
                maxCollapsePx = 160f,
                isAtTop = true,
                mode = CommonListHeaderCollapseMode.SHOW_AT_TOP_ONLY
            )
        )
    }
}

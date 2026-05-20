package com.android.purebilibili.feature.video.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class VideoDetailScreenMethodSizeStructureTest {

    @Test
    fun videoDetailScreenDelegatesLargeDialogsAndMenusToChildComposables() {
        val source = loadSource("app/src/main/java/com/android/purebilibili/feature/video/screen/VideoDetailScreen.kt")
        val videoDetailBody = source
            .substringAfter("fun VideoDetailScreen(")
            .substringBefore("@OptIn(ExperimentalHazeMaterialsApi::class)")

        assertTrue(videoDetailBody.contains("VideoDetailFollowGroupDialog(viewModel = viewModel)"))
        assertTrue(videoDetailBody.contains("VideoDetailPlaybackEndedDialog("))
        assertTrue(videoDetailBody.contains("VideoDetailQualitySwitchFailureDialog("))
        assertTrue(videoDetailBody.contains("VideoDetailDanmakuContextMenu("))
        assertTrue(source.contains("private fun VideoDetailFollowGroupDialog("))
        assertTrue(source.contains("private fun VideoDetailPlaybackEndedDialog("))
        assertTrue(source.contains("private fun VideoDetailQualitySwitchFailureDialog("))
        assertTrue(source.contains("private fun VideoDetailDanmakuContextMenu("))
    }

    private fun loadSource(path: String): String {
        val candidates = listOf(
            File(path),
            File("app", path.removePrefix("app/")),
            File(path.removePrefix("app/")),
            File("..", path)
        )
        return candidates.first { it.exists() }.readText()
    }
}

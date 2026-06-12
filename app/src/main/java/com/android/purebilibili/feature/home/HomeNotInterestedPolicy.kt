package com.android.purebilibili.feature.home

import com.android.purebilibili.data.model.response.VideoItem
import com.android.purebilibili.data.model.response.RecommendationFeedbackLocalAction
import com.android.purebilibili.data.model.response.RecommendationFeedbackReason
import com.android.purebilibili.data.model.response.RecommendationFeedbackType

internal data class HomeNotInterestedAction(
    val bvid: String,
    val shouldBlockCreator: Boolean,
    val shouldSyncCreatorToBilibiliBlockedList: Boolean,
    val creatorMid: Long,
    val creatorName: String,
    val creatorFace: String,
    val keywords: Set<String>
)

internal data class HomeNotInterestedVisualTransition(
    val shouldStartDissolve: Boolean,
    val shouldRemoveImmediately: Boolean
)

internal fun resolveHomeNotInterestedVisualTransition(
    isFeedbackRecorded: Boolean,
    isDissolveAnimationAvailable: Boolean
): HomeNotInterestedVisualTransition {
    return HomeNotInterestedVisualTransition(
        shouldStartDissolve = isFeedbackRecorded && isDissolveAnimationAvailable,
        shouldRemoveImmediately = isFeedbackRecorded && !isDissolveAnimationAvailable
    )
}

internal fun resolveHomeDismissVisualTransition(
    isFeedbackRecorded: Boolean,
    cardAnimationEnabled: Boolean
): HomeNotInterestedVisualTransition {
    return resolveHomeNotInterestedVisualTransition(
        isFeedbackRecorded = isFeedbackRecorded,
        isDissolveAnimationAvailable = cardAnimationEnabled
    )
}

internal fun resolveHomeNotInterestedAction(
    video: VideoItem,
    reason: RecommendationFeedbackReason
): HomeNotInterestedAction {
    val creatorMid = video.owner.mid
    val shouldBlockCreator = reason.localAction == RecommendationFeedbackLocalAction.CREATOR &&
        creatorMid > 0L
    return HomeNotInterestedAction(
        bvid = video.bvid,
        shouldBlockCreator = shouldBlockCreator,
        shouldSyncCreatorToBilibiliBlockedList = shouldBlockCreator,
        creatorMid = creatorMid,
        creatorName = video.owner.name.ifBlank {
            if (creatorMid > 0L) "UP主$creatorMid" else ""
        },
        creatorFace = video.owner.face,
        keywords = resolveHomeNotInterestedKeywords(video, reason)
    )
}

internal fun resolveHomeNotInterestedReasons(video: VideoItem): List<RecommendationFeedbackReason> {
    val remoteReasons = video.recommendationFeedback?.reasons.orEmpty()
        .filter { it.name.isNotBlank() }
        .distinctBy { it.type to it.id }
    if (remoteReasons.isNotEmpty()) return remoteReasons

    return buildList {
        add(
            RecommendationFeedbackReason(
                name = "这个内容",
                localAction = RecommendationFeedbackLocalAction.VIDEO_ONLY
            )
        )
        if (video.owner.mid > 0L) {
            add(
                RecommendationFeedbackReason(
                    name = "UP主：${video.owner.name.ifBlank { video.owner.mid.toString() }}",
                    localAction = RecommendationFeedbackLocalAction.CREATOR
                )
            )
        }
        if (video.tname.isNotBlank()) {
            add(
                RecommendationFeedbackReason(
                    name = "分区：${video.tname}",
                    localAction = RecommendationFeedbackLocalAction.CATEGORY
                )
            )
        }
        add(
            RecommendationFeedbackReason(
                name = "此类内容过多",
                localAction = RecommendationFeedbackLocalAction.SIMILAR_CONTENT
            )
        )
        add(
            RecommendationFeedbackReason(
                name = "推荐过",
                localAction = RecommendationFeedbackLocalAction.VIDEO_ONLY
            )
        )
    }
}

private fun resolveHomeNotInterestedKeywords(
    video: VideoItem,
    reason: RecommendationFeedbackReason
): Set<String> {
    return when (reason.localAction) {
        RecommendationFeedbackLocalAction.CATEGORY -> {
            val reasonKeyword = reason.name
                .substringAfter(':')
                .substringAfter('：')
                .trim()
            setOf(reasonKeyword, video.tname.trim())
                .filter { it.isNotBlank() }
                .toSet()
        }
        RecommendationFeedbackLocalAction.SIMILAR_CONTENT -> extractHomeFeedbackKeywords(video.title)
        RecommendationFeedbackLocalAction.VIDEO_ONLY,
        RecommendationFeedbackLocalAction.CREATOR -> emptySet()
    }
}

internal fun extractHomeFeedbackKeywords(title: String): Set<String> {
    if (title.isBlank()) return emptySet()
    val normalized = title.lowercase()
    val stopWords = setOf("视频", "合集", "最新", "一个", "我们", "你们", "今天", "真的", "这个")
    val zhTokens = Regex("[\\u4e00-\\u9fa5]{2,6}")
        .findAll(normalized)
        .map { it.value }
        .filter { it !in stopWords }
        .take(6)
        .toList()
    val enTokens = Regex("[a-z0-9]{3,}")
        .findAll(normalized)
        .map { it.value }
        .take(4)
        .toList()
    return (zhTokens + enTokens).toSet()
}

internal fun filterHomeVideosByNotInterestedFeedback(
    videos: List<VideoItem>,
    dislikedBvids: Set<String> = emptySet(),
    dislikedCreatorMids: Set<Long> = emptySet(),
    dislikedKeywords: Set<String> = emptySet()
): List<VideoItem> {
    if (videos.isEmpty()) return videos
    val normalizedBvids = dislikedBvids
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .toSet()
    val normalizedCreators = dislikedCreatorMids.filter { it > 0L }.toSet()
    val normalizedKeywords = dislikedKeywords
        .map { it.trim().lowercase() }
        .filter { it.length >= 2 }
        .toSet()

    if (normalizedBvids.isEmpty() && normalizedCreators.isEmpty() && normalizedKeywords.isEmpty()) {
        return videos
    }

    return videos.filter { video ->
        video.bvid !in normalizedBvids &&
            video.owner.mid !in normalizedCreators &&
            !shouldFilterByDislikedKeywords(video.title, normalizedKeywords)
    }
}

private fun shouldFilterByDislikedKeywords(
    title: String,
    dislikedKeywords: Set<String>
): Boolean {
    if (title.isBlank() || dislikedKeywords.isEmpty()) return false
    val normalizedTitle = title.lowercase()
    val hitKeywords = dislikedKeywords.filter { keyword ->
        normalizedTitle.contains(keyword)
    }
    if (hitKeywords.isEmpty()) return false

    // 单个短词容易误伤，例如“日常”；长短语或多个命中才认为是同类型内容。
    return hitKeywords.any { it.length >= 4 } || hitKeywords.size >= 2
}

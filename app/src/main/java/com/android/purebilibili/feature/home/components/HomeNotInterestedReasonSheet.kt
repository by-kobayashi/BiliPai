package com.android.purebilibili.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.purebilibili.core.ui.IOSModalBottomSheet
import com.android.purebilibili.data.model.response.RecommendationFeedbackReason
import com.android.purebilibili.data.model.response.RecommendationFeedbackType
import com.android.purebilibili.data.model.response.VideoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNotInterestedReasonSheet(
    video: VideoItem,
    reasons: List<RecommendationFeedbackReason>,
    onReasonSelected: (RecommendationFeedbackReason) -> Unit,
    onDismissRequest: () -> Unit
) {
    IOSModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = "选择不感兴趣的原因",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Text(
                text = video.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            ReasonGroup(
                title = "减少推荐",
                reasons = reasons.filter { it.type == RecommendationFeedbackType.DISLIKE },
                onReasonSelected = onReasonSelected
            )
            ReasonGroup(
                title = "内容反馈",
                reasons = reasons.filter { it.type == RecommendationFeedbackType.FEEDBACK },
                onReasonSelected = onReasonSelected
            )
        }
    }
}

@Composable
private fun ReasonGroup(
    title: String,
    reasons: List<RecommendationFeedbackReason>,
    onReasonSelected: (RecommendationFeedbackReason) -> Unit
) {
    if (reasons.isEmpty()) return
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 4.dp)
    )
    reasons.forEachIndexed { index, reason ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clickable { onReasonSelected(reason) }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reason.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = "选择",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (index != reasons.lastIndex) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
            )
        }
    }
}

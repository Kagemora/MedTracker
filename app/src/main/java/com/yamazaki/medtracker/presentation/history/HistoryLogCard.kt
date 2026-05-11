package com.yamazaki.medtracker.presentation.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yamazaki.medtracker.domain.model.LogStatus
import com.yamazaki.medtracker.domain.model.MedicineLog
import com.yamazaki.medtracker.util.DateUtils

@Composable
fun HistoryLogCard(
    log: MedicineLog,
    is24Hour: Boolean = true
) {
    val containerColor = when (log.status) {
        LogStatus.TAKEN -> MaterialTheme.colorScheme.primaryContainer
        LogStatus.SKIPPED -> MaterialTheme.colorScheme.errorContainer
        LogStatus.PENDING -> MaterialTheme.colorScheme.surfaceVariant
    }
    val icon = when (log.status) {
        LogStatus.TAKEN -> Icons.Rounded.Check
        LogStatus.SKIPPED -> Icons.Rounded.Close
        LogStatus.PENDING -> Icons.Rounded.Schedule
    }
    val iconTint = when (log.status) {
        LogStatus.TAKEN -> MaterialTheme.colorScheme.primary
        LogStatus.SKIPPED -> MaterialTheme.colorScheme.error
        LogStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.medicineName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${log.dosage} ${log.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = DateUtils.formatTime(log.scheduledAt, is24Hour),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (log.status == LogStatus.TAKEN && log.takenAt != null) {
                    Text(
                        text = "✓ ${DateUtils.formatTime(log.takenAt, is24Hour)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
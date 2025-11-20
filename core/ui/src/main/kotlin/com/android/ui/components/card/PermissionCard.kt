package com.android.ui.components.card

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.ui.theme.AppTypography
import com.android.ui.theme.AppColors

@Composable
fun PermissionCard(
    modifier: Modifier = Modifier,
    message: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.black.copy(alpha = 0.65f),
            contentColor = AppColors.white
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = message,
            style = AppTypography.bodyMedium,
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center
        )
    }
}

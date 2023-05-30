package com.number869.telemone.ui.screens.about.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutCard(label: String, content: @Composable () -> Unit = {}) {
	OutlinedCard(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(16.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = label,
				style = MaterialTheme.typography.titleSmall,
				fontSize = 12.sp,
				color = MaterialTheme.colorScheme.onPrimaryContainer
			)

			Spacer(modifier = Modifier.height(8.dp))

			content()
		}
	}
}
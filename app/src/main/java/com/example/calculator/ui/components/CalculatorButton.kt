package com.example.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.R

enum class ButtonType { NUMBER, OPERATOR, TOP }

@Composable
fun CalculatorButton(
    text: String,
    type: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isWide: Boolean = false
) {
    val backgroundColor = when {
        isSelected -> colorResource(R.color.operator_selected)
        type == ButtonType.OPERATOR -> colorResource(R.color.operator_button)
        type == ButtonType.TOP -> colorResource(R.color.top_button)
        else -> colorResource(R.color.number_button)
    }
    val textColor = when {
        isSelected -> colorResource(R.color.operator_button)
        type == ButtonType.TOP -> colorResource(R.color.top_text)
        else -> colorResource(R.color.number_text)
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .padding(6.dp)
            .aspectRatio(if (isWide) 2.1f else 1f)
            .background(backgroundColor, if (isWide) RoundedCornerShape(50.dp) else CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = if (isWide) RoundedCornerShape(50.dp) else CircleShape
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = if (type == ButtonType.OPERATOR) 36.sp else 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
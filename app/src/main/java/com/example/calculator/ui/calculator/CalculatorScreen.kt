package com.example.calculator.ui.calculator

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.R
import com.example.calculator.data.CalculatorModel.CalcError
import com.example.calculator.data.CalculatorModel.Operator
import com.example.calculator.ui.components.ButtonType
import com.example.calculator.ui.components.CalculatorButton
import kotlinx.coroutines.delay

@Composable
fun CalculatorScreen(
    onNavigateToHistory: () -> Unit
) {
    // Misma instancia a nivel Activity: el historial se comparte con HistoryScreen
    val viewModel: CalculatorViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val state by viewModel.uiState.collectAsState()

    // Error semántico → string (traducción SOLO en la capa UI)
    val errorMessage = when (state.error) {
        CalcError.DivisionByZero -> stringResource(R.string.error_division_by_zero)
        CalcError.InvalidInput -> stringResource(R.string.error_invalid_input)
        null -> null
    }

    // Mostrar el error 1.5 s y luego resetear la entrada (sin cerrar la app)
    LaunchedEffect(state.error) {
        if (state.error != null) {
            delay(1500)
            viewModel.onErrorShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.ios_background))
            .padding(12.dp)
    ) {
        // Botón historial (arriba a la derecha)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onNavigateToHistory) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_recent_history),
                    contentDescription = stringResource(R.string.history_title),
                    tint = colorResource(R.color.display_text)
                )
            }
        }

        // ===== DISPLAY =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = errorMessage ?: state.currentInput,
                color = if (errorMessage != null)
                    colorResource(R.color.operator_button)
                else colorResource(R.color.display_text),
                fontSize = 80.sp,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ===== FILAS DE BOTONES =====
        CalculatorRow {
            CalculatorButton(stringResource(R.string.btn_clear), ButtonType.TOP, viewModel::onClearPressed, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_sign), ButtonType.TOP, viewModel::onSignToggled, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_percent), ButtonType.TOP, viewModel::onPercentPressed, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_divide), ButtonType.OPERATOR,
                { viewModel.onOperatorSelected(Operator.DIVIDE) }, Modifier.weight(1f),
                isSelected = state.pendingOperator == Operator.DIVIDE)
        }
        CalculatorRow {
            CalculatorButton(stringResource(R.string.digit_7), ButtonType.NUMBER, { viewModel.onDigitPressed("7") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_8), ButtonType.NUMBER, { viewModel.onDigitPressed("8") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_9), ButtonType.NUMBER, { viewModel.onDigitPressed("9") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_multiply), ButtonType.OPERATOR,
                { viewModel.onOperatorSelected(Operator.MULTIPLY) }, Modifier.weight(1f),
                isSelected = state.pendingOperator == Operator.MULTIPLY)
        }
        CalculatorRow {
            CalculatorButton(stringResource(R.string.digit_4), ButtonType.NUMBER, { viewModel.onDigitPressed("4") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_5), ButtonType.NUMBER, { viewModel.onDigitPressed("5") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_6), ButtonType.NUMBER, { viewModel.onDigitPressed("6") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_subtract), ButtonType.OPERATOR,
                { viewModel.onOperatorSelected(Operator.SUBTRACT) }, Modifier.weight(1f),
                isSelected = state.pendingOperator == Operator.SUBTRACT)
        }
        CalculatorRow {
            CalculatorButton(stringResource(R.string.digit_1), ButtonType.NUMBER, { viewModel.onDigitPressed("1") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_2), ButtonType.NUMBER, { viewModel.onDigitPressed("2") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.digit_3), ButtonType.NUMBER, { viewModel.onDigitPressed("3") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_add), ButtonType.OPERATOR,
                { viewModel.onOperatorSelected(Operator.ADD) }, Modifier.weight(1f),
                isSelected = state.pendingOperator == Operator.ADD)
        }
        CalculatorRow {
            CalculatorButton(stringResource(R.string.digit_0), ButtonType.NUMBER,
                { viewModel.onDigitPressed("0") }, Modifier.weight(2f), isWide = true)
            CalculatorButton(stringResource(R.string.btn_dot), ButtonType.NUMBER,
                { viewModel.onDigitPressed(".") }, Modifier.weight(1f))
            CalculatorButton(stringResource(R.string.btn_equals), ButtonType.OPERATOR,
                viewModel::onEqualsPressed, Modifier.weight(1f))
        }
    }
}

/** Fila reutilizable: evita repetir el Row 5 veces (código limpio) */
@Composable
private fun CalculatorRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) { content() }
}
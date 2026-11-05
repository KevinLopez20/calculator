package com.example.calculator.ui.history


import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.R
import com.example.calculator.ui.calculator.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit
) {
    // MISMA instancia que la calculadora → historial sincronizado
    val viewModel: CalculatorViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val entries by viewModel.history.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_revert),
                            contentDescription = stringResource(R.string.history_back),
                            tint = colorResource(R.color.display_text)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = viewModel::clearHistory) {
                        Text(
                            text = stringResource(R.string.btn_clear),
                            color = colorResource(R.color.operator_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.ios_background),
                    titleContentColor = colorResource(R.color.display_text)
                )
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            // Estado vacío exigido por el curso
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.ios_background))
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.history_empty),
                    color = colorResource(R.color.top_button),
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.ios_background))
                    .padding(padding)
            ) {
                items(entries.reversed()) { entry ->
                    Text(
                        text = entry.toDisplayText(),
                        color = colorResource(R.color.display_text),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    )
                    HorizontalDivider(color = colorResource(R.color.number_button))
                }
            }
        }
    }
}
package com.alab.deferredrequests_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Представляет экран с отложенными запросами.
 */
class DeferredRequestsActivity : ComponentActivity() {

    private val deferredRequestsService = DeferredRequestApplication.application.deferredRequestsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeferredRequestsScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        //Останавливаем сервис, пока мы на экране управления запросами, они не должны отправляться автоматически
        deferredRequestsService.stopService()
    }

    override fun onStop() {
        super.onStop()
        //Запускаем сервис, когда уходим с экрана управления запросами, сервис должен снова заработать в автоматическом режиме
        deferredRequestsService.startService()
    }
}

@Composable
private fun DeferredRequestsScreen() {
    Scaffold(content = {
        DeferredRequestsContent()
    })
}

@Composable
private fun DeferredRequestsContent(viewModel: DeferredRequestsViewModel = viewModel()) {
    val commandState = viewModel.executeState.collectAsState()
    val requests = viewModel.requests.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)) {
            if (commandState.value == ExecuteStates.SUCCESS) {
                AssuranceCommandsSuccess()
            } else {
                if (commandState.value == ExecuteStates.INPROGRESS) {
                    AssuranceCommandsProgress()
                } else {
                    if (requests.value.isEmpty()) {
                        AssuranceCommandsAbsent()
                    } else {
                        AssuranceCommandsList(requests.value)
                    }
                }
            }
        }
        AssuranceCommandsButtons()
    }
}

@Composable
private fun AssuranceCommandsList(requests: List<IDeferredRequestModel>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        content = {
            items(requests) { request ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = request.title, color = Color(0xFF333333), fontSize = 16.sp)
                    Text(text = request.date!!, color = Color(0xFF828282), fontSize = 12.sp)
                }
                Divider()
            }
        })
}

@Composable
private fun AssuranceCommandsAbsent() {
    Box (Modifier.fillMaxSize()) {
        Text(
            color = Color(0xFFBDBDBD),
            text = "Нет отложенных запросов",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun AssuranceCommandsButtons(viewModel: DeferredRequestsViewModel = viewModel()) {
    val requests = viewModel.requests.collectAsState()
    val isShowAlertDialog = viewModel.isShowAlertDialog.collectAsState()
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { viewModel.retryHandleRequests() },
            shape = RoundedCornerShape(8.dp),
            enabled = requests.value.isNotEmpty(),
            modifier = Modifier.weight(1f).padding(12.dp)
            ) {
            Text(text = "Отправить")
        }
        Button(
            onClick = { viewModel.isShowAlertDialog.value = true },
            shape = RoundedCornerShape(8.dp),
            enabled = requests.value.isNotEmpty(),
            modifier = Modifier.weight(1f).padding(12.dp)
        ) {
            Text(text = "Очистить список")
        }
    }

    if (isShowAlertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.isShowAlertDialog.value = false
            },
            title = { Text(text = "Внимание!") },
            text = {
                AlertDialogContent()
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor =
                        colorResource(id = R.color.purple_700)
                    ),
                    onClick = {
                        viewModel.isShowAlertDialog.value = false
                        viewModel.removeAllRequests()
                    }
                ) {
                    Text(
                        text = "Очистить",
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Composable
private fun AlertDialogContent() {
    Box(modifier = Modifier.padding()) {
        Text(text = "Очистить список команд?\nВсе не отправленные команды будут потеряны.")
    }
}

@Composable
private fun AssuranceCommandsProgress() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            CircularProgressIndicator(
                color = Color(0xFF29597B),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                color = Color(0xFF29597B),
                text = "Отправка запросов"
            )
        }
    }
}

@Composable
private fun AssuranceCommandsSuccess() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(R.drawable.ic_success),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                contentDescription = null
            )
            Text(
                color = Color(0xFF0F9D58),
                text = "Запросы успешно отправлены"
            )
        }
    }
}
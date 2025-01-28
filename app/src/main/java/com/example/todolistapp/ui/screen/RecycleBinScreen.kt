package com.example.todolistapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.R
import com.example.todolistapp.ui.data.Recycle
import kotlinx.coroutines.launch

@Composable
fun RecycleBinScreen(
    viewModel: RecycleBinViewModel,
    onNavigateToCurrentList: () -> Unit,
    onRestoreTask: (Int) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent),
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    onNavigateToCurrentList = onNavigateToCurrentList,
                    onNavigateToRecycleBin = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    drawerState = drawerState
                )
            }
        ) {
            RecycleList(
                viewModel = viewModel,
                drawerState = drawerState
            )
        }
    }
}

@Composable
fun RecycleList(
    viewModel: RecycleBinViewModel,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {

    val binList = viewModel.binList.observeAsState(emptyList())
    var deleteAllDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    onDeleteAllClick = {
                        deleteAllDialog = true
                    },
                    title = stringResource(R.string.recycle_bin),
                    subtitle = stringResource(R.string.sub_title, "bin"),
                    drawerState = drawerState
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                DarkBackground()

                if (binList.value.isEmpty()) {
                    // Display message when there are no items
                    Text(
                        text = stringResource(R.string.clean_bin),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    // Display the list of items
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(binList.value, key = { it.id }) { item ->
                            BinListCard(
                                listItem = item,
                                onCheckedChange = { viewModel.toggleChecked(item) },
                                onDelete = { viewModel.delete(item) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (deleteAllDialog && binList.value.isNotEmpty()) {
        DeleteAllAlertMessage(
            onDismissRequest = { deleteAllDialog = false },
            onConfirmation = {
                viewModel.deleteAll()
                deleteAllDialog = false
            }
        )
    } else {
        deleteAllDialog = false
    }
}

@Composable
fun BinListCard(
    listItem: Recycle,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
    ) {
        // Priority Color Strip
        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
                .background(
                    color = when (listItem.priority) {
                        "HIGH" -> Color(0xFFD32F2F)
                        "MEDIUM" -> Color(0xFFFBC02D)
                        else -> Color(0xFF388E3C)
                    },
                )
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = "Added: ${listItem.timeStamp}",
                color = Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Checkbox(
                    checked = listItem.isChecked,
                    onCheckedChange = {
                        onCheckedChange()
                        onDelete()
                    },
                    modifier = Modifier
                        .padding(2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = listItem.message,
                        textDecoration = if (listItem.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
                    )
                }
            }
//            listItem.deadLine?.let {
//                Text(
//                    text = "Deadline: ${listItem.deadLine}",
//                    color = Gray,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 12.sp,
//                    style = MaterialTheme.typography.bodySmall.copy(
//                        color = MaterialTheme.colorScheme.onSurface.copy(
//                            alpha = 0.6f
//                        )
//                    ),
//                    modifier = Modifier
//                        .padding(start = 16.dp)
//                        .align(Alignment.Start)
//                )
//            }
            HorizontalDivider(thickness = 2.dp)
        }
    }
    if (showDeleteDialog) {
        MyAlertMessage(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {
                onDelete()
                showDeleteDialog = false
            }
        )
    }
}

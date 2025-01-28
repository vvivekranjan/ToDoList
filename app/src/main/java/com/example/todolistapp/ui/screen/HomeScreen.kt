package com.example.todolistapp.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.R
import com.example.todolistapp.ui.data.DataItem
import com.example.todolistapp.ui.data.Priority
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onNavigateToRecycleBin: () -> Unit,
    modifier: Modifier = Modifier
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    onNavigateToCurrentList = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onNavigateToRecycleBin = onNavigateToRecycleBin,
                    drawerState = drawerState
                )
            },
        ) {
            DefaultList(
                viewModel = viewModel,
                drawerState = drawerState
            )
        }
    }

    
}

@Composable
fun DefaultList(
    viewModel: HomeScreenViewModel,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {

    val toDoList = viewModel.workList.observeAsState(emptyList())
    var deleteAllDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    onDeleteAllClick = {
                        deleteAllDialog = true
                    },
                    title = stringResource(R.string.app_name),
                    subtitle = stringResource(R.string.sub_title, "tasks"),
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

                if (toDoList.value.isEmpty()) {
                    // Display message when there are no items
                    Text(
                        text = stringResource(R.string.add_your_schedule),
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
                        items(toDoList.value) { item ->
                            ListCard(
                                listItem = item,
                                onCheckedChange = { viewModel.toggleChecked(item) },
                                onDelete = { viewModel.delete(item) }
                            )
                        }
                    }
                }

                // Add New Item button fixed at the bottom of the screen
                AddNewItem(
                    viewModel = viewModel,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }

    if (deleteAllDialog && toDoList.value.isNotEmpty()) {
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
fun AddNewItem(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    var newItem by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var taskDeadline by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.LOW) }
    var showDatePicker by remember { mutableStateOf(false) }

//    if (showDatePicker) {
//        DatePickerDialog(
//            onDateSelected = { selectedDate ->
//                taskDeadline = selectedDate
//                showDatePicker = false
//            },
//            onDismissRequest = { showDatePicker = false }
//        )
//    }

    // Rotation animation
    val rotation by animateFloatAsState(
        targetValue = if (newItem) -45f else 0f,
        label = "FAB Rotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedVisibility(visible = newItem) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {
                    Text(
                        text = stringResource(R.string.new_item),
                        fontSize = 18.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                // Cycle through priority levels
                                selectedPriority = when (selectedPriority) {
                                    Priority.LOW -> Priority.HIGH
                                    Priority.HIGH -> Priority.MEDIUM
                                    Priority.MEDIUM -> Priority.LOW
                                }
                                // Show Toast for priority change
                                Toast
                                    .makeText(
                                        context,
                                        "Priority set to ${selectedPriority.name}",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                            .background(
                                color = when (selectedPriority) {
                                    Priority.HIGH -> Color(0xFFD32F2F)
                                    Priority.MEDIUM -> Color(0xFFFBC02D)
                                    Priority.LOW -> Color(0xFF388E3C)
                                },
                                shape = CircleShape
                            )
                    )
                },
                trailingIcon = {
//                    IconButton(
//                            onClick = { showDatePicker = true }
//                        ) {
//                            Icon(
//                                Icons.Default.DateRange,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(20.dp)
//                            )
//                        }
                    IconButton(
                        onClick = {
                            viewModel.add(
                                text = text,
                                priority = when (selectedPriority) {
                                    Priority.HIGH -> "HIGH"
                                    Priority.MEDIUM -> "MEDIUM"
                                    Priority.LOW -> "LOW"
                                },
                                deadLine = String.format("MMM dd, yyyy hh:mm:ss aa", taskDeadline)
                            )
                            newItem = !newItem
                            text = ""
                        },
                        enabled = text.isNotEmpty(),
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            contentColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = Gray,
                            disabledContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .padding(4.dp)

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_up),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color(0xFF121212))
            )
        }
        // Floating Action Button with rotation
        FloatingActionButton(
            onClick = {
                newItem = !newItem // Toggle visibility
            },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.rotate(rotation) // Rotate icon
            )
        }
    }
}

@Composable
fun ListCard(
    listItem: DataItem,
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
                    onCheckedChange = { onCheckedChange() },
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
                IconButton(
                    onClick = {
                        if (!listItem.isChecked) {
                            showDeleteDialog = true
                        } else {
                            onDelete()
                        }
                    },
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
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

//@Composable
//fun DatePickerDialog(
//    onDateSelected: (String) -> Unit,
//    onDismissRequest: () -> Unit
//) {
//    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
//
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            val selectedDate = String.format("%02d/%03d/%04d", dayOfMonth, month + 1, year)
//            onDateSelected(selectedDate)
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    )
//
//    LaunchedEffect(Unit) {
//        datePickerDialog.show()
//    }
//
//    DisposableEffect(Unit) {
//        onDispose { datePickerDialog.dismiss() }
//    }
//}

@Preview(showBackground = true)
@Composable
fun ListCardPreview() {
    ListCard(DataItem(1, stringResource(R.string.clean_the_room), priority = "MEDIUM", deadLine = Date.from(
        Instant.now()).toString()), { }, { })
}

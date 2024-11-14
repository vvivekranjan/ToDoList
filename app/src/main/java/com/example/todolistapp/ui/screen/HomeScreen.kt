package com.example.todolistapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.R
import com.example.todolistapp.ui.data.DataItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val toDoList = viewModel.todoList.observeAsState(emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (toDoList.value.isEmpty()) {
                    // Display message when there are no items
                    Text(
                        text = stringResource(R.string.add_your_schedule),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 80.dp) // Space for the button at the bottom
                    )
                } else {
                    // Display the list of items
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp) // Leave space for the button
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
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    )
}

@Composable
fun AddNewItem(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    var newItem by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        if (newItem) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.new_item), fontSize = 24.sp) },
                textStyle = TextStyle(
                    fontSize = 24.sp
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.add(text)
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
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        ElevatedCard(
            onClick = { newItem = !newItem },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp)
                )
                Text(
                    text = "Add New Item",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
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

    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Added: ${listItem.timeStamp}",
            color = Color.Gray,
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
                if (!listItem.isChecked) {
                    Text(
                        text = listItem.message,
                        textDecoration = TextDecoration.None,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
                    )
                } else {
                    Text(
                        text = listItem.message,
                        textDecoration = TextDecoration.LineThrough,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 2.dp, bottom = 2.dp)
                    )
                }
            }
            IconButton(
                onClick = {
                    if(!listItem.isChecked) {
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
        HorizontalDivider(thickness = 2.dp)
    }

    if(showDeleteDialog) {
        MyAlertMessage(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = onDelete
        )
    }

}

@Composable
fun MyAlertMessage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
            )
        },
        title = {
            Text("Confirmation Request")
        },
        text = {
            Text("Are you really want to delete this item without checking it off?")
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListCardPreview() {
    ListCard(DataItem(1, stringResource(R.string.clean_the_room)), { }, { })
}
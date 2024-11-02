package com.example.todolistapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.R
import com.example.todolistapp.ui.data.DataItem
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    val todoItems by viewModel.toDoList.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        },
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
        ) {
            AddNewItem(viewModel)
            ListCardItem(
                listItem = todoItems,
                viewModel = viewModel
            )
        }
    }

}

@Composable
fun AddNewItem(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    var newItem by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val rainbowColors = listOf(Magenta, Yellow, Green, Blue)
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        ElevatedCard(
            onClick = { newItem = !newItem },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp)
                )
                Text(
                    text = "Add New Item",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
        if (newItem) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.new_item), fontSize = 18.sp) },
                textStyle = TextStyle(
                    brush = brush,
                    fontSize = 24.sp
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        newItem = !newItem
                        viewModel.addItems(text)
                        text = ""
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ListCardItem(
    listItem: List<DataItem>,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        if(listItem.isNotEmpty()) {
            LazyColumn() {
                items(items = listItem) { item ->
                    ListCard(
                        listItem = item,
                        onDelete = { viewModel.deleteItems(item.id) }
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.nothing_to_show),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp
            )
        }
    }
}

@Composable
fun ListCard(
    listItem: DataItem,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(
            text = SimpleDateFormat(
                "MMM dd, yyyy hh:mm:ss aa",
                Locale.ENGLISH
            ).format(listItem.timeStamp),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.Start)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = listItem.isChecked.value,
                    onCheckedChange = { listItem.isChecked.value = !listItem.isChecked.value },
                )
                if(!listItem.isChecked.value){
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
                onClick = onDelete,
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListCardPreview() {
    ListCard(DataItem(1, stringResource(R.string.clean_the_room), Date.from(Instant.now())), { })
}
package com.example.todolistapp.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.R
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onDeleteAllClick: () -> Unit,
    title: String,
    subtitle: String,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C)) // Background for the header section
            .shadow(8.dp, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)) // Soft shadow
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    ) {
        // Top App Bar with Icons
        androidx.compose.material3.TopAppBar(
            modifier = Modifier.background(Color.Transparent), // Transparent so the Box background shows
            navigationIcon = {
                // Leading Drawer Icon
                IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = if (drawerState.isOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color(0xFF80DEEA) // Vibrant Cyan for leading icon
                    )
                }
            },
            actions = {
                // Trailing Delete All Icon
                IconButton(onClick = onDeleteAllClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_all),
                        contentDescription = "Delete All",
                        tint = MaterialTheme.colorScheme.primary // Orange tint for contrast
                    )
                }
            },
            title = {
                // Custom Title Section
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF81D4FA), // Light Blue
                                    Color(0xFF80CBC4)  // Light Teal
                                )
                            ),
                            shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                        )
                    )
                    Text(
                        text = subtitle,
                        color = Color(0xFFB0BEC5), // Subtle Light Gray-Blue
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            shadow = Shadow(Color(0xFF000000), Offset(1f, 1f), 2f)
                        )
                    )
                }
            }
        )

        // Bottom Fancy Gradient Line
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(4.dp)
        ) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF7E57C2), // Purple
                        Color(0xFF4CAF50), // Green
                        Color(0xFFFFA726), // Orange
                        Color(0xFF29B6F6)  // Light Blue
                    )
                ),
                size = Size(size.width, size.height)
            )
        }
    }
}

@Composable
fun DarkBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark background
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Large decorative circle
            drawCircle(
                color = Color(0xFF6A82FB).copy(alpha = 0.2f),
                radius = size.minDimension * 0.3f,
                center = Offset(x = size.width * 0.8f, y = size.height * 0.1f)
            )

            // Smaller decorative circle
            drawCircle(
                color = Color(0xFFFC5C7D).copy(alpha = 0.15f),
                radius = size.minDimension * 0.2f,
                center = Offset(x = size.width * 0.2f, y = size.height * 0.85f)
            )

            // Wavy line
            val path = Path().apply {
                moveTo(0f, size.height * 0.5f)
                for (i in 0..size.width.toInt() step 30) {
                    lineTo(i.toFloat(), size.height * 0.5f + 20 * sin(i.toFloat() * 0.05f))
                }
            }
            drawPath(
                path = path,
                color = Color(0xFFB2BFF0).copy(alpha = 0.3f),
                style = Stroke(
                    width = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )
        }
    }
}

@Composable
fun NavigationDrawer(
    onNavigateToCurrentList: () -> Unit,
    onNavigateToRecycleBin: () -> Unit,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    // Using Animatable for synchronized animations
    val animationProgress = remember { Animatable(0f) }

//    var workListState by remember { mutableStateOf(workList) }

    // Trigger animations when drawer state changes
    LaunchedEffect(drawerState.isOpen) {
        animationProgress.animateTo(
            targetValue = if (drawerState.isOpen) 1f else 0f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }

    val drawerGestureModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragEnd = {
                if (animationProgress.value > 0.7f) {
                    DrawerState(DrawerValue.Open)
                } else {
                    DrawerState(DrawerValue.Closed)
                }
            },
            onHorizontalDrag = { _, dragAmount ->
                val newValue = animationProgress.value + (dragAmount / 400f)
                scope.launch {
                    animationProgress.snapTo(newValue.coerceIn(0f, 1f))
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.background)
            .then(drawerGestureModifier) // Enable swipe gestures
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Add New Task List Button
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                TextButton(
                    onClick = onNavigateToCurrentList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Default List",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
//                HorizontalDivider(thickness = 2.dp)
//                TextButton(
//                    onClick = { },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_add),
//                            contentDescription = null
//                        )
//                        Text(
//                            text = "Add New Task List",
//                            style = MaterialTheme.typography.titleLarge,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//                }
            }

            // Recycle Bin at the bottom
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToRecycleBin() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Text(
                        text = "Recycle Bin",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkBackground() {
    DarkBackground()
}
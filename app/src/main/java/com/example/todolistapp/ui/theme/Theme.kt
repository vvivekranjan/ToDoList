package com.example.todolistapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor_Dark,
    secondary = SecondaryColor_Dark,
    background = BackgroundColor_Dark,
    surface = SurfaceColor_Dark,
    onPrimary = OnPrimaryColor_Dark,
    onSecondary = OnSecondaryColor_Dark,
    onBackground = OnBackgroundColor_Dark,
    onSurface = OnSurfaceColor_Dark,
    )
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor_Light,
    secondary = SecondaryColor_Light,
    background = BackgroundColor_Light,
    surface = SurfaceColor_Light,
    onPrimary = OnPrimaryColor_Light,
    onSecondary = OnSecondaryColor_Light,
    onBackground = OnBackgroundColor_Light,
    onSurface = OnSurfaceColor_Light,
    )

@Composable
fun ToDoListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
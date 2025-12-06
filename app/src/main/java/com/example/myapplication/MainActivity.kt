package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables full screen content (API 36 standard)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // Box acts as a container for our custom view
                Box(modifier = Modifier.padding(innerPadding)) {
                    PaintApp()
                }
            }
        }
    }
}

@Composable
fun PaintApp() {
    // AndroidView allows us to use our custom "DrawingView" inside Jetpack Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            DrawingView(context)
        }
    )
}
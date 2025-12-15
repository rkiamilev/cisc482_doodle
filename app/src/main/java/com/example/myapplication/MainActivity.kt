package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    PaintApp()
                }
            }
        }
    }
}

@Composable
fun PaintApp() {
    // 1. State for holding the selected color. Defaults to Black.
    var selectedColor by remember { mutableStateOf(Color.Black) }

    // 2. State to hold a reference to our DrawingView instance. This is crucial.
    var drawingViewInstance: DrawingView? by remember { mutableStateOf(null) }

    // Generate the hex color string to use in both the dialog and the view
    val hexColor = String.format("#%06X", (0xFFFFFF and selectedColor.toArgb()))

    Scaffold(
        floatingActionButton = {
            // FIX: Conditionally display the FloatingActionButton only when the
            // DrawingView instance is available. This prevents crashes and is the
            // correct way to handle enabled/disabled state for this component.
            if (drawingViewInstance != null) {
                FloatingActionButton(
                    onClick = {
                        // The context is guaranteed to be available here.
                        val activity = drawingViewInstance?.context as? Activity
                        activity?.let {
                            ColorPickerDialog
                                .Builder(it) // Use the Activity context here
                                .setTitle("Pick Color")
                                .setColorShape(ColorShape.SQAURE)
                                .setDefaultColor(hexColor)
                                .setColorListener { color, _ ->
                                    selectedColor = Color(color)
                                }
                                .show()
                        }
                    }
                ) {
                    Icon(Icons.Default.Brush, contentDescription = "Select Color")
                }
            }
        }
    ) { innerPadding ->
        // 4. AndroidView now takes the selected color and updates the DrawingView
        AndroidView(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            factory = { ctx ->
                // Create the DrawingView instance and store it in our state
                DrawingView(ctx).also { view ->
                    drawingViewInstance = view
                }
            },
            update = { view ->
                // This block is called whenever `selectedColor` changes.
                view.setColor(hexColor)
            }
        )
    }
}
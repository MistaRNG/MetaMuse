package com.example.metamuse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.metamuse.data.DefaultAppContainer
import com.example.metamuse.ui.theme.MetaMuseTheme
import androidx.compose.material3.Surface
import com.example.metamuse.ui.MetaMuseApp


val container = DefaultAppContainer()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetaMuseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MetaMuseApp()
                }
            }
        }
    }
}



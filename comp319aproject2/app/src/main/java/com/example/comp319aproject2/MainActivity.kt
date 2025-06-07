package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.project2.ui.theme.Project2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoBoardTheme {
                //we will build MainScreen() next
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    //for now, just a full-screen box
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TodoBoard coming soon…")
    }
}


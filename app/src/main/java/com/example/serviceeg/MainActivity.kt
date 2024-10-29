package com.example.serviceeg

import android.os.Bundle
import androidx.activity.compose.setContent
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.serviceeg.ui.theme.ServiceEgTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceEgTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Button(onClick = {
        val serviceIntent = Intent(context, Servico::class.java)
        context.startService(serviceIntent)
    }) {
        Text(text = "Iniciar Serviço")
    }
}
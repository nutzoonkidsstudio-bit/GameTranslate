package com.example.altyazi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameCaptionTheme {
                CameraOverlayScreen()
            }
        }
    }
}

@Composable
fun GameCaptionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(background = Color.Black),
        content = content
    )
}

@Composable
fun CameraOverlayScreen() {
    var isPaused by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. KAMERA ARKA PLANI (İleride CameraX Preview buraya bağlanacak)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        )

        // 2. ÜST BAR (Geri, Durum, Oynat/Durdur)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Button(
                    onClick = { /* Geri İşlemi */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Geri", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .size(8.dp)
                        .background(if (isPaused) Color.Red else Color.Green, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPaused) "Duraklatıldı" else "Çeviri açık • Kaliteli",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(
                onClick = { isPaused = !isPaused },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Close,
                    contentDescription = "Oynat/Durdur",
                    tint = Color.White
                )
            }
        }

        // 3. ÇEVİRİ ALANI KUTUSU (Üst Orta)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .width(400.dp)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ÇEVİRİ ALANI",
                color = Color.LightGray, 
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 4. ALTYAZI HEDEF ÇERÇEVESİ (Kameranın okuyacağı alan)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .fillMaxWidth(0.6f)
                .height(100.dp)
                .border(2.dp, Color(0xFFFFD700).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        )

        // 5. ALT MENÜ (Ayarlar)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Ayarlar Menüsünü Aç */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Ayarlar", tint = Color.Black)
            }
            IconButton(onClick = { /* Hata Bildir */ }) {
                Icon(Icons.Default.Warning, contentDescription = "Sorun", tint = Color.Red)
            }
            IconButton(onClick = { /* Arama */ }) {
                Icon(Icons.Default.Search, contentDescription = "Ara", tint = Color.Black)
            }
        }
    }
}
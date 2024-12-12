package com.example.ubtrace.presentation.util.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ubtrace.R

data class CustomButton(
    val label : String,
    val icon : Painter,
    val pointer : String
)

@Composable
fun Bottombar(navController: NavController) {
    var selectedIcon by remember{ mutableIntStateOf(0) }
    val items = listOf(
        CustomButton("Halaman", painterResource(id = R.drawable.homebar), "home"),
        CustomButton("Peta", painterResource(id = R.drawable.map), "map"),
        CustomButton("Lapor", painterResource(id = R.drawable.lapor), "report"),
        CustomButton("Status", painterResource(id = R.drawable.status), "status")
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, modifier = Modifier.size(24.dp), tint = Color.Unspecified, contentDescription = item.label)},
                label = { Text(item.label) },
                selected = selectedIcon == index,
                onClick = {
                    selectedIcon = index
                    navController.navigate(item.pointer)
                }
            )
        }
    }
}

//@Composable
//@Preview
//fun preview(){
//    Bottombar()
//}
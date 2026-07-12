package com.winmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var containers by remember { mutableStateOf(listOf<Container>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Header
        HeaderSection()

        // Tab Navigation
        TabNavigation(selectedTab) { selectedTab = it }

        // Content based on selected tab
        when (selectedTab) {
            0 -> ContainersTab(containers, onCreateClick = { showCreateDialog = true })
            1 -> SettingsTab()
            2 -> PerformanceTab()
        }
    }

    // Create Container Dialog
    if (showCreateDialog) {
        CreateContainerDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                containers = containers + Container(name = name)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1F3A))
            .padding(24.dp)
    ) {
        Text(
            "WinMobile",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00D9FF)
        )
        Text(
            "Run Windows on Your Mobile Device",
            fontSize = 14.sp,
            color = Color(0xFFB0B0B0),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun TabNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1F3A))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Containers", "Settings", "Performance").forEachIndexed { index, label ->
            TabButton(
                label = label,
                isSelected = selectedTab == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TabButton(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00D9FF) else Color(0xFF2A3050),
            contentColor = if (isSelected) Color(0xFF0F172A) else Color(0xFFB0B0B0)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ContainersTab(containers: List<Container>, onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onCreateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C3AED)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Create Container", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (containers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No containers yet.\nCreate one to get started!",
                    color = Color(0xFF808080),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(containers) { container ->
                    ContainerCard(container)
                }
            }
        }
    }
}

@Composable
fun ContainerCard(container: Container) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1F3A)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFF00D9FF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    container.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
                Text(
                    "Ready to launch",
                    fontSize = 12.sp,
                    color = Color(0xFF00FF00),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Button(
                onClick = { },
                modifier = Modifier
                    .width(80.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00D9FF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Launch", fontSize = 10.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Settings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00D9FF),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingItem("Resolution", "720p")
        SettingItem("FPS Cap", "60 FPS")
        SettingItem("Graphics Driver", "Turnip (Auto-detected)")
        SettingItem("Performance Preset", "Balanced")
    }
}

@Composable
fun SettingItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, fontSize = 14.sp, color = Color(0xFFFFFFFF), fontWeight = FontWeight.SemiBold)
            Text(value, fontSize = 12.sp, color = Color(0xFF808080), modifier = Modifier.padding(top = 4.dp))
        }
        Text(">", fontSize = 16.sp, color = Color(0xFF00D9FF))
    }
}

@Composable
fun PerformanceTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Performance Monitor",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00D9FF),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PerformanceMetric("FPS", "0", Color(0xFF00FF00))
        PerformanceMetric("CPU Usage", "0%", Color(0xFF00D9FF))
        PerformanceMetric("GPU Usage", "0%", Color(0xFF7C3AED))
        PerformanceMetric("Memory", "0 MB", Color(0xFFFFC107))
        PerformanceMetric("Temperature", "0°C", Color(0xFFFF5252))
    }
}

@Composable
fun PerformanceMetric(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1F3A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 14.sp, color = Color(0xFFFFFFFF), fontWeight = FontWeight.SemiBold)
            Text(value, fontSize = 16.sp, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CreateContainerDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var containerName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Container", color = Color(0xFF00D9FF)) },
        text = {
            TextField(
                value = containerName,
                onValueChange = { containerName = it },
                placeholder = { Text("Container name", color = Color(0xFF808080)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1A1F3A),
                    unfocusedContainerColor = Color(0xFF1A1F3A),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(containerName) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D9FF))
            ) {
                Text("Create", color = Color(0xFF0F172A))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3050))
            ) {
                Text("Cancel", color = Color(0xFFB0B0B0))
            }
        },
        containerColor = Color(0xFF1A1F3A)
    )
}

data class Container(
    val name: String,
    val status: String = "Ready",
    val size: String = "0 MB"
)

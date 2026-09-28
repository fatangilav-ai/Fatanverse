package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ModeStandby
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.FatanViewModel
import com.example.ui.theme.CyanCore
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.MalignantRed
import com.example.ui.theme.RoseCrimson
import com.example.ui.theme.SlateDark700
import com.example.ui.theme.SlateDark800
import com.example.ui.theme.SlateDark900
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SpaceDark950
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractiveTerminalScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val reactorState by viewModel.reactorState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark950)
    ) {
        // Mode Tabs: Reaktor Simulator vs Ilustrasi Adegan Interaktif
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SlateDark900,
            contentColor = CyanCore,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = CyanCore
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("HUD Reaktor Armor", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Adegan Interaktif", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            ReactorCoreTerminalTab(
                state = reactorState,
                onToggleMode = { viewModel.toggleReactorMode() },
                onVentHeat = { viewModel.ventHeatThroughThrusters() },
                onTriggerBlade = { viewModel.triggerWhiteEnergyBlade() },
                onReset = { viewModel.stabilizeReactor() }
            )
        } else {
            StoryScenesVisualizerTab()
        }
    }
}

@Composable
private fun ReactorCoreTerminalTab(
    state: com.example.ui.ReactorTerminalState,
    onToggleMode: () -> Unit,
    onVentHeat: () -> Unit,
    onTriggerBlade: () -> Unit,
    onReset: () -> Unit
) {
    val tempColor = when {
        state.temperature >= 130f -> MalignantRed
        state.temperature >= 75f -> GoldenSun
        else -> CyanCore
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Header Status
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, tempColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TERMINAL TAKTIS: WHITE SPARK MK-II",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = tempColor,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = if (state.isConstructorMode)
                                "MODE: POSITIVE CONSTRUCTOR (HARAPAN)"
                            else
                                "MODE: MALIGNANT X-ENTROPY (BAHAYA)",
                            fontSize = 11.sp,
                            color = if (state.isConstructorMode) CyanCore else MalignantRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(tempColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (state.isOverheated) "OVERHEAT!" else "ONLINE",
                            color = tempColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Animated Interactive Reactor Canvas
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reactor_canvas_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedReactorCoreCanvas(
                        temp = state.temperature,
                        coreColor = tempColor,
                        isConstructor = state.isConstructorMode,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gauges Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        GaugeReadout(
                            label = "Suhu Sirkuit",
                            value = "${state.temperature.toInt()}°C",
                            color = tempColor,
                            icon = Icons.Default.Thermostat
                        )
                        GaugeReadout(
                            label = "Daya Output",
                            value = "${state.powerOutputPercent}%",
                            color = CyanCore,
                            icon = Icons.Default.Speed
                        )
                        GaugeReadout(
                            label = "Vapor Chamber",
                            value = "${state.coolingEfficiencyPercent}%",
                            color = GoldenSun,
                            icon = Icons.Default.Waves
                        )
                    }
                }
            }
        }

        // Live Console Log Box
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, tempColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(tempColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI LOG TERMINAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateMuted,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "> ${state.lastSystemMessage}",
                        fontSize = 12.sp,
                        color = tempColor,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Interactive Controls Grid
        item {
            Text(
                text = "Kontrol Resonansi Armor",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimaryDark
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Button 1: Toggle Constructor vs Negative Entropy
                Button(
                    onClick = onToggleMode,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isConstructorMode) CyanCore else MalignantRed
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_toggle_reactor_mode")
                ) {
                    Icon(
                        imageVector = if (state.isConstructorMode) Icons.Default.AutoAwesome else Icons.Default.Warning,
                        contentDescription = "Mode",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.isConstructorMode)
                            "Ganti ke X-Entropy Negatif (Uji Dendam)"
                        else
                            "Ganti ke Constructor Mode (Harapan Lindungi)",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Button 2: Vapor Chamber Cooling Vent (Jet Thrust)
                Button(
                    onClick = onVentHeat,
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark800),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_vent_vapor_chamber")
                ) {
                    Icon(
                        imageVector = Icons.Default.Waves,
                        contentDescription = "Vapor Chamber",
                        tint = CyanCore
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vapor Chamber: Buang Panas ke Pendorong Jet",
                        fontWeight = FontWeight.Bold,
                        color = CyanCore
                    )
                }

                // Button 3: Trigger White Energy Blade
                Button(
                    onClick = onTriggerBlade,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenSun),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_trigger_blade")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Tebas Pedang",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tebaskan Bilah Pedang Energi Putih",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Button 4: Reset & Stabilize
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_reset_reactor")
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset",
                        tint = SlateMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset Kalibrasi Reaktor (Bantuan Kael)", color = TextSecondaryDark)
                }
            }
        }
    }
}

@Composable
private fun AnimatedReactorCoreCanvas(
    temp: Float,
    coreColor: Color,
    isConstructor: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "reactor_pulse")
    val pulseScale by transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (temp > 100f) 350 else 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val rotationDeg by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (temp > 100f) 2000 else 6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2 * 0.9f

        // Outer Tech Ring
        drawCircle(
            color = coreColor.copy(alpha = 0.2f),
            radius = maxRadius,
            center = center,
            style = Stroke(width = 3f)
        )

        // Pulsing Wave Ring
        drawCircle(
            color = coreColor.copy(alpha = 0.35f),
            radius = maxRadius * pulseScale * 0.85f,
            center = center,
            style = Stroke(width = 4f)
        )

        // Rotating Data Bits
        val numBlades = 8
        for (i in 0 until numBlades) {
            val angle = Math.toRadians((rotationDeg + (i * 360f / numBlades)).toDouble())
            val bladeRadius = maxRadius * 0.65f
            val x = center.x + (bladeRadius * cos(angle)).toFloat()
            val y = center.y + (bladeRadius * sin(angle)).toFloat()
            drawCircle(
                color = coreColor,
                radius = if (isConstructor) 4.5f else 7f,
                center = Offset(x, y)
            )
        }

        // Inner Reactor Core Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White,
                    coreColor,
                    coreColor.copy(alpha = 0.4f),
                    Color.Transparent
                ),
                center = center,
                radius = maxRadius * 0.5f * pulseScale
            ),
            radius = maxRadius * 0.5f * pulseScale,
            center = center
        )

        // Central Pure Core
        drawCircle(
            color = Color.White,
            radius = maxRadius * 0.2f,
            center = center
        )
    }
}

@Composable
private fun GaugeReadout(
    label: String,
    value: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondaryDark
        )
    }
}

@Composable
private fun StoryScenesVisualizerTab() {
    var activeScene by remember { mutableIntStateOf(1) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Simulasi Adegan Tempur Kanonikal",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimaryDark
            )
            Text(
                text = "Pilih momen pertarungan kunci untuk menjalankan visualisasi interaktif.",
                fontSize = 12.sp,
                color = TextSecondaryDark
            )
        }

        // Scene Selectors
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SceneSelectorButton(
                    title = "Sektor 4",
                    subtitle = "Iblis 3 Kepala",
                    isSelected = activeScene == 1,
                    onClick = { activeScene = 1 },
                    modifier = Modifier.weight(1f)
                )
                SceneSelectorButton(
                    title = "Sektor 7",
                    subtitle = "Time Conqueror",
                    isSelected = activeScene == 2,
                    onClick = { activeScene = 2 },
                    modifier = Modifier.weight(1f)
                )
                SceneSelectorButton(
                    title = "Bunker DHC",
                    subtitle = "Latihan Skuad",
                    isSelected = activeScene == 3,
                    onClick = { activeScene = 3 },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Scene Details & Interactive Trigger
        item {
            when (activeScene) {
                1 -> SceneSector4Card()
                2 -> SceneSector7Card()
                3 -> SceneDhcTrainingCard()
            }
        }
    }
}

@Composable
private fun SceneSelectorButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SlateDark800 else SlateDark900
        ),
        modifier = modifier
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) CyanCore else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) CyanCore else TextPrimaryDark
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondaryDark
            )
        }
    }
}

@Composable
private fun SceneSector4Card() {
    var laserClashActive by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MalignantRed.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ARC 1: PERTEMPURAN SEKTOR 4",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MalignantRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tebasan Pedang Putih vs Laser Iblis Tiga Kepala",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Iblis berkepala tiga setinggi 10 meter menembakkan laser hitam pekat. Fatan mengabaikan rasa sakit Brain-Overheat (suhu reaktor 150°C) dan melesat lurus membelah tubuh raksasa itu menjadi dua bagian besar!",
                fontSize = 12.sp,
                color = TextSecondaryDark,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Simulation Animation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .border(1.dp, if (laserClashActive) MalignantRed else SlateDark700, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (laserClashActive) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "⚡ DUARRRRRRRRRRRR!! ⚡",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MalignantRed
                        )
                        Text(
                            text = "Bilah Putih Membelah Laser Hitam! Tubuh Iblis Terbelah & Meleleh Jadi Abu.",
                            fontSize = 11.sp,
                            color = CyanCore,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = "Tekan tombol di bawah untuk menyimulasikan tebasan pamungkas Fatan!",
                        fontSize = 12.sp,
                        color = SlateMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { laserClashActive = !laserClashActive },
                colors = ButtonDefaults.buttonColors(containerColor = CyanCore),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Simulasi",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (laserClashActive) "Reset Adegan" else "Luncurkan Tebasan Pedang Putih (150°C)",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun SceneSector7Card() {
    var dataLanceActive by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF6B21A8).copy(alpha = 0.3f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ARC 2: KONFRONTASI TIME CONQUEROR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC084FC)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tombak Data Raksasa vs Pembalikan Waktu Tubuh 1 Detik",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Armor White Spark membusuk oleh sentuhan waktu. Fatan membuang pelat dadanya dan meretas kode real-time menjadi tombak data raksasa untuk menghajar entitas skala dewa Time Conqueror!",
                fontSize = 12.sp,
                color = TextSecondaryDark,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .border(1.dp, if (dataLanceActive) CyanCore else SlateDark700, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (dataLanceActive) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "✨ DATA LANCE STRIKE!! ✨",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = CyanCore
                        )
                        Text(
                            text = "Time Conqueror terdorong 2 langkah mundur sebelum memutar mundur waktu tubuhnya 1 detik.",
                            fontSize = 11.sp,
                            color = GoldenSun,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = "Tekan tombol untuk mengarahkan seluruh sisa daya reaktor ke Tombak Data!",
                        fontSize = 12.sp,
                        color = SlateMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { dataLanceActive = !dataLanceActive },
                colors = ButtonDefaults.buttonColors(containerColor = GoldenSun),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Tombak Data",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (dataLanceActive) "Reset Pertemuan" else "Padatkan Tombak Data Raksasa di Langit",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun SceneDhcTrainingCard() {
    var comboStep by remember { mutableIntStateOf(0) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyanCore.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ARC 3: SIMULASI KOMBINASI TIM DHC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanCore
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Formasi 6 Pilar vs 4 Petinggi DHC",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "The Void menyerap panas sihir Rose -> Rina & Lio ciptakan bilah es raksasa -> Zephyr memutar topan -> Fatan melesat dengan reaktor 85% menyergap garis belakang!",
                fontSize = 12.sp,
                color = TextSecondaryDark,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Step display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .border(1.dp, CyanCore, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                when (comboStep) {
                    0 -> Text(
                        text = "Langkah 1: Tekan untuk mengaktifkan Penyerapan Void!",
                        fontSize = 12.sp,
                        color = SlateMuted
                    )
                    1 -> Text(
                        text = "🌌 THE VOID: 'Area Hampa!' Menyerap panas api Rose, mengalirkan kinetik ke White Spark (Daya 85%)!",
                        fontSize = 11.sp,
                        color = CyanCore,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    2 -> Text(
                        text = "❄️ RINA & LIO: Tebasan gelombang pedang dibekukan menjadi bilah es raksasa, diterbangkan topan Zephyr!",
                        fontSize = 11.sp,
                        color = GoldenSun,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    3 -> Text(
                        text = "⚡ FATAN ZIG-ZAG BLITZ: 'Delay waktu aktivasi adalah titik buta kalian!' Namun Rose mengunci kaki dengan cincin sihir duri!",
                        fontSize = 11.sp,
                        color = RoseCrimson,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { comboStep = (comboStep + 1) % 4 },
                colors = ButtonDefaults.buttonColors(containerColor = CyanCore),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (comboStep) {
                        0 -> "Mulai Rantai Kombinasi (Langkah 1)"
                        1 -> "Lanjut ke Bilah Es & Topan (Langkah 2)"
                        2 -> "Luncurkan Fatan 85% Blitz (Langkah 3)"
                        else -> "Ulangi Latihan Taktis Skuad"
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

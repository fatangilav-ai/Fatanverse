package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CharacterProfile
import com.example.data.model.VsBattleEntry
import com.example.ui.FatanViewModel
import com.example.ui.theme.CyanCore
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.MalignantRed
import com.example.ui.theme.SlateDark700
import com.example.ui.theme.SlateDark800
import com.example.ui.theme.SlateDark900
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SpaceDark950
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun VsBattleScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark950)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SlateDark900,
            contentColor = GoldenSun,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldenSun
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Dokumentasi Wiki", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Simulator Duel", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            VsWikiDocumentationTab(viewModel = viewModel)
        } else {
            VsMatchupSimulatorTab(viewModel = viewModel)
        }
    }
}

@Composable
private fun VsWikiDocumentationTab(viewModel: FatanViewModel) {
    val entries = viewModel.vsEntries
    var selectedEntryId by remember { mutableStateOf(entries.first().characterId) }
    val currentEntry = entries.find { it.characterId == selectedEntryId } ?: entries.first()
    val characterProfile = viewModel.characters.find { it.id == currentEntry.characterId }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "FATANVERSE POWER SCALING WIKI",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldenSun,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Dokumentasi Standar Tiering & Statistik Pertarungan",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryDark
            )
        }

        // Horizontal Character Selector Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entries) { entry ->
                    val isSelected = entry.characterId == selectedEntryId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) GoldenSun else SlateDark900)
                            .border(1.dp, if (isSelected) GoldenSun else SlateDark700, RoundedCornerShape(8.dp))
                            .clickable { selectedEntryId = entry.characterId }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = entry.canonicalName.split("(").first().trim(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextPrimaryDark
                        )
                    }
                }
            }
        }

        // Tier Card Header
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldenSun.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentEntry.canonicalName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldenSun.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "VERIFIED CANON",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldenSun
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "TIER SYSTEM: ${currentEntry.tier}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanCore,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Stats Table
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Parameter Statistik & Kalkulasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoldenSun
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    VsSpecRow("Attack Potency (AP)", currentEntry.attackPotency)
                    VsSpecRow("Speed (Kecepatan)", currentEntry.speed)
                    VsSpecRow("Lifting Strength", currentEntry.liftingStrength)
                    VsSpecRow("Striking Strength", currentEntry.strikingStrength)
                    VsSpecRow("Durability (Ketahanan)", currentEntry.durability)
                    VsSpecRow("Stamina", currentEntry.stamina)
                    VsSpecRow("Jarak Serang (Range)", currentEntry.range)
                    VsSpecRow("Intelligence (IQ Tempur)", currentEntry.intelligence)
                    VsSpecRow("Standard Equipment", currentEntry.standardEquipment)
                }
            }
        }

        // Notable Abilities
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kemampuan & Teknik Khusus",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoldenSun
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    currentEntry.notableAbilities.forEach { ability ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CyanCore)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ability,
                                fontSize = 12.sp,
                                color = TextPrimaryDark
                            )
                        }
                    }
                }
            }
        }

        // Form / Key Stages
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bentuk / Kunci Garis Waktu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    currentEntry.keys.forEach { k ->
                        Text(
                            text = "• $k",
                            fontSize = 12.sp,
                            color = TextSecondaryDark,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VsMatchupSimulatorTab(viewModel: FatanViewModel) {
    val characters = viewModel.characters
    val fighter1 by viewModel.vsFighter1.collectAsStateWithLifecycle()
    val fighter2 by viewModel.vsFighter2.collectAsStateWithLifecycle()
    val result by viewModel.simulationResult.collectAsStateWithLifecycle()

    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "SIMULATOR DUEL & KALKULASI MENANG",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldenSun,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Adu dua karakter dari cerita FATANVERSE secara komparatif!",
                fontSize = 14.sp,
                color = TextSecondaryDark
            )
        }

        // Fighters Selection Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Fighter 1 Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded1,
                        onExpandedChange = { expanded1 = !expanded1 }
                    ) {
                        OutlinedTextField(
                            value = fighter1.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Petarung 1", color = CyanCore, fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1) },
                            colors = OutlinedTextFieldDefaults(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = { expanded1 = false },
                            modifier = Modifier.background(SlateDark900)
                        ) {
                            characters.forEach { char ->
                                DropdownMenuItem(
                                    text = { Text(char.name, color = TextPrimaryDark) },
                                    onClick = {
                                        viewModel.setVsFighter1(char)
                                        expanded1 = false
                                    }
                                )
                            }
                        }
                    }
                }

                // VS Icon in Center
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MalignantRed)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Fighter 2 Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded2,
                        onExpandedChange = { expanded2 = !expanded2 }
                    ) {
                        OutlinedTextField(
                            value = fighter2.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Petarung 2", color = GoldenSun, fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                            colors = OutlinedTextFieldDefaults(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                            modifier = Modifier.background(SlateDark900)
                        ) {
                            characters.forEach { char ->
                                DropdownMenuItem(
                                    text = { Text(char.name, color = TextPrimaryDark) },
                                    onClick = {
                                        viewModel.setVsFighter2(char)
                                        expanded2 = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stats Comparison Bar Chart
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Perbandingan Atribut (Radar)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    VsCompareRow("Attack", fighter1.stats.attack, fighter2.stats.attack, fighter1.name, fighter2.name)
                    VsCompareRow("Speed", fighter1.stats.speed, fighter2.stats.speed, fighter1.name, fighter2.name)
                    VsCompareRow("Durability", fighter1.stats.durability, fighter2.stats.durability, fighter1.name, fighter2.name)
                    VsCompareRow("IQ Tempur", fighter1.stats.intelligence, fighter2.stats.intelligence, fighter1.name, fighter2.name)
                    VsCompareRow("Energy", fighter1.stats.energy, fighter2.stats.energy, fighter1.name, fighter2.name)
                }
            }
        }

        // Calculate Battle Button
        item {
            Button(
                onClick = { viewModel.runVsSimulation() },
                colors = ButtonDefaults.buttonColors(containerColor = GoldenSun),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_run_vs_simulation")
            ) {
                Icon(
                    imageVector = Icons.Default.SportsKabaddi,
                    contentDescription = "Simulasi",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kalkulasi Hasil Pertarungan",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Simulation Outcome Card
        if (result != null) {
            val res = result!!
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateDark900),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldenSun.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "HASIL KALKULASI DUEL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldenSun,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Peluang: ${res.winProbability}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanCore
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Pemenang: ${res.winnerName}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Win bar meter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${fighter1.name} (${res.fighter1Score}%)",
                                fontSize = 11.sp,
                                color = CyanCore
                            )
                            Text(
                                "${fighter2.name} (${res.fighter2Score}%)",
                                fontSize = 11.sp,
                                color = GoldenSun
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LinearProgressIndicator(
                            progress = { (res.fighter1Score / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = CyanCore,
                            trackColor = GoldenSun
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SlateDark800)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Kunci Kemenangan: ${res.keyAdvantage}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimaryDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Ulasan Taktis Kronologis:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        res.tacticalBreakdown.forEach { point ->
                            Text(
                                text = "• $point",
                                fontSize = 12.sp,
                                color = TextSecondaryDark,
                                modifier = Modifier.padding(vertical = 2.dp),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VsSpecRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 11.sp, color = CyanCore, fontWeight = FontWeight.SemiBold)
        Text(text = value, fontSize = 12.sp, color = TextPrimaryDark)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SlateDark800)
        )
    }
}

@Composable
private fun VsCompareRow(
    label: String,
    val1: Int,
    val2: Int,
    name1: String,
    name2: String
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$name1: $val1", fontSize = 11.sp, color = CyanCore)
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
            Text("$name2: $val2", fontSize = 11.sp, color = GoldenSun)
        }
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LinearProgressIndicator(
                progress = { (val1 / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CyanCore,
                trackColor = SlateDark800
            )
            LinearProgressIndicator(
                progress = { (val2 / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GoldenSun,
                trackColor = SlateDark800
            )
        }
    }
}

@Composable
private fun OutlinedTextFieldDefaults(): androidx.compose.material3.TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = SlateDark900,
        unfocusedContainerColor = SlateDark900,
        focusedTextColor = TextPrimaryDark,
        unfocusedTextColor = TextPrimaryDark,
        focusedIndicatorColor = CyanCore,
        unfocusedIndicatorColor = SlateDark700
    )
}

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CharacterProfile
import com.example.data.model.CharacterStats
import com.example.ui.AppScreen
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

@Composable
fun CharacterListScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    val characters = viewModel.characters
    var selectedFilter by remember { mutableStateOf("Semua") }

    val filteredCharacters = when (selectedFilter) {
        "Skuad Fatan" -> characters.filter { it.affiliation.contains("Skuad") || it.id == "fatan" }
        "Petinggi DHC" -> characters.filter { it.affiliation.contains("Eksekutif") || it.affiliation.contains("Garis Depan") || it.id == "kael" || it.id == "rose" }
        "Anomali / Musuh" -> characters.filter { it.affiliation.contains("Anomali") || it.affiliation.contains("Faksi Misterius") }
        else -> characters
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark950),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Biodata Karakter FATANVERSE",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryDark
            )
            Text(
                text = "Dokumentasi resmi profil pejuang Demon Hunter Corp dan anomali semesta.",
                fontSize = 12.sp,
                color = TextSecondaryDark
            )
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Semua", "Skuad Fatan", "Petinggi DHC", "Anomali / Musuh").forEach { filter ->
                    item {
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanCore,
                                selectedLabelColor = Color.Black,
                                containerColor = SlateDark900,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }
                }
            }
        }

        items(filteredCharacters) { char ->
            CharacterListItemCard(
                character = char,
                onClick = { viewModel.selectCharacter(char) }
            )
        }
    }
}

@Composable
private fun CharacterListItemCard(
    character: CharacterProfile,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("char_card_${character.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            character.id == "fatan" -> CyanCore.copy(alpha = 0.2f)
                            character.id == "time_conqueror" -> Color(0xFF6B21A8).copy(alpha = 0.3f)
                            character.id == "golden_knight" -> GoldenSun.copy(alpha = 0.2f)
                            character.id == "rose" -> RoseCrimson.copy(alpha = 0.2f)
                            else -> SlateDark700
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = character.name.take(2).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        character.id == "fatan" -> CyanCore
                        character.id == "time_conqueror" -> Color(0xFFC084FC)
                        character.id == "golden_knight" -> GoldenSun
                        character.id == "rose" -> RoseCrimson
                        else -> TextPrimaryDark
                    }
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = character.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SlateDark800)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = character.tier.split("|").first().trim(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanCore
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = character.alias,
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = character.powerType,
                    fontSize = 11.sp,
                    color = CyanCore,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CharacterDetailScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val character by viewModel.selectedCharacter.collectAsStateWithLifecycle()

    if (character == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Karakter tidak ditemukan", color = Color.White)
        }
        return
    }

    val c = character!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark950),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Navigation Button
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("char_detail_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = CyanCore
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dossier Karakter: ${c.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }
        }

        // Profile Card Header
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(CyanCore.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = c.name.take(2).uppercase(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanCore
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = c.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = c.alias,
                                fontSize = 12.sp,
                                color = CyanCore,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${c.age} • ${c.role}",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateDark800)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = c.quote,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = GoldenSun
                        )
                    }
                }
            }
        }

        // Stats Matrix Section
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Statistik Kekuatan Tempur (VS Scale)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CyanCore
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    StatRow(label = "Attack (Serangan)", value = c.stats.attack, color = MalignantRed)
                    StatRow(label = "Speed (Kecepatan)", value = c.stats.speed, color = CyanCore)
                    StatRow(label = "Durability (Ketahanan)", value = c.stats.durability, color = GoldenSun)
                    StatRow(label = "Intelligence (Kecerdasan)", value = c.stats.intelligence, color = RoseCrimson)
                    StatRow(label = "Energy (Kapasitas X-Power)", value = c.stats.energy, color = Color(0xFF38BDF8))
                    StatRow(label = "Versatility (Keluwesan Taktik)", value = c.stats.versatility, color = Color(0xFFA855F7))
                }
            }
        }

        // Canonical VS Wiki Stats Table
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Data VS Battle Wiki",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CyanCore
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    WikiDataRow("Tier", c.tier)
                    WikiDataRow("Attack Potency", c.attackPotency)
                    WikiDataRow("Speed", c.speed)
                    WikiDataRow("Durability", c.durability)
                    WikiDataRow("Signature Move", c.signatureMove)
                    WikiDataRow("Senjata / Alat", c.weaponEquipment)
                    WikiDataRow("Tipe Kekuatan", c.powerType)
                }
            }
        }

        // Strengths & Weaknesses
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kelebihan & Kelemahan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    c.strengths.forEach { s ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = CyanCore, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = s, fontSize = 12.sp, color = TextPrimaryDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    c.weaknesses.forEach { w ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = MalignantRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = w, fontSize = 12.sp, color = TextSecondaryDark)
                        }
                    }
                }
            }
        }

        // Background Lore
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SlateDark900),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Biografi & Latar Belakang",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = c.description,
                        fontSize = 13.sp,
                        color = TextSecondaryDark,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Action button to test in simulator
        item {
            Button(
                onClick = {
                    viewModel.setVsFighter1(c)
                    viewModel.navigateTo(AppScreen.VS_BATTLE_WIKI)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldenSun),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_test_in_vs_sim")
            ) {
                Icon(
                    imageVector = Icons.Default.SportsKabaddi,
                    contentDescription = "Simulasi",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Adu ${c.name} di VS Battle Simulator",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, color = TextSecondaryDark)
            Text("$value / 100", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { (value / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = SlateDark800
        )
    }
}

@Composable
private fun WikiDataRow(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, fontSize = 11.sp, color = CyanCore, fontWeight = FontWeight.SemiBold)
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

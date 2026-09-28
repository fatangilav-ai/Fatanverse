package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.ArcChapter
import com.example.ui.AppScreen
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
fun HomeScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    val progressList by viewModel.readingProgressList.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarksList.collectAsStateWithLifecycle()
    val chapters = viewModel.allChapters

    val lastReadProgress = progressList.maxByOrNull { it.lastReadTimestamp }
    val lastReadChapter = chapters.find { it.id == lastReadProgress?.chapterId } ?: chapters.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark950),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Banner
        item {
            HeroHeaderCard(
                onReadLatestClick = { viewModel.openChapter(lastReadChapter.id) },
                onTerminalClick = { viewModel.navigateTo(AppScreen.INTERACTIVE_TERMINAL) }
            )
        }

        // Quick Feature Shortcut Bar
        item {
            QuickShortcutsRow(
                onVsWikiClick = { viewModel.navigateTo(AppScreen.VS_BATTLE_WIKI) },
                onTerminalClick = { viewModel.navigateTo(AppScreen.INTERACTIVE_TERMINAL) },
                onNotificationClick = { viewModel.navigateTo(AppScreen.NOTIFICATIONS_SETTINGS) }
            )
        }

        // Continue Reading Card (Offline Persisted)
        item {
            ContinueReadingCard(
                chapter = lastReadChapter,
                percent = lastReadProgress?.progressPercent ?: 0f,
                onContinueClick = { viewModel.openChapter(lastReadChapter.id) }
            )
        }

        // Section Title: Bab Cerita FATANVERSE
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(CyanCore)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daftar Arc & Bab Cerita",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimaryDark
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = "Offline Ready",
                        tint = CyanCore,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "100% Offline",
                        fontSize = 12.sp,
                        color = CyanCore
                    )
                }
            }
        }

        // Arc Chapter Items
        items(chapters) { chapter ->
            val prog = progressList.find { it.chapterId == chapter.id }
            ArcChapterCard(
                chapter = chapter,
                progressPercent = prog?.progressPercent ?: 0f,
                isCompleted = prog?.isCompleted ?: false,
                onClick = { viewModel.openChapter(chapter.id) }
            )
        }

        // Bookmarks Quick View if available
        if (bookmarks.isNotEmpty()) {
            item {
                BookmarksPreviewSection(
                    bookmarksCount = bookmarks.size,
                    onOpenLastBookmark = {
                        val firstBm = bookmarks.first()
                        viewModel.openChapter(firstBm.chapterId)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeroHeaderCard(
    onReadLatestClick: () -> Unit,
    onTerminalClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_header_card")
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_fatan_hero),
                contentDescription = "FATANVERSE Hero Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SlateDark900.copy(alpha = 0.85f),
                                SlateDark900
                            )
                        )
                    )
            )

            // Content on Hero
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MalignantRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FATANVERSE OFFICIAL STORY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Perjalanan Sang Insinyur & White Spark",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimaryDark
                )

                Text(
                    text = "Dari stickman 14 tahun pembuat kacamata tunanetra hingga pertempuran kosmik melawan Time Conqueror.",
                    fontSize = 12.sp,
                    color = TextSecondaryDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onReadLatestClick,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanCore),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("hero_read_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Baca",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Baca Sekarang",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    OutlinedButton(
                        onClick = onTerminalClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanCore),
                        modifier = Modifier.testTag("hero_terminal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Engineering,
                            contentDescription = "Terminal",
                            tint = CyanCore,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Terminal Reaktor")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickShortcutsRow(
    onVsWikiClick: () -> Unit,
    onTerminalClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickShortcutButton(
            title = "VS Battle Wiki",
            subtitle = "Tier & Duel",
            icon = Icons.Default.SportsKabaddi,
            color = GoldenSun,
            onClick = onVsWikiClick,
            modifier = Modifier.weight(1f)
        )
        QuickShortcutButton(
            title = "White Spark",
            subtitle = "HUD Reaktor",
            icon = Icons.Default.AutoAwesome,
            color = CyanCore,
            onClick = onTerminalClick,
            modifier = Modifier.weight(1f)
        )
        QuickShortcutButton(
            title = "Update Bab",
            subtitle = "Notifikasi",
            icon = Icons.Default.Bookmark,
            color = MalignantRed,
            onClick = onNotificationClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickShortcutButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = modifier
            .clickable { onClick() }
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondaryDark,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ContinueReadingCard(
    chapter: ArcChapter,
    percent: Float,
    onContinueClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark800),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onContinueClick() }
            .testTag("continue_reading_card")
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CyanCore.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Lanjutkan",
                    tint = CyanCore,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lanjutkan Membaca",
                    fontSize = 11.sp,
                    color = CyanCore,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Arc ${chapter.arcNumber}: ${chapter.title}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { percent.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = CyanCore,
                    trackColor = SlateDark700
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${(percent * 100).toInt()}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanCore
            )
        }
    }
}

@Composable
private fun ArcChapterCard(
    chapter: ArcChapter,
    progressPercent: Float,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                1.dp,
                if (isCompleted) CyanCore.copy(alpha = 0.4f) else Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .testTag("arc_card_${chapter.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = chapter.coverDrawableRes),
                    contentDescription = chapter.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SlateDark900.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Arc Number Badge
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (chapter.arcNumber == 4) MalignantRed else SlateDark800.copy(alpha = 0.9f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (chapter.arcNumber == 4) "TEASER ARC 4" else "ARC ${chapter.arcNumber}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Timeline badge
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SpaceDark950.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = chapter.timeline,
                        fontSize = 11.sp,
                        color = CyanCore
                    )
                }

                // Completion icon
                if (isCompleted) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.BottomEnd),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selesai dibaca",
                            tint = CyanCore,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Selesai", fontSize = 11.sp, color = CyanCore, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = chapter.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = chapter.subtitle,
                    fontSize = 12.sp,
                    color = CyanCore,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = chapter.summary,
                    fontSize = 12.sp,
                    color = TextSecondaryDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Key characters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        chapter.keyCharacters.take(3).forEach { name ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SlateDark800)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = name, fontSize = 10.sp, color = TextSecondaryDark)
                            }
                        }
                    }

                    Text(
                        text = "${chapter.paragraphs.size} Paragraf",
                        fontSize = 11.sp,
                        color = SlateMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksPreviewSection(
    bookmarksCount: Int,
    onOpenLastBookmark: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateDark900),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenLastBookmark() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Bookmarks",
                tint = GoldenSun,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Penanda Bacaan ($bookmarksCount tersimpan)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimaryDark
                )
                Text(
                    text = "Ketuk untuk membuka kutipan terakhir yang kamu tandai",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
            }
        }
    }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.example.data.local.BookmarkEntity
import com.example.data.model.ArcChapter
import com.example.data.model.ParagraphType
import com.example.data.model.StoryParagraph
import com.example.ui.AppScreen
import com.example.ui.FatanViewModel
import com.example.ui.theme.CyanCore
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.MalignantRed
import com.example.ui.theme.PaperSepiaBg
import com.example.ui.theme.PaperSepiaText
import com.example.ui.theme.RoseCrimson
import com.example.ui.theme.SlateDark700
import com.example.ui.theme.SlateDark800
import com.example.ui.theme.SlateDark900
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SpaceDark950
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ReaderScreen(
    viewModel: FatanViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val chapter by viewModel.activeChapter.collectAsStateWithLifecycle()
    val fontSize by viewModel.readerFontSize.collectAsStateWithLifecycle()
    val readerTheme by viewModel.readerTheme.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarksList.collectAsStateWithLifecycle()
    val progressList by viewModel.readingProgressList.collectAsStateWithLifecycle()

    if (chapter == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Tidak ada bab yang dipilih", color = Color.White)
        }
        return
    }

    val currentChapter = chapter!!
    val currentProgress = progressList.find { it.chapterId == currentChapter.id }

    val listState = rememberLazyListState()

    // Restore saved reading progress
    LaunchedEffect(currentChapter.id) {
        val savedIndex = currentProgress?.scrollIndex ?: 0
        val savedOffset = currentProgress?.scrollOffset ?: 0
        if (savedIndex > 0 || savedOffset > 0) {
            listState.scrollToItem(savedIndex, savedOffset)
        }
    }

    // Auto-save scroll progress periodically
    LaunchedEffect(listState, currentChapter.id) {
        snapshotFlow {
            val totalItems = listState.layoutInfo.totalItemsCount
            val firstVisible = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            val percent = if (totalItems > 1) {
                firstVisible.toFloat() / (totalItems - 1).toFloat()
            } else 0f
            Triple(firstVisible, offset, percent)
        }
            .distinctUntilChanged()
            .collect { (index, offset, percent) ->
                viewModel.saveReadingProgress(
                    chapterId = currentChapter.id,
                    scrollIndex = index,
                    scrollOffset = offset,
                    percent = percent
                )
            }
    }

    // Theme Color Mapping
    val (bgColor, cardColor, textPrimary, textSecondary, accentColor) = when (readerTheme) {
        "PAPER_SEPIA" -> Quintuple(
            PaperSepiaBg,
            Color(0xFFEADDC5),
            PaperSepiaText,
            Color(0xFF6B5841),
            Color(0xFF8B4513)
        )
        "CYBER_NEON" -> Quintuple(
            Color(0xFF030712),
            Color(0xFF111827),
            Color(0xFFE0F2FE),
            Color(0xFF38BDF8),
            CyanCore
        )
        "PITCH_BLACK" -> Quintuple(
            Color(0xFF000000),
            Color(0xFF121212),
            Color(0xFFEDEDED),
            Color(0xFFA0A0A0),
            CyanCore
        )
        else -> Quintuple( // DARK_SCI_FI
            SpaceDark950,
            SlateDark900,
            TextPrimaryDark,
            TextSecondaryDark,
            CyanCore
        )
    }

    var showControls by remember { mutableStateOf(false) }
    var showBookmarksDrawer by remember { mutableStateOf(false) }
    var bookmarkDialogParagraph by remember { mutableStateOf<StoryParagraph?>(null) }
    var bookmarkNoteText by remember { mutableStateOf("") }

    val readingProgressPercent by remember {
        derivedStateOf {
            val total = listState.layoutInfo.totalItemsCount
            if (total > 1) {
                (listState.firstVisibleItemIndex.toFloat() / (total - 1).toFloat()).coerceIn(0f, 1f)
            } else 0f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Pinned Top Reader Header
            ReaderHeaderBar(
                chapter = currentChapter,
                progress = readingProgressPercent,
                accentColor = accentColor,
                bgColor = cardColor,
                textColor = textPrimary,
                onBack = { viewModel.navigateBack() },
                onToggleControls = { showControls = !showControls },
                onToggleBookmarks = { showBookmarksDrawer = true }
            )

            // Optional Reader Configuration Panel
            AnimatedVisibility(visible = showControls) {
                ReaderControlPanel(
                    fontSize = fontSize,
                    onFontSizeChange = { viewModel.updateFontSize(it) },
                    selectedTheme = readerTheme,
                    onThemeChange = { viewModel.updateReaderTheme(it) },
                    containerColor = cardColor,
                    textColor = textPrimary,
                    accentColor = accentColor
                )
            }

            // Story Paragraphs LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .testTag("reader_lazy_column"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Chapter Title Header in Reader
                item {
                    ChapterIntroHeader(
                        chapter = currentChapter,
                        accentColor = accentColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }

                // Paragraph items
                itemsIndexed(currentChapter.paragraphs) { index, paragraph ->
                    ParagraphItem(
                        paragraph = paragraph,
                        fontSize = fontSize,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        accentColor = accentColor,
                        cardColor = cardColor,
                        onBookmarkClick = {
                            bookmarkDialogParagraph = paragraph
                            bookmarkNoteText = ""
                        },
                        onInteractiveSceneClick = { sceneKey ->
                            viewModel.navigateTo(AppScreen.INTERACTIVE_TERMINAL)
                        }
                    )
                }

                // Chapter Navigation Footer
                item {
                    ChapterNavigationFooter(
                        allChapters = viewModel.allChapters,
                        currentChapter = currentChapter,
                        onSelectChapter = { nextId ->
                            viewModel.openChapter(nextId)
                        },
                        accentColor = accentColor,
                        cardColor = cardColor,
                        textColor = textPrimary
                    )
                }
            }
        }

        // Bookmark Dialog
        if (bookmarkDialogParagraph != null) {
            val p = bookmarkDialogParagraph!!
            AlertDialog(
                onDismissRequest = { bookmarkDialogParagraph = null },
                containerColor = SlateDark900,
                title = {
                    Text(
                        text = "Tambah Penanda Bacaan",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "\"${p.text.take(120)}...\"",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = CyanCore
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = bookmarkNoteText,
                            onValueChange = { bookmarkNoteText = it },
                            label = { Text("Catatan Pribadi (Opsional)", color = SlateMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addBookmark(
                                paragraphIndex = p.id,
                                quote = p.text.take(160),
                                note = bookmarkNoteText
                            )
                            bookmarkDialogParagraph = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanCore)
                    ) {
                        Text("Simpan", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { bookmarkDialogParagraph = null }) {
                        Text("Batal", color = SlateMuted)
                    }
                }
            )
        }

        // Bookmarks Drawer
        if (showBookmarksDrawer) {
            BookmarksDrawerOverlay(
                bookmarks = bookmarks.filter { it.chapterId == currentChapter.id },
                onClose = { showBookmarksDrawer = false },
                onDelete = { viewModel.deleteBookmark(it) }
            )
        }
    }
}

@Composable
private fun ReaderHeaderBar(
    chapter: ArcChapter,
    progress: Float,
    accentColor: Color,
    bgColor: Color,
    textColor: Color,
    onBack: () -> Unit,
    onToggleControls: () -> Unit,
    onToggleBookmarks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("reader_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = accentColor
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Arc ${chapter.arcNumber}: ${chapter.title}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 1
                )
                Text(
                    text = "${(progress * 100).toInt()}% dibaca",
                    fontSize = 11.sp,
                    color = accentColor
                )
            }

            Row {
                IconButton(
                    onClick = onToggleControls,
                    modifier = Modifier.testTag("reader_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Ukuran Teks & Tema",
                        tint = accentColor
                    )
                }

                IconButton(
                    onClick = onToggleBookmarks,
                    modifier = Modifier.testTag("reader_bookmarks_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Penanda",
                        tint = GoldenSun
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            color = accentColor,
            trackColor = bgColor.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun ReaderControlPanel(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    selectedTheme: String,
    onThemeChange: (String) -> Unit,
    containerColor: Color,
    textColor: Color,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ukuran Huruf: ${fontSize.toInt()}sp", fontSize = 13.sp, color = textColor)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { onFontSizeChange((fontSize - 1).coerceAtLeast(13f)) },
                        colors = ButtonDefaults.textButtonColors(contentColor = accentColor)
                    ) {
                        Text("A-", fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = { onFontSizeChange((fontSize + 1).coerceAtMost(25f)) },
                        colors = ButtonDefaults.textButtonColors(contentColor = accentColor)
                    ) {
                        Text("A+", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Slider(
                value = fontSize,
                onValueChange = onFontSizeChange,
                valueRange = 13f..25f,
                steps = 11,
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Reading Theme Selector
            Text("Tema Tampilan:", fontSize = 13.sp, color = textColor)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeOptionButton(
                    label = "Sci-Fi",
                    isSelected = selectedTheme == "DARK_SCI_FI",
                    bg = SpaceDark950,
                    text = TextPrimaryDark,
                    onClick = { onThemeChange("DARK_SCI_FI") },
                    modifier = Modifier.weight(1f)
                )
                ThemeOptionButton(
                    label = "Cyber",
                    isSelected = selectedTheme == "CYBER_NEON",
                    bg = Color(0xFF030712),
                    text = CyanCore,
                    onClick = { onThemeChange("CYBER_NEON") },
                    modifier = Modifier.weight(1f)
                )
                ThemeOptionButton(
                    label = "Sepia",
                    isSelected = selectedTheme == "PAPER_SEPIA",
                    bg = PaperSepiaBg,
                    text = PaperSepiaText,
                    onClick = { onThemeChange("PAPER_SEPIA") },
                    modifier = Modifier.weight(1f)
                )
                ThemeOptionButton(
                    label = "OLED",
                    isSelected = selectedTheme == "PITCH_BLACK",
                    bg = Color.Black,
                    text = Color.White,
                    onClick = { onThemeChange("PITCH_BLACK") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionButton(
    label: String,
    isSelected: Boolean,
    bg: Color,
    text: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) CyanCore else SlateMuted.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = text
        )
    }
}

@Composable
private fun ChapterIntroHeader(
    chapter: ArcChapter,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "ARC ${chapter.arcNumber} • ${chapter.timeline}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = chapter.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textPrimary
        )
        Text(
            text = chapter.subtitle,
            fontSize = 13.sp,
            color = textSecondary,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(accentColor.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun ParagraphItem(
    paragraph: StoryParagraph,
    fontSize: Float,
    textPrimary: Color,
    textSecondary: Color,
    accentColor: Color,
    cardColor: Color,
    onBookmarkClick: () -> Unit,
    onInteractiveSceneClick: (String) -> Unit
) {
    when (paragraph.type) {
        ParagraphType.DIALOGUE -> {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialogue_card_${paragraph.id}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (paragraph.speaker) {
                                        "Fatan" -> CyanCore.copy(alpha = 0.2f)
                                        "Rose" -> RoseCrimson.copy(alpha = 0.2f)
                                        "Kael" -> GoldenSun.copy(alpha = 0.2f)
                                        "Time Conqueror" -> Color(0xFF6B21A8).copy(alpha = 0.3f)
                                        else -> SlateDark700
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = paragraph.speaker ?: "Karakter",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (paragraph.speaker) {
                                    "Fatan" -> CyanCore
                                    "Rose" -> RoseCrimson
                                    "Kael" -> GoldenSun
                                    "Time Conqueror" -> Color(0xFFC084FC)
                                    else -> TextPrimaryDark
                                }
                            )
                        }

                        IconButton(
                            onClick = onBookmarkClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Tandai",
                                tint = SlateMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = paragraph.text,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.5).sp,
                        color = textPrimary
                    )
                }
            }
        }

        ParagraphType.ACTION_EFFECT -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MalignantRed.copy(alpha = 0.15f))
                    .border(1.dp, MalignantRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = paragraph.text,
                    fontSize = (fontSize + 2).sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MalignantRed,
                    letterSpacing = 2.sp
                )
            }
        }

        ParagraphType.WARNING, ParagraphType.SYSTEM_AI -> {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (paragraph.type == ParagraphType.WARNING)
                        MalignantRed.copy(alpha = 0.15f)
                    else
                        CyanCore.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Peringatan",
                        tint = if (paragraph.type == ParagraphType.WARNING) MalignantRed else CyanCore,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = paragraph.text,
                        fontSize = (fontSize - 1).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (paragraph.type == ParagraphType.WARNING) MalignantRed else CyanCore
                    )
                }
            }
        }

        ParagraphType.NARRATIVE -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = paragraph.text,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.6).sp,
                    color = textPrimary
                )

                // Interactive Scene Button embedded if tagged
                if (paragraph.interactiveSceneKey != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onInteractiveSceneClick(paragraph.interactiveSceneKey) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanCore),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("interactive_scene_btn_${paragraph.interactiveSceneKey}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Terminal",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (paragraph.interactiveSceneKey) {
                                "SCENE_REACTOR_ACTIVATE" -> "Buka HUD Reaktor: Simulasi Armor Pertama"
                                "SCENE_WHITE_SPARK_BLUEPRINT" -> "Buka Blueprint White Spark: Uji Vapor Chamber"
                                "SCENE_TIME_CONQUEROR" -> "Simulasi Melawan Time Conqueror: Reset Kode"
                                else -> "Buka Terminal Tempur White Spark"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterNavigationFooter(
    allChapters: List<ArcChapter>,
    currentChapter: ArcChapter,
    onSelectChapter: (String) -> Unit,
    accentColor: Color,
    cardColor: Color,
    textColor: Color
) {
    val currentIndex = allChapters.indexOfFirst { it.id == currentChapter.id }
    val prevChapter = if (currentIndex > 0) allChapters[currentIndex - 1] else null
    val nextChapter = if (currentIndex in 0 until allChapters.size - 1) allChapters[currentIndex + 1] else null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(accentColor.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (prevChapter != null) {
                Button(
                    onClick = { onSelectChapter(prevChapter.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = cardColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arc Sebelumnya",
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Arc ${prevChapter.arcNumber}", color = textColor, fontSize = 12.sp)
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            if (nextChapter != null) {
                Button(
                    onClick = { onSelectChapter(nextChapter.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (nextChapter.arcNumber == 4) "Teaser Arc 4" else "Lanjut Arc ${nextChapter.arcNumber}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arc Selanjutnya",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksDrawerOverlay(
    bookmarks: List<BookmarkEntity>,
    onClose: () -> Unit,
    onDelete: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onClose() },
        contentAlignment = Alignment.CenterEnd
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxSize()
                .clickable(enabled = false) {},
            color = SlateDark900
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Penanda Kutipan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimaryDark
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = CyanCore
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (bookmarks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada kutipan yang ditandai di arc ini.\nKetuk ikon bookmark di samping dialog untuk menyimpan!",
                            color = SlateMuted,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(bookmarks.size) { index ->
                            val bm = bookmarks[index]
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = SlateDark800),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Paragraf #${bm.paragraphIndex}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyanCore
                                        )
                                        IconButton(
                                            onClick = { onDelete(bm.id) },
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus",
                                                tint = MalignantRed,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "\"${bm.quote}\"",
                                        fontSize = 12.sp,
                                        color = TextPrimaryDark,
                                        fontStyle = FontStyle.Italic
                                    )

                                    if (bm.note.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Catatan: ${bm.note}",
                                            fontSize = 11.sp,
                                            color = GoldenSun
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

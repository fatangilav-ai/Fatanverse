package com.example.ui

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkEntity
import com.example.data.local.FatanDatabase
import com.example.data.local.ReadingProgressEntity
import com.example.data.model.ArcChapter
import com.example.data.model.CharacterDataSource
import com.example.data.model.CharacterProfile
import com.example.data.model.FunFactDataSource
import com.example.data.model.FunFactItem
import com.example.data.model.SimulationResult
import com.example.data.model.StoryDataSource
import com.example.data.model.VsBattleDataSource
import com.example.data.model.VsBattleEntry
import com.example.data.repository.FatanRepository
import com.example.util.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    READER,
    INTERACTIVE_TERMINAL,
    CHARACTERS,
    CHARACTER_DETAIL,
    VS_BATTLE_WIKI,
    VS_SIMULATOR,
    FUN_FACTS,
    NOTIFICATIONS_SETTINGS
}

data class ReactorTerminalState(
    val temperature: Float = 42f, // Celsius (normal 35-70, danger > 120, fatal > 150)
    val powerOutputPercent: Int = 85, // 0 - 100
    val coolingEfficiencyPercent: Int = 94, // Vapor chamber
    val isConstructorMode: Boolean = true, // true = Constructor (Positive), false = X-Entropy (Negative)
    val bladeCharge: Float = 1.0f,
    val isOverheated: Boolean = false,
    val thrusterBurstActive: Boolean = false,
    val lastSystemMessage: String = "Sistem White Spark siap. Reaktor inti stabil pada mode Constructor."
)

class FatanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FatanRepository

    init {
        val db = FatanDatabase.getDatabase(application)
        repository = FatanRepository(db.fatanDao())
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    // Story / Reading
    val allChapters = StoryDataSource.allChapters

    private val _activeChapter = MutableStateFlow<ArcChapter?>(allChapters.first())
    val activeChapter: StateFlow<ArcChapter?> = _activeChapter.asStateFlow()

    val readingProgressList: StateFlow<List<ReadingProgressEntity>> =
        repository.getAllProgress()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarksList: StateFlow<List<BookmarkEntity>> =
        repository.getAllBookmarks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reader UI Preferences
    private val _readerFontSize = MutableStateFlow(17f)
    val readerFontSize: StateFlow<Float> = _readerFontSize.asStateFlow()

    private val _readerTheme = MutableStateFlow("DARK_SCI_FI") // DARK_SCI_FI, CYBER_NEON, PAPER_SEPIA, PITCH_BLACK
    val readerTheme: StateFlow<String> = _readerTheme.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // Characters & Lore
    val characters = CharacterDataSource.characters

    private val _selectedCharacter = MutableStateFlow<CharacterProfile?>(characters.first())
    val selectedCharacter: StateFlow<CharacterProfile?> = _selectedCharacter.asStateFlow()

    // VS Battle Wiki & Simulator
    val vsEntries: List<VsBattleEntry> = VsBattleDataSource.wikiEntries

    private val _vsFighter1 = MutableStateFlow(characters.first { it.id == "fatan" })
    val vsFighter1: StateFlow<CharacterProfile> = _vsFighter1.asStateFlow()

    private val _vsFighter2 = MutableStateFlow(characters.first { it.id == "golden_knight" })
    val vsFighter2: StateFlow<CharacterProfile> = _vsFighter2.asStateFlow()

    private val _simulationResult = MutableStateFlow<SimulationResult?>(null)
    val simulationResult: StateFlow<SimulationResult?> = _simulationResult.asStateFlow()

    // Fun facts
    val funFacts = FunFactDataSource.funFacts

    private val _selectedFunFactCategory = MutableStateFlow("Semua")
    val selectedFunFactCategory: StateFlow<String> = _selectedFunFactCategory.asStateFlow()

    // Interactive Terminal State
    private val _reactorState = MutableStateFlow(ReactorTerminalState())
    val reactorState: StateFlow<ReactorTerminalState> = _reactorState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Load initial settings
        viewModelScope.launch {
            val savedSize = repository.getSetting("reader_font_size", "17").toFloatOrNull() ?: 17f
            _readerFontSize.value = savedSize
            val savedTheme = repository.getSetting("reader_theme", "DARK_SCI_FI")
            _readerTheme.value = savedTheme
            val savedNotif = repository.getSetting("notifications_enabled", "true").toBoolean()
            _notificationsEnabled.value = savedNotif

            // Default simulation
            runVsSimulation()
        }
    }

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    // Chapter selection & Reading
    fun openChapter(chapterId: String) {
        val chapter = allChapters.find { it.id == chapterId } ?: allChapters.first()
        _activeChapter.value = chapter
        navigateTo(AppScreen.READER)
    }

    fun saveReadingProgress(chapterId: String, scrollIndex: Int, scrollOffset: Int, percent: Float) {
        viewModelScope.launch {
            repository.saveProgress(
                chapterId = chapterId,
                scrollIndex = scrollIndex,
                scrollOffset = scrollOffset,
                percent = percent,
                isCompleted = percent >= 0.95f
            )
        }
    }

    fun addBookmark(paragraphIndex: Int, quote: String, note: String = "") {
        val chapter = _activeChapter.value ?: return
        viewModelScope.launch {
            repository.addBookmark(
                chapterId = chapter.id,
                arcTitle = "Arc ${chapter.arcNumber}: ${chapter.title}",
                paragraphIndex = paragraphIndex,
                quote = quote,
                note = note
            )
            _toastMessage.value = "Penanda bacaan berhasil disimpan!"
        }
    }

    fun deleteBookmark(id: Long) {
        viewModelScope.launch {
            repository.removeBookmark(id)
            _toastMessage.value = "Penanda bacaan dihapus"
        }
    }

    fun updateFontSize(newSize: Float) {
        _readerFontSize.value = newSize
        viewModelScope.launch {
            repository.setSetting("reader_font_size", newSize.toString())
        }
    }

    fun updateReaderTheme(themeName: String) {
        _readerTheme.value = themeName
        viewModelScope.launch {
            repository.setSetting("reader_theme", themeName)
        }
    }

    // Character Detail
    fun selectCharacter(char: CharacterProfile) {
        _selectedCharacter.value = char
        navigateTo(AppScreen.CHARACTER_DETAIL)
    }

    // VS Battle Simulator
    fun setVsFighter1(char: CharacterProfile) {
        _vsFighter1.value = char
        runVsSimulation()
    }

    fun setVsFighter2(char: CharacterProfile) {
        _vsFighter2.value = char
        runVsSimulation()
    }

    fun runVsSimulation() {
        val result = VsBattleDataSource.simulateBattle(_vsFighter1.value, _vsFighter2.value)
        _simulationResult.value = result
    }

    // Fun fact category filter
    fun setFunFactCategory(category: String) {
        _selectedFunFactCategory.value = category
    }

    // Interactive Terminal Actions
    fun toggleReactorMode() {
        val current = _reactorState.value
        val newMode = !current.isConstructorMode
        val newTemp = if (newMode) 45f else 135f
        val isOverheat = !newMode
        _reactorState.value = current.copy(
            isConstructorMode = newMode,
            temperature = newTemp,
            isOverheated = isOverheat,
            lastSystemMessage = if (newMode)
                "Mode Constructor Aktif: Energi berbasis harapan melindungi. Sirkuit stabil."
            else
                "PERINGATAN! X-Entropy Aktif: Suhu melonjak ke 135°C! Risiko Malignant Radiation dan Brain-Overheat!"
        )
    }

    fun ventHeatThroughThrusters() {
        val current = _reactorState.value
        val cooledTemp = (current.temperature - 35f).coerceAtLeast(38f)
        _reactorState.value = current.copy(
            temperature = cooledTemp,
            thrusterBurstActive = true,
            isOverheated = cooledTemp > 120f,
            lastSystemMessage = "Vapor Chamber Aktif: Panas dibuang sebagai daya pendorong jet! Suhu turun ke ${cooledTemp.toInt()}°C."
        )
    }

    fun triggerWhiteEnergyBlade() {
        val current = _reactorState.value
        val heatIncrement = if (current.isConstructorMode) 8f else 30f
        val newTemp = current.temperature + heatIncrement
        val isOverheat = newTemp > 120f
        _reactorState.value = current.copy(
            temperature = newTemp,
            bladeCharge = 1f,
            isOverheated = isOverheat,
            lastSystemMessage = if (isOverheat)
                "Bilah Pedang Energi Putih Ditebaskan! SUHU KRITIS: ${newTemp.toInt()}°C! Pendarahan saraf terdeteksi!"
            else
                "Bilah Pedang Energi Putih Menebas! Suhu reaktor terkontrol pada ${newTemp.toInt()}°C."
        )
    }

    fun stabilizeReactor() {
        _reactorState.value = ReactorTerminalState(
            temperature = 40f,
            powerOutputPercent = 85,
            coolingEfficiencyPercent = 98,
            isConstructorMode = true,
            bladeCharge = 1f,
            isOverheated = false,
            thrusterBurstActive = false,
            lastSystemMessage = "Sistem White Spark di-reset ulang ke kondisi optimal oleh Kael."
        )
    }

    // Notification Trigger
    fun sendSimulatedChapterNotification(): Boolean {
        val success = NotificationHelper.sendChapterUpdateNotification(
            context = getApplication(),
            chapterTitle = "Bab Arc 4: Embrio Bawah Tanah",
            arcSubtitle = "Anomali Kerak Bumi Sektor 4 terdeteksi oleh radar DHC!"
        )
        if (success) {
            _toastMessage.value = "Notifikasi rilis bab baru berhasil dikirim ke perangkat!"
        } else {
            _toastMessage.value = "Izin notifikasi belum diizinkan atau dinonaktifkan."
        }
        return success
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        viewModelScope.launch {
            repository.setSetting("notifications_enabled", enabled.toString())
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}

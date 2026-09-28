package com.example.data.model

data class VsBattleEntry(
    val characterId: String,
    val canonicalName: String,
    val tier: String,
    val attackPotency: String,
    val speed: String,
    val liftingStrength: String,
    val strikingStrength: String,
    val durability: String,
    val stamina: String,
    val range: String,
    val standardEquipment: String,
    val intelligence: String,
    val notableAbilities: List<String>,
    val keys: List<String> // e.g. ["Arc 1 (Age 14 / Prototype Armor)", "Arc 2 (White Spark)", "Arc 3 (DHC Team Tactics)"]
)

data class SimulationResult(
    val fighter1Score: Int,
    val fighter2Score: Int,
    val winnerName: String,
    val winProbability: Int,
    val battleSummary: String,
    val keyAdvantage: String,
    val tacticalBreakdown: List<String>
)

object VsBattleDataSource {

    val wikiEntries: List<VsBattleEntry> = listOf(
        VsBattleEntry(
            characterId = "fatan",
            canonicalName = "Fatan (The Constructor)",
            tier = "Tier 8-C | Tier 7-C (White Spark) | High 7-A (Peak Overheat)",
            attackPotency = "Building Level (Awal) | Town Level (White Spark) | Mountain Level (Tombak Data Raksasa)",
            speed = "Subsonik+ (Awal) | Supersonik (400 m/s manuver) | Hipersonik Rendah (Jet Pendorong)",
            liftingStrength = "Kelas 25 (Mampu mengangkat balok baja industri) hingga Kelas 100 dengan exoskeleton",
            strikingStrength = "Town Class (Bilah Pedang Energi Putih membelah iblis berkepala tiga dan dummy baja padat)",
            durability = "Town Level (Pelat Titanium komposit dengan peredam gelombang)",
            stamina = "Tinggi dengan White Spark Vapor Chamber; Rendah jika memaksakan Malignant X-Entropy (Risiko pecah pembuluh darah 3 menit)",
            range = "Jarak Dekat (Pedang Energi) hingga Puluhan Kilometer (Tombak Data & Meriam Plasma)",
            standardEquipment = "Armor Tempur White Spark, Vapor Chamber Cooling, Micro HUD Display, Pedang Energi Putih",
            intelligence = "Jenius Tingkat Tinggi (Merancang kacamata tunanetra di usia 14 tahun, meretas aliran energi armor secara real-time, menguasai teori termodinamika)",
            notableAbilities = listOf(
                "Konstruksi Data Digital (Materialisasi senjata energi dari data)",
                "Penyerapan & Pembuangan Panas Cepat (Vapor Chamber)",
                "Bypass Penuaan Real-time (Me-reset struktur data saat diserang distorsi waktu)",
                "Brain-Overheat Thruster Burst (Mengalihkan daya panas menjadi pendorong darurat)",
                "Penerbangan Supersonik & Manuver Udara Ekstrem"
            ),
            keys = listOf("Arc 1: Prototipe Armor (Usia 14)", "Arc 2: White Spark (Usia 19)", "Arc 3: Sinergi Skuad DHC")
        ),
        VsBattleEntry(
            characterId = "time_conqueror",
            canonicalName = "Time Conqueror (Anomali Dimensi)",
            tier = "Low 2-C (Universe Level+ / Entitas Krono)",
            attackPotency = "Mengabaikan Durabilitas Konvensional (Membusukkan materi dengan percepatan waktu ribuan tahun)",
            speed = "Immeasurable (Mampu bergerak saat waktu di sekitarnya dihentikan total)",
            liftingStrength = "Tidak Terikat Materi Fisik",
            strikingStrength = "Tidak Terikat Massa Fisik",
            durability = "Abadi (Tipe 1, 2, dan 3: Pembalikan Waktu Tubuh 1 Detik secara instan)",
            stamina = "Tak Terbatas (Beroperasi di luar aliran waktu linier)",
            range = "Universal / Dimensional (Mampu meruntuhkan peradaban satu sektor menjadi debu dalam satu kedipan)",
            standardEquipment = "Singularitas Waktu & Jubah Abu-abu Nir-Masa",
            intelligence = "Kosmik / Maha Tahu tentang garis waktu masa depan",
            notableAbilities = listOf(
                "Manipulasi Waktu Mutlak (Temporal Stop, Acceleration, Rewind)",
                "Age Decay (Membusukkan logam padat menjadi serbuk dalam hitungan detik)",
                "Pembalikan Waktu Diri (Memutar mundur luka ke detik sebelum serangan menyentuh)",
                "Stasis Gerak Musuh (Menghentikan bilah pedang 1 milimeter dari leher)",
                "Teleportasi Lintas Celah Waktu"
            ),
            keys = listOf("Bentuk Dasar (Kemunculan Sektor 7)")
        ),
        VsBattleEntry(
            characterId = "golden_knight",
            canonicalName = "Golden Knight (Petinggi DHC)",
            tier = "Tier 7-A (Mountain Level)",
            attackPotency = "Mountain Level (Rentetan proyektil foton mampu melubangi dan melelehkan batuan gunung)",
            speed = "Kecepatan Cahaya (Speed of Light burst) / Relativistik Rendah",
            liftingStrength = "Kelas 50",
            strikingStrength = "Mountain Class",
            durability = "Mountain Level (Armor Keemasan Fotonik)",
            stamina = "Sangat Tinggi (Ditenagai resonansi foton surya)",
            range = "Ratusan Kilometer (Sinar cahaya tembus pandang)",
            standardEquipment = "Armor Keemasan Ringan, Pedang Cahaya Helios",
            intelligence = "Kombatan Veteran Tingkat Tinggi dengan keahlian kepemimpinan militer",
            notableAbilities = listOf(
                "Pergerakan Fotonik Secepat Cahaya",
                "Rentetan Peluru Cahaya Suhu Ekstrem",
                "Pelepasan Silau Buta Total (Flashbang skala kota)",
                "Pelelehan Es & Struktur Energi Seketika"
            ),
            keys = listOf("Petinggi DHC: Mode Terang Penuh")
        ),
        VsBattleEntry(
            characterId = "rose",
            canonicalName = "Rose (Penyihir Garis Depan DHC)",
            tier = "Tier 7-C (Town Level)",
            attackPotency = "Town Level (Ignis Burst melelehkan pelat baja 5 meter hingga menembus batuan bumi)",
            speed = "Supersonik (Proyektil sihir 400 m/s) dengan Teleportasi Instan",
            liftingStrength = "Tingkat Manusia Super Ringan (Didukung energi sihir)",
            strikingStrength = "Multi-City Block Class",
            durability = "City Block Level (Tanpa barrier) | Town Level (Dengan Magic Aegis)",
            stamina = "Tinggi (Mampu mempertahankan pertempuran melawan puluhan iblis serigala)",
            range = "Kilometeran (Teleportasi pelacak 5 km, proyektil melengkung)",
            standardEquipment = "Crest Sihir Crimson, Rune Pengikat Durjana",
            intelligence = "Master Taktik Sihir & Jebakan (Mampu memancing lawan ke titik buta lapis dua)",
            notableAbilities = listOf(
                "Ignis Burst & Ignis Barrage (Api bersuhu di atas 2000°C)",
                "Teleportasi Pelacak Ruang",
                "Cincin Sihir Pengikat Duri Pembeku (Menghambat pendorong mekanik)",
                "Pencegah Serangan Garis Belakang Otomatis"
            ),
            keys = listOf("Bentuk Tempur Penuh")
        ),
        VsBattleEntry(
            characterId = "vinki",
            canonicalName = "Vinki (Pengamat Mata Gravitasi)",
            tier = "Tier 7-B hingga Tier 6-C (Island Level)",
            attackPotency = "City Level hingga Mountain Level (Singularitas Gravitasi)",
            speed = "Hipersonik",
            liftingStrength = "Kelas G (Manipulasi daya tarik gravitasi)",
            strikingStrength = "City Class",
            durability = "City Level+",
            stamina = "Sangat Tinggi (Beroperasi dari bayangan tanpa lelah)",
            range = "Lintas Sektor Pemukiman",
            standardEquipment = "Mata Merah Raksasa Melayang, Kacamata Hitam Filter Entropi",
            intelligence = "Mastermind / Jenius Konspirasi (Memahami ekosistem reproduksi iblis kerak bumi)",
            notableAbilities = listOf(
                "Manipulasi Gravitasi Berskala Luas",
                "Pemanenan Malignant Energy (Energi Kebencian)",
                "Inkubasi Embrio Iblis Kerak Bumi",
                "Distorsi Pandangan & Kamuflase Bayangan"
            ),
            keys = listOf("Bentuk Pengamat")
        ),
        VsBattleEntry(
            characterId = "aspire",
            canonicalName = "Aspire (Arsitek Realitas)",
            tier = "Tier 7-A (Mountain Level)",
            attackPotency = "Mountain Level (Membalikkan gravitasi seluruh arena latihan)",
            speed = "Subsonik Gerak Fisik, Kecepatan Pikiran Instan",
            liftingStrength = "Kelas 100+ via Telekinesis Realitas",
            strikingStrength = "Mountain Class",
            durability = "Mountain Level (Perisai Realitas Psikis)",
            stamina = "Tinggi",
            range = "Ratusan Meter di Sekelilingnya",
            standardEquipment = "Kursi Melayang Psikis Khusus",
            intelligence = "Jenius Konseptual & Strategi Elegansi",
            notableAbilities = listOf(
                "Reality Grid Warping (Memecah daratan menjadi balok gravitasi melayang)",
                "Perisai Realitas Mutlak",
                "Levitasi Bebas Hambatan",
                "Pikiran Membentuk Materi"
            ),
            keys = listOf("Petinggi DHC: Mode Arsitek")
        ),
        VsBattleEntry(
            characterId = "the_void",
            canonicalName = "The Void (Penelan Hampa)",
            tier = "Tier 7-C (Town Level)",
            attackPotency = "City Block Level",
            speed = "Supersonik Rendah",
            liftingStrength = "Tingkat Manusia Normal",
            strikingStrength = "City Block Class",
            durability = "Town Level+ (Melalui Pembelokan Hampa Udara)",
            stamina = "Tinggi",
            range = "Puluhan Meter",
            standardEquipment = "Sarung Tangan Ruang Kosong",
            intelligence = "Tinggi dalam Taktik Pertahanan Kolektif",
            notableAbilities = listOf(
                "Penyerapan Elemen Panas, Sihir, dan Ledakan",
                "Penciptaan Distorsi Hampa Udara",
                "Redistribusi Energi Kinetik Bersih ke Sekutu"
            ),
            keys = listOf("Skuad Tempur Fatan")
        )
    )

    fun simulateBattle(char1: CharacterProfile, char2: CharacterProfile): SimulationResult {
        // Special canonical logic for Time Conqueror
        if (char1.id == "time_conqueror" && char2.id != "time_conqueror") {
            return SimulationResult(
                fighter1Score = 98,
                fighter2Score = 2,
                winnerName = char1.name,
                winProbability = 98,
                battleSummary = "${char1.name} dengan mudah memanipulasi aliran waktu. Serangan ${char2.name} dibekukan 1 mm dari lehernya atau di-reset mundur ke masa sebelum dilepaskan.",
                keyAdvantage = "Manipulasi Aliran Waktu Mutlak & Pembalikan Waktu Tubuh 1 Detik",
                tacticalBreakdown = listOf(
                    "Waktu di sekitar ${char2.name} dihentikan atau dipercepat hingga ratusan tahun.",
                    "Percepatan usia ekstrim menghancurkan armor maupun fisik ${char2.name}.",
                    "${char2.name} sempat memberikan perlawanan data/sihir, namun Time Conqueror mereset tubuhnya ke kondisi sebelum menerima luka."
                )
            )
        } else if (char2.id == "time_conqueror" && char1.id != "time_conqueror") {
            return SimulationResult(
                fighter1Score = 2,
                fighter2Score = 98,
                winnerName = char2.name,
                winProbability = 98,
                battleSummary = "${char2.name} menghentikan waktu dan membusukkan persenjataan ${char1.name} menjadi serbuk dalam hitungan detik.",
                keyAdvantage = "Manipulasi Aliran Waktu Mutlak & Pembalikan Waktu Tubuh 1 Detik",
                tacticalBreakdown = listOf(
                    "Serangan ${char1.name} dibekukan di udara sebelum menyentuh target.",
                    "Sentuhan Chronos membusukkan struktur materi dalam waktu singkat.",
                    "${char1.name} tak mampu menembus paradoks waktu Time Conqueror tanpa bantuan entitas skala kosmik."
                )
            )
        }

        // Calculate based on stats and affinities
        val score1 = (char1.stats.attack * 1.2 + char1.stats.speed * 1.1 + char1.stats.durability * 1.0 + char1.stats.intelligence * 1.3 + char1.stats.versatility * 1.1).toInt()
        val score2 = (char2.stats.attack * 1.2 + char2.stats.speed * 1.1 + char2.stats.durability * 1.0 + char2.stats.intelligence * 1.3 + char2.stats.versatility * 1.1).toInt()

        val diff = score1 - score2
        val prob1 = (50 + (diff / 8)).coerceIn(15, 85)
        val prob2 = 100 - prob1

        val winner = if (prob1 >= prob2) char1 else char2
        val loser = if (prob1 >= prob2) char2 else char1
        val winProb = if (prob1 >= prob2) prob1 else prob2

        val advantage = when {
            winner.stats.speed > loser.stats.speed + 10 -> "Keunggulan Kecepatan dan Manuver Lincah (${winner.speed})"
            winner.stats.intelligence > loser.stats.intelligence + 8 -> "Kecerdasan Taktis & Analisis Titik Buta (${winner.stats.intelligence}/100)"
            winner.stats.attack > loser.stats.attack + 8 -> "Daya Hancur Serangan Ekstrem (${winner.attackPotency})"
            else -> "Kombinasi Sinergi Kemampuan dan Fleksibilitas Pertempuran"
        }

        val breakdown = listOf(
            "Fase Pembuka: ${char1.name} dan ${char2.name} saling menjajaki jarak dengan ${char1.signatureMove} vs ${char2.signatureMove}.",
            "Fase Pertengahan: ${winner.name} berhasil menemukan celah pada ${loser.weaknesses.firstOrNull() ?: "pertahanan musuh"}.",
            "Puncak Duel: ${winner.name} mengerahkan ${winner.signatureMove}, memaksa ${loser.name} terpojok dengan persentase dominasi ${winProb}%."
        )

        return SimulationResult(
            fighter1Score = prob1,
            fighter2Score = prob2,
            winnerName = winner.name,
            winProbability = winProb,
            battleSummary = "${winner.name} diprediksi memenangkan duel sengit ini berkat $advantage.",
            keyAdvantage = advantage,
            tacticalBreakdown = breakdown
        )
    }
}

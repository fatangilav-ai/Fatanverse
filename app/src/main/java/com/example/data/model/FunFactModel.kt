package com.example.data.model

data class FunFactItem(
    val id: Int,
    val category: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val tags: List<String>,
    val secretTrivia: String
)

object FunFactDataSource {

    val funFacts = listOf(
        FunFactItem(
            id = 1,
            category = "Lore Dunia & DHC",
            title = "Mengapa Alam Melahirkan Iblis?",
            subtitle = "Keseimbangan Ekosistem Semesta FATANVERSE",
            content = "Di semesta FATANVERSE, manusia pernah mencapai era kedamaian mutlak yang begitu lama tanpa konflik. Ironisnya, alam semesta menganggap ketiadaan tantangan sebagai anomali fatal, sehingga alam sendiri melahirkan iblis sebagai penyeimbang ekosistem untuk menguji ketahanan eksistensi manusia.",
            tags = listOf("DHC", "Ekologi Iblis", "Asal Mula"),
            secretTrivia = "DHC didirikan bukan sekadar sebagai tentara pemburu, melainkan benteng pertahanan ekosistem agar alam tidak mengirim bencana pemusnah peradaban yang lebih ekstrem."
        ),
        FunFactItem(
            id = 2,
            category = "Karakter & Masa Lalu",
            title = "Pencapaian Terbesar Fatan Usia 14 Tahun",
            subtitle = "Bukan Senjata, Melainkan Kacamata Medis",
            content = "Sebelum rumahnya dihancurkan iblis, Fatan bercita-cita murni sebagai insinyur sipil. Pencapaian terbesarnya sebelum tragedi adalah merancang dan menyolder kacamata saraf optik canggih yang berhasil membantu tunanetra melihat kembali dunia secara jelas.",
            tags = listOf("Fatan", "Usia 14", "Inovasi"),
            secretTrivia = "Blueprint kacamata tersebut disimpan Kael di arsip paten DHC dan dijadikan dasar teknologi sensor pada helm armor tempur Fatan."
        ),
        FunFactItem(
            id = 3,
            category = "Teknologi Armor",
            title = "Rahasia Armor 'White Spark' & Vapor Chamber",
            subtitle = "Solusi Inovatif Menghilangkan Brain-Overheat",
            content = "Setelah lumpuh akibat kepanasan reaktor di Arc 1, Fatan merombak total jalur pembuangan panas pada armor White Spark. Ia memasang sistem pendingin ruang uap (*vapor chamber*) terintegrasi yang mampu menyerap kelebihan panas sirkuit dan menyemburkannya sebagai pendorong jet tambahan alih-alih memanggang otak pengguna.",
            tags = listOf("White Spark", "Vapor Chamber", "Fisika Armor"),
            secretTrivia = "Kael menyebut teknologi vapor chamber Fatan sebagai ide gila karena teknisi elit DHC butuh waktu berbulan-bulan hanya untuk memahami teori termodinamikanya."
        ),
        FunFactItem(
            id = 4,
            category = "Sistem Kekuatan",
            title = "Dua Sisi X-Power: Constructor vs X-Entropy",
            subtitle = "Energi Harapan Melindungi vs Dendam Membakar",
            content = "X-Power sepenuhnya mencerminkan pola pikir pengguna. Jika digerakkan oleh dendam dan amarah (X-Entropy / Malignant Energy), radiasi negatifnya merusak pembuluh darah pengguna dan menjadi santapan energi bagi kerak bumi untuk melahirkan iblis baru. Namun jika dipicu oleh harapan melindungi orang lain (Constructor Mode), partikel cahayanya stabil dan dapat meretas serta memadatkan data murni.",
            tags = listOf("X-Power", "Malignant Energy", "Constructor"),
            secretTrivia = "Saat Fatan bertarung di Sektor 4 dengan dendam, radiasi negatifnya merembes ke bawah tanah dan langsung menetaskan 3 embrio iblis baru yang diamati oleh Vinki."
        ),
        FunFactItem(
            id = 5,
            category = "Medis & Saraf",
            title = "Sindrom Brain-Overheat",
            subtitle = "Mengapa Fatan Berdarah dari Hidung dan Telinga?",
            content = "Saat seorang Constructor memaksakan kemampuan manipulasi data digital melebihi kapasitas saraf manusia, gelombang otak beresonansi pada frekuensi destruktif. Panas reaktor yang terhubung langsung ke saraf optik memicu pendarahan pada selaput kapiler hidung dan telinga, yang jika terlambat ditangani selama 3 menit dapat membakar tengkorak secara permanen.",
            tags = listOf("Brain-Overheat", "Medis DHC", "Batas Manusia"),
            secretTrivia = "Kael melarang siapa pun melepas armor Fatan secara paksa di Sektor 4 karena sistem sarafnya masih terkunci dengan reaktor; penarikan paksa saat itu bisa memutus sumsum tulang belakang Fatan."
        ),
        FunFactItem(
            id = 6,
            category = "Misteri & Anomali",
            title = "Mata Gravitasi Raksasa Vinki",
            subtitle = "Sosok di Balik Reruntuhan Gedung Sektor 4",
            content = "Vinki mengenakan kacamata hitam bukan untuk gaya, melainkan untuk menyaring distorsi spektrum radiasi gravitasi yang dipancarkan oleh mata raksasa merah gelap yang melayang di belakangnya. Vinki meneliti batas ketahanan otak pengguna X-Power dan membudidayakan embrio iblis dari energi kebencian manusia.",
            tags = listOf("Vinki", "Mata Merah", "Konspirasi"),
            secretTrivia = "Vinki bukan bagian dari DHC maupun aliansi iblis liar; ia mewakili faksi ketiga yang mengincar singularitas gravitasi di kerak bumi."
        ),
        FunFactItem(
            id = 7,
            category = "Misteri & Anomali",
            title = "Hukum Waktu Time Conqueror",
            subtitle = "Mengapa Kecepatan Super Sekalipun Sia-sia?",
            content = "Time Conqueror tidak membutuhkan perisai baja untuk menahan serangan. Ia menguasai hukum 'Body Temporal Rewind': jika terkena hantaman, ia cukup memutar mundur waktu pada tubuhnya sendiri sebanyak 1 detik ke momen sebelum serangan dilepaskan. Oleh karena itu, semua serangan fisik dan energi yang membakar nyawa musuh menjadi sia-sia.",
            tags = listOf("Time Conqueror", "Hukum Waktu", "Entitas Kosmik"),
            secretTrivia = "Satu-satunya hal yang mampu membuat Time Conqueror mundur 2 langkah adalah peluru kode digital Fatan yang di-reset kondisinya ke masa kini secara real-time sebelum sempat dituakan oleh medan distorsi waktu."
        ),
        FunFactItem(
            id = 8,
            category = "Skuad & Persahabatan",
            title = "Trik Dummy Robot Fatan",
            subtitle = "Alasan Fatan Mengelabui Para Petinggi DHC",
            content = "Pada hari pertama latihan gabungan bersama para Petinggi DHC, Fatan mengganti dirinya dengan dummy robot mekanik agar bisa menyelinap ke bunker bawah tanah tua untuk merakit armornya secara sembunyi-sembunyi. Namun ia langsung digerebek oleh kemampuan teleportasi pelacak milik Rose!",
            tags = listOf("Humor DHC", "Fatan & Rose", "Bunker"),
            secretTrivia = "Rose langsung menyadari dummy robot tersebut palsu karena dummy itu tidak mengeluarkan komentar sarkas atau mengeluh lelah seperti kebiasaan asli Fatan."
        )
    )
}

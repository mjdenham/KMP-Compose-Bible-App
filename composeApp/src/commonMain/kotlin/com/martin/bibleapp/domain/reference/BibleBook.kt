package com.martin.bibleapp.domain.reference

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class BibleBook(val usfmCode: String) {
    GEN("GEN"),
    EXOD("EXO"),
    LEV("LEV"),
    NUM("NUM"),
    DEUT("DEU"),
    JOSH("JOS"),
    JUDG("JDG"),
    RUTH("RTH"),
    SAM1("1SA"),
    SAM2("2SA"),
    KGS1("1KI"),
    KGS2("2KI"),
    CHR1("1CH"),
    CHR2("2CH"),
    EZRA("EZR"),
    NEH("NEH"),
    ESTH("EST"),
    JOB("JOB"),
    PS("PSA"),
    PROV("PRO"),
    ECCL("ECC"),
    SONG("SNG"),
    ISA("ISA"),
    JER("JER"),
    LAM("LAM"),
    EZEK("EZK"),
    DAN("DAN"),
    HOS("HOS"),
    JOEL("JOL"),
    AMOS("AMO"),
    OBAD("OBA"),
    JONAH("JON"),
    MIC("MIC"),
    NAH("NAM"),
    HAB("HAB"),
    ZEPH("ZEP"),
    HAG("HAG"),
    ZECH("ZEC"),
    MAL("MAL"),
    MATT("MAT"),
    MARK("MRK"),
    LUKE("LUK"),
    JOHN("JHN"),
    ACTS("ACT"),
    ROM("ROM"),
    COR1("1CO"),
    COR2("2CO"),
    GAL("GAL"),
    EPH("EPH"),
    PHIL("PHP"),
    COL("COL"),
    THESS1("1TH"),
    THESS2("2TH"),
    TIM1("1TI"),
    TIM2("2TI"),
    TITUS("TIT"),
    PHLM("PHM"),
    HEB("HEB"),
    JAS("JAS"),
    PET1("1PE"),
    PET2("2PE"),
    JOHN1("1JN"),
    JOHN2("2JN"),
    JOHN3("3JN"),
    JUDE("JUDE"),
    REV("REV"),;

    fun shortLabel(): String = name.lowercase().capitalize(Locale.current)
}
/* -----------------------------------------------------------------------------
GLEAM/AE                  Sprach-Header-File (Deutsch)            
Package: gl_opalv               File: gleam_d.h              Version:     V1.4.0
Status : o.k.                   Autor: W.Jakob                 Datum: 05.10.2017
--------------------------------------------------------------------------------
This is free software, which is governed by the Lesser GNU Public Licence (LGPL), 
version 3, see the comment section at the end of this file.
-------------------------------------------------------------------------------- */


/* ----------------------------- Globale Texte ------------------------------ */
#ifdef ONLINE
  #define CONNECTION     "On "
#else
  #define CONNECTION     "Off"
#endif
#define VERS_NR        "2.2.0"
#define VERSION        VERS_NR"/"CONNECTION
#define STAND          "27.06.2018"


/* ------------------------ Texte des Hauptprogramms ------------------------ */
#define NO_EXP_PARAM   "Kein EXP-File angegeben. Versuche es mit \"%s\""
#define INIT_HDR_1     "Initialisierung:"
#define INIT_HDR_2     "================"
#define EXP_FILE_TXT   "Experimentdatei: %s"
#define NO_CH_FILE_TXT "  Kein Kettenfile eingelesen."
#define INIT_FIN_TXT   "Initialisierung abgeschlossen."
#define NO_INIT_CRASH  "InitFehler! Falls keine anderen Meldungen, evtl. nicht genug Speicher.\n"
#define HELP_TXT1      "Aufruf:"
#define HELP_TXT2      "  %s [<exp-file> [<log-file>]]"
#define HELP_TXT3      "Wenn kein <log-file> angegeben ist, wird der Name des Exp-Files f"ue"r das Logfile "
#define HELP_TXT4      "verwendet, sofern das EXP-File existiert. Andernfalls wird  \"%s\" genommen."
#define HELP_TXT5      "Wenn kein <exp-file> angegeben ist, wird \"%s\" und \"%s\" benutzt."
#define HELP_TXT6      "Die Angabe beider Dateien kann mit oder ohne Extension (.exp, .log) erfolgen."
#define HELP_TXT7      "Bei Nichtexistenz der EXP-Datei erfolgt Programmabbruch."

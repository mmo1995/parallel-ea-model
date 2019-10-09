/* -----------------------------------------------------------------------------
GLEAM/AE                  Sprach-Header-File (Englisch)
Package: gl_opalv               File: gleam_gb.h             Version:     V1.4.0
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
#define STAND          "06/27/2018"


/* ------------------------ Texte des Hauptprogramms ------------------------ */
#define NO_EXP_PARAM   "No exp-file specified. Trying \"%s\""
#define INIT_HDR_1     "Initialisation:"
#define INIT_HDR_2     "==============="
#define EXP_FILE_TXT   "Experiment file: %s"
#define NO_CH_FILE_TXT "  No chain file read."
#define INIT_FIN_TXT   "Initialisation completed."
#define NO_INIT_CRASH  "Initialisation failed! If no other messages check for sufficient memory.\n"
#define HELP_TXT1      "Program call:"
#define HELP_TXT2      "  %s [<exp-file> [<log-file>]]"
#define HELP_TXT3      "If no <log-file> is specified, the name of the exp-file will be used for the"
#define HELP_TXT4      "logfile instead, provided the exp-file exists. Otherwise, \"%s\" is used."
#define HELP_TXT5      "If no <exp-file> is specified, \"%s\" and \"%s\" will be used. "
#define HELP_TXT6      "The specification of both files can be done with or without extension (.exp, .log)."
#define HELP_TXT7      "The program will be terminated, if the exp-file does not exist."


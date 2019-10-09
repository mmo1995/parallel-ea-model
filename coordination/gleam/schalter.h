/* -----------------------------------------------------------------------------
GLEAM und HyGLEAM               Global Header-File
Package: -                       File: schalter.h            Version:     1.11.1
Status : o.k.                   Autor: W.Jakob                 Datum: 08.06.2018
--------------------------------------------------------------------------------

                            For GLEAM/ESS and HyGLEAM/ESS
                                    (gleam_ess)
Betriebssysteme:
  Solaris 5.8      / Sun cc und CC 5.1 sowie g++ 2.95
  Linux Suse  11.0 / gcc 4.3.1
  Debian Linux 6.0 / gcc 4.4.5

Benutzerschnittstelle:
    GLEAM_USR   Entscheidet , ob interaktive Version (gesetzt) oder reines 
                Kommandozeilenprogramm (CLV). Falls gesetzt:
    TEXT_MODE   Dialoge und Menues scrollend als ASCII-Text, BS-unabhaengig (TUI).
    GUI_MODE    GUI mit Qt.

--------------------------------------------------------------------------------
This is free software, which is governed by the Lesser GNU Public Licence (LGPL), 
version 3, see the comment section at the end of this file.
--------------------------------------------------------------------------------
Aenderungen:
 1. Linux-Portierung: Differenzierung zw. BS_SOLARIS und BS_LINUX.   WJ 12.05.05
 2. Entfernung des Schalters MIT_ADI, der in MIT_LSV aufgegangen ist. 
    Neuer Schalter MIT_ROCO, der neu bei MIT_LSV gueltig sein kann.  WJ 23.08.06
 3. Neuer Schalter CEC_MBF zur Aktivierung der CEC-Experimentierbe-
    dingungen (FEsMax=dim*10000, usw.); nur im Engine-Kontext imple-
    mentiert! Schalter MIT_CEC_MBFS in "cec_sim.c" aktiviert die 
    CEC-MBFs ohne CEC-Experimentierumgebung.                         WJ 05.11.07
 4. Definition der Environmentvariablen hier. Pfad der Fehlertext-
    files aus dem Environment und daher hier nur noch die Namen.     WJ 28.08.09
 5. Zur Vorbereitung der MPI-basierten Parallelversion wurde fol-
    gendes entfernt: MITGRAFIK, MIT_ANIM, SMALL_SCREEN
    - alte Eintraege: BGI_MODE und DOS
    - Neuerungen der Matlab-Kopplung: PAR_SIMU und BS_WINXP_GNU
    - Ueberbleibsel der pvm-Version: PAR_VERS
    - Neu geregelt wurden: GNUPLOT
    Neu hinzugekommen sind: MPI_PAR_VERS, GUI_MODE                   WJ 29.06.12
 6. Erweiterungen fuer die MPI-Parallelversion. Einrueckung der 
    bedingten Preprocessor-Anweisungen. NEU: MIN_ANZ_NACHBARN.       WJ 31.08.12
 7. Einige Definitionen fuer OPAL/V angepasst oder hinzugefuegt.     WJ 10.01.14
 8. Streichung von MOD_, BEW_, TSK_ und EVO_DEFAULT_FNAME.           WJ 10.03.16
 9. Umbenennung von MOD_FILE_IDENT nach PROG_IDENT. Streichung des 
    Schalters LOGFILE, da immer eins angelegt wird.                  WJ 03.04.17
10. Einfuehrung des Schalters OPAL_V fuer die OPAL/V-Anwendung.      WJ 11.04.17
11. Neuer Schalter EXT_MATLAB_SIMU, welcher im OPAL/V-Kontext nicht 
    zu setzen ist! Zusammenfuehrung der Definition des EXE_NAME aus
    "gleam_d/gb.h" und des EXE_FNAME aus "hyGleamEngine2.c" in 
    dieser Datei. Neuer Schalter EXT_SIMU_SERV fuer die Kopplung zu 
    den "external simulation services" und der parallelen Bewertung 
    der Nachkommenschaft einer Generation. Wenn gesetzt, dann ent-
    sprechendes Makefile benutzen!                                   WJ 30.06.17
12. Neue Datei USR_MAIN_TEXTS.                                       WJ 05.10.17
13. Einheiliches "schalter"-File fuer alle GLEAM-Varianten mit Anbin-
    dung an die externen Simulationsdienste (ESS) und paralleler Si-
    mulation basierend auf OPAL/V. Aufwertung des ONLINE-Schalters.  WJ 30.11.17
14. Streichung von ENG_MAIN_TEXTS. CLV_MAIN_TEXTS ersetzt   
    ENG2_MAIN_TEXTS.                                                 WJ 22.12.17
15. Neue Variante PAR_POP_VERS fuer die parallele Bearbeitung von 
    Teilpopulationen auf dem Cluster basierend auf dem Insel-Modell.
    Zunaechst nur in vier Varianten: Zwei zum Testen und zwei 
    Produktionsversionen.                                            WJ 12.06.18
-------------------------------------------------------------------------------- */

/* ----------- Globaler Schalter fuer Varianten und Betriebsysteme: --------- */
#undef BS_SOLARIS          /* Gesetzt: Sun/Solaris     Nur ein "BS-Schalter  */
#define BS_LINUX            /* Gesetzt: Linux          darf gesetzt  sein!    */
#undef GLEAM_USR           /* Gesetzt: Mit User-Interface, sonst ohne        */
#undef EXT_MATLAB_SIMU     /* Gesetzt: Mit ext.Matlab/Matpower } max nur ei- */
#undef EXT_SIMU_SERV       /* Gesetzt: Mit ext. Simu-Service   } ner gesetzt */
#define PAR_POP_VERS       /* Version mit parallelen Populationen (Island)   */
#undef MPI_PAR_VERS        /* MPI-basierte Parallelversion für Linux         */
#undef MIT_AEHNL_CHECK     /* mit Aehnlichkeitspruefung bevor simuliert wird */
#undef MIT_LSV             /* GLEAM_AE mit lokalen Suchverfahren             */
#undef ONLINE              /* ESS-Kopplung ODER Test-Dialoge u."testOpalSimu"*/

/* --------------------------- Festlegung der Sprache: ---------------------- */
#define DEU                  /* Gesetzt: Deutsch.         Es darf nur jeweils */
#undef ENG                  /* Gesetzt: Englisch.        einer gesetzt sein. */


/* ------------------------- Programm und EXE-Name: ------------------------- */
#ifdef MIT_LSV
  #define PROGRAM_NAME    "HyGLEAM/ParPop"
  #if defined(ONLINE) && defined(EXT_SIMU_SERV)
    #define EXE_NAME        "hyGleamParPopEssO_CLV"     /* Produktionsversion */
  #else /* no ONLINE and no ESS */
    #define EXE_NAME        "hyGleamParPopCLV"          /* Testversion        */
  #endif /* no ONLINE */
#else /* ----------- no MIT_LSV ----------- */
  #define PROGRAM_NAME    "GLEAM/ParPop"
  #if defined(ONLINE) && defined(EXT_SIMU_SERV)
    #define EXE_NAME        "gleamParPopEssO_CLV"       /* Produktionsversion */
  #else /* no ONLINE and no ESS */
    #define EXE_NAME        "gleamParPopCLV"            /* Testversion        */
  #endif /* no ONLINE */
#endif /* no MIT_LSV */


/* ----------------------------- User-Interface: ---------------------------- */
#if defined(GLEAM_USR)
  #define TEXT_MODE          /* textuelle Benutzerschnittstelle }     alter-  */
  #undef GUI_MODE           /* grafische Benutzerschnittstelle }     nativ   */
  #define GNUPLOT            /* Anschluss von GNUPLOT                         */
#else /* ------------------- kein GLEAM_USR ------------------- */
  #undef TEXT_MODE           /* textuelle Benutzerschnittstelle               */
  #undef GUI_MODE            /* grafische Benutzerschnittstelle               */
  #undef GNUPLOT             /* Anschluss von GNUPLOT                         */
#endif /* kein GLEAM_USR */


/* -------------------- Globaler Schalter fuer Testcode: -------------------- */
#define GLO_CHECKS           /* Art und Umfang wird in den Packages bestimmt. */

/* -------------------- Schalter fuer Statistik-Logfile: -------------------- */
#define MIT_OP_STATISTIK         /* Gesetzt: Mit Statistik der gen.Operatoren */
#define LOGFILE_MODE      "at"   /* Schreiben und anhaengen                   */

/* --------------------------- Simulator-Schalter: -------------------------- */
#define MITSIMULOG           /* Ein Simu-Testlogfile wird angelegt.           */
#define SIMU_LOGF_MODE  "wt" /* Schreiben und neu anlegen                     */
#undef SIMU_DBG             /* Debug-Ausgaben ins Scrollfenster u. Log-Datei */

/* -------------------- Namen der Environment-Variable: --------------------- */
#define SIM_MOD_ROOT_ENV     "SIM_MOD_ROOT"
#define GLEAM_ROOT_ENV       "GLEAM_ROOT"



/* ========================================================================== */
/* ------------------------- abgeleitete Schalter: -------------------------- */
#ifdef MIT_LSV
  #define MIT_ROCO           /* Gesetzt: mit Rosenbrock/Complex vom ITEM      */
#else
  #undef MIT_ROCO            /* Kein LSV, dann auch kein MIT_ROCO !           */
#endif /* MIT_LSV */


/* ----------------------------- Solaris/Linux: ----------------------------- */
#if defined(BS_SOLARIS) || defined(BS_LINUX)
  #define DIR_DELIMITER      "/"
  #define MAX_POPEL_SIZE   10000     /* Maximale Anzahl der Individuen        */
  #define MIN_ANZ_NACHBARN     2     /* min.Nachbarschaftsgroesse (gerade)    */
  #define MAX_ANZ_NACHBARN    32     /* max.Nachbarschaftsgroesse (gerade)    */
				     /* MUSS <= 65535 / PRECISION sein        */
  #define MAX_EVO_WS           2     /* Groesse der Opt-Job-Liste; nur CLV!   */
#endif /* BS_SOLARIS oder Linux */


/* ------------------------------ Sprachfiles: ------------------------------ */
#ifdef DEU        /* ----------------------- Deutsch: ----------------------- */
  #define FTEXTE_FNAME       "ftext_d.txt"
  #define ADD_FTEXTE_FNAME   ""                 // opftxt_d.txt
  #ifdef MPI_PAR_VERS            
    #define FTEXTEPAR_FNAME  "ftext_mpi_d.txt"
  #endif /* MPI_PAR_VERS */
  #define GLOBAL_TEXTS       "glob_d.h"
  #define SYS_TEXTS          "sys_d.h"
  #define WEBIO_TEXTS        "webIO_d.h"
  #define CTIO_TEXTS         "ctio_d.h"
  #define FBHM_TEXTS         "fbhm_d.h"
  #define LGSW_TEXTS         "lgsw_d.h"
  #define CHIO_TEXTS         "chio_d.h"
  #define BEW_TEXTS          "bew_d.h"
  #define AUFG_TEXTS         "aufg_d.h"
  #define HMOD_TEXTS         "hmod_d.h"
  #define CHED_TEXTS         "ched_d.h"
  #define SIMU_TEXTS         "simu_d.h"
  #define EVO_TEXTS          "evo_d.h"
  #define APPL_TEXTS         "appl_d.h"
  #define MEN_TEXTS          "men_d.h"
  #define VERS_TEXTS         "vers_d.h"
  #define MAIN_TEXTS         "gleam_d.h"
  #define USR_MAIN_TEXTS     "gleam_usr_d.h"
  #define CLV_MAIN_TEXTS     "gleamCLV_d.h"
  #define ANW_TEXTS          "opal_d.h"
  #ifdef MPI_PAR_VERS
    #define PCOM_TEXTS       "pcom_d.h"
    #define MPAR_TEXTS       "mpar_d.h"
    #define PAR_MEN_TEXTS    "par_men_d.h"
  #endif /* MPI_PAR_VERS*/
  #ifdef PAR_POP_VERS
    #define PAR_POP_TEXTS    "parPop_d.h"
  #endif /* PAR_POP_VERS */
#endif /* DEU */
#ifdef ENG        /* ----------------------- Englisch: ---------------------- */
  #define FTEXTE_FNAME       "ftext_gb.txt"
  #define ADD_FTEXTE_FNAME   ""                   //opftxtgb.txt
  #ifdef MPI_MPI_PAR_VERS            
    #define FTEXTEPAR_FNAME  "ftext_mpi_gb.txt"
  #endif /* MPI_PAR_VERS */
  #define GLOBAL_TEXTS       "glob_gb.h"
  #define SYS_TEXTS          "sys_gb.h"
  #define WEBIO_TEXTS        "webIO_gb.h"
  #define CTIO_TEXTS         "ctio_gb.h"
  #define FBHM_TEXTS         "fbhm_gb.h"
  #define LGSW_TEXTS         "lgsw_gb.h"
  #define CHIO_TEXTS         "chio_gb.h"
  #define BEW_TEXTS          "bew_gb.h"
  #define AUFG_TEXTS         "aufg_gb.h"
  #define HMOD_TEXTS         "hmod_gb.h"
  #define CHED_TEXTS         "ched_gb.h"
  #define SIMU_TEXTS         "simu_gb.h"
  #define EVO_TEXTS          "evo_gb.h"
  #define APPL_TEXTS         "appl_gb.h"
  #define MEN_TEXTS          "men_gb.h"
  #define VERS_TEXTS         "vers_gb.h"
  #define MAIN_TEXTS         "gleam_gb.h"
  #define USR_MAIN_TEXTS     "gleam_usr_gb.h"
  #define CLV_MAIN_TEXTS     "gleamCLV_gb.h"
  #define ANW_TEXTS          "opal_gb.h"
  #ifdef MPI_PAR_VERS
    #define PCOM_TEXTS       "pcom_gb.h"
    #define MPAR_TEXTS       "mpar_gb.h"
    #define PAR_MEN_TEXTS    "par_men_gb.h"
  #endif /* MPI_PAR_VERS */
  #ifdef PAR_POP_VERS
    #define PAR_POP_TEXTS    "parPop_gb.h"
  #endif /* PAR_POP_VERS */
#endif /* ENG */


/* -------------------- File-Namen und MOD-File-Kennung: -------------------- */
#define PROG_IDENT         "GLEAM/AE"
#define EXP_DEFAULT_FNAME  "default.exp"
#define TOP_RING_DEF_FNAME "symRing.top" /* Gross/Kleinschreibung unerheblich */
#define VERS_DOKU_FNAME    "versdoku.txt"
#define LOGFILE_NAME       "gleam_ae.log"
#define SIMU_LOGFILE_NAME  "simu_tst.log"
#define EVO_ABBRUCH_FSPEC  "evo_stop.tmp"

/* --- Allg. Inkludierungen: ttype.h, zs_dos.h bzw. szsunsol.h und tcl.h: --- */
#include "ttype.h"
#if defined(BS_SOLARIS) || defined(BS_LINUX)
#include "szsunsol.h"
#endif /* Solaris oder Linux */


/* -----------------------------------------------------------------------------
Copyright (c) 2016  Christian Blume, Wilfried Jakob

Das Urheberrecht (Copyright) fuer GLEAM (General Learning Evolutionary Algorithm 
and Method, frueher: Genetic Learning Algorithm and Method) liegt bei 
Prof. Dr. Christian Blume, FH Koeln.

Das Urheberrecht (Copyright) fuer HyGLEAM (Hybrid General purpose Evolutionary 
Algorithm and Method), einem auf GLEAM beruhenden Werk, liegt bei
Dr. Wilfried Jakob, Karlsruher Institut fuer Technologie (KIT).


Hiermit wird unentgeltlich jeder Person, die eine Kopie der Software und der 
zugehoerigen Dokumentationen (die "Software") erhaelt, die Erlaubnis erteilt, 
sie uneingeschraenkt zu nutzen, inklusive und ohne Ausnahme mit dem Recht, sie 
zu verwenden, zu kopieren, zu veraendern, zusammenzufuegen, zu veroeffentlichen, 
zu verbreiten, zu unterlizenzieren und/oder zu verkaufen, und Personen, denen 
diese Software ueberlassen wird, diese Rechte zu verschaffen, unter den folgenden 
Bedingungen:

Der obige Urheberrechtsvermerk und dieser Erlaubnisvermerk sind in allen Kopien 
oder Teilkopien der Software beizulegen.

In allen Publikationen, welche unter Verwendung der Software oder darauf 
beruhender Werke entstanden sind, ist auf die Urheber von GLEAM und gegebenen-
falls HyGLEAM hinzuweisen. Dies kann durch nachstehende Literaturverweise 
geschehen:

  C. Blume, W. Jakob:  GLEAM - ein Evolutionärer Algorithmus und seine 
  Anwendungen. KIT Scientific Publishing, Schriftenreihe des Instituts fuer 
  Angewandte Informatik / Automatisierungstechnik (AIA), Band 32, 
  ISBN 978-3-86644-436-2, 2009. 

  W. Jakob:  A general cost-benefit-based adaptation framework for multimeme 
  algorithms. Memetic Computing, 2(2010), S.201-218. 

DIE SOFTWARE WIRD OHNE JEDE AUSDRUECKLICHE ODER IMPLIZIERTE GARANTIE BEREIT-
GESTELLT, EINSCHLIESSLICH DER GARANTIE ZUR BENUTZUNG FUER DEN VORGESEHENEN ODER 
EINEM BESTIMMTEN ZWECK SOWIE JEGLICHER RECHTSVERLETZUNG, JEDOCH NICHT DARAUF 
BESCHRAENKT. IN KEINEM FALL SIND DIE AUTOREN ODER COPYRIGHTINHABER FUER 
JEGLICHEN SCHADEN ODER SONSTIGE ANSPRUECHE HAFTBAR ZU MACHEN, OB INFOLGE DER
ERFUELLUNG EINES VERTRAGES, EINES DELIKTES ODER ANDERS IM ZUSAMMENHANG MIT 
DER SOFTWARE ODER SONSTIGER VERWENDUNG DER SOFTWARE ENTSTANDEN.

--------------------------------------------------------------------------------
Copyright (c) 2016  Christian Blume, Wilfried Jakob

The copyright of GLEAM (General Learning Evolutionary Algorithm and Method, 
former: Genetic Learning Algorithm and Method) is owned by Prof. Dr. Christian 
Blume, University of Applied Sciences, Koeln.

The copyright of HyGLEAM (Hybrid General purpose Evolutionary Algorithm and 
Method),  a software based on GLEAM, is owned by Dr. Wilfried Jakob, Karlsruhe 
Institute of Technology (KIT).


Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the "Software"), to deal 
in the Software without restriction, including without limitation the rights 
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished 
to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

In all publications which refer to the Software or works based on it or to 
results obtained by the usage of the Software or works based on it,
a reference to the Author(s) of GLEAM and HyGLEAM, if applicable, shall be 
includeded. This can be done by the following references:

  Blume, C.: GLEAM - A System for Intuitive Learning. In: Schwefel, H.P., 
  Maenner, R. (eds.): Proc. of PPSN I, LNCS 496, Springer, Berlin, S.48-54, 1990.

  Blume, C., Jakob, W.: GLEAM - an Evolutionary Algorithm for Planning and 
  Control Based on Evolution Strategy. In: E. Cantú-Paz (ed.): GECCO 2002, Vol. 
  Late-Breaking Papers, L. Livermor National Laboratory. S.31-38, 2002.

  W. Jakob:  A general cost-benefit-based adaptation framework for multimeme 
  algorithms. Memetic Computing, 2(2010), S.201-218. 

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

----------------------------------------------------------------------------- */

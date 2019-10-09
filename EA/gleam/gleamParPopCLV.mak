#-------------------------------------------------------------------------------
#
# Makefile for Debian Linux 7 - GNU gcc V4.6.3 Compiler   MAKE Utility
#
# Aufruf: make -f gleamParPopCLV.mak [min_clean | clean | ]
#         clean:     L�scht das Executable und alle Objects in den beteiligten Dirs
#         min_clean: L�scht das Executable und alle Objects ausser LSKP, CEC und ROCO
#         <leer>:    Erzeugt das Executable
#
# Fuer: gleamParPopCLV
#
#-------------------------------------------------------------------------------
# This is free software, which is governed by the Lesser GNU Public Licence (LGPL),
# version 3, see the comment section at the end of the main program files or the
# files "schalter.h".
#-------------------------------------------+----------------------------------+
#   Autor: W.Jakob                          |   Stand: 08.06.2018              |
#-------------------------------------------+----------------------------------+
.SILENT:

#----------------------------- Wurzelverzeichnis: ------------------------------
ROOT = /home/phil/Desktop/root


# --------------------------------- Executable: --------------------------------
ziel = gleamParPopCLV

# --- Targets: ------
all: $(ziel)


#------------------------- Defintion des Compilers: ----------------------------
CC = gcc

#-------------------------- Compiler-Schalter f. g++: --------------------------
#dbg_flag = -g
dbg_flag =
#OptFlag     =
#OptFlag     = -O1
OptFlag     = -O2
ProfileFlag =
#ProfileFlag = -p
WarnFlag    = -Wimplicit-function-declaration -Wunused-function
#-------------------------------------------------------------------------------
CFLAGS   = $(WarnFlag) $(OptFlag) $(dbg_flag) $(ProfileFlag)
CPPFLAGS = -Wno-deprecated $(OptFlag) $(dbg_flag) $(ProfileFlag)
INCFLAGS = $(STD_INC_FLAGS) -I$(PAR_POP)
#-------------------------------------------------------------------------------


#--------------------------- Globales Include-File: ----------------------------
include glob_inc.mak


#-------------------------------------------------------------------------------
#      Definitionen der Objects:
#-------------------------------------------------------------------------------
ALLOBJ = $(BASIC_OBJ)  $(SIMU_CLV)  $(EVO_CLV)  $(ALLG)/hyGleamEngine2.o


#-------------------------------------------------------------------------------
#      Beschreibung der Abhaengigkeiten:
#-------------------------------------------------------------------------------
$(ALLOBJ):   $(MAIN)/schalter.h  $(SYS)/ttype.h     $(ALLG)/szsunsol.h


#-------------------------------------------------------------------------------
#             Linker - Pass:
#-------------------------------------------------------------------------------
$(ziel): $(ALLOBJ)
	echo =============================== Linking $@ =============================
	$(CC) $(CFLAGS) -o $@ $(ALLOBJ) -lm
	echo

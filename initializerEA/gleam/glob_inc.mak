#-------------------------------------------------------------------------------
#
# Makefile for Suse Linux 10.2 - GNU gcc V4.1.2 Compiler   MAKE Utility
#
# Globales Include-File fuer alle GLEAM/ParPop-Varianten mit und ohne Anbindung 
# an die externen Simulationsdienste (ESS) und paralleler Simulation:
#   - gleamParPopCLV      und hyGleamParPopCLV        Testversionen ohne ESS
#   - gleamParPopEssO_CLV und hyGleamParPopEssO_CLV   Produktionsversion mit ESS
# Nur als Kommandozeilenversionen (CLV).
#
#-------------------------------------------------------------------------------
# This is free software, which is governed by the Lesser GNU Public Licence (LGPL), 
# version 3, see the comment section at the end of the main program files or the 
# files "schalter.h".
#-------------------------------------------+----------------------------------+
#   Autor: W.Jakob                          |   Stand: 11.06.2018              |
#-------------------------------------------+----------------------------------+

#-------------------------- Directory-Vereinbarungen: --------------------------
ALLG  = $(ROOT)/sources
MAIN  = $(ROOT)/debian/islandGleam


#------------------- Include allgemeiner GLEAM-Definitionen: -------------------
include $(ALLG)/common_def.mak


#----------------- Abhaengigkeiten der spezifischen Packages: ------------------
include $(FBHM)/fbhm_inc.mak
include $(CHIO)/chio_inc.mak
include $(AUFG)/aufg_inc.mak
include $(SIMU)/simu_inc.mak
include $(EVO)/evo_inc.mak
include $(MEN)/men_inc.mak

include $(WEBIO)/webIO_inc.mak
include $(PAR_POP)/parPop_inc.mak
#include $(ROCO)/roco_inc.mak


#-------------------------------------------------------------------------------
#      Definitionen der grundlegenden Objectgruppen:
#-------------------------------------------------------------------------------
BASIC_OBJ = $(SYS_OBJ)   $(CTIO_CLV)    $(FBHM_OBJ)  $(LGSW_OBJ)  $(CHIO_OBJ)  \
            $(HMOD_OBJ)  $(BEW_CLV)     $(AUFG_CLV)  $(APPL_OBJ)  $(LSKP_CLV)  \
            $(MEN_CLV)   $(PAR_POP_OBJ)



#-------------------------------------------------------------------------------
#		Aufraeumen:
#-------------------------------------------------------------------------------
min_clean:
	echo rm -f $(ALLG)/*.o $(ziel)
	rm      -f $(ALLG)/*.o $(ziel)
	echo rm -f $(SYS)/*.o
	rm      -f $(SYS)/*.o
	echo rm -f $(WEBIO)/*.o
	rm      -f $(WEBIO)/*.o
	echo rm -f $(CTIO)/*.o
	rm      -f $(CTIO)/*.o
	echo rm -f $(FBHM)/*.o
	rm      -f $(FBHM)/*.o
	echo rm -f $(LGSW)/*.o
	rm      -f $(LGSW)/*.o
	echo rm -f $(CHIO)/*.o
	rm      -f $(CHIO)/*.o
	echo rm -f $(BEW)/*.o
	rm      -f $(BEW)/*.o
	echo rm -f $(AUFG)/*.o
	rm      -f $(AUFG)/*.o
	echo rm -f $(APPL)/*.o
	rm      -f $(APPL)/*.o
	echo rm -f $(HMOD)/*.o
	rm      -f $(HMOD)/*.o
	echo rm -f $(CHED)/*.o
	rm      -f $(CHED)/*.o
	echo rm -f $(SIMU)/*.o
	rm      -f $(SIMU)/*.o
	echo rm -f $(EVO)/*.o
	rm      -f $(EVO)/*.o
	echo rm -f $(MEN)/*.o
	rm      -f $(MEN)/*.o

clean:
	echo rm -f $(ALLG)/*.o $(ziel)
	rm      -f $(ALLG)/*.o $(ziel)
	echo rm -f $(SYS)/*.o
	rm      -f $(SYS)/*.o
	echo rm -f $(WEBIO)/*.o
	rm      -f $(WEBIO)/*.o
	echo rm -f $(CTIO)/*.o
	rm      -f $(CTIO)/*.o
	echo rm -f $(FBHM)/*.o
	rm      -f $(FBHM)/*.o
	echo rm -f $(LGSW)/*.o
	rm      -f $(LGSW)/*.o
	echo rm -f $(CHIO)/*.o
	rm      -f $(CHIO)/*.o
	echo rm -f $(BEW)/*.o
	rm      -f $(BEW)/*.o
	echo rm -f $(AUFG)/*.o
	rm      -f $(AUFG)/*.o
	echo rm -f $(APPL)/*.o
	rm      -f $(APPL)/*.o
	echo rm -f $(HMOD)/*.o
	rm      -f $(HMOD)/*.o
	echo rm -f $(CHED)/*.o
	rm      -f $(CHED)/*.o
	echo rm -f $(SIMU)/*.o
	rm      -f $(SIMU)/*.o
	echo rm -f $(LSKP)/*.o
	rm      -f $(LSKP)/*.o
	echo rm -f $(CEC)/*.o
	rm      -f $(CEC)/*.o
	echo rm -f $(EVO)/*.o
	rm      -f $(EVO)/*.o
	echo rm -f $(MEN)/*.o
	rm      -f $(MEN)/*.o
	echo rm -f $(ROCO)/*.o
	rm      -f $(ROCO)/*.o


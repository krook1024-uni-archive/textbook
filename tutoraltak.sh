#!/usr/bin/env bash

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

printf "${GREEN}-> ${RED}Tutoráltak száma:${NC} $(grep 'tutoráltam' tb-feladatok* | wc -l) \n"
printf "${GREEN}-> ${RED}Engem tutoráltak:${NC} $(grep 'tutorált ' tb-feladatok* | wc -l) \n"

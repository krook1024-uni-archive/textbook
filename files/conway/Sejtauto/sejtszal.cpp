// sejtszal.cpp
//
// Eletjatek rajzolo
// Programozo Paternoszter
//
// Copyright (C) 2011, Batfai Norbert, nbatfai@inf.unideb.hu, nbatfai@gmail.com
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// Ez a program szabad szoftver; terjesztheto illetve modosithato a
// Free Software Foundation altal kiadott GNU General Public License
// dokumentumaban leirtak; akar a licenc 3-as, akar (tetszoleges) kesobbi
// valtozata szerint.
//
// Ez a program abban a remenyben kerul kozreadasra, hogy hasznos lesz,
// de minden egyeb GARANCIA NELKUL, az ELADHATOSAGRA vagy VALAMELY CELRA
// VALO ALKALMAZHATOSAGRA valo szarmaztatott garanciat is beleertve.
// Tovabbi reszleteket a GNU General Public License tartalmaz.
//
// A felhasznalonak a programmal egyutt meg kell kapnia a GNU General
// Public License egy peldanyat; ha megsem kapta meg, akkor
// tekintse meg a <http://www.gnu.org/licenses/> oldalon.
//
//
// Version history:
//
// 0.0.1    A ket osztaly tervezesenek fo szempontja az volt, hogy
// ne vagy alig kulonbozzon az elso C++-os peldatol, a Mandelostol:
// http://progpater.blog.hu/2011/02/26/tan_csodallak_amde_nem_ertelek_de_kepzetem_hegyvolgyedet_bejarja
// ezert az olyan kenyesebb dolgokkal, hogy kezeljuk a racsIndex-et a
// ket osztalyra bontott C++ megoldasban, amikor irjuk at a Javasbol, nem foglalkoztunk
// a kiindulo Javas: http://www.tankonyvtar.hu/informatika/javat-tanitok-1-2-080904-1
// (a bazar eszme: ?Release Early, Release Often" irjuk ki a posztra)
//

#include "sejtszal.h"

SejtSzal::SejtSzal(bool ***racsok, int szelesseg, int magassag, int varakozas, SejtAblak *sejtAblak)
{
    this->racsok = racsok;
    this->szelesseg = szelesseg;
    this->magassag = magassag;
    this->varakozas = varakozas;
    this->sejtAblak = sejtAblak;

    racsIndex = 0;
}

/**
   * Az kerdezett allapotban levo nyolcszomszedok szama.
   *
   * @param   racs    a sejtter racs
   * @param   sor     a racs vizsgalt sora
   * @param   oszlop  a racs vizsgalt oszlopa
   * @param   allapor a nyolcszomszedok vizsgalt allapota
   * @return int a kerdezett allapotbeli nyolcszomszedok szama.
   */
int SejtSzal::szomszedokSzama(bool **racs,
                              int sor, int oszlop, bool allapot) {
    int allapotuSzomszed = 0;
    // A nyolcszomszedok vegigzongorazasa:
    for(int i=-1; i<2; ++i)
        for(int j=-1; j<2; ++j)
            // A vizsgalt sejtet magat kihagyva:
            if(!((i==0) && (j==0))) {
        // A sejtterbol szelenek szomszedai
        // a szembe oldalakon ("periodikus hatarfeltetel")
        int o = oszlop + j;
        if(o < 0)
            o = szelesseg-1;
        else if(o >= szelesseg)
            o = 0;

        int s = sor + i;
        if(s < 0)
            s = magassag-1;
        else if(s >= magassag)
            s = 0;

        if(racs[s][o] == allapot)
            ++allapotuSzomszed;
    }

    return allapotuSzomszed;
}

/**
 * A sejtter idobeli fejlodese a John H. Conway fele
 * eletjatek sejtautomata szabalyai alapjan tortenik.
 * A szabalyok reszletes ismerteteset lasd peldaul a
 * [MATEK JATEK] hivatkozasban (Csakany Bela: Diszkret
 * matematikai jatekok. Polygon, Szeged 1998. 171. oldal.)
 */
void SejtSzal::idoFejlodes() {

    bool **racsElotte = racsok[racsIndex];
    bool **racsUtana = racsok[(racsIndex+1)%2];

    for(int i=0; i<magassag; ++i) { // sorok
        for(int j=0; j<szelesseg; ++j) { // oszlopok

            int elok = szomszedokSzama(racsElotte, i, j, SejtAblak::ELO);

            if(racsElotte[i][j] == SejtAblak::ELO) {
                /* Elo elo marad, ha ketto vagy harom elo
             szomszedja van, kulonben halott lesz. */
                if(elok==2 || elok==3)
                    racsUtana[i][j] = SejtAblak::ELO;
                else
                    racsUtana[i][j] = SejtAblak::HALOTT;
            }  else {
                /* Halott halott marad, ha harom elo
             szomszedja van, kulonben elo lesz. */
                if(elok==3)
                    racsUtana[i][j] = SejtAblak::ELO;
                else
                    racsUtana[i][j] = SejtAblak::HALOTT;
            }
        }
    }
    racsIndex = (racsIndex+1)%2;
}


/** A sejtter idobeli fejlodese. */
void SejtSzal::run()
{
    while(true) {
        QThread::msleep(varakozas);
        idoFejlodes();
        sejtAblak->vissza(racsIndex);
    }

}

SejtSzal::~SejtSzal()
{
}

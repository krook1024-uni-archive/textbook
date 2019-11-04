// sejtablak.cpp
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

#include "sejtablak.h"

SejtAblak::SejtAblak(int szelesseg, int magassag, QWidget *parent)
: QMainWindow(parent)
{
  setWindowTitle("A John Horton Conway-fele eletjatek");

  this->magassag = magassag;
  this->szelesseg = szelesseg;


  cellaSzelesseg = 6;
  cellaMagassag = 6;

  setFixedSize(QSize(szelesseg*cellaSzelesseg, magassag*cellaMagassag));

  racsok = new bool**[2];
  racsok[0] = new bool*[magassag];
  for(int i=0; i<magassag; ++i)
    racsok[0][i] = new bool [szelesseg];
  racsok[1] = new bool*[magassag];
  for(int i=0; i<magassag; ++i)
    racsok[1][i] = new bool [szelesseg];

  racsIndex = 0;
  racs = racsok[racsIndex];

  // A kiindulo racs minden cellaja HALOTT
  for(int i=0; i<magassag; ++i)
    for(int j=0; j<szelesseg; ++j)
      racs[i][j] = HALOTT;
    // A kiindulo racsra "ELOlenyeket" helyezunk
    //siklo(racs, 2, 2);

    sikloKilovo(racs, 5, 60);

  eletjatek = new SejtSzal(racsok, szelesseg, magassag, 120, this);

  eletjatek->start();

}

void SejtAblak::paintEvent(QPaintEvent*) {
  QPainter qpainter(this);

  // Az aktualis
  bool **racs = racsok[racsIndex];
  // racsot rajzoljuk ki:
  for(int i=0; i<magassag; ++i) { // vegig lepked a sorokon
    for(int j=0; j<szelesseg; ++j) { // s az oszlopok
      // Sejt cella kirajzolasa
      if(racs[i][j] == ELO)
	qpainter.fillRect(j*cellaSzelesseg, i*cellaMagassag,
			  cellaSzelesseg, cellaMagassag, Qt::black);
	else
	  qpainter.fillRect(j*cellaSzelesseg, i*cellaMagassag,
			    cellaSzelesseg, cellaMagassag, Qt::white);
	  qpainter.setPen(QPen(Qt::gray, 1));

	qpainter.drawRect(j*cellaSzelesseg, i*cellaMagassag,
			  cellaSzelesseg, cellaMagassag);
    }
  }

  qpainter.end();
}


SejtAblak::~SejtAblak()
{
  delete eletjatek;

  for(int i=0; i<magassag; ++i) {
    delete[] racsok[0][i];
    delete[] racsok[1][i];
  }

  delete[] racsok[0];
  delete[] racsok[1];
  delete[] racsok;


}

void SejtAblak::vissza(int racsIndex)
{
  this->racsIndex = racsIndex;
  update();
}

/**
 * A sejtterbe "ELOlenyeket" helyezunk, ez a "siklo".
 * Adott iranyban halad, masolja magat a sejtterben.
 * Az ELOleny ismerteteset lasd peldaul a
 * [MATEK JATEK] hivatkozasban (Csakany Bela: Diszkret
 * matematikai jatekok. Polygon, Szeged 1998. 172. oldal.)
 *
 * @param   racs    a sejtter ahova ezt az allatkat helyezzuk
 * @param   x       a befoglalo tegla bal felso sarkanak oszlopa
 * @param   y       a befoglalo tegla bal felso sarkanak sora
 */
void SejtAblak::siklo(bool **racs, int x, int y) {

  racs[y+ 0][x+ 2] = ELO;
  racs[y+ 1][x+ 1] = ELO;
  racs[y+ 2][x+ 1] = ELO;
  racs[y+ 2][x+ 2] = ELO;
  racs[y+ 2][x+ 3] = ELO;

}
/**
 * A sejtterbe "ELOlenyeket" helyezunk, ez a "siklo agyu".
 * Adott iranyban siklokat lo ki.
 * Az ELOleny ismerteteset lasd peldaul a
 * [MATEK JATEK] hivatkozasban /Csakany Bela: Diszkret
 * matematikai jatekok. Polygon, Szeged 1998. 173. oldal./,
 * de itt az abra hibas, egy oszloppal told meg balra a
 * bal oldali 4 sejtes negyzetet. A helyes agyu rajzat
 * lasd pl. az [ELET CIKK] hivatkozasban /Robert T.
 * Wainwright: Life is Universal./ (Megemlithetjuk, hogy
 * mindketto tartalmaz ket felesleges sejtet is.)
 *
 * @param   racs    a sejtter ahova ezt az allatkat helyezzuk
 * @param   x       a befoglalo tegla bal felso sarkanak oszlopa
 * @param   y       a befoglalo tegla bal felso sarkanak sora
 */
void SejtAblak::sikloKilovo(bool **racs, int x, int y) {

  racs[y+ 6][x+ 0] = ELO;
  racs[y+ 6][x+ 1] = ELO;
  racs[y+ 7][x+ 0] = ELO;
  racs[y+ 7][x+ 1] = ELO;

  racs[y+ 3][x+ 13] = ELO;

  racs[y+ 4][x+ 12] = ELO;
  racs[y+ 4][x+ 14] = ELO;

  racs[y+ 5][x+ 11] = ELO;
  racs[y+ 5][x+ 15] = ELO;
  racs[y+ 5][x+ 16] = ELO;
  racs[y+ 5][x+ 25] = ELO;

  racs[y+ 6][x+ 11] = ELO;
  racs[y+ 6][x+ 15] = ELO;
  racs[y+ 6][x+ 16] = ELO;
  racs[y+ 6][x+ 22] = ELO;
  racs[y+ 6][x+ 23] = ELO;
  racs[y+ 6][x+ 24] = ELO;
  racs[y+ 6][x+ 25] = ELO;

  racs[y+ 7][x+ 11] = ELO;
  racs[y+ 7][x+ 15] = ELO;
  racs[y+ 7][x+ 16] = ELO;
  racs[y+ 7][x+ 21] = ELO;
  racs[y+ 7][x+ 22] = ELO;
  racs[y+ 7][x+ 23] = ELO;
  racs[y+ 7][x+ 24] = ELO;

  racs[y+ 8][x+ 12] = ELO;
  racs[y+ 8][x+ 14] = ELO;
  racs[y+ 8][x+ 21] = ELO;
  racs[y+ 8][x+ 24] = ELO;
  racs[y+ 8][x+ 34] = ELO;
  racs[y+ 8][x+ 35] = ELO;

  racs[y+ 9][x+ 13] = ELO;
  racs[y+ 9][x+ 21] = ELO;
  racs[y+ 9][x+ 22] = ELO;
  racs[y+ 9][x+ 23] = ELO;
  racs[y+ 9][x+ 24] = ELO;
  racs[y+ 9][x+ 34] = ELO;
  racs[y+ 9][x+ 35] = ELO;

  racs[y+ 10][x+ 22] = ELO;
  racs[y+ 10][x+ 23] = ELO;
  racs[y+ 10][x+ 24] = ELO;
  racs[y+ 10][x+ 25] = ELO;

  racs[y+ 11][x+ 25] = ELO;

}

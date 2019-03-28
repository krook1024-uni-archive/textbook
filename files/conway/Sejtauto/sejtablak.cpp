// sejtablak.cpp
//
// Életjáték rajzoló
// Programozó Páternoszter
//
// Copyright (C) 2011, Bátfai Norbert, nbatfai@inf.unideb.hu, nbatfai@gmail.com
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
// Ez a program szabad szoftver; terjeszthetõ illetve módosítható a
// Free Software Foundation által kiadott GNU General Public License
// dokumentumában leírtak; akár a licenc 3-as, akár (tetszõleges) késõbbi
// változata szerint.
//
// Ez a program abban a reményben kerül közreadásra, hogy hasznos lesz,
// de minden egyéb GARANCIA NÉLKÜL, az ELADHATÓSÁGRA vagy VALAMELY CÉLRA
// VALÓ ALKALMAZHATÓSÁGRA való származtatott garanciát is beleértve.
// További részleteket a GNU General Public License tartalmaz.
//
// A felhasználónak a programmal együtt meg kell kapnia a GNU General
// Public License egy példányát; ha mégsem kapta meg, akkor
// tekintse meg a <http://www.gnu.org/licenses/> oldalon.
//
//
// Version history:
//
// 0.0.1    A két osztály tervezésének fõ szempontja az volt, hogy
// ne vagy alig különbözzön az elsõ C++-os példától, a Mandelostól:
// http://progpater.blog.hu/2011/02/26/tan_csodallak_amde_nem_ertelek_de_kepzetem_hegyvolgyedet_bejarja
// ezért az olyan kényesebb dolgokkal, hogy kezeljük a racsIndex-et a
// két osztályra bontott C++ megoldásban, amikor írjuk át a Javásból, nem foglalkoztunk
// a kiinduló Javás: http://www.tankonyvtar.hu/informatika/javat-tanitok-1-2-080904-1
// (a bazár eszme: „Release Early, Release Often" írjuk ki a posztra)
//

#include "sejtablak.h"

SejtAblak::SejtAblak(int szelesseg, int magassag, QWidget *parent)
: QMainWindow(parent)
{
  setWindowTitle("A John Horton Conway-féle életjáték");
  
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

  // A kiinduló racs minden cellája HALOTT
  for(int i=0; i<magassag; ++i)
    for(int j=0; j<szelesseg; ++j)
      racs[i][j] = HALOTT;
    // A kiinduló racsra "ELOlényeket" helyezünk
    //siklo(racs, 2, 2);

    sikloKilovo(racs, 5, 60);

  eletjatek = new SejtSzal(racsok, szelesseg, magassag, 120, this);

  eletjatek->start();
  
}

void SejtAblak::paintEvent(QPaintEvent*) {
  QPainter qpainter(this);
  
  // Az aktuális
  bool **racs = racsok[racsIndex];
  // racsot rajzoljuk ki:
  for(int i=0; i<magassag; ++i) { // végig lépked a sorokon
    for(int j=0; j<szelesseg; ++j) { // s az oszlopok
      // Sejt cella kirajzolása
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
 * A sejttérbe "ELOlényeket" helyezünk, ez a "sikló".
 * Adott irányban halad, másolja magát a sejttérben.
 * Az ELOlény ismertetését lásd például a
 * [MATEK JÁTÉK] hivatkozásban (Csákány Béla: Diszkrét
 * matematikai játékok. Polygon, Szeged 1998. 172. oldal.)
 *
 * @param   racs    a sejttér ahová ezt az állatkát helyezzük
 * @param   x       a befoglaló tégla bal felsõ sarkának oszlopa
 * @param   y       a befoglaló tégla bal felsõ sarkának sora
 */
void SejtAblak::siklo(bool **racs, int x, int y) {
  
  racs[y+ 0][x+ 2] = ELO;
  racs[y+ 1][x+ 1] = ELO;
  racs[y+ 2][x+ 1] = ELO;
  racs[y+ 2][x+ 2] = ELO;
  racs[y+ 2][x+ 3] = ELO;
  
}
/**
 * A sejttérbe "ELOlényeket" helyezünk, ez a "sikló ágyú".
 * Adott irányban siklókat lõ ki.
 * Az ELOlény ismertetését lásd például a
 * [MATEK JÁTÉK] hivatkozásban /Csákány Béla: Diszkrét
 * matematikai játékok. Polygon, Szeged 1998. 173. oldal./,
 * de itt az ábra hibás, egy oszloppal told még balra a
 * bal oldali 4 sejtes négyzetet. A helyes ágyú rajzát
 * lásd pl. az [ÉLET CIKK] hivatkozásban /Robert T.
 * Wainwright: Life is Universal./ (Megemlíthetjük, hogy
 * mindkettõ tartalmaz két felesleges sejtet is.)
 *
 * @param   racs    a sejttér ahová ezt az állatkát helyezzük
 * @param   x       a befoglaló tégla bal felsõ sarkának oszlopa
 * @param   y       a befoglaló tégla bal felsõ sarkának sora
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

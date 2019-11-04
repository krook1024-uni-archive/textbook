#ifndef SEJTABLAK_H
#define SEJTABLAK_H

#include <QMainWindow>
#include <QPainter>
#include "sejtszal.h"

class SejtSzal;

class SejtAblak : public QMainWindow
{
  Q_OBJECT

public:
  SejtAblak(int szelesseg = 100, int magassag = 75, QWidget *parent = 0);
  ~SejtAblak();
  // Egy sejt lehet elo
  static const bool ELO = true;
  // vagy halott
  static const bool HALOTT = false;
  void vissza(int racsIndex);

protected:
  // Ket racsot hasznalunk majd, az egyik a sejtter allapotat
  // a t_n, a masik a t_n+1 idopillanatban jellemzi.
  bool ***racsok;
  // Valamelyik racsra mutat, technikai jellegu, hogy ne kelljen a
  // [2][][]-bol az elso dimenziot hasznalni, mert vagy az egyikre
  // allitjuk, vagy a masikra.
  bool **racs;
  // Megmutatja melyik racs az aktualis: [racsIndex][][]
  int racsIndex;
  // Pixelben egy cella adatai.
  int cellaSzelesseg;
  int cellaMagassag;
  // A sejtter nagysaga, azaz hanyszor hany cella van?
  int szelesseg;
  int magassag;
  void paintEvent(QPaintEvent*);
  void siklo(bool **racs, int x, int y);
  void sikloKilovo(bool **racs, int x, int y);

private:
  SejtSzal* eletjatek;

};

#endif // SEJTABLAK_H

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
  // Egy sejt lehet élõ
  static const bool ELO = true;
  // vagy halott
  static const bool HALOTT = false;
  void vissza(int racsIndex);
  
protected:
  // Két rácsot használunk majd, az egyik a sejttér állapotát
  // a t_n, a másik a t_n+1 idõpillanatban jellemzi.
  bool ***racsok;
  // Valamelyik rácsra mutat, technikai jellegû, hogy ne kelljen a
  // [2][][]-ból az elsõ dimenziót használni, mert vagy az egyikre
  // állítjuk, vagy a másikra.
  bool **racs;
  // Megmutatja melyik rács az aktuális: [rácsIndex][][]
  int racsIndex;
  // Pixelben egy cella adatai.
  int cellaSzelesseg;
  int cellaMagassag;
  // A sejttér nagysága, azaz hányszor hány cella van?
  int szelesseg;
  int magassag;    
  void paintEvent(QPaintEvent*);
  void siklo(bool **racs, int x, int y);
  void sikloKilovo(bool **racs, int x, int y);
  
private:
  SejtSzal* eletjatek;
  
};

#endif // SEJTABLAK_H

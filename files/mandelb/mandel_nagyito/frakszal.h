#ifndef FRAKSZAL_H
#define FRAKSZAL_H

#include <QThread>
#include <math.h>
#include "frakablak.h"

class FrakAblak;

class FrakSzal : public QThread
{
    Q_OBJECT

public:
    FrakSzal(double a, double b, double c, double d,
             int szelesseg, int magassag, int iteraciosHatar, FrakAblak *frakAblak);
    ~FrakSzal();
    void run();

protected:
    // A komplex sik vizsgalt tartomanya [a,b]x[c,d].
    double a, b, c, d;
    // A komplex sik vizsgalt tartomanyara feszitett
    // halo szelessege es magassaga.
    int szelesseg, magassag;
    // Max. hany lepesig vizsgaljuk a z_{n+1} = z_n * z_n + c iteraciot?
    // (tk. most a nagyitasi pontossag)
    int iteraciosHatar;
    // Kinek szamolok?
    FrakAblak* frakAblak;
    // Soronkent kuldom is neki vissza a kiszamoltakat.
    int* egySor;

};

#endif // FRAKSZAL_H

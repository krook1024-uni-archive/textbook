#ifndef FRAKABLAK_H
#define FRAKABLAK_H

#include <QMainWindow>
#include <QImage>
#include <QPainter>
#include <QMouseEvent>
#include <QKeyEvent>
#include "frakszal.h"

class FrakSzal;

class FrakAblak : public QMainWindow
{
    Q_OBJECT

public:
    FrakAblak(double a = -2.0, double b = .7, double c = -1.35,
              double d = 1.35, int szelesseg = 600,
              int iteraciosHatar = 255, QWidget *parent = 0);
    ~FrakAblak();
    void vissza(int magassag , int * sor, int meret) ;
    void vissza(void) ;
    // A komplex sik vizsgalt tartomanya [a,b]x[c,d].
    double a, b, c, d;
    // A komplex sik vizsgalt tartomanyara feszitett
    // halo szelessege es magassaga.
    int szelesseg, magassag;
    // Max. hany lepesig vizsgaljuk a z_{n+1} = z_n * z_n + c iteraciot?
    // (tk. most a nagyitasi pontossag)
    int iteraciosHatar;

protected:
    void paintEvent(QPaintEvent*);
    void mousePressEvent(QMouseEvent*);
    void mouseMoveEvent(QMouseEvent*);
    void mouseReleaseEvent(QMouseEvent*);
    void keyPressEvent(QKeyEvent*);

private:
    QImage* fraktal;
    FrakSzal* mandelbrot;
    bool szamitasFut;
    // A nagyitando kijelolt teruletet bal felso sarka.
    int x, y;
    // A nagyitando kijelolt terulet szelessege es magassaga.
    int mx, my;
};

#endif // FRAKABLAK_H

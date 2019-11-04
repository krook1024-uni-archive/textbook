// frakszal.cpp
//
// Mandelbrot halmaz rajzolo


#include "frakszal.h"

FrakSzal::FrakSzal(double a, double b, double c, double d,
                   int szelesseg, int magassag, int iteraciosHatar, FrakAblak *frakAblak)
{
    this->a = a;
    this->b = b;
    this->c = c;
    this->d = d;
    this->szelesseg = szelesseg;
    this->iteraciosHatar = iteraciosHatar;
    this->frakAblak = frakAblak;
    this->magassag = magassag;

    egySor = new int[szelesseg];
}

FrakSzal::~FrakSzal()
{
    delete[] egySor;
}

void FrakSzal::run()
{
    // A [a,b]x[c,d] tartomanyon milyen suru a
    // megadott szelesseg, magassag halo:
    double dx = (b-a)/szelesseg;
    double dy = (d-c)/magassag;
    double reC, imC, reZ, imZ, ujreZ, ujimZ;
    // Hany iteraciot csinaltunk?
    int iteracio = 0;
    // Vegigzongorazzuk a szelesseg x magassag halot:
    for(int j=0; j<magassag; ++j) {
        //sor = j;
        for(int k=0; k<szelesseg; ++k) {
            // c = (reC, imC) a halo racspontjainak
            // megfelelo komplex szam
            reC = a+k*dx;
            imC = d-j*dy;
            // z_0 = 0 = (reZ, imZ)
            reZ = 0;
            imZ = 0;
            iteracio = 0;
            // z_{n+1} = z_n * z_n + c iteraciok
            // szamitasa, amig |z_n| < 2 vagy meg
            // nem ertuk el a 255 iteraciot, ha
            // viszont elertuk, akkor ugy vesszuk,
            // hogy a kiindulaci c komplex szamra
            // az iteracio konvergens, azaz a c a
            // Mandelbrot halmaz eleme
            while(reZ*reZ + imZ*imZ < 4 && iteracio < iteraciosHatar) {
                // z_{n+1} = z_n * z_n + c

                ujreZ = reZ*reZ - imZ*imZ + reC;
                ujimZ = 2*reZ*imZ + imC;

                reZ = ujreZ;
                imZ = ujimZ;

                ++iteracio;

            }
            // ha a < 4 feltetel nem teljesult es a
            // iteracio < iteraciosHatar serulesevel lepett ki, azaz
            // feltesszuk a c-rol, hogy itt a z_{n+1} = z_n * z_n + c
            // sorozat konvergens, azaz iteracio = iteraciosHatar
            // ekkor az iteracio %= 256 egyenlo 255, mert az esetleges
            // nagyitasok soran az iteracio = valahany * 256 + 255

            iteracio %= 256;

            //a szinezest viszont mar majd a FrakAblak osztalyban lesz
            egySor[k] = iteracio;
        }
        // Abrazolasra atadjuk a kiszamolt sort a FrakAblak-nak.
        frakAblak->vissza(j, egySor, szelesseg);
    }
    frakAblak->vissza();

}



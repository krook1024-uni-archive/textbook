#include <iostream>		// mert olvassuk a std::cin, írjuk a std::cout csatornákat
#include <cmath>		// mert vonunk gyököt a szóráshoz: std::sqrt
#include <fstream>		// fájlból olvasunk, írunk majd

class LZWBinFa
{
public:
    LZWBinFa ():fa (gyoker)
    {
    }

    ~LZWBinFa ()
    {
        szabadit (gyoker->egyesGyermek ());
        szabadit (gyoker->nullasGyermek ());
    }

    void operator<< (char b)
    {
        if (b == '0')
        {
            if (!fa->nullasGyermek ())
            {
                Csomopont *uj = new Csomopont ('0');
                fa->ujNullasGyermek (uj);
				fa = gyoker;
            }
            else
            {
                fa = fa->nullasGyermek ();
            }
        }

        else
        {
            if (!fa->egyesGyermek ())
            {
                Csomopont *uj = new Csomopont ('1');
                fa->ujEgyesGyermek (uj);
                fa = gyoker;
            }
            else
            {
                fa = fa->egyesGyermek ();
            }
        }
    }

    void kiir (void)
    {
        melyseg = 0;
        kiir (gyoker, std::cout);
    }

    int getMelyseg (void);
    double getAtlag (void);
    double getSzoras (void);

    friend std::ostream & operator<< (std::ostream & os, LZWBinFa & bf)
    {
        bf.kiir (os);
        return os;
    }
    void kiir (std::ostream & os)
    {
        melyseg = 0;
        kiir (gyoker, os);
    }

private:
    class Csomopont
    {
    public:
        /* A paraméter nélküli konstruktor az elepértelmezett '/' "gyökér-betűvel" hozza
       létre a csomópontot, ilyet hívunk a fából, aki tagként tartalmazza a gyökeret.
       Máskülönben, ha valami betűvel hívjuk, akkor azt teszi a "betu" tagba, a két
       gyermekre mutató mutatót pedig nullra állítjuk, C++-ban a 0 is megteszi. */
        Csomopont (char b = '/'):betu (b), balNulla (0), jobbEgy (0)
        {
        };

        ~Csomopont ()
        {
        };

        Csomopont *nullasGyermek () const
        {
            return balNulla;
        }
        Csomopont *egyesGyermek () const
        {
            return jobbEgy;
        }
        void ujNullasGyermek (Csomopont * gy)
        {
            balNulla = gy;
        }
        void ujEgyesGyermek (Csomopont * gy)
        {
            jobbEgy = gy;
        }
        char getBetu () const
        {
            return betu;
        }

    private:
        char betu;
        Csomopont *balNulla;
        Csomopont *jobbEgy;
        Csomopont (const Csomopont &); //másoló konstruktor
        Csomopont & operator= (const Csomopont &);
    };

    Csomopont *fa;
    int melyseg, atlagosszeg, atlagdb;
    double szorasosszeg;
    LZWBinFa (const LZWBinFa &);
    LZWBinFa & operator= (const LZWBinFa &);

    void kiir (Csomopont * elem, std::ostream & os)
    {
        if (elem != NULL)
        {
            ++melyseg;
            kiir (elem->egyesGyermek (), os);
            // 1-el nagyobb mélység, ezért -1
            for (int i = 0; i < melyseg; ++i)
                os << "---";
            os << elem->getBetu () << "(" << melyseg - 1 << ")" << std::endl;
            kiir (elem->nullasGyermek (), os);
            --melyseg;
        }
    }
    void szabadit (Csomopont * elem)
    {
        if (elem != NULL)
        {
            szabadit (elem->egyesGyermek ());
            szabadit (elem->nullasGyermek ());
            delete elem;
        }
    }

protected:			// ha esetleg egyszer majd kiterjesztjük az osztályt, mert
    // akkor ezek látszanak majd a gyerek osztályban is

    /* A fában tagként benne van egy csomópont, ez erősen ki van tüntetve, Ő a gyökér: */
	Csomopont *gyoker = new Csomopont;
    int maxMelyseg;
    double atlag, szoras;

    void rmelyseg (Csomopont * elem);
    void ratlag (Csomopont * elem);
    void rszoras (Csomopont * elem);

};

int
LZWBinFa::getMelyseg (void)
{
    melyseg = maxMelyseg = 0;
    rmelyseg (gyoker);
    return maxMelyseg - 1;
}

double
LZWBinFa::getAtlag (void)
{
    melyseg = atlagosszeg = atlagdb = 0;
    ratlag (gyoker);
    atlag = ((double) atlagosszeg) / atlagdb;
    return atlag;
}

double
LZWBinFa::getSzoras (void)
{
    atlag = getAtlag ();
    szorasosszeg = 0.0;
    melyseg = atlagdb = 0;

    rszoras (gyoker);

    if (atlagdb - 1 > 0)
        szoras = std::sqrt (szorasosszeg / (atlagdb - 1));
    else
        szoras = std::sqrt (szorasosszeg);

    return szoras;
}

void
LZWBinFa::rmelyseg (Csomopont * elem)
{
    if (elem != NULL)
    {
        ++melyseg;
        if (melyseg > maxMelyseg)
            maxMelyseg = melyseg;
        rmelyseg (elem->egyesGyermek ());
        rmelyseg (elem->nullasGyermek ());
        --melyseg;
    }
}

void
LZWBinFa::ratlag (Csomopont * elem)
{
    if (elem != NULL)
    {
        ++melyseg;
        ratlag (elem->egyesGyermek ());
        ratlag (elem->nullasGyermek ());
        --melyseg;
        if (elem->egyesGyermek () == NULL && elem->nullasGyermek () == NULL)
        {
            ++atlagdb;
            atlagosszeg += melyseg;
        }
    }
}

void
LZWBinFa::rszoras (Csomopont * elem)
{
    if (elem != NULL)
    {
        ++melyseg;
        rszoras (elem->egyesGyermek ());
        rszoras (elem->nullasGyermek ());
        --melyseg;
        if (elem->egyesGyermek () == NULL && elem->nullasGyermek () == NULL)
        {
            ++atlagdb;
            szorasosszeg += ((melyseg - atlag) * (melyseg - atlag));
        }
    }
}

void
usage (void)
{
    std::cout << "Usage: lzwtree in_file -o out_file" << std::endl;
}

int
main (int argc, char *argv[])
{
    if (argc != 4)
    {
        usage ();
        return -1;
    }

    char *inFile = *++argv;

    if (*((*++argv) + 1) != 'o')
    {
        usage ();
        return -2;
    }

    // ha igen, akkor az 5. előadásból kimásoljuk a fájlkezelés C++ változatát:
    std::fstream beFile (inFile, std::ios_base::in);

    if (!beFile)
    {
        std::cout << inFile << " nem letezik..." << std::endl;
        usage ();
        return -3;
    }

    std::fstream kiFile (*++argv, std::ios_base::out);

    unsigned char b;		
    LZWBinFa binFa;		

    // a bemenetet binárisan olvassuk, de a kimenő fájlt már karakteresen írjuk, hogy meg tudjuk
    // majd nézni... :) l. az említett 5. ea. C -> C++ gyökkettes átírási példáit

    while (beFile.read ((char *) &b, sizeof (unsigned char)))
        if (b == 0x0a)
            break;

    bool kommentben = false;

    while (beFile.read ((char *) &b, sizeof (unsigned char)))
    {

        if (b == 0x3e)
        {			// > karakter
            kommentben = true;
            continue;
        }

        if (b == 0x0a)
        {			// újsor
            kommentben = false;
            continue;
        }

        if (kommentben)
            continue;

        if (b == 0x4e)		// N betű
            continue;

        // egyszerűen a korábbi d.c kódját bemásoljuk
        // laboron többször lerajzoltuk ezt a bit-tologatást:
        // a b-ben lévő bájt bitjeit egyenként megnézzük
        for (int i = 0; i < 8; ++i)
        {
            // maszkolunk eddig..., most már simán írjuk az if fejébe a legmagasabb helyiértékű bit vizsgálatát
            // csupa 0 lesz benne a végén pedig a vizsgált 0 vagy 1, az if megmondja melyik:
            if (b & 0x80)
                // ha a vizsgált bit 1, akkor az '1' betűt nyomjuk az LZW fa objektumunkba
                binFa << '1';
            else
                // különben meg a '0' betűt:
                binFa << '0';
            b <<= 1;
        }

    }

    //std::cout << binFa.kiir (); // így rajzolt ki a fát a korábbi verziókban de, hogy izgalmasabb legyen
    // a példa, azaz ki lehessen tolni az LZWBinFa-t kimeneti csatornára:

    kiFile << binFa;		// ehhez kell a globális operator<< túlterhelése, lásd fentebb
    // (jó ez az OO, mert mi ugye nem igazán erre gondoltunk, amikor írtuk, mégis megy, hurrá)

    kiFile << "depth = " << binFa.getMelyseg () << std::endl;
    kiFile << "mean = " << binFa.getAtlag () << std::endl;
    kiFile << "var = " << binFa.getSzoras () << std::endl;

    kiFile.close ();
    beFile.close ();

    return 0;
}

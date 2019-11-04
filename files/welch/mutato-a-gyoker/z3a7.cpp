// Egyutt tamadjuk meg: http://progpater.blog.hu/2011/04/14/egyutt_tamadjuk_meg
// LZW fa epito 3. C++ atirata a C valtozatbol (+melyseg, atlag es szoras)
// Programozo Paternoszter
//
// Copyright (C) 2011, 2012, Batfai Norbert, nbatfai@inf.unideb.hu, nbatfai@gmail.com
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
// 0.0.1,       http://progpater.blog.hu/2011/02/19/gyonyor_a_tomor
// 0.0.2,       csomopontok mutatoinak NULLazasa (nem fejtette meg senki :)
// 0.0.3,       http://progpater.blog.hu/2011/03/05/labormeres_otthon_avagy_hogyan_dolgozok_fel_egy_pedat
// 0.0.4,       z.cpp: a C verziobol svn: bevezetes/C/ziv/z.c atirjuk C++-ra
//              http://progpater.blog.hu/2011/03/31/imadni_fogjatok_a_c_t_egy_emberkent_tiszta_szivbol
// 0.0.5,       z2.cpp: az fgv(*mut)-ok helyett fgv(&ref)
// 0.0.6,       z3.cpp: Csomopont beagyazva
//              http://progpater.blog.hu/2011/04/01/imadni_fogjak_a_c_t_egy_emberkent_tiszta_szivbol_2
// 0.0.6.1      z3a2.c: LZWBinFa mar nem baratja a Csomopont-nak, mert annak tagjait nem hasznalja direktben
// 0.0.6.2      Kis kommentezest teszunk bele 1. lepeskent (hogy a kicsit lemaradt hallgatoknak is
//              konnyebb legyen, jol megtuzdeljuk tovabbi olvasmanyokkal)
//              http://progpater.blog.hu/2011/04/14/egyutt_tamadjuk_meg
//              (majd a 2. lepesben "beletesszuk a d.c-t", majd s 3. lepesben a parancssorsor argok feldolgozasat)
// 0.0.6.3      z3a2.c: Fejlesztgetjuk a forrast: http://progpater.blog.hu/2011/04/17/a_tizedik_tizenegyedik_labor
// 0.0.6.4      SVN-beli, http://www.inf.unideb.hu/~nbatfai/p1/forrasok-SVN/bevezetes/vedes/
// 0.0.6.5      2012.03.20, z3a4.cpp: N betuk (hianyok), sorvegek, vezeto komment figyelmen kivul: http://progpater.blog.hu/2012/03/20/a_vedes_elokeszitese
// 0.0.6.6      z3a5.cpp: mamenyaka kollega eszrevetelere a tobb komment sor figyelmen kivul hagyasa
//		http://progpater.blog.hu/2012/03/20/a_vedes_elokeszitese/fullcommentlist/1#c16150365
// 0.0.6.7	Javaslom ezt a verziot valasztani vedendo programnak
// 0.0.6.8	z3a7.cpp: par kisebb javitas, illetve a vedesek tamogatasahoz tovabbi komment a <<
// 		eltolo operatort tagfuggvenykent, illetve globalis fuggvenykent tulterhelo reszekhez.
//		http://progpater.blog.hu/2012/04/10/imadni_fogjak_a_c_t_egy_emberkent_tiszta_szivbol_4/fullcommentlist/1#c16341099
//

#include <iostream>		// mert olvassuk a std::cin, irjuk a std::cout csatornakat
#include <cmath>		// mert vonunk gyokot a szorashoz: std::sqrt
#include <fstream>		// fajlbol olvasunk, irunk majd

/* Az LZWBinFa osztalyban absztrahaljuk az LZW algoritmus binaris fa epiteset. Az osztaly
 definiciojaba beagyazzuk a fa egy csomopontjanak az absztrakt jellemzeset, ez lesz a
 beagyazott Csomopont osztaly. Miert agyazzuk be? Mert kulon nem szanunk neki szerepet, ezzel
 is jelezzuk, hogy csak a fa reszekent szamiolunk vele.*/

class LZWBinFa
{
public:
    /* Szemben a binaris keresofankkal (BinFa osztaly)
     http://progpater.blog.hu/2011/04/12/imadni_fogjak_a_c_t_egy_emberkent_tiszta_szivbol_3
     itt (LZWBinFa osztaly) a fa gyokere nem pointer, hanem a '/' betut tartalmazo objektum,
     lasd majd a vedett tagok kozott lent: Csomopont gyoker;
     A fa viszont mar pointer, mindig az epulo LZW-fank azon csomopontjara mutat, amit az
     input feldolgozasa soran az LZW algoritmus logikaja diktal:
     http://progpater.blog.hu/2011/02/19/gyonyor_a_tomor
     Ez a konstruktor annyit csinal, hogy a fa mutatot raallitja a gyokerre. (Mert ugye
     laboron, blogon, eloadasban tisztaztuk, hogy a tartalmazott tagok, most "Csomopont gyoker"
     konstruktora elobb lefut, mint a tagot tartalmazo LZWBinFa osztaly konstruktora, eppen a
     kovetkezo, azaz a fa=&gyoker OK.)
   */
    LZWBinFa ():fa (&gyoker)
    {
    }
    ~LZWBinFa ()
    {
        szabadit (gyoker.egyesGyermek ());
        szabadit (gyoker.nullasGyermek ());
    }

    /* Tagfuggvenykent tulterheljuk a << operatort, ezzel a celunk, hogy felkeltsuk a
     hallgato erdeklodeset, mert ekkor igy nyomhatjuk a faba az inputot: binFa << b; ahol a b
     egy '0' vagy '1'-es betu.
     Mivel tagfuggveny, igy van ra "ertelmezve" az aktualis (this "rejtett parameterkent"
     kapott ) peldany, azaz annak a fanak amibe eppen be akarjuk nyomni a b betut a tagjai
     (pl.: "fa", "gyoker") hasznalhatoak a fuggvenyben.

     A fuggvenybe programoztuk az LZW fa epitesenek algoritmusat tk.:
     http://progpater.blog.hu/2011/02/19/gyonyor_a_tomor

     a b formalis param az a betu, amit eppen be kell nyomni a faba.

     a binFa << b (ahol a b majd a vegen latszik, hogy mar az '1' vagy a '0') azt jelenti
     tagfuggvenykent, hogy binFa.operator<<(b) (globaliskent igy festene: operator<<(binFa, b) )

     */
    void operator<< (char b)
    {
        // Mit kell betenni eppen, '0'-t?
        if (b == '0')
        {
            /* Van '0'-s gyermeke az aktualis csomopontnak?
           megkerdezzuk Tole, a "fa" mutato eppen rea mutat */
            if (!fa->nullasGyermek ())	// ha nincs, hat akkor csinalunk
            {
                // elkeszitjuk, azaz paldanyositunk a '0' betu akt. parammal
                Csomopont *uj = new Csomopont ('0');
                // az aktualis csomopontnak, ahol allunk azt uzenjuk, hogy
                // jegyezze mar be maganak, hogy nullas gyereke mostantol van
                // kuldjuk is Neki a gyerek cimet:
                fa->ujNullasGyermek (uj);
                // es visszaallunk a gyokerre (mert ezt diktalja az alg.)
                fa = &gyoker;
            }
            else			// ha van, arra ralepunk
            {
                // azaz a "fa" pointer mar majd a szoban forgo gyermekre mutat:
                fa = fa->nullasGyermek ();
            }
        }
        // Mit kell betenni eppen, vagy '1'-et?
        else
        {
            if (!fa->egyesGyermek ())
            {
                Csomopont *uj = new Csomopont ('1');
                fa->ujEgyesGyermek (uj);
                fa = &gyoker;
            }
            else
            {
                fa = fa->egyesGyermek ();
            }
        }
    }
    /* A bejarassal kapcsolatos fuggvenyeink (tulterhelt kiir-ok, atlag, ratlag stb.) rekurzivak,
     tk. a rekurziv fabejarast valositjak meg (lasd a 3. eloadas "Fabejaras" c. foliajat es tarsait)

     (Ha a rekurziv fuggvennyel altalaban gondod van => K&R konyv megfelelo resze: a 3. ea. izometrikus
     reszeben ezt "letancoltuk" :) es kulon ideztuk a K&R allaspontjat :)
   */
    void kiir (void)
    {
        // Sokkal elegansabb lenne (es mas, a bevezetesben nem kibontando reentrans kerdesek miatt is, mert
        // ugye ha most ket helyrol hivjak meg az objektum ilyen fuggvenyeit, tahat ha ketszer kezd futni az
        // objektum kiir() fgv.-e pl., az komoly hiba, mert elromlana a melyseg... tehat a mostani megoldasunk
        // nem reentrans) ha nem hasznalnank a C verzioban globalis valtozokat, a C++ valtozatban peldanytagot a
        // melyseg kezelesere: http://progpater.blog.hu/2011/03/05/there_is_no_spoon
        melyseg = 0;
        // ha nem mondta meg a hivo az uzenetben, hogy hova irjuk ki a fat, akkor a
        // sztenderd out-ra nyomjuk
        kiir (&gyoker, std::cout);
    }
    /* mar nem hasznaljuk, tartalmat a dtor hivja
  void szabadit (void)
  {
    szabadit (gyoker.egyesGyermek ());
    szabadit (gyoker.nullasGyermek ());
    // magat a gyokeret nem szabaditjuk, hiszen azt nem mi foglaltuk a szabad tarban (halmon).
  }
  */

    /* A valtozatossag kedveert ezeket az osztalydefinicio (class LZWBinFa {...};) utan definialjuk,
     hogy kenytelen legy az LZWBinFa es a :: hatokor operatorral minositve definialni :) l. lentebb */
    int getMelyseg (void);
    double getAtlag (void);
    double getSzoras (void);

    /* Vagyunk, hogy a felepitett LZW fat ki tudjuk nyomni ilyenforman: std::cout << binFa;
     de mivel a << operator is a sztenderd nevterben van, de a using namespace std-t elvbol
     nem hasznaljuk bevezeto kurzusban, igy ez a konstrukcio csak az argfuggo nevfeloldas miatt
     fordul le (B&L konyv 185. o. teteje) am itt nem az a lenyeg, hanem, hogy a cout ostream
     osztalybeli, igy abban az osztalyban kene modositani, hogy tudjon kiirni LZWBinFa osztalybelieket...
     e helyett a globalis << operatort terheljuk tul,

     a kiFile << binFa azt jelenti, hogy

      - tagfuggvenykent: kiFile.operator<<(binFa) de ehhez a kiFile valamilyen
      std::ostream stream osztaly forrasaba kellene beleirni ezt a tagfuggvenyt,
      amely ismeri a mi LZW binfankat...

      - globalis fuggvenykent: operator<<(kiFile, binFa) es pont ez latszik a kovetkezo sorban:

     */
    friend std::ostream & operator<< (std::ostream & os, LZWBinFa & bf)
    {
        bf.kiir (os);
        return os;
    }
    void kiir (std::ostream & os)
    {
        melyseg = 0;
        kiir (&gyoker, os);
    }

private:
    class Csomopont
    {
    public:
        /* A parameter nelkuli konstruktor az elepertelmezett '/' "gyoker-betuvel" hozza
       letre a csomopontot, ilyet hivunk a fabol, aki tagkent tartalmazza a gyokeret.
       Maskulonben, ha valami betuvel hivjuk, akkor azt teszi a "betu" tagba, a ket
       gyermekre mutato mutatot pedig nullra allitjuk, C++-ban a 0 is megteszi. */
        Csomopont (char b = '/'):betu (b), balNulla (0), jobbEgy (0)
        {
        };
        ~Csomopont ()
        {
        };
        // Aktualis csomopont, mondd meg nekem, ki a bal oldali gyermeked
        // (a C verzio logikajaval muxik ez is: ha nincs, akkor a null megy vissza)
        Csomopont *nullasGyermek () const
        {
            return balNulla;
        }
        // Aktualis csomopon,t mondd meg nekem, ki a jobb oldali gyermeked?
        Csomopont *egyesGyermek () const
        {
            return jobbEgy;
        }
        // Aktualis csomopont, imhol legyen a "gy" mutatta csomopont a bal oldali gyereked!
        void ujNullasGyermek (Csomopont * gy)
        {
            balNulla = gy;
        }
        // Aktualis csomopont, imhol legyen a "gy" mutatta csomopont a jobb oldali gyereked!
        void ujEgyesGyermek (Csomopont * gy)
        {
            jobbEgy = gy;
        }
        // Aktualis csomopont: Te milyen betut hordozol?
        // (a const kulcsszoval jelezzuk, hogy nem bantjuk a peldanyt)
        char getBetu () const
        {
            return betu;
        }

    private:
        // friend class LZWBinFa; /* mert ebben a valtozatban az LZWBinFa metodusai nem kozvetlenul
        // a Csomopont tagjaival dolgoznak, hanem beallito/lekerdezo uzenetekkel erik el azokat */

        // Milyen betut hordoz a csomopont
        char betu;
        // Melyik masik csomopont a bal oldali gyermeke? (a C valtozatbol "orokolt" logika:
        // ha hincs ilyen csermek, akkor balNulla == null) igaz
        Csomopont *balNulla;
        Csomopont *jobbEgy;
        // nem masolhato a csomopont (okorszabaly: ha van valamilye a szabad tarban,
        // letiltjuk a masolo konstruktort, meg a masolo ertekadast)
        Csomopont (const Csomopont &); //masolo konstruktor
        Csomopont & operator= (const Csomopont &);
    };

    /* Mindig a fa "LZW algoritmus logikaja szerinti aktualis" csomopontjara mutat */
    Csomopont *fa;
    // technikai
    int melyseg, atlagosszeg, atlagdb;
    double szorasosszeg;
    // szokasosan: nocopyable
    LZWBinFa (const LZWBinFa &);
    LZWBinFa & operator= (const LZWBinFa &);

    /* Kiirja a csomopontot az os csatornara. A rekurzio kapcsan lasd a korabbi K&R-es utalast... */
    void kiir (Csomopont * elem, std::ostream & os)
    {
        // Nem letezo csomoponttal nem foglalkozunk... azaz ez a rekurzio leallitasa
        if (elem != NULL)
        {
            ++melyseg;
            kiir (elem->egyesGyermek (), os);
            // ez a postorder bejarashoz kepest
            // 1-el nagyobb melyseg, ezert -1
            for (int i = 0; i < melyseg; ++i)
                os << "---";
            os << elem->getBetu () << "(" << melyseg - 1 << ")" << std::endl;
            kiir (elem->nullasGyermek (), os);
            --melyseg;
        }
    }
    void szabadit (Csomopont * elem)
    {
        // Nem letezo csomoponttal nem foglalkozunk... azaz ez a rekurzio leallitasa
        if (elem != NULL)
        {
            szabadit (elem->egyesGyermek ());
            szabadit (elem->nullasGyermek ());
            // ha a csomopont mindket gyermeket felszabaditottuk
            // azutan szabaditjuk magat a csomopontot:
            delete elem;
        }
    }

protected:			// ha esetleg egyszer majd kiterjesztjuk az osztalyt, mert
    // akarunk benne valami ujdonsagot csinalni, vagy meglevo tevekenyseget mashogy... stb.
    // akkor ezek latszanak majd a gyerek osztalyban is

    /* A faban tagkent benne van egy csomopont, ez erosen ki van tuntetve, O a gyoker: */
    Csomopont gyoker;
    int maxMelyseg;
    double atlag, szoras;

    void rmelyseg (Csomopont * elem);
    void ratlag (Csomopont * elem);
    void rszoras (Csomopont * elem);

};

// Nehany fuggvenyt az osztalydefinicio utan definialunk, hogy lassunk ilyet is ... :)
// Nem eroltetjuk viszont a kulon fajlba szedest, mert a sablonosztalyositott tovabb
// fejlesztesben az linkelesi gondot okozna, de ez a tema mar kivezet a laborteljesites
// szukseges feladatabol: http://progpater.blog.hu/2011/04/12/imadni_fogjak_a_c_t_egy_emberkent_tiszta_szivbol_3

// Egyebkent a melyseg, atlag es szoras fgv.-ek a kiir fgv.-el teljesen egy kaptafa.

int
LZWBinFa::getMelyseg (void)
{
    melyseg = maxMelyseg = 0;
    rmelyseg (&gyoker);
    return maxMelyseg - 1;
}

double
LZWBinFa::getAtlag (void)
{
    melyseg = atlagosszeg = atlagdb = 0;
    ratlag (&gyoker);
    atlag = ((double) atlagosszeg) / atlagdb;
    return atlag;
}

double
LZWBinFa::getSzoras (void)
{
    atlag = getAtlag ();
    szorasosszeg = 0.0;
    melyseg = atlagdb = 0;

    rszoras (&gyoker);

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
        // ez a postorder bejarashoz kepest
        // 1-el nagyobb melyseg, ezert -1
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

// teszt pl.: http://progpater.blog.hu/2011/03/05/labormeres_otthon_avagy_hogyan_dolgozok_fel_egy_pedat
// [norbi@sgu ~]$ echo "01111001001001000111"|./z3a2
// ------------1(3)
// ---------1(2)
// ------1(1)
// ---------0(2)
// ------------0(3)
// ---------------0(4)
// ---/(0)
// ---------1(2)
// ------0(1)
// ---------0(2)
// depth = 4
// mean = 2.75
// var = 0.957427
// a laborvedeshez majd ezt a tesztelest hasznaljuk:
// http://

/* Ez volt eddig a main, de most komplexebb kell, mert explicite bejovo, kimeno fajlokkal kell dolgozni
int
main ()
{
    char b;
    LZWBinFa binFa;

    while (std::cin >> b)
    {
        binFa << b;
    }

    //std::cout << binFa.kiir (); // igy rajzolt ki a fat a korabbi verziokban de, hogy izgalmasabb legyen
    // a pelda, azaz ki lehessen tolni az LZWBinFa-t kimeneti csatornara:

    std::cout << binFa; // ehhez kell a globalis operator<< tulterhelese, lasd fentebb

    std::cout << "depth = " << binFa.getMelyseg () << std::endl;
    std::cout << "mean = " << binFa.getAtlag () << std::endl;
    std::cout << "var = " << binFa.getSzoras () << std::endl;

    binFa.szabadit ();

    return 0;
}
*/

/* A parancssor arg. kezelest egyszeruen bedolgozzuk a 2. hullam kapcsolodo feladatabol:
 http://progpater.blog.hu/2011/03/12/hey_mikey_he_likes_it_ready_for_more_3
 de mivel nekunk sokkal egyszerubb is eleg, alig hagyunk meg belole valamit...
 */

void
usage (void)
{
    std::cout << "Usage: lzwtree in_file -o out_file" << std::endl;
}

int
main (int argc, char *argv[])
{
    // http://progpater.blog.hu/2011/03/12/hey_mikey_he_likes_it_ready_for_more_3
    // alapjan a parancssor argok ottani elegans feldolgozasabol kb. ennyi marad:
    // "*((*++argv)+1)"...

    // a kiiras szerint ./lzwtree in_file -o out_file alakra kell mennie, ez 4 db arg:
    if (argc != 4)
    {
        // ha nem annyit kapott a program, akkor felhomalyositjuk errol a juzetr:
        usage ();
        // es jelezzuk az operacios rendszer fele, hogy valami gaz volt...
        return -1;
    }

    // "Megjegyezzuk" a bemeno fajl nevet
    char *inFile = *++argv;

    // a -o kapcsolo jon?
    if (*((*++argv) + 1) != 'o')
    {
        usage ();
        return -2;
    }

    // ha igen, akkor az 5. eloadasbol kimasoljuk a fajlkezeles C++ valtozatat:
    std::fstream beFile (inFile, std::ios_base::in);

    // fejlesztgetjuk a forrast: http://progpater.blog.hu/2011/04/17/a_tizedik_tizenegyedik_labor
    if (!beFile)
    {
        std::cout << inFile << " nem letezik..." << std::endl;
        usage ();
        return -3;
    }

    std::fstream kiFile (*++argv, std::ios_base::out);

    unsigned char b;		// ide olvassik majd a bejovo fajl bajtjait
    LZWBinFa binFa;		// s nyomjuk majd be az LZW fa objektumunkba

    // a bemenetet binarisan olvassuk, de a kimeno fajlt mar karakteresen irjuk, hogy meg tudjuk
    // majd nezni... :) l. az emlitett 5. ea. C -> C++ gyokkettes atirasi peldait

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
        {			// ujsor
            kommentben = false;
            continue;
        }

        if (kommentben)
            continue;

        if (b == 0x4e)		// N betu
            continue;

        // egyszeruen a korabbi d.c kodjat bemasoljuk
        // laboron tobbszor lerajzoltuk ezt a bit-tologatast:
        // a b-ben levo bajt bitjeit egyenkent megnezzuk
        for (int i = 0; i < 8; ++i)
        {
            // maszkolunk eddig..., most mar siman irjuk az if fejebe a legmagasabb helyierteku bit vizsgalatat
            // csupa 0 lesz benne a vegen pedig a vizsgalt 0 vagy 1, az if megmondja melyik:
            if (b & 0x80)
                // ha a vizsgalt bit 1, akkor az '1' betut nyomjuk az LZW fa objektumunkba
                binFa << '1';
            else
                // kulonben meg a '0' betut:
                binFa << '0';
            b <<= 1;
        }

    }

    //std::cout << binFa.kiir (); // igy rajzolt ki a fat a korabbi verziokban de, hogy izgalmasabb legyen
    // a pelda, azaz ki lehessen tolni az LZWBinFa-t kimeneti csatornara:

    kiFile << binFa;		// ehhez kell a globalis operator<< tulterhelese, lasd fentebb
    // (jo ez az OO, mert mi ugye nem igazan erre gondoltunk, amikor irtuk, megis megy, hurra)

    kiFile << "depth = " << binFa.getMelyseg () << std::endl;
    kiFile << "mean = " << binFa.getAtlag () << std::endl;
    kiFile << "var = " << binFa.getSzoras () << std::endl;

    kiFile.close ();
    beFile.close ();

    return 0;
}

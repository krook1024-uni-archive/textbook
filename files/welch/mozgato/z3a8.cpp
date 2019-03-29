#include <iostream>
#include <cmath>
#include <fstream>

class LZWBinFa {
 public:
	LZWBinFa() {
		gyoker = new Csomopont('/');
		fa = gyoker;
	}
	~LZWBinFa() {
		szabadit(gyoker->egyesGyermek());
		szabadit(gyoker->nullasGyermek());
		delete gyoker;
	}

	LZWBinFa(const LZWBinFa & regi) {
		std::cout << "LZWBinFa copy ctor" << std::endl;
		gyoker = masol(regi.gyoker, regi.fa);
	}

	LZWBinFa & operator=(LZWBinFa & regi) {
		std::cout << "mozgato ctor" << std::endl;
		this->gyoker = regi.gyoker;
		this->fa = regi.fa;
		return *this;
	}

	LZWBinFa & operator<<(char b) {
		if (b == '0') {
			if (!fa->nullasGyermek()) {	// ha nincs, hát akkor csinálunk
				Csomopont *uj = new Csomopont('0');
				fa->ujNullasGyermek(uj);
				fa = gyoker;
			} else {	// ha van, arra rálépünk
				fa = fa->nullasGyermek();
			}
		}
		else {
			if (!fa->egyesGyermek()) {
				Csomopont *uj = new Csomopont('1');
				fa->ujEgyesGyermek(uj);
				fa = gyoker;
			} else {
				fa = fa->egyesGyermek();
			}
		}

		return *this;
	}

	void kiir(void) {
		melyseg = 0;
		kiir(gyoker, std::cout);
	}

	int getMelyseg(void);
	double getAtlag(void);
	double getSzoras(void);

	friend std::ostream & operator<<(std::ostream & os, LZWBinFa & bf) {
		bf.kiir(os);
		return os;
	}
	void kiir(std::ostream & os) {
		melyseg = 0;
		kiir(gyoker, os);
	}

 private:
	class Csomopont {
 public:
 Csomopont(char b = '/'):betu(b), balNulla(0), jobbEgy(0) {
		};
		~Csomopont() {
		};
		Csomopont *nullasGyermek() const {
			return balNulla;
		} Csomopont *egyesGyermek() const {
			return jobbEgy;
		}
		void ujNullasGyermek(Csomopont * gy) {
			balNulla = gy;
		}
		void ujEgyesGyermek(Csomopont * gy) {
			jobbEgy = gy;
		}
		char getBetu() const {
			return betu;
		}

		char betu;
		Csomopont *balNulla;
		Csomopont *jobbEgy;
		Csomopont(const Csomopont &);
		Csomopont & operator=(const Csomopont &);

	};

	Csomopont *fa;
	int melyseg, atlagosszeg, atlagdb;
	double szorasosszeg;

	void kiir(Csomopont * elem, std::ostream & os) {
		if (elem != NULL) {
			++melyseg;
			kiir(elem->egyesGyermek(), os);
			for (int i = 0; i < melyseg; ++i)
				os << "---";

			os << elem->getBetu() << "(" << melyseg -
			    1 << ")" << std::endl;
			kiir(elem->nullasGyermek(), os);
			--melyseg;
		}
	}

	void szabadit(Csomopont * elem) {
		if (elem != NULL) {
			szabadit(elem->egyesGyermek());
			szabadit(elem->nullasGyermek());
			delete elem;
		}
	}

	Csomopont *masol(Csomopont * elem, Csomopont * regifa) {
		Csomopont *ujelem = NULL;
		if (elem != NULL) {
			switch (elem->getBetu()) {
			case '/':
				ujelem = new Csomopont('/');
				break;
			case '0':
				ujelem = new Csomopont('1');
				break;
			case '1':
				ujelem = new Csomopont('0');
				break;
			default:
				std::cerr << "HIBA!" << std::endl;
				break;
			}
			ujelem->ujEgyesGyermek(masol
					       (elem->egyesGyermek(), regifa));
			ujelem->ujNullasGyermek(masol
						(elem->nullasGyermek(),
						 regifa));
			if (regifa == elem)
				fa = ujelem;

		}

		return ujelem;
	}

 protected:
	Csomopont * gyoker;
	int maxMelyseg;
	double atlag, szoras;

	void rmelyseg(Csomopont * elem);
	void ratlag(Csomopont * elem);
	void rszoras(Csomopont * elem);
};

int LZWBinFa::getMelyseg(void)
{
	melyseg = maxMelyseg = 0;
	rmelyseg(gyoker);
	return maxMelyseg - 1;
}

double LZWBinFa::getAtlag(void)
{
	melyseg = atlagosszeg = atlagdb = 0;
	ratlag(gyoker);
	atlag = ((double)atlagosszeg) / atlagdb;
	return atlag;
}

double LZWBinFa::getSzoras(void)
{
	atlag = getAtlag();
	szorasosszeg = 0.0;
	melyseg = atlagdb = 0;

	rszoras(gyoker);

	if (atlagdb - 1 > 0)
		szoras = std::sqrt(szorasosszeg / (atlagdb - 1));
	else
		szoras = std::sqrt(szorasosszeg);

	return szoras;
}

void LZWBinFa::rmelyseg(Csomopont * elem)
{
	if (elem != NULL) {
		++melyseg;
		if (melyseg > maxMelyseg)
			maxMelyseg = melyseg;
		rmelyseg(elem->egyesGyermek());
		rmelyseg(elem->nullasGyermek());
		--melyseg;
	}
}

void LZWBinFa::ratlag(Csomopont * elem)
{
	if (elem != NULL) {
		++melyseg;
		ratlag(elem->egyesGyermek());
		ratlag(elem->nullasGyermek());
		--melyseg;
		if (elem->egyesGyermek() == NULL
		    && elem->nullasGyermek() == NULL) {
			++atlagdb;
			atlagosszeg += melyseg;
		}
	}
}

void LZWBinFa::rszoras(Csomopont * elem)
{
	if (elem != NULL) {
		++melyseg;
		rszoras(elem->egyesGyermek());
		rszoras(elem->nullasGyermek());
		--melyseg;
		if (elem->egyesGyermek() == NULL
		    && elem->nullasGyermek() == NULL) {
			++atlagdb;
			szorasosszeg += ((melyseg - atlag) * (melyseg - atlag));
		}
	}
}

void usage(void)
{
	std::cout << "Usage: lzwtree in_file -o out_file" << std::endl;
}

void fgv(LZWBinFa binFa) {
	std::cout << binFa;
	std::cout << "depth = " << binFa.getMelyseg() << std::endl;
	std::cout << "mean = " << binFa.getAtlag() << std::endl;
	std::cout << "var = " << binFa.getSzoras() << std::endl;
}

int main(int argc, char *argv[])
{
	if (argc != 4) {
		usage();
		return -1;
	}

	char *inFile = *++argv;

	if (*((*++argv) + 1) != 'o') {
		usage();
		return -2;
	}

	std::fstream beFile(inFile, std::ios_base::in);

	if (!beFile) {
		std::cout << inFile << " nem letezik..." << std::endl;
		usage();
		return -3;
	}

	std::fstream kiFile(*++argv, std::ios_base::out);

	unsigned char b;
	LZWBinFa binFa, binFa2;

	bool kommentben = false;
	while (beFile.read((char *)&b, sizeof(unsigned char))) {
		if (b == 0x3e) {	// > karakter
			kommentben = true;
			continue;
		}

		if (b == 0x0a) {	// újsor
			kommentben = false;
			continue;
		}

		if (kommentben)
			continue;

		if (b == 0x4e)	// N betű
			continue;

		for (int i = 0; i < 8; ++i) {
			if (b & 0x80)
				binFa << '1';
			else
				binFa << '0';
			b <<= 1;
		}
	}

	// Kipróbáljuk a másoló ctort
	fgv(binFa);

	// Kiírjuk az eredeti fát, mint eddig.
	kiFile << binFa;
	kiFile << "depth = " << binFa.getMelyseg() << std::endl;
	kiFile << "mean = " << binFa.getAtlag() << std::endl;
	kiFile << "var = " << binFa.getSzoras() << std::endl;

	// Átmozgatjuk a fát binFa2-be.
	binFa2 = binFa;

	// Majd kiírjuk ezt
	kiFile << binFa2;
	kiFile << "depth = " << binFa2.getMelyseg() << std::endl;
	kiFile << "mean = " << binFa2.getAtlag() << std::endl;
	kiFile << "var = " << binFa2.getSzoras() << std::endl;

	kiFile.close();
	beFile.close();

	return 0;
}

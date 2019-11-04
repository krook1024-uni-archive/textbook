#include <iostream>

class tesztOsztaly {
public:
	~tesztOsztaly() {
		std::cout << "lefuto kod vagyok" << std::endl;
	}
};

void f(void);
void g(void);

int
main(void) {
	try {
		f();
	}

	catch (const char* exception) {
		std::cout << "Hiba lepett fel! Uzenet: " << exception << std::endl;
	}

	std::cout << "Program vege." << std::endl;

	return 0;
}

void
f(void) {
	tesztOsztaly t;
	g();
}

void
g(void) {
	throw "hoppa";
}



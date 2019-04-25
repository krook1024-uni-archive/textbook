#include <iostream>

class tesztOsztaly {
public:
	~tesztOsztaly() {
		std::cout << "lefutó kód vagyok" << std::endl;
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
		std::cout << "Hiba lépett fel! Üzenet: " << exception << std::endl;
	}

	std::cout << "Program vége." << std::endl;

	return 0;
}

void
f(void) {
	tesztOsztaly t;
	g();
}

void
g(void) {
	throw "hoppá";
}



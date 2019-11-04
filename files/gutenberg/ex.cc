#include <iostream>

int
main(void) {
	try {
		double n;
		std::cout << "Adj meg egy nemnulla szamot: ";
		std::cin >> n;

		if(n == 0)
			throw "Nullanak nem letezik a reciproka!";

		std::cout << n << " reciproka: " << 1/n << std::endl;
	}

	catch (const char* exception) {
		std::cout << "Hiba lepett fel! Uzenet: " << exception << std::endl;
	}

	std::cout << "Program vege." << std::endl;

	return 0;
}

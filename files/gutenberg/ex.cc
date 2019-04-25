#include <iostream>

int
main(void) {
	try {
		double n;
		std::cout << "Adj meg egy nemnulla számot: ";
		std::cin >> n;

		if(n == 0)
			throw "Nullának nem létezik a reciproka!";

		std::cout << n << " reciproka: " << 1/n << std::endl;
	}

	catch (const char* exception) {
		std::cout << "Hiba lépett fel! Üzenet: " << exception << std::endl;
	}

	std::cout << "Program vége." << std::endl;

	return 0;
}

#include <iostream>
#include <string>

class Szulo {
public:
    void print(std::string s) {
        std::cout << s << std::endl;
    }
};

class Gyermek : public Szulo {
public:
    void echo(std::string s) {
        std::cout << s << s << std::endl;
    }
};

int main(int argc, char **argv) {
    Szulo* szulo = new Szulo();
    Szulo* szulo2 = new Gyermek();

    // Ez az, ami nem fog mukodni:
    szulo2->echo("damn, son. where'd u find dis???");

    return 0;
}

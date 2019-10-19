#include <iostream>

class Data {
private:
short n;

public:
Data(){}
Data(int n) : n(n) {
    std::cout << "Contructing object with"
        " number " << n << std::endl;
}

Data(const Data& otherData) {
    std::cout << "Calling copy constructor"
        " on object (addr: " << &otherData << ")"
        << std::endl;
    n = otherData.n;
}

Data& operator= (const Data& otherData) {
    n = otherData.n;
    return *this;
}

friend std::ostream& operator<< (std::ostream &os, const Data& d) {
    os << d.n << " ";
    return os;
}
};

int main (int argc, char **argv)  {
    Data c;
    Data d(4);
    Data e(d);
    Data f;
    f = d;
    f = 5;

    std::cout << c << d << e << f << std::endl;

    return 0;
}


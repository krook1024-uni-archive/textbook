#include <iostream>
#include <vector>
#include <functional>

template<typename T>
void ForEach(const std::vector<T>& v, const std::function<void(int)>& f) {
    for (auto itr : v)
        f(itr);
}

int main(int argc, char *argv[])
{
    // 1. pelda: egyszeru negyzetre emeles lambda kifejezessel
    auto square = [=] (int n) { return n*n; };
    std::cout << square(4) << std::endl;

    // 2. pelda: szimpla foreach vektor elemeire
    std::vector<int> v = {3, 4, 5};
    ForEach(v, [=] (int n) {
            std::cout << square(n) << std::endl;
        }
    );

    return 0;
}

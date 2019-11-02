/*
 * CustomAlloc class.
 * @author Norbert Bátfai
 */

#include <iostream>
#include <cxxabi.h>
#include <vector>

template<typename T>
struct CustomAlloc
{
    using size_type         = size_t;
    using value_type        = T;
    using pointer           = T*;
    using const_pointer     = const T*;
    using reference         = T&;
    using const_reference   = const T&;
    using difference_type   = ptrdiff_t;

    CustomAlloc() {}
    CustomAlloc ( const CustomAlloc &) {}
    ~CustomAlloc() {}

    pointer allocate(size_type n) {
        int s;
        char *p = abi::__cxa_demangle(typeid(T).name(), 0, 0, &s);
        std::cout << "Allocating "
                  << n << " object(s) of "
                  << n * sizeof(T)
                  << " bytes. "
                  << typeid(T).name() << "=" << p
                  << std::endl;
        free(p);
        return reinterpret_cast<T*> (new char[n*sizeof(T)]);
    }

    void deallocate(pointer p, size_type n) {
        delete[] reinterpret_cast<char *> (p);
    }

};

int main(int argc, char *argv[])
{
    std::vector<int, CustomAlloc<int>> ints;
    ints.push_back(3);

    std::vector<long, CustomAlloc<long>> longs;
    longs.push_back(3213125211);

    std::vector<std::string, CustomAlloc<std::string>> strings;
    strings.push_back("a");
    return 0;
}


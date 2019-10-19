#include "../inc/String.h"
#include <iostream>

int main (int argc, char **argv) {
    String v ("Hello,");
    String w = " world";
    String x = std::move(v);
    String y (std::move(w));
    String z ("  !  ");
    std::cout << x << y << z[2] << std::endl;
    z[2] = '.';
    std::cout << x << y << z[2] << std::endl;

    return 0;
}

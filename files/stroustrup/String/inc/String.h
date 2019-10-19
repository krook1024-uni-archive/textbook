// String.h

#ifndef     STRING_H // include guard
#define     STRING_H

#include    <iostream>
#include    <cstring>

class String {
private:
    char* buf;
    int length;

public:
    String (const char*);
    String (const String& str) { *this = str; }
    String (String&& str) { buf = nullptr; *this = std::move(str); }
    ~String() { delete[] buf; }

    String& operator= (const String&);
    char& operator[] (int);

    friend std::ostream& operator<< (std::ostream&, String&);

    char* get() { return buf; }
    int len() { return length; }
};

std::ostream& operator<< (std::ostream&, String&);

#endif

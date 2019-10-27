// String.cpp

#include    "../inc/String.h"
#include    <iostream>
#include    <cstring>

String::String (const char* str)
    : length(std::strlen(str)), buf(new char [length+1])
{
    if ( nullptr != buf )
        std::copy (str, str + length, buf);
    else
        exit (1);
}

String& String::operator= (const String& rhs) {
    if (this != &rhs) {
        delete[] buf;
        length = rhs.length;
        buf = new char [length+1];
        if ( nullptr != buf )
            std::copy (rhs.buf, rhs.buf + length, buf);
        else
            exit (2);

    }

    return *this;
}

std::ostream& operator<< (std::ostream& stream, String& str) {
    stream << str.buf;
    return stream;
}

char& String::operator[] (int i) {
    if (i >= length)
        exit (3);

    return (buf[i]);
}

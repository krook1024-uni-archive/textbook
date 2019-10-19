// String.cpp

#include    "../inc/String.h"
#include    <iostream>
#include    <cstring>

String::String (const char* str) {
    this -> length = std::strlen(str);
    this -> buf = new char[length + 1];

    if ( nullptr != buf )
        std::copy (str, str + this->length, this->buf);
    else
        exit (1);
}

String& String::operator= (const String& otherString) {
    if (this != &otherString) {
        delete[] buf;
        length = otherString.length;
        buf = new char [length+1];
        if ( nullptr != buf )
            std::copy (otherString.buf, otherString.buf + length, buf);
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

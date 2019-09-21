/*
 *  WARNING
 *  std::to_string() is a C++11 feature!
 */

#include <iostream>
#include <string>

class BlogPost {
private:
    std::string content;

public:
    std::string getContent() {
        return content;
    }
};

class VideoBlogPost : BlogPost {
private:
    int videoId;

public:
    std::string getContent() {
        std::string before ("<iframe src=\"https://youtu.be/embed/");
        std::string after ("\"></iframe>");
        return before + std::to_string(videoId) + after;
    }
};

int main(int argc, char** argv) {

}

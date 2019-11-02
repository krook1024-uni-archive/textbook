#include <iostream>
#include <map>

int main(int argc, char *argv[]) {
    std::map<int, int> pairs;
    pairs.insert(std::pair<int, int> (1, 10));
    pairs.insert(std::pair<int, int> (2, 20));
    pairs.insert(std::pair<int, int> (3, 30));
    pairs.insert(std::pair<int, int> (4, 40));
    pairs.insert(std::pair<int, int> (5, 50));

    for(auto itr = pairs.begin(); itr != pairs.end(); ++itr) {
        std::cout << itr->first << "\t" << itr->second << std::endl;
    }

    return 0;
}

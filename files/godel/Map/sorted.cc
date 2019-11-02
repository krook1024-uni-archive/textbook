#include <iostream>
#include <map>
#include <algorithm>
#include <vector>

std::vector<std::pair<int, int>> sort_map(std::map<int, int> &pairs) {
    std::vector<std::pair<int, int>> s;
    for (auto &itr : pairs)
        s.push_back(itr);

    std::sort(s.begin(), s.end(), [=]
            (const auto& a, const auto& b) {
                return a.second < b.second;
            });

    return s;
}

int main(int argc, char *argv[]) {
    std::map<int, int> pairs;
    std::vector<std::pair<int, int>> _pairs;

    pairs.insert(std::pair<int, int> (1, 50));
    pairs.insert(std::pair<int, int> (2, 10));
    pairs.insert(std::pair<int, int> (3, 20));
    pairs.insert(std::pair<int, int> (4, 40));
    pairs.insert(std::pair<int, int> (5, 30));

    std::cout << "Map before sorting:" << std::endl;
    for (auto &itr : pairs) {
        std::cout << itr.first << "\t" << itr.second << std::endl;
        _pairs.push_back(itr);
    }

    auto sorted = sort_map(pairs);

    std::cout << "Map after sorting:" << std::endl;
    for (auto &itr : sorted)
        std::cout << itr.first << "\t" << itr.second << std::endl;

    return 0;
}

/*
 *   jdk_classes.cc
 * List classes in JDK.
 *
 * @author Molnar Antal Albert
 * @date 2019. 10. 07
 */

#include <iostream>
#include <string>
#include <fstream>
#include <vector>

#include <boost/filesystem.hpp>
#include <boost/filesystem/fstream.hpp>

std::vector<boost::filesystem::path> return_vec; // Shouldn't worry too much
    // about garbage collection since the vector is allocated on the stack

std::vector<boost::filesystem::path>
read_files(const boost::filesystem::path path) {
    if (is_regular_file(path)) {
        std::string ext(".java");	// We only care about .java files
        if ( ! ext.compare(boost::filesystem::extension(path))) {
            return_vec.push_back(path);
        }
    } else if (is_directory(path)) {
        for (boost::filesystem::directory_entry & entry
                :
                boost::filesystem::directory_iterator(path)) {
            read_files(entry.path());
        }
    }

    return return_vec;
}

int
main(int argc, char **argv) {
    // Where the unzipped JDK sources are
    boost::filesystem::path sources_path { "/home/b1/src" };

    // Get all filenames
    std::vector<boost::filesystem::path> paths = read_files(sources_path);

    // Loop through the paths array and echo valid classes
    std::string l;
    int n = 0;
    for (auto const &path : paths) {
        std::ifstream in(path.c_str());
        if (in) {
            while (std::getline(in, l)) {
                // In Java, a file should contain a class
                // that has the same name as the file itself
                std::string find_str = "class " + path.stem().string();
                if (l.find(find_str) != std::string::npos) {
                    std::cout
                        << path.stem().c_str()
                        << " (file: " << path << ")"
                        << std::endl;
                    n++;
                    break;
                }
            }
        }
    }

    std::cout
        << "There are "
        << n
        << " classes in JDK (sources dir: " << sources_path << ")."
        << std::endl;

    return 0;
}

#include <iostream>
#include <string>
#include <fstream>
#include <vector>

#include <boost/filesystem.hpp>
#include <boost/filesystem/fstream.hpp>
//#include <boost/program_options.hpp>
//#include <boost/tokenizer.hpp>
//#include <boost/date_time/posix_time/posix_time.hpp>

std::vector < boost::filesystem::path > return_vec;
std::vector < boost::filesystem::path > read_files(const boost::filesystem::path path)
{
    if (is_regular_file(path)) {
        std::string ext(".java");	// We only care about .java files
        if (!ext.compare(boost::filesystem::extension(path))) {
            return_vec.push_back(path);
            //std::cout << path << " ";
        }
    } else if (is_directory(path)) {
        for (boost::filesystem::directory_entry & entry:boost::filesystem::directory_iterator(path)) {
            read_files(entry.path());
        }
    }

    return return_vec;
}

int main(int argc, char **argv)
{
    // Where the unzipped JDK sources are
    boost::filesystem::path sources_path {
        "/home/b1/src/"};

    // Get all filenames
    std::vector < boost::filesystem::path > paths = read_files(sources_path);

    std::cout << paths.size() << std::endl;

    // Loop through the paths array
    std::string l;
    for (auto const &path : paths) {
        std::ifstream in(path.c_str());
        if (in) {
            while (std::getline(in, l)) {
                if (l.find("class") != std::string::npos) {
                    std::cout << path.stem().c_str() << std::endl;
                    break;
                }
            }
        }
    }

    std::cout << "Classes in JDK (sources dir: " << sources_path << "):" << std::endl;
    return 0;
}

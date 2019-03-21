#include "ml.hpp"

#include <iostream>
#include <png++/png.hpp>

int main(int argc, char **argv) {
	png::image<png::rgb_pixel> image(argv[1]);

	int size = image.get_width() * image.get_height();

	Perceptron *p = new Perceptron(3, size, 256, 1);

	double *image_d = new double[size];

	for(int i = 0; i < image.get_width(); i++)
		for(int j = 0; j < image.get_height(); j++)
			image_d[i*image.get_width() + j] = image[i][j].red;

	double value = (*p)(image_d);

	std::cout << value << std::endl;

	delete p;
	delete[] image_d;

	return 0;
}

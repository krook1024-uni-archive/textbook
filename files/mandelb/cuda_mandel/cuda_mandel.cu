// Copyright (C) 2019
// Norbert Batfai, batfai.norbert@inf.unideb.hu
// Released under GNU GPLv3

#include <png++/image.hpp>
#include <png++/rgb_pixel.hpp>
#include <sys/times.h>
#include <iostream>

#define SIZE 600
#define ITERATION_LIMIT 32000

// Vegigzongorazza a CUDA a szelesseg x magassag racsot:
__device__ int mandel(int k, int j)
{
	// most eppen a j. sor k. oszlopaban vagyunk

	float a = -2.0, b = .7, c = -1.35, d = 1.35;
	int width = SIZE, height = SIZE, iterationLimit = ITERATION_LIMIT;

	float dx = (b - a) / width;
	float dy = (d - c) / height;
	float reC, imC, reZ, imZ, ujreZ, ujimZ;
	int iteration = 0;

	reC = a + k * dx;
	imC = d - j * dy;
	reZ = 0.0;
	imZ = 0.0;
	iteration = 0;

	while (reZ * reZ + imZ * imZ < 4 && iteration < iterationLimit) {
		ujreZ = reZ * reZ - imZ * imZ + reC;
		ujimZ = 2 * reZ * imZ + imC;
		reZ = ujreZ;
		imZ = ujimZ;

		++iteration;

	}
	return iteration;
}

__global__ void mandelkernel(int *buffer)
{

	int tj = threadIdx.x;
	int tk = threadIdx.y;

	int j = blockIdx.x * 10 + tj;
	int k = blockIdx.y * 10 + tk;

	buffer[j + k * SIZE] = mandel(j, k);

}

void cudamandel(int buffer[SIZE][SIZE])
{

	int *deviceImageBuffer;
	cudaMalloc((void **)&deviceImageBuffer, SIZE * SIZE * sizeof(int));

	dim3 grid(SIZE / 10, SIZE / 10);
	dim3 tgrid(10, 10);
	mandelkernel <<< grid, tgrid >>> (deviceImageBuffer);

	cudaMemcpy(buffer, deviceImageBuffer, SIZE * SIZE * sizeof(int),
		   cudaMemcpyDeviceToHost);
	cudaFree(deviceImageBuffer);

}

int main(int argc, char *argv[])
{

	// Merunk idot (PP 64)
	clock_t delta = clock();
	// Merunk idot (PP 66)
	struct tms tmsbuf1, tmsbuf2;
	times(&tmsbuf1);

	int buffer[SIZE][SIZE];

	cudamandel(buffer);

	png::image < png::rgb_pixel > image(SIZE, SIZE);

	for (int j = 0; j < SIZE; ++j) {
		//sor = j;
		for (int k = 0; k < SIZE; ++k) {
			image.set_pixel(k, j,
					png::rgb_pixel(255 -
						       (255 * buffer[j][k]) /
						       ITERATION_LIMIT,
						       255 -
						       (255 * buffer[j][k]) /
						       ITERATION_LIMIT,
						       255 -
						       (255 * buffer[j][k]) /
						       ITERATION_LIMIT));
		}
	}

	image.write("mandel.png");

	times(&tmsbuf2);
	std::cout << tmsbuf2.tms_utime - tmsbuf1.tms_utime + tmsbuf2.tms_stime -
	    tmsbuf1.tms_stime << std::endl;

	delta = clock() - delta;
	std::cout << (float)delta / CLOCKS_PER_SEC << " sec" << std::endl;

}

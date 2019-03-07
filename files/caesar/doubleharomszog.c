#include <stdio.h>
#include <stdlib.h>		// malloc()

int main(void)
{
	const int db = 5;

	double **hm;
	printf("Fo mutato cime: %p\n", &hm);

	hm = (double **)malloc(db * sizeof(double));
	printf("Lefoglalt 5 double** cime: %p\n", hm);

	for (int i = 0; i < db; i++)
		hm[i] = (double *)malloc(db * sizeof(double));
	printf("Elso szint elso eleme: %p\n", hm[0]);

	for (int i = 0; i < db; i++) {
		for (int j = 0; j < db; j++) {
			hm[i][j] = i + j;
			printf("[%d][%d]: %f ", i, j, hm[i][j]);
		}
		printf("\n");
	}

	return 0;
}

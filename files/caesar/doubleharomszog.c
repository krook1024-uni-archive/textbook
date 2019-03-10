#include <stdio.h>
#include <stdlib.h>		// malloc()

int main(void)
{
	const int db = 5;

	double **hm;
	printf("Mutato cime: %p\n", &hm);

	hm = (double **)malloc(db * sizeof(double));
	printf("Sorok tombjenek cime: %p\n", hm);

	for (int i = 0; i < db; i++)
		hm[i] = (double *)malloc(db * sizeof(double));
	printf("Elso sor cime: %p\n", hm[0]);

	for (int i = 0; i < db; i++) {
		printf("|");
		for (int j = 0; j < db; j++) {
			hm[i][j] = (i + 1)/(j + 1);
			printf("%.2f  ", hm[i][j]);
		}
		printf("|\n");
	}

	return 0;
}

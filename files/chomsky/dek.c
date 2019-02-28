#include <stdlib.h>

int main(void)
{
	// Egesz
	int a;

	// Egeszre mutato mutato
	int *b;

	// Egesz referenciaja
	int *c = &a;

	// Egeszek tombje
	int d[5];

	// Egeszek tombjenek referenciaja
	int *e = malloc(5 * sizeof(int));

	// Egeszre mutato mutatok tombje
	int **f[5];

	// Egeszre mutato mutatot visszaado fv.
	int *g(void);

	// Egeszre mutato mutatot visszaado fv.re mutato mutato
	int *(* (*h)(void)) = h;

	// Egeszet visszaado es ket egeszet kapo fvre mutato
	// mutatot visszaado, egeszet kapo fv
	int (* (*i)(int))(int, int);

	// Fuggvenymutato egy egeszet visszaado  es ket egeszet
	// kapo fvre mutato mutatot visszaado, egeszet kapo
	// fvre
	int (* (*j)(int))(int, int) = i;

	return 0;
}

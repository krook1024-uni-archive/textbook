#include <stdio.h>

int main(void)
{

	int tomb[5] = { 1, 2, 3, 4, 5 };
	for (int i = 0; i < 5; tomb[i] = i++) {
		printf("%d %d\n", i, tomb[i]);
	}

	return 0;
}

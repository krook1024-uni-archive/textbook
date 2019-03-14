#include <stdio.h>

void swap(int *a, int *b)
{
	*a -= *b;
	*b += *a;
	*a = *b - *a;
}

int main()
{
	int x = 3, y = 7;

	printf("x=%d y=%d\n", x, y);
	swap(&x, &y);
	printf("x=%d y=%d\n", x, y);

	return 0;
}

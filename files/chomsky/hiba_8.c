#include <stdio.h>

int f(int *a)
{
	return *a * *a;
}

int main()
{
	int a = 2;
	// kimenet: 4 2
	printf("%d %d \n", f(&a), a);
	return 0;
}

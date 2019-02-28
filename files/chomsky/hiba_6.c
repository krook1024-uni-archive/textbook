#include <stdio.h>

int f(int a, int b) {
	return a+b;
}

int main()
{
	int a = 4;

	// 4 + 4 = 8
	// 4 + 5 = 9 -> ehelyett 12-et kapunk
	// 6 + 6 = 10 -> ehelyett 10-et kapunk
	printf("%d %d \n", f(a, ++a), f(++a, a));
	return 0;
}

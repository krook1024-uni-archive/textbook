#include <stdio.h>

int main(void)
{
	int a = 4, b = 4, n = 5, *d = &a, *s = &b;

	for (int i = 0; i < n && (*d++ = *s++); ++i) {
		printf("%d\n", i);
	}

	return 0;
}

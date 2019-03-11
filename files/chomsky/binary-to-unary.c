#include <stdio.h>

int main(void)
{
	printf
	    ("Please enter a decimal number you'd like to convert to unary: ");

	int in = 0;
	scanf("%d", &in);

	for (int i = 0; i < in; ++i)
		(i % 5) ? printf("|") : printf(" |");

	printf("\n");
	return 0;
}

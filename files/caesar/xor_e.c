#include <stdio.h>
#include <unistd.h>

#define BUFFER_SIZE 200

char *exor(char *str, char *key)
{
	while (*str)
		*str++ ^= *key;

	return str;
}

int main(void)
{
	char *buffer;
	char key[5] = "1234";
	while (read(0, (char *)buffer, BUFFER_SIZE))
		printf("%s", exor(buffer, key));

	return 0;
}

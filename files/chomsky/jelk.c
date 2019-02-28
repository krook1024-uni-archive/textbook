#include <stdio.h>
#include <signal.h>

void jelkezelo()
{
	printf("\nNe csukj be! ;-(\n");
}

int main()
{
	for(;;) {
		if(signal(SIGINT, jelkezelo)==SIG_IGN)
			signal(SIGINT, SIG_IGN);
	}

	return 0;
}

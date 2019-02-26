#include <pthread.h>
#include <unistd.h>		// sleep()

#define NO_OF_THREADS 4

void *func()
{
	for (;;) ;
}

int main()
{
	pthread_t thread[NO_OF_THREADS];

	for (int i = 0; i < NO_OF_THREADS; i++)
		pthread_create(&thread[i], NULL, func, NULL);

	for (;;)
		sleep(1);

	return 0;
}

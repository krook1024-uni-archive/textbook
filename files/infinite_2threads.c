#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>		// sleep()

#define NO_OF_CORES 4

void *
func ()
{
  for (;;);
}

int
main ()
{
  pthread_t thread[NO_OF_CORES];

  for (int i = 0; i < NO_OF_CORES; i++)
    pthread_create (&thread[i], NULL, func, NULL);

  for (;;)
    sleep (1);

  return 0;
}

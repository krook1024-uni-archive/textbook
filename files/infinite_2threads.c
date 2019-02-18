#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h> // sleep()

#define EVER ;;

void *func1(void *a)
{
    (void)a;

    for(EVER) {
        printf("1 ");
    }
}

void
*func2(void *b) {
    (void)b;

    for(EVER) {
        printf("2 ");
    }
}

int
main() {
    pthread_t thread[2];

    pthread_create(&thread[0], NULL, func1, NULL);
    pthread_create(&thread[1], NULL, func2, NULL);

    while(1)
        sleep(10);

    return 0;
}

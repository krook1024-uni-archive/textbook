#include <stdio.h>

#define BYTE_TO_BINARY_PATTERN "%c%c%c%c%c%c%c%c"
#define BYTE_TO_BINARY(byte)  \
  (byte & 0x80 ? '1' : '0'), \
  (byte & 0x40 ? '1' : '0'), \
  (byte & 0x20 ? '1' : '0'), \
  (byte & 0x10 ? '1' : '0'), \
  (byte & 0x08 ? '1' : '0'), \
  (byte & 0x04 ? '1' : '0'), \
  (byte & 0x02 ? '1' : '0'), \
  (byte & 0x01 ? '1' : '0')

void
swap (int *a, int *b)
{
  if (a != b)
    {
      printf ("a=" BYTE_TO_BINARY_PATTERN ", b="
	      BYTE_TO_BINARY_PATTERN "\n", BYTE_TO_BINARY (*a),
	      BYTE_TO_BINARY (*b));

      *a ^= *b;
      printf ("a=" BYTE_TO_BINARY_PATTERN ", b="
	      BYTE_TO_BINARY_PATTERN "\n", BYTE_TO_BINARY (*a),
	      BYTE_TO_BINARY (*b));

      *b ^= *a;
      printf ("a=" BYTE_TO_BINARY_PATTERN ", b="
	      BYTE_TO_BINARY_PATTERN "\n", BYTE_TO_BINARY (*a),
	      BYTE_TO_BINARY (*b));

      *a ^= *b;
      printf ("a=" BYTE_TO_BINARY_PATTERN ", b="
	      BYTE_TO_BINARY_PATTERN "\n", BYTE_TO_BINARY (*a),
	      BYTE_TO_BINARY (*b));
    }
}

int
main ()
{
  int x = 3, y = 7;

  swap (&x, &y);

  //printf ("x=%d y=%d\n", x, y);
  return 0;
}

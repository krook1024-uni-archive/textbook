#include <ncurses.h>
#include <unistd.h>
#include <stdlib.h>

int
main ()
{
  int xj = 0, xk = 0, yj = 0, yk = 0, mx = 0, my = 0, h = 0, w = 0;

  initscr ();
  cbreak ();
  noecho ();
  nodelay (stdscr, TRUE);
  getmaxyx (stdscr, h, w);
  mx = w * 2;
  my = h * 2;

  while (true)
    {
      xj = (xj - 1) % mx;
      xk = (xk + 1) % mx;

      yj = (yj - 1) % my;
      yk = (yk + 1) % my;

      clear ();

      mvprintw (abs ((yj + (my - yk)) / 2),
		abs ((xj + (mx - xk)) / 2), "o");

      refresh ();
      usleep (30000);
    }

  return 0;
}

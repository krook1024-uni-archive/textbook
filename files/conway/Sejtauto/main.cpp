#include <QApplication>
#include "sejtablak.h"
#include <QDesktopWidget>

int main(int argc, char *argv[])
{
  QApplication a(argc, argv);
  SejtAblak w(100, 75);
  w.show();
  
  return a.exec();
}

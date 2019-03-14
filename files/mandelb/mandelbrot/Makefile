all: mandelbrot clean

mandelbrot.o: mandelbrot.cpp
	@g++ -c mandelbrot.cpp `libpng-config --cflags`

mandelbrot: mandelbrot.o
	@g++ -o mandelbrot mandelbrot.o `libpng-config --ldflags`

clean:
	@rm -rf *.o
	@./mandelbrot
	@rm -rf mandelbrot
	




all: mandelbrot_komplex clean

mandelbrot_komplex.o: mandelbrot_komplex.cpp
	@g++ -c mandelbrot_komplex.cpp `libpng-config --cflags`

mandelbrot_komplex: mandelbrot_komplex.o
	@g++ -o mandelbrot_komplex mandelbrot_komplex.o `libpng-config --ldflags`

clean:
	@rm -rf *.o
	@./mandelbrot_komplex 800 600
	@rm -rf mandelbrot_komplex

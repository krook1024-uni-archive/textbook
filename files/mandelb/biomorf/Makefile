all: biomorf clean

biomorf.o: biomorf.cc
	@g++ -c biomorf.cc `libpng-config --cflags` -O3

biomorf: biomorf.o
	@g++ -o biomorf biomorf.o `libpng-config --ldflags` -O3

clean:
	@rm -rf *.o
	@./biomorf bmorf.png 800 800 10 -2 2 -2 2 .285 0 10
	@rm -rf biomorf

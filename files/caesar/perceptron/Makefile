all: perceptron clean

perceptron.o: main.cc
	@g++ -c -o perceptron.o main.cc `libpng-config --cflags` -std=c++11

perceptron: perceptron.o 
	@g++ -o perceptron perceptron.o `libpng-config --ldflags` -std=c++11

clean:
	@rm -rf *.o
	@./perceptron
	@rm -rf perceptron

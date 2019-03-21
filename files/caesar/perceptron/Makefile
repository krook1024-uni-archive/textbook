all: perceptron clean

perceptron: main.cc ml.hpp
	@g++ -o perceptron ml.hpp main.cc -std=c++11 -O3 -lpng

clean:
	@rm -rf *.o
	@./perceptron mandel.png
	@rm -rf perceptron

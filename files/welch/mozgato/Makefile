all: clean z3a8 test

clean:
	@rm -rf teszt
	@rm -rf z3a8

z3a8:
	@rm -rf z3a8
	@g++ -o z3a8 z3a8.cpp -O3

test:
	@./z3a8 teszt.be -o teszt && cat teszt
	@rm -rf teszt
	@rm -rf z3a8

all: clean z3a7 test

clean:
	@rm -rf teszt
	@rm -rf z3a7

z3a7:
	@rm -rf z3a7
	@g++ -o z3a7 z3a7.cpp -O3

test:
	@./z3a7 teszt.be -o teszt && cat teszt
	@rm -rf teszt
	@rm -rf z3a7

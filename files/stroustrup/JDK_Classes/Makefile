all: clean jdk_classes

jdk_classes: jdk_classes.cc
	g++ -std=c++17 -l boost_filesystem -O3 -o jdk_classes jdk_classes.cc

@PHONY clean:
clean:
	@rm -f jdk_classes

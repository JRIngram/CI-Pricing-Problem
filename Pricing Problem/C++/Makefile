all: main.o PricingProblem.o
	g++ main.o PricingProblem.o -o main.out -O3 -std=c++0x

run:
	make all
	./main.out

%.o: %.cpp
	g++ -O3 -Wall -Werror -std=c++0x -c $<

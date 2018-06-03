LEX = flex
YACC = bison
CC = g++
PY = python
OBJECT = main.o lex.yy.o tiger.tab.o datatype.o 

main: $(OBJECT)
	$(CC) -o main $(OBJECT)

main.o: main.cpp tiger.h absyn2json.h
	$(CC) -c main.cpp

datatype.o: datatype.cpp datatype.h
	$(CC) -c datatype.cpp
	
lex.yy.o: lex.yy.c tiger.h
	$(CC) -c lex.yy.c

tiger.tab.o: tiger.tab.c tiger.h 
	$(CC) -c tiger.tab.c
	
tiger.h : tiger.tab.h datatype.h

tiger.tab.c tiger.tab.h: tiger.y
	$(YACC) -d -v tiger.y

lex.yy.c: tiger.l tiger.tab.h
	$(LEX) tiger.l

# use python to generate C++ code 
tiger.y datatype.h datatype.cpp : type.py
	$(PY) type.py
	
.PHONY : clean
clean:
	@del $(OBJECT)  *.o *.yy.c *.tab.* datatype.* *.y *.output
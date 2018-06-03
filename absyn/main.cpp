#include "tiger.h"
#include <iostream>
using namespace std;

int main() {
    yyparse(); //使yacc开始读取输入和解析，它会调用lex的yylex()读取记号
    cout << "End of parser" << endl;
	
	return 0;
}
#include "tiger.h"
#include "absyn2json.h"
#include <iostream>
using namespace std;
extern exp_Type* absyn_root;
int main() {
    yyparse(); //使yacc开始读取输入和解析，它会调用lex的yylex()读取记号
    cout << "End of parser" << endl;
	absyn2json();
	return 0;
}
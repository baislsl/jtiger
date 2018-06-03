#ifndef TIGHR_H
#define TIGER_H
#include "datatype.h"
#define YYSTYPE Type *
// from yacc
#include "tiger.tab.h"

extern semicolon_exp_list_Type *p_start;

extern "C"
{
	// defined in lex
    int yywrap(void);
    int yylex(void);
	void yyerror(const char *s);
	// defined in yacc
	int yyparse();
}

#endif
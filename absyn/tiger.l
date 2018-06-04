%{
#include "tiger.h"
#include <iostream>

using namespace std;

int commentNesting = 0;

string str;

%}

space [ \r\t]
newline \n
identifier [a-z|A-Z]+[a-z|A-Z|0-9|_]*
integer [0-9]+

/* 定义新的状态 */
%x COMMENT_STATE STRING_STATE

%% /* -------------------------------- */


{space}	 {continue;}
{newline}	 {continue;}

while     {return WHILE;}
for       {return FOR;}
to        {return TO;}
break     {return BREAK;}
let       {return LET;}
in        {return IN;}
end       {return END;}
function  {return FUNCTION;}
var       {return VAR;}
type      {return TYPE;}
array     {return ARRAY;}
with     {return WITH;}
if        {return IF;}
then      {return THEN;}
else      {return ELSE;}
do        {return DO;}
of        {return OF;}
nil       {return NIL;}

","   {return COMMA;}
":"   {return COLON;}
";"   {return SEMICOLON;}
"("   {return LPAREN;}
")"   {return RPAREN;}
"["   {return LBRACK;}
"]"   {return RBRACK;}
"{"   {return LBRACE;}
"}"   {return RBRACE;}
"."   {return DOT;}
":="  {return ASSIGN;}

"+"   {yylval = new infixOp_Type("+"); return PLUS;}
"-"   {yylval = new infixOp_Type("-"); return MINUS;}
"*"   {yylval = new infixOp_Type("*"); return TIMES;}
"/"   {yylval = new infixOp_Type("/"); return DIV;}
"&"   {yylval = new infixOp_Type("&"); return AND;}
"|"   {yylval = new infixOp_Type("|"); return OR;}

"="   {yylval = new infixOp_Type("="); return EQ;}
"<>"  {yylval = new infixOp_Type("<>"); return NEQ;}
">"   {yylval = new infixOp_Type(">"); return GT;}
"<"   {yylval = new infixOp_Type("<"); return LT;}
">="  {yylval = new infixOp_Type(">="); return GE;}
"<="  {yylval = new infixOp_Type("<="); return LE;}

"/*" {
     commentNesting += 1;
     BEGIN(COMMENT_STATE);
   }

"*/" {
     yyerror("Comment not open!");
     yyterminate();
   }

{identifier} {
  yylval = new id_Type(string(yytext));
  return ID;
}

{integer} {
	yylval = new intLit_Type(atoi(yytext));
	return INTLIT;
}

\"  {
    BEGIN(STRING_STATE);
	str = ""
}

<STRING_STATE>{
    \" {
			BEGIN(INITIAL);
			yylval = new stringLit_Type(str);
			return STRINGLIT;
       }

    \n {
         yyerror("Unterminated string constant!");
         yyterminate();
       }

    \\n {str += "\n";}
    \\t {str += "\t";}
    \\r {str += "\r";}
    \\b {str += '\b';}
    \\f {str += '\f';}

    "\\\"" {str += "\"";}
    "\\'" {str += "\'";}
    "\\/" {str += "/";}
    "\\\\" {str += "\\";}

    <<EOF>> {
              yyerror("Encounter EOF.");
              yyterminate();
            }

    . {
		str += string(yytext);
    }
}



<COMMENT_STATE>{
    "/*" {
           commentNesting += 1;
           continue;
         }

    "*/" {
           commentNesting -= 1;
           if (commentNesting == 0) {
             BEGIN(INITIAL);
           }
         }

    <<EOF>> {
              yyerror("Encounter EOF.");
              yyterminate();
            }

    \n  {
      continue;
    }

    . {}

}

. {continue;}

%% /* -------------------------------- */

int yywrap(void) {
	return 1;
}
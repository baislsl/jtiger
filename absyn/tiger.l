%{
#include "tiger.h"
#include <iostream>

using namespace std;

void error(string msg); // display error
int commentNesting = 0;
string str;
int numOfChars = 0; //number of chars
int numOfLines = 0; //number of lines

%}

space [ \r\t]
newline \n
identifier [a-z|A-Z]+[a-z|A-Z|0-9|_]*
integer [0-9]+

/* 定义新的状态 */
%x COMMENT_STATE STRING_STATE

%% /* -------------------------------- */


{space}	 {numOfChars += yyleng; continue;}
{newline}	 {numOfChars = 0; numOfLines++; continue;}

while     {numOfChars += yyleng; return WHILE;}
for       {numOfChars += yyleng; return FOR;}
to        {numOfChars += yyleng; return TO;}
break     {numOfChars += yyleng; return BREAK;}
let       {numOfChars += yyleng; return LET;}
in        {numOfChars += yyleng; return IN;}
end       {numOfChars += yyleng; return END;}
function  {numOfChars += yyleng; return FUNCTION;}
var       {numOfChars += yyleng; return VAR;}
type      {numOfChars += yyleng; return TYPE;}
array     {numOfChars += yyleng; return ARRAY;}
with     {numOfChars += yyleng; return WITH;}
if        {numOfChars += yyleng; return IF;}
then      {numOfChars += yyleng; return THEN;}
else      {numOfChars += yyleng; return ELSE;}
do        {numOfChars += yyleng; return DO;}
of        {numOfChars += yyleng; return OF;}
nil       {numOfChars += yyleng; return NIL;}

","   {numOfChars += yyleng; return COMMA;}
":"   {numOfChars += yyleng; return COLON;}
";"   {numOfChars += yyleng; return SEMICOLON;}
"("   {numOfChars += yyleng; return LPAREN;}
")"   {numOfChars += yyleng; return RPAREN;}
"["   {numOfChars += yyleng; return LBRACK;}
"]"   {numOfChars += yyleng; return RBRACK;}
"{"   {numOfChars += yyleng; return LBRACE;}
"}"   {numOfChars += yyleng; return RBRACE;}
"."   {numOfChars += yyleng; return DOT;}
":="  {numOfChars += yyleng; return ASSIGN;}

"+"   {numOfChars += yyleng; yylval = new infixOp_Type("+"); return PLUS;}
"-"   {numOfChars += yyleng; yylval = new infixOp_Type("-"); return MINUS;}
"*"   {numOfChars += yyleng; yylval = new infixOp_Type("*"); return TIMES;}
"/"   {numOfChars += yyleng; yylval = new infixOp_Type("/"); return DIV;}
"&"   {numOfChars += yyleng; yylval = new infixOp_Type("&"); return AND;}
"|"   {numOfChars += yyleng; yylval = new infixOp_Type("|"); return OR;}

"="   {numOfChars += yyleng; yylval = new infixOp_Type("="); return EQ;}
"<>"  {numOfChars += yyleng; yylval = new infixOp_Type("<>"); return NEQ;}
">"   {numOfChars += yyleng; yylval = new infixOp_Type(">"); return GT;}
"<"   {numOfChars += yyleng; yylval = new infixOp_Type("<"); return LT;}
">="  {numOfChars += yyleng; yylval = new infixOp_Type(">="); return GE;}
"<="  {numOfChars += yyleng; yylval = new infixOp_Type("<="); return LE;}

"/*" {
      numOfChars += yyleng;
     commentNesting += 1;
     BEGIN(COMMENT_STATE);
   }

"*/" {
    numOfChars += yyleng;
     error("Comment not open!");
     yyterminate();
   }

{identifier} {
  numOfChars += yyleng;
  yylval = new id_Type(string(yytext));
  return ID;
}

{integer} {
  numOfChars += yyleng;
	yylval = new intLit_Type(atoi(yytext));
	return INTLIT;
}

\"  {
  numOfChars += yyleng;
    BEGIN(STRING_STATE);
	str = "";
}

<STRING_STATE>{
    \" {
      numOfChars += yyleng;
			BEGIN(INITIAL);
			yylval = new stringLit_Type(str);
			return STRINGLIT;
       }

    \n {
         error("Unterminated string constant!");
         yyterminate();
       }

    \\n {numOfChars += yyleng; str += "\n";}
    \\t {numOfChars += yyleng; str += "\t";}

    "\\\"" {numOfChars += yyleng; str += "\"";}
    "\\'" {numOfChars += yyleng; str += "\'";}
    "\\/" {numOfChars += yyleng; str += "/";}
    "\\\\" {numOfChars += yyleng; str += "\\";}

    <<EOF>> {numOfChars += yyleng; 
              error("Encounter EOF.");
              yyterminate();
            }

    . {numOfChars += yyleng; 
		str += string(yytext);
    }
}



<COMMENT_STATE>{
    "/*" {
            numOfChars += yyleng;
            commentNesting += 1;
            continue;
         }

    "*/" {
            numOfChars += yyleng;
            commentNesting -= 1;
            if (commentNesting == 0) {
              BEGIN(INITIAL);
            }
         }

    <<EOF>> {
              error("Encounter EOF.");
              yyterminate();
            }

    \n  {
      numOfChars = 0; 
      numOfLines++;
      continue;
    }

    . {numOfChars += yyleng;}

}

. {error("Unknown token"); yyterminate();}

%% /* -------------------------------- */

void error(string msg) {
  cerr << numOfLines+1 << ":" << numOfChars << ": ";
	cerr << "Lexical Error: " << msg << endl;
}

int yywrap(void) {
	return 1;
}
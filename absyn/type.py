# token_labels
token_labels = ['tyId', 'id', 'nil', 'intLit', 'stringLit', 'infixOp']
token_types = ['std::string', 'std::string', '', 'int', 'std::string', 'std::string']

# branches ==================================================

list1 = ['Type', 'dec', 'varDec', 'funDec', 'ty', 'exp', 'lValue', 'dec_list', 'comma_exp_list', 'nonempty_comma_exp_list', 'semicolon_exp_list', 'nonempty_semicolon_exp_list', 'fieldDec_list', 'nonempty_fieldDec_list', 'fieldCreate_list', 'nonempty_fieldCreate_list', 'subscript']

Type = ['dec', 'dec_list', 'ty', 'exp', 'comma_exp_list', 'semicolon_exp_list', 'fieldDec_list', 'fieldCreate_list', 'fieldDec', 'fieldCreate']

dec = ['tyDec', 'varDec', 'funDec']
varDec = ['void_varDec', 'type_varDec']
funDec = ['void_funDec', 'type_funDec']

ty = ['tyIdTy', 'arrTy', 'recTy']
exp = ['lValue', 'nilExp', 'intLitExp', 'stringLitExp', 'seqExp', 'negation', 'callExp', 'infixExp', 'arrCreate', 'recCreate', 'assignment', 'ifThenElse', 'ifThen', 'whileExp', 'forExp', 'letExp', 'breakExp']
lValue = ['idlValue', 'subscript', 'fieldExp']
subscript = ['idSubscript', 'lValueSubscript']

dec_list = ['empty_dec_list', 'nonempty_dec_list']

comma_exp_list = ['empty_comma_exp_list', 'nonempty_comma_exp_list']
nonempty_comma_exp_list = ['single_comma_exp_list', 'multi_comma_exp_list']

semicolon_exp_list = ['empty_semicolon_exp_list', 'nonempty_semicolon_exp_list']
nonempty_semicolon_exp_list = ['single_semicolon_exp_list', 'multi_semicolon_exp_list']

fieldDec_list = ['empty_fieldDec_list', 'nonempty_fieldDec_list']
nonempty_fieldDec_list = ['single_fieldDec_list', 'multi_fieldDec_list']

fieldCreate_list = ['empty_fieldCreate_list', 'nonempty_fieldCreate_list']
nonempty_fieldCreate_list = ['single_fieldCreate_list', 'multi_fieldCreate_list']

# grammar =======================================================
list2 = ['tyDec', 'arrTy', 'fieldDec', 
'nonempty_dec_list', 'empty_dec_list',
'empty_fieldDec_list', 'single_fieldDec_list', 'multi_fieldDec_list', 
'empty_comma_exp_list', 'single_comma_exp_list', 'multi_comma_exp_list', 
'empty_semicolon_exp_list', 'single_semicolon_exp_list', 'multi_semicolon_exp_list', 
'empty_fieldCreate_list', 'single_fieldCreate_list', 'multi_fieldCreate_list',
'nilExp', 'intLitExp', 'stringLitExp', 'seqExp', 'negation', 'recTy', 'idSubscript', 'lValueSubscript', 'fieldExp', 'void_varDec', 'type_varDec', 'void_funDec', 'type_funDec', 'callExp', 'arrCreate', 'recCreate', 'fieldCreate',  'assignment', 'ifThenElse', 'ifThen', 'whileExp', 'forExp', 'letExp', 'idlValue', 'tyIdTy', 'breakExp']

list3 = ['infixExp']

dict = {}

empty_dec_list = []
dict['empty_dec_list'] = ['']
empty_comma_exp_list = []
dict['empty_comma_exp_list'] = ['']
empty_semicolon_exp_list = []
dict['empty_semicolon_exp_list'] = ['']
empty_fieldDec_list = []
dict['empty_fieldDec_list'] = ['']
empty_fieldCreate_list = []
dict['empty_fieldCreate_list'] = ['']

tyDec = ['tyId', 'ty']
dict['tyDec'] = ['TYPE ID EQ ty', 2, 4]
arrTy = ['tyId']
dict['arrTy'] = ['ARRAY OF ID', 3]
fieldDec = ['id', 'tyId']
dict['fieldDec'] = ['ID COLON ID', 1, 3]

nonempty_dec_list = ['dec', 'dec_list']
dict['nonempty_dec_list'] = ['dec dec_list', 1, 2]

single_fieldDec_list = ['fieldDec']
dict['single_fieldDec_list'] = ['fieldDec', 1]
multi_fieldDec_list = ['fieldDec', 'nonempty_fieldDec_list']
dict['multi_fieldDec_list'] = ['fieldDec COMMA nonempty_fieldDec_list', 1, 3]

single_comma_exp_list = ['exp']
dict['single_comma_exp_list'] = ['exp', 1]
multi_comma_exp_list = ['exp', 'nonempty_comma_exp_list']
dict['multi_comma_exp_list'] = ['exp COMMA nonempty_comma_exp_list', 1, 3]
single_semicolon_exp_list = ['exp']
dict['single_semicolon_exp_list'] = ['exp', 1]
multi_semicolon_exp_list = ['exp', 'nonempty_semicolon_exp_list']
dict['multi_semicolon_exp_list'] = ['exp SEMICOLON nonempty_semicolon_exp_list', 1, 3]

nilExp = ['nil']
dict['nilExp'] = ['NIL', 1]
intLitExp = ['intLit']
dict['intLitExp'] = ['INTLIT', 1]
stringLitExp = ['stringLit']
dict['stringLitExp'] = ['STRINGLIT', 1]
seqExp = ['semicolon_exp_list']
dict['seqExp'] = ['LPAREN semicolon_exp_list RPAREN', 2]
negation = ['exp']
dict['negation'] = ['MINUS exp', 2]
recTy = ['fieldDec_list']
dict['recTy'] = ['LBRACE fieldDec_list RBRACE', 1]

idSubscript = ['id', 'exp']
dict['idSubscript'] = ['ID LBRACK exp RBRACK', 1, 3]
lValueSubscript = ['lValue', 'exp']
dict['lValueSubscript'] = ['lValue LBRACK exp RBRACK', 1, 3]
fieldExp = ['lValue', 'id']
dict['fieldExp'] = ['lValue DOT ID', 1, 3]
void_varDec = ['id', 'exp']
dict['void_varDec'] = ['VAR ID ASSIGN exp', 2, 4]
type_varDec = ['id', 'tyId', 'exp']
dict['type_varDec'] = ['VAR ID COLON ID ASSIGN exp', 2, 4, 6]
void_funDec = ['id', 'fieldDec_list', 'exp']
dict['void_funDec'] = ['FUNCTION ID LPAREN fieldDec_list RPAREN EQ exp', 2, 4, 7]
type_funDec = ['id', 'fieldDec_list', 'tyId', 'exp']
dict['type_funDec'] = ['FUNCTION ID LPAREN fieldDec_list RPAREN COLON ID EQ exp', 2, 4, 6, 9]
callExp = ['id', 'comma_exp_list']
dict['callExp'] = ['ID LPAREN comma_exp_list RPAREN', 1, 3]

infixExp = ['exp', 'infixOp', 'exp']
dict['infixExp'] = [
['exp PLUS exp', 1, 2, 3],
['exp MINUS exp', 1, 2, 3],
['exp TIMES exp', 1, 2, 3],
['exp DIV exp', 1, 2, 3],
['exp AND exp', 1, 2, 3],
['exp OR exp', 1, 2, 3],
['exp EQ exp', 1, 2, 3],
['exp NEQ exp', 1, 2, 3],
['exp GT exp', 1, 2, 3],
['exp LT exp', 1, 2, 3],
['exp GE exp', 1, 2, 3],
['exp LE exp', 1, 2, 3],
]

dict['andExp'] = ['exp AND exp', 1, 2, 3]
orExp = ['exp', 'infixOp', 'exp']
dict['orExp'] = ['exp OR exp', 1, 2, 3]
eqExp = ['exp', 'infixOp', 'exp']
dict['eqExp'] = ['exp EQ exp', 1, 2, 3]
neqExp = ['exp', 'infixOp', 'exp']
dict['neqExp'] = ['exp NEQ exp', 1, 2, 3]
gtExp = ['exp', 'infixOp', 'exp']
dict['gtExp'] = ['exp GT exp', 1, 2, 3]
ltExp = ['exp', 'infixOp', 'exp']
dict['ltExp'] = ['exp LT exp', 1, 2, 3]
geExp = ['exp', 'infixOp', 'exp']
dict['geExp'] = ['exp GE exp', 1, 2, 3]
leExp = ['exp', 'infixOp', 'exp']
dict['leExp'] = ['exp LE exp', 1, 2, 3]


arrCreate = ['tyId', 'exp', 'exp']
dict['arrCreate'] = ['ID LBRACK exp RBRACK OF exp', 1, 3, 6]
recCreate = ['tyId', 'fieldCreate_list']
dict['recCreate'] = ['ID LBRACE fieldCreate_list RBRACE', 1, 3]
fieldCreate = ['id', 'exp']
dict['fieldCreate'] = ['ID EQ exp', 1, 3]

single_fieldCreate_list = ['fieldCreate']
dict['single_fieldCreate_list'] = ['fieldCreate', 1]
multi_fieldCreate_list = ['fieldCreate', 'nonempty_fieldCreate_list']
dict['multi_fieldCreate_list'] = ['fieldCreate COMMA nonempty_fieldCreate_list', 1, 3]

assignment = ['lValue', 'exp']
dict['assignment'] = ['lValue ASSIGN exp', 1, 3]
ifThenElse = ['exp', 'exp', 'exp']
dict['ifThenElse'] = ['IF exp THEN exp ELSE exp', 2, 4, 6]
ifThen = ['exp', 'exp']
dict['ifThen'] = ['IF exp THEN exp', 2, 4]
whileExp = ['exp', 'exp']
dict['whileExp'] = ['WHILE exp DO exp', 2, 4]
forExp = ['id', 'exp', 'exp', 'exp']
dict['forExp'] = ['FOR ID ASSIGN exp TO exp DO exp', 2, 4, 6, 8]
letExp = ['dec', 'dec_list', 'semicolon_exp_list']
dict['letExp'] = ['LET dec dec_list IN semicolon_exp_list END', 2, 3, 5]
idlValue = ['id']
dict['idlValue'] = ['ID', 1]
tyIdTy = ['tyId']
dict['tyIdTy'] = ['ID', 1]
breakExp = []
dict['breakExp'] = ['']


# -------------------------------------------------
# ==========Head File print the structures' definition code===========
f = open('datatype.h', 'w')

# includes
print(
'''#ifndef DATATYPE_H
#define DATATYPE_H
#include <string>
''', file=f)

# define the labels
cnt = 0

for item in token_labels:
	print('const int ' + item + '_label = ' + str(cnt) + ';', file=f)
	cnt = cnt + 1

for item in list1:
	print('const int ' + item + '_label = ' + str(cnt) + ';', file=f)
	cnt = cnt + 1

for item in list2:
	print('const int ' + item + '_label = ' + str(cnt) + ';', file=f)
	cnt = cnt + 1

for item in list3:
	print('const int ' + item + '_label = ' + str(cnt) + ';', file=f)
	cnt = cnt + 1
	
print(file=f)
	
# base class is Type
print(
'''struct Type {
	int label;
};
''', file=f)

# tokens
for i in range(len(token_labels)):
	item = token_labels[i]
	type = token_types[i]
	print('struct ' + item + '_Type : public Type {', file=f)
	if type != '':
		print('\t' + type, 'val;', file=f)
		print('\t' + item + '_Type(' + type + ');', file=f)
	else:
		print('\t' + item + '_Type(' + type + ');', file=f)
	print('};', file=f)

for listname in list1:
	fatherclassname = listname + '_Type' # class name
	if listname == 'Type':
		fatherclassname = 'Type'
	for item in locals()[listname]:
		classname = item + '_Type'
		if (item in list3) or (item in list2):
			print('struct', classname, ': public', fatherclassname, '{', file=f)
			# members
			for index in range(len(locals()[item])):
				t = locals()[item][index] + '_Type'
				print('\t' + t + ' *child' + str(index) + ';', file=f)
			# constructor
			print('\t' + classname + '(', file=f)
			for index in range(len(locals()[item])):
				t = locals()[item][index] + '_Type'
				if index < len(locals()[item])-1:
					print('\t\t' + t + ' *p' + str(index) + ',', file=f)
				else:
					print('\t\t' + t + ' *p' + str(index), file=f)
			print('\t);', file=f)
			print('};', file=f)
		else:
			print('struct', classname, ': public', fatherclassname, '{};', file=f)

print('#endif', file=f)
f.close()

# ==============CPP File print the constructors' definition code==============
f = open('datatype.cpp', 'w')	
print(
'''#include "datatype.h"
using namespace std;
''', file=f)

# tokens
for i in range(len(token_labels)):
	item = token_labels[i]
	type = token_types[i]
	if type != '':
		print(item + '_Type::' + item + '_Type(' + type + ' val) {', file=f)
		print('\t' + 'this->val = val;', file=f)
		print('\t' + 'label = ' + item + '_label;', file=f)
	else:
		print(item + '_Type::' + item + '_Type() {', file=f)
		print('\t' + 'label = ' + item + '_label;', file=f)
	print('}', file=f)
			
for item in list2:
	classname = item + '_Type'
	print(classname + '::' + classname + '(', file=f)
	for index in range(len(locals()[item])):
		t = locals()[item][index] + '_Type'
		if index < len(locals()[item])-1:
			print('\t' + t + ' *p' + str(index) + ',', file=f)
		else:
			print('\t' + t + ' *p' + str(index), file=f)
	print(') {', file=f)
	print('\t' + 'label = ' + item + '_label;', file=f)
	for index in range(len(locals()[item])):
		t = locals()[item][index] + '_Type'
		print('\t' + 'child' + str(index) + ' = p' + str(index) + ';', file=f)
	print('}\n', file=f)

for item in list3:
	classname = item + '_Type'
	print(classname + '::' + classname + '(', file=f)
	for index in range(len(locals()[item])):
		t = locals()[item][index] + '_Type'
		if index < len(locals()[item])-1:
			print('\t' + t + ' *p' + str(index) + ',', file=f)
		else:
			print('\t' + t + ' *p' + str(index), file=f)
	print(') {', file=f)
	print('\t' + 'label = ' + item + '_label;', file=f)
	for index in range(len(locals()[item])):
		t = locals()[item][index] + '_Type'
		print('\t' + 'child' + str(index) + ' = p' + str(index) + ';', file=f)
	print('}\n', file=f)
	
f.close()

# Yacc file
f = open('tiger.y', 'w')

print(
'''%{
#include "tiger.h"
#include <iostream>

using namespace std;

exp_Type *absyn_root = nullptr;

%}
/* tokens */
%token ID STRINGLIT INTLIT NIL
 
/* Reserved words */
%token
WHILE FOR TO BREAK LET IN END FUNCTION VAR TYPE ARRAY IF THEN ELSE DO OF WITH

/* Punctuation symbols */
%token
COMMA COLON SEMICOLON LPAREN RPAREN LBRACK RBRACK LBRACE RBRACE DOT ASSIGN

/* operators */
%left OR AND
%nonassoc EQ NEQ GT LT GE LE
%left PLUS MINUS
%left TIMES DIV
%right UMINUS

%start program

%% /*------------------------------------------*/

program : exp {
	absyn_root = (exp_Type *)$1;
}

'''
, file=f)

# branches
for item in list1:
	if item != 'Type': 
		print(item, file=f)
		flag = False
		for i in locals()[item]:
			if flag:
				print('|', i, '{$$ = $1;}', file=f)
			else:
				print(':', i, '{$$ = $1;}', file=f)
				flag = True
		print(file=f)
		
# grammar
for item in list2:
	if item == 'negation':
		print(item + ':', dict[item][0], '%prec UMINUS {', file=f)
	else:	
		print(item + ':', dict[item][0], '{', file=f)
	# print('\t', '''cout << "''' + item + '''"<< endl;''', file=f)
	print('\t' + '$$ = new ' + item + '_Type(', file=f)
	for i in range(len(locals()[item])):
		t = locals()[item][i] + '_Type'
		if i < len(locals()[item])-1:
			print('\t\t' + '(' + t +' *)$' + str(dict[item][i+1]) + ',', file=f)
		else:
			print('\t\t' + '(' + t +' *)$' + str(dict[item][i+1]), file=f)
	print('\t);', file=f)
	print('}', file=f)
	
for item in list3:
	for l in dict[item]:
		print(item + ':', l[0], '{', file=f)
		print('\t' + '$$ = new ' + item + '_Type(', file=f)
		for i in range(len(locals()[item])):
			t = locals()[item][i] + '_Type'
			if i < len(locals()[item])-1:
				print('\t\t' + '(' + t +' *)$' + str(l[i+1]) + ',', file=f)
			else:
				print('\t\t' + '(' + t +' *)$' + str(l[i+1]), file=f)
		print('\t);', file=f)
		print('}', file=f)
	
# Part 3
print(
'''%% /*------------------------------------------*/
void yyerror(const char *s) {
    cerr << s << endl;
}
''', file=f)
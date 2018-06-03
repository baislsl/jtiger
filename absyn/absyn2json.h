#ifndef ABSYN2JSON_H
#define ABSYN2JSON_H
#include <iostream>
#include <cstdlib>
#include <string>
#include <fstream>

#define RAPIDJSON_HAS_STDSTRING 1

#include "rapidjson/document.h"
#include "rapidjson/prettywriter.h"
#include "rapidjson/stringbuffer.h"
#include "tiger.h"

using namespace std;
using namespace rapidjson;

extern exp_Type* absyn_root;
int len;
char buffer[20];
Document doc;

void ProcessIDLvalue(Value& obj, idlValue_Type* structPtr);
void ProcessLvalue(Value& obj, lValue_Type* structPtr);
void ProcessSubscript(Value& obj, subscript_Type* structPtr);
void ProcessFieldExp(Value& obj, fieldExp_Type* structPtr);
void ProcessNil(Value& obj, nilExp_Type* structPtr);
void ProcessIntExp(Value& obj, intLitExp_Type* structPtr);
void ProcessStrExp(Value& obj, stringLitExp_Type* structPtr);
void ProcessSeqExp(Value& obj, seqExp_Type* structPtr);
void ProcessSemiExpList(Value& obj, semicolon_exp_list_Type* structPtr);
void ProcessNonemptySemiExpList(Value& obj, nonempty_semicolon_exp_list_Type* structPtr);
void ProcessNeg(Value& obj, negation_Type* structPtr);
void ProcessCall(Value& obj, callExp_Type* structPtr);
void ProcessCommaExpList(Value& obj, comma_exp_list_Type* structPtr);
void ProcessNonemptyCommaExpList(Value& obj, nonempty_comma_exp_list_Type* structPtr);
void ProcessInfixExp(Value& obj, infixExp_Type* structPtr);
void ProcessArrCrt(Value& obj, arrCreate_Type* structPtr);
void ProcessRecCrt(Value& obj, recCreate_Type* structPtr);
void ProcessFieldCrtList(Value& obj, fieldCreate_list_Type* structPtr);
void ProcessNonemptyFieldCrtList(Value& obj, nonempty_fieldCreate_list_Type* structPtr);
void ProcessFieldCrt(Value& obj, fieldCreate_Type* structPtr);
void ProcessAssign(Value& obj, assignment_Type* structPtr);
void ProcessIfThenElse(Value& obj, ifThenElse_Type* structPtr);
void ProcessIfThen(Value& obj, ifThen_Type* structPtr);
void ProcessWhile(Value& obj, whileExp_Type* structPtr);
void ProcessFor(Value& obj, forExp_Type* structPtr);
void ProcessBreak(Value& obj, breakExp_Type* structPtr);
void ProcessLet(Value& obj, letExp_Type* structPtr);
void ProcessDec(Value& obj, dec_Type* structPtr);
void ProcessTyDec(Value& obj, tyDec_Type* structPtr);
void ProcessTy(Value& obj, ty_Type* structPtr);
void ProcessFieldDecList(Value& obj, fieldDec_list_Type* structPtr);
void ProcessNonemptyFieldDecList(Value& obj, nonempty_fieldDec_list_Type* structPtr);
void ProcessFieldDec(Value& obj, fieldDec_Type* structPtr);
void ProcessVarDec(Value& obj, varDec_Type* structPtr);
void ProcessFunDec(Value& obj, funDec_Type* structPtr);
void ProcessDecList(Value& obj, dec_list_Type* structPtr);
void ProcessExp(Value& obj, exp_Type* structPtr);

void ProcessIDLvalue(Value& obj, idlValue_Type* structPtr)
{
	obj.AddMember("class", "IDOnlyLvalue", doc.GetAllocator());
	obj.AddMember("id", structPtr->child0->val, doc.GetAllocator());
}

void ProcessLvalue(Value& obj, lValue_Type* structPtr)
{
	switch (structPtr->label)
	{
	case idlValue_label:
		ProcessIDLvalue(obj, (idlValue_Type *)structPtr);
		break;
	case lValueSubscript_label:
	case idSubscript_label:
		ProcessSubscript(obj, (subscript_Type *)structPtr);
		break;
	case fieldExp_label:
		ProcessFieldExp(obj, (fieldExp_Type *)structPtr);
		break;
	default:
		break;
	}
}

void ProcessSubscript(Value& obj, subscript_Type* structPtr)
{
	obj.AddMember("class", "Subscript", doc.GetAllocator());
	Value lvalueObj(kObjectType);
	Value expObj(kObjectType);
	switch (structPtr->label)
	{
	case lValueSubscript_label:
		ProcessLvalue(lvalueObj, ((lValueSubscript_Type *)structPtr)->child0);
		ProcessExp(expObj, ((lValueSubscript_Type *)structPtr)->child1);
		obj.AddMember("Lvalue", lvalueObj, doc.GetAllocator());
		obj.AddMember("exp", expObj, doc.GetAllocator());
		break;
	case idSubscript_label:
		ProcessExp(expObj, ((idSubscript_Type *)structPtr)->child1);
		lValue_Type *lvaluePtr = new idlValue_Type(((idSubscript_Type *)structPtr)->child0);
		ProcessLvalue(lvalueObj, lvaluePtr);
		obj.AddMember("Lvalue", lvalueObj, doc.GetAllocator());
		obj.AddMember("exp", expObj, doc.GetAllocator());
	}
}

void ProcessFieldExp(Value& obj, fieldExp_Type* structPtr)
{
	obj.AddMember("class", "FieldExp", doc.GetAllocator());
	Value lvalueObj(kObjectType);
	ProcessLvalue(lvalueObj, structPtr->child0);
	obj.AddMember("Lvalue", lvalueObj, doc.GetAllocator());
	obj.AddMember("id", structPtr->child1->val, doc.GetAllocator());
}

void ProcessNil(Value&obj, nilExp_Type* structPtr)
{
	obj.AddMember("class", "Nil", doc.GetAllocator());
}

void ProcessIntExp(Value& obj, intLitExp_Type* structPtr)
{
	obj.AddMember("class", "IntLit", doc.GetAllocator());
	obj.AddMember("value", structPtr->child0->val, doc.GetAllocator());
}

void ProcessStrExp(Value& obj, stringLitExp_Type* structPtr)
{
	obj.AddMember("class", "StringLit", doc.GetAllocator());
	obj.AddMember("value", structPtr->child0->val, doc.GetAllocator());
}

void ProcessSeqExp(Value& obj, seqExp_Type* structPtr)
{
	ProcessSemiExpList(obj, structPtr->child0);
}

void ProcessSemiExpList(Value& obj, semicolon_exp_list_Type* structPtr)
{
	switch (structPtr->label)
	{
	case empty_semicolon_exp_list_label:
		break;
	default:
		ProcessNonemptySemiExpList(obj, (nonempty_semicolon_exp_list_Type *)structPtr);
		break;
	}
}

void ProcessNonemptySemiExpList(Value& obj, nonempty_semicolon_exp_list_Type* structPtr)
{
	Value expObj(kObjectType);
	switch (structPtr->label)
	{
	case single_semicolon_exp_list_label:
		ProcessExp(expObj, ((single_semicolon_exp_list_Type *)structPtr)->child0);
		obj.AddMember("", expObj, doc.GetAllocator());
		break;
	case multi_semicolon_exp_list_label:
		ProcessExp(expObj, ((multi_semicolon_exp_list_Type *)structPtr)->child0);
		obj.AddMember("", expObj, doc.GetAllocator());
		ProcessNonemptySemiExpList(obj, ((multi_semicolon_exp_list_Type *)structPtr)->child1);
		break;
	}
}

void ProcessNeg(Value& obj, negation_Type* structPtr)
{
	obj.AddMember("class", "Negation", doc.GetAllocator());
	Value expObj(kObjectType);
	ProcessExp(expObj, structPtr->child0);
	obj.AddMember("exp", expObj, doc.GetAllocator());
}

void ProcessCall(Value& obj, callExp_Type* structPtr)
{
	obj.AddMember("class", "Call", doc.GetAllocator());
	obj.AddMember("id", structPtr->child0->val, doc.GetAllocator());
	Value callArr(kArrayType);
	ProcessCommaExpList(callArr, structPtr->child1);
	obj.AddMember("exps", callArr, doc.GetAllocator());
}

void ProcessCommaExpList(Value& obj, comma_exp_list_Type* structPtr)
{
	switch (structPtr->label)
	{
	case empty_comma_exp_list_label:
		break;
	default:
		ProcessNonemptyCommaExpList(obj, (nonempty_comma_exp_list_Type *)structPtr);
		break;
	}
}

void ProcessNonemptyCommaExpList(Value& obj, nonempty_comma_exp_list_Type* structPtr)
{
	Value expObj(kObjectType);
	switch (structPtr->label)
	{
	case single_comma_exp_list_label:
		ProcessExp(expObj, ((single_comma_exp_list_Type *)structPtr)->child0);
		obj.AddMember("", expObj, doc.GetAllocator());
		break;
	case multi_comma_exp_list_label:
		ProcessExp(expObj, ((multi_comma_exp_list_Type *)structPtr)->child0);
		obj.AddMember("", expObj, doc.GetAllocator());
		ProcessNonemptyCommaExpList(obj, ((multi_comma_exp_list_Type *)structPtr)->child1);
		break;
	}
}

void ProcessInfixExp(Value& obj, infixExp_Type* structPtr)
{
	obj.AddMember("class", "InfixExp", doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType);
	ProcessExp(expObj1, structPtr->child0);
	ProcessExp(expObj2, structPtr->child2);
	obj.AddMember("exp1", expObj1, doc.GetAllocator());
	obj.AddMember("exp2", expObj2, doc.GetAllocator());
	obj.AddMember("infixOp", structPtr->child1->val, doc.GetAllocator());
}

void ProcessArrCrt(Value& obj, arrCreate_Type* structPtr)
{
	obj.AddMember("class", "ArrCreate", doc.GetAllocator());
	obj.AddMember("tyId", structPtr->child0->val, doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType);
	ProcessExp(expObj1, structPtr->child1);
	ProcessExp(expObj2, structPtr->child2);
	obj.AddMember("exp1", expObj1, doc.GetAllocator());
	obj.AddMember("exp2", expObj2, doc.GetAllocator());
}

void ProcessRecCrt(Value& obj, recCreate_Type* structPtr)
{
	obj.AddMember("class", "RecCreate", doc.GetAllocator());
	obj.AddMember("tyId", structPtr->child0->val, doc.GetAllocator());
	Value fieldCrtList(kArrayType);
	ProcessFieldCrtList(fieldCrtList, structPtr->child1);
	obj.AddMember("fieldCreates", fieldCrtList, doc.GetAllocator());
}

void ProcessFieldCrtList(Value& obj, fieldCreate_list_Type* structPtr)
{
	switch (structPtr->label)
	{
	case empty_fieldCreate_list_label:
		break;
	default:
		ProcessNonemptyFieldCrtList(obj, (nonempty_fieldCreate_list_Type *)structPtr);
		break;
	}
}

void ProcessNonemptyFieldCrtList(Value& obj, nonempty_fieldCreate_list_Type* structPtr)
{
	Value fieldCrtObj(kObjectType);
	switch (structPtr->label)
	{
	case single_fieldCreate_list_label:
		ProcessFieldCrt(fieldCrtObj, ((single_fieldCreate_list_Type *)structPtr)->child0);
		obj.AddMember("", fieldCrtObj, doc.GetAllocator());
		break;
	case multi_fieldCreate_list_label:
		ProcessFieldCrt(fieldCrtObj, ((multi_fieldCreate_list_Type *)structPtr)->child0);
		obj.AddMember("", fieldCrtObj, doc.GetAllocator());
		ProcessNonemptyFieldCrtList(obj, ((multi_fieldCreate_list_Type *)structPtr)->child1);
		break;
	}
}

void ProcessFieldCrt(Value& obj, fieldCreate_Type* structPtr)
{
	obj.AddMember("class", "FieldCreate", doc.GetAllocator());
	obj.AddMember("id", structPtr->child0->val, doc.GetAllocator());
	Value expObj(kObjectType);
	ProcessExp(expObj, structPtr->child1);
	obj.AddMember("exp", expObj, doc.GetAllocator());
}

void ProcessAssign(Value& obj, assignment_Type* structPtr)
{
	obj.AddMember("class", "Assignment", doc.GetAllocator());
	Value lvalueObj(kObjectType);
	Value expObj(kObjectType);
	ProcessLvalue(lvalueObj, structPtr->child0);
	ProcessExp(expObj, structPtr->child1);
	obj.AddMember("Lvalue", lvalueObj, doc.GetAllocator());
	obj.AddMember("exp", expObj, doc.GetAllocator());
}

void ProcessIfThenElse(Value& obj, ifThenElse_Type* structPtr)
{
	obj.AddMember("class", "IfThenElse", doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType), expObj3(kObjectType);
	ProcessExp(expObj1, structPtr->child0);
	ProcessExp(expObj2, structPtr->child1);
	ProcessExp(expObj3, structPtr->child2);
	obj.AddMember("ifExp", expObj1, doc.GetAllocator());
	obj.AddMember("thenExp", expObj2, doc.GetAllocator());
	obj.AddMember("elseExp", expObj3, doc.GetAllocator());
}

void ProcessIfThen(Value& obj, ifThen_Type* structPtr)
{
	obj.AddMember("class", "IfThen", doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType);
	ProcessExp(expObj1, structPtr->child0);
	ProcessExp(expObj2, structPtr->child1);
	obj.AddMember("ifExp", expObj1, doc.GetAllocator());
	obj.AddMember("thenExp", expObj2, doc.GetAllocator());
}

void ProcessWhile(Value& obj, whileExp_Type* structPtr)
{
	obj.AddMember("class", "While", doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType);
	ProcessExp(expObj1, structPtr->child0);
	ProcessExp(expObj2, structPtr->child1);
	obj.AddMember("whileExp", expObj1, doc.GetAllocator());
	obj.AddMember("doExp", expObj2, doc.GetAllocator());
}

void ProcessFor(Value& obj, forExp_Type* structPtr)
{
	obj.AddMember("class", "ForExp", doc.GetAllocator());
	obj.AddMember("id", structPtr->child0->val, doc.GetAllocator());
	Value expObj1(kObjectType), expObj2(kObjectType), expObj3(kObjectType);
	ProcessExp(expObj1, structPtr->child1);
	ProcessExp(expObj2, structPtr->child2);
	ProcessExp(expObj3, structPtr->child3);
	obj.AddMember("fromExp", expObj1, doc.GetAllocator());
	obj.AddMember("toExp", expObj2, doc.GetAllocator());
	obj.AddMember("doExp", expObj3, doc.GetAllocator());
}

void ProcessBreak(Value& obj, breakExp_Type* structPtr)
{
	obj.AddMember("class", "BreakExp", doc.GetAllocator());
}

void ProcessLet(Value& obj, letExp_Type* structPtr)
{
	obj.AddMember("class", "LetExp", doc.GetAllocator());
	Value decArr(kArrayType), expArr(kArrayType);
	Value decObj(kObjectType);
	ProcessDec(decObj, structPtr->child0);
	decArr.AddMember("", decObj, doc.GetAllocator());
	ProcessDecList(decArr, structPtr->child1);
	obj.AddMember("decs", decArr, doc.GetAllocator());
	ProcessSemiExpList(expArr, structPtr->child2);
	obj.AddMember("exps", expArr, doc.GetAllocator());
}

void ProcessDec(Value& obj, dec_Type* structPtr)
{
	switch (structPtr->label)
	{
	case tyIdTy_label:
	case arrTy_label:
	case recTy_label:
		ProcessTyDec(obj, (tyDec_Type *)structPtr);
		break;
	case void_varDec_label:
	case type_varDec_label:
		ProcessVarDec(obj, (varDec_Type *)structPtr);
		break;
	case void_funDec_label:
	case type_funDec_label:
		ProcessFunDec(obj, (funDec_Type *)structPtr);
		break;
	}
}

void ProcessTyDec(Value& obj, tyDec_Type* structPtr)
{
	obj.AddMember("class", "TyDec", doc.GetAllocator());
	obj.AddMember("tyId", structPtr->child0->val, doc.GetAllocator());
	Value tyObj(kObjectType);
	ProcessTy(tyObj, structPtr->child1);
	obj.AddMember("ty", tyObj, doc.GetAllocator());
}

void ProcessTy(Value& obj, ty_Type* structPtr)
{
	Value decArr(kArrayType);
	switch (structPtr->label)
	{
	case tyIdTy_label:
		obj.AddMember("class", "IdOnlyTy", doc.GetAllocator());
		obj.AddMember("id", ((tyIdTy_Type *)structPtr)->child0->val, doc.GetAllocator());
		break;
	case arrTy_label:
		obj.AddMember("class", "ArrTy", doc.GetAllocator());
		obj.AddMember("tyid", ((arrTy_Type *)structPtr)->child0->val, doc.GetAllocator());
		break;
	case recTy_label:
		obj.AddMember("class", "RecTy", doc.GetAllocator());
		ProcessFieldDecList(decArr, ((recTy_Type *)structPtr)->child0);
		obj.AddMember("decs", decArr, doc.GetAllocator());
		break;
	}
}

void ProcessFieldDecList(Value& obj, fieldDec_list_Type* structPtr)
{
	switch (structPtr->label)
	{
	case empty_fieldDec_list_label:
		break;
	default:
		ProcessNonemptyFieldDecList(obj, (nonempty_fieldDec_list_Type *)structPtr);
		break;
	}
}

void ProcessNonemptyFieldDecList(Value& obj, nonempty_fieldDec_list_Type* structPtr)
{
	Value fieldDecObj(kObjectType);
	switch (structPtr->label)
	{
	case single_fieldDec_list_label:
		ProcessFieldDec(fieldDecObj, ((single_fieldDec_list_Type *)structPtr)->child0);
		obj.AddMember("", fieldDecObj, doc.GetAllocator());
		break;
	case multi_fieldDec_list_label:
		ProcessFieldDec(fieldDecObj, ((multi_fieldDec_list_Type *)structPtr)->child0);
		obj.AddMember("", fieldDecObj, doc.GetAllocator());
		ProcessNonemptyFieldDecList(obj, ((multi_fieldDec_list_Type *)structPtr)->child1);
		break;
	}
}

void ProcessFieldDec(Value& obj, fieldDec_Type* structPtr)
{
	obj.AddMember("class", "FieldDec", doc.GetAllocator());
	obj.AddMember("id", structPtr->child0->val, doc.GetAllocator());
	obj.AddMember("tyid", structPtr->child1->val, doc.GetAllocator());
}

void ProcessVarDec(Value& obj, varDec_Type* structPtr)
{
	obj.AddMember("class", "VarDec", doc.GetAllocator());
	Value expObj(kObjectType);
	switch (structPtr->label)
	{
	case void_varDec_label:
		obj.AddMember("id", ((void_varDec_Type *)structPtr)->child0->val, doc.GetAllocator());
		ProcessExp(expObj, ((void_varDec_Type *)structPtr)->child1);
		obj.AddMember("exp", expObj, doc.GetAllocator());
		break;
	case type_varDec_label:
		obj.AddMember("id", ((type_varDec_Type *)structPtr)->child0->val, doc.GetAllocator());
		obj.AddMember("tyId", ((type_varDec_Type *)structPtr)->child1->val, doc.GetAllocator());
		ProcessExp(expObj, ((type_varDec_Type *)structPtr)->child2);
		obj.AddMember("exp", expObj, doc.GetAllocator());
		break;
	}
}

void ProcessFunDec(Value& obj, funDec_Type* structPtr)
{
	obj.AddMember("class", "FunDec", doc.GetAllocator());
	Value decArr(kArrayType);
	Value expObj(kObjectType);
	switch (structPtr->label)
	{
	case void_funDec_label:
		obj.AddMember("id", ((void_funDec_Type *)structPtr)->child0->val, doc.GetAllocator());
		ProcessFieldDecList(decArr, ((void_funDec_Type *)structPtr)->child1);
		obj.AddMember("decs", decArr, doc.GetAllocator());
		ProcessExp(expObj, ((void_funDec_Type *)structPtr)->child2);
		obj.AddMember("exp", expObj, doc.GetAllocator());
		break;
	case type_funDec_label:
		obj.AddMember("id", ((type_funDec_Type *)structPtr)->child0->val, doc.GetAllocator());
		ProcessFieldDecList(decArr, ((type_funDec_Type *)structPtr)->child1);
		obj.AddMember("decs", decArr, doc.GetAllocator());
		obj.AddMember("tyid", ((type_funDec_Type *)structPtr)->child2->val, doc.GetAllocator());
		ProcessExp(expObj, ((type_funDec_Type *)structPtr)->child3);
		obj.AddMember("exp", expObj, doc.GetAllocator());
		break;
	}
}

void ProcessDecList(Value& obj, dec_list_Type* structPtr)
{
	Value decObj(kObjectType);
	Value decArr(kArrayType);
	switch (structPtr->label)
	{
	case empty_dec_list_label:
		break;
	case nonempty_dec_list_label:
		ProcessDec(decObj, ((nonempty_dec_list_Type *)structPtr)->child0);
		obj.AddMember("", decObj, doc.GetAllocator());
		ProcessDecList(decArr, ((nonempty_dec_list_Type *)structPtr)->child1);
		break;
	}
}

void ProcessExp(Value& obj, exp_Type* structPtr)
{
	Value lvalueObj(kObjectType);
	Value seqArr(kArrayType);
	switch (structPtr->label)
	{
	case idlValue_label:
	case lValueSubscript_label:
	case idSubscript_label:
	case fieldExp_label:
		ProcessLvalue(lvalueObj, (lValue_Type *)structPtr);
		obj.AddMember("Lvalue", lvalueObj, doc.GetAllocator());
		break;
	case nilExp_label:
		ProcessNil(obj, (nilExp_Type *)structPtr);
		break;
	case intLitExp_label:
		ProcessIntExp(obj, (intLitExp_Type *)structPtr);
		break;
	case stringLitExp_label:
		ProcessStrExp(obj, (stringLitExp_Type *)structPtr);
		break;
	case seqExp_label:
		ProcessSeqExp(seqArr, (seqExp_Type *)structPtr);
		obj.AddMember("exps", seqArr, doc.GetAllocator());
		break;
	case negation_label:
		ProcessNeg(obj, (negation_Type *)structPtr);
		break;
	case callExp_label:
		ProcessCall(obj, (callExp_Type *)structPtr);
		break;
	case plusExp_label:
	case minusExp_label:
	case timesExp_label:
	case divExp_label:
	case eqExp_label:
	case neqExp_label:
	case andExp_label:
	case orExp_label:
	case gtExp_label:
	case ltExp_label:
	case geExp_label:
	case leExp_label:
		ProcessInfixExp(obj, (infixExp_Type *)structPtr);
		break;
	case arrCreate_label:
		ProcessArrCrt(obj, (arrCreate_Type *)structPtr);
		break;
	case recCreate_label:
		ProcessRecCrt(obj, (recCreate_Type *)structPtr);
		break;
	case assignment_label:
		ProcessAssign(obj, (assignment_Type *)structPtr);
		break;
	case ifThenElse_label:
		ProcessIfThenElse(obj, (ifThenElse_Type *)structPtr);
		break;
	case ifThen_label:
		ProcessIfThen(obj, (ifThen_Type *)structPtr);
		break;
	case whileExp_label:
		ProcessWhile(obj, (whileExp_Type *)structPtr);
		break;
	case forExp_label:
		ProcessFor(obj, (forExp_Type *)structPtr);
		break;
	case breakExp_label:
		ProcessBreak(obj, (breakExp_Type *)structPtr);
		break;
	case letExp_label:
		ProcessLet(obj, (letExp_Type *)structPtr);
		break;
	case fieldCreate_label:
		ProcessFieldCrt(obj, (fieldCreate_Type *)structPtr);
		break;
	}
}

int absyn2json(void)
{
	StringBuffer sb;
	PrettyWriter<StringBuffer> writer(sb);
	doc.SetObject();
	doc.AddMember("class", "Program", doc.GetAllocator());
	Value expObj(kObjectType);
	ProcessExp(expObj, absyn_root);
	doc.AddMember("exp", expObj, doc.GetAllocator());
	doc.Accept(writer);
	cout << sb.GetString() << endl;
	ofstream output("output.json");
	output << sb.GetString() << endl;

	return 0;
}
#endif
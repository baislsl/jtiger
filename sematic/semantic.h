#include <cstdlib>
#include <string>
#include "tiger.h"
#include "symbol.h"
#include "env.h"
#include "type.h"

using namespace std;

extern exp_Type* absyn_root;

void transProg(exp_Type* structPtr);

struct expTy{
	Tr_exp exp;
	Ty* type;
	expTy(Tr_exp exp, Ty* type)
	{
		this->exp = exp;
		this->type = type;
	}
};

struct expTy transVar(S_table venv, S_table tenv, exp_Type* structPtr)
{
	switch (structPtr->label)
	{
	case idlValue_label:
		idlValue_Type* idlvptr = (idlValue_Type *)structPtr; 
		E_enventry* x = S_look(venv, Symbol(idlvptr->child0->val));
		if (x && x->label == varEnv_label)
			return expTy(NULL, x->type);
		else
			printf("variable %s is undefined.\n", idlvptr->child0->val.c_str());
		break;
	case idSubscript_label:
		idSubscript_Type* idsptr = (idSubscript_Type *)structPtr;
		E_enventry* x = S_look(venv, Symbol(idsptr->child0->val));
		if (x && x->label == varEnv_label)
		{
			if (x->type->label != TyArr_label)
				printf("%s is not an array.\n", idsptr->child0->val.c_str());
			else
			{
				if (idsptr->child1->label != intLit_label)
					printf("index isn't a integer literal.\n")
				else
					return expTy(NULL, (TyArr *)(x->type)->type);
			}
		}
		else
			printf("variable %s is undefined.\n", idsptr->child0->val.c_str());
		break;
	case lValueSubscript_label:
		bool flag = true;
		lValueSubscript_Type* lvsptr = (lValueSubscript_Type *)structPtr;
		switch (lvsptr->child0->label)
		{
		case idSubscript_label:
		case lValueSubscript_label:
			printf("illegal array variable.\n");
			break;
		case idlValue_label:
			idlValue_Type* tmpptr = (idlValue_Type *)(lvsptr->child0);
			E_enventry* x = S_look(venv, Symbol(tmpptr->child0->val));
			if (x && x->label == varEnv_label)
			{
				if (x->type->label != TyArr_label)
					printf("%s is not an array.\n", tmpptr->child0->val.c_str());
				else
				{
					if (lvsptr->child1->label != intLit_label)
						printf("index isn't a integer literal.\n")
					else
						return expTy(NULL, (TyArr *)(x->type)->type);
				}
			}
			else
				printf("variable %s is undefined.\n", tmpptr->child0->val.c_str());
		case fieldExp_label:

		}
	case fieldExp_label:
		fieldExp_Type* fptr = (fieldExp_Type *)structPtr;
		struct expTy lvalue = transVar(venv, tenv, (lValue_Type *)fptr->child0);
		if (lvalue.type->label != TyRec_label)
			printf("illegal record varaible.\n");
		else
		{
			TyRec* RecPtr = (TyRec *)lvalue.type;
			TyFieldList* tmpField = RecPtr->fields;
			while (tmpField)
			{
				if (fptr->child1->val == tmpField->head->sym->name)
					return expTy(NULL, tmpField->head->ty);
				tmpField = tmpField->next;
			}
			printf("unknown data field of this record.\n");
		}
	}
}

struct expTy transExp(S_table venv, S_table tenv, exp_Type* structPtr)
{
	struct expTy tmpexpty;
	switch (structPtr->label)
	{
	case idlValue_label:
	case idSubscript_label:
	case lValueSubscript_label:
	case fieldExp_label:
		return transVar(venv, tenv, structPtr);
	case nilExp_label:
		return expTy(NULL, new TyNil());
	case intLit_label:
		return expTy(NULL, new TyInt());
	case stringLit_label:
		return expTy(NULL, new TyString());
	case seqExp_label:
		seqExp_Type* seq = (seqExp_Type *)structPtr;
		semicolon_exp_list_Type* semiExpList = seq->child0;
		if (semiExpList->label == empty_semicolon_exp_list_label)
			return expTy(NULL, new TyVoid());
		while (semiExpList->label != single_semicolon_exp_list_label)
		{
			transExp(venv, tenv, (multi_semicolon_exp_list_Type *)semiExpList->child0);
			semiExpList = (multi_semicolon_exp_list_Type *)semiExpList->child1;
		}
		tmpexpty = transExp(venv, tenv, (single_semicolon_exp_list_Type *)semiExpList->child0);
			return tmpexpty;
	case negation_label:
		negation_Type* neg = (negation_Type *)structPtr;
		return transExp(venv, tenv, neg->child0);
	case callExp_label:
		callExp_Type* fun = (callExp_Type *)structPtr;
		E_enventry* x = S_look(venv, Symbol(fun->child0->val));
		if (x && x->label == funEnv_label)
		{
			bool callFlag = true;
			TyList* formals = x->formals;
			comma_exp_list_Type* expList = fun->child1;
			if (formals->head->label == TyVoid_label)
			{
				if (expList->label == empty_comma_exp_list_label)
					return expTy(NULL, x->result);
				else
					printf("redundant parameters.\n");
			}
			else
			{
				if (expList->label == empty_comma_exp_list_label)
					printf("parameters required.\n");
				else
				{
					while (1)
					{
						Ty* tmp = formals->head;
						if (expList->label == single_comma_exp_list_label)
						{
							if (formals->next)
							{
								printf("need more parameters.\n");
								callFlag = false;
								break;
							}
							exp_Type* tmpExp = expList->child0;
							struct expTy expty = transExp(venv, tenv, tmpExp);
							if (expty.type->label == tmp->label)
								break;
							else
							{
								printf("incorrect parameter type.\n");
								callFlag = false;
								break;
							}
						}
						else
						{
							if (!(formals->next))
							{
								printf("too many parameters.\n");
								callFlag = false;
								break;
							}
							exp_Type* tmpExp = expList->child0;
							struct expTy expty = transExp(venv, tenv, tmpExp);
							if (expty->type->label == tmp->label)
							{
								formals = formals->next;
								expList = expList->child1;
								continue;
							}
							else
							{
								printf("incorrect parameter type.\n");
								callFlag = false;
								break;
							}
						}
					}
					if (callFlag)
						return expTy(NULL, x->result);
				}
			}
		}
		else
			printf("illegal function call.\n");
	case infixExp_label:
		infixExp_Type* infix = (infixExp_Type *)structPtr;
		struct expTy left = transExp(venv, tenv, infix->child0);
		struct expTy right = transExp(venv, tenv, infix->child2);
		if (infix->child1->val == "=" || infix->child1->val == "<>")
		{
			if (left.type->label != left.type->label)
				printf("expression type unmatched.\n");
			else
				return expTy(NULL, TyInt());
		}
		else
		{
			if (left.type->label != TyInt_label || right.type->label != TyInt_label)
				printf("integer required.\n");
			else
				return expTy(NULL, TyInt());
		}
	case arrCreate_label:
		arrCreate_Type* arrcrt = (arrCreate_Type *)structPtr;
		E_enventry* x = S_look(venv, Symbol(arrcrt->child0->val));
		if (x && x->label == varEnv_label)
		{
			if (x->type->label != TyArr_label)
				printf("%s is not an array type.\n", arrcrt->child0->val);
			else
			{
				struct expTy n = transExp(venv, tenv, arrcrt->child1);
				struct expTy v = transExp(venv, tenv, arrcrt->child2);
				if (n.type->label != TyInt_label)
					printf("illegal array create expression (n isn't integer).\n");
				else
				{
					if (v.type->label != ((TyArr *)(x->type))->type->label)
						printf("illegal array create expression (type unmatched).\n");
					else
						return expTy(NULL, x->type);
				}
			}
		} 
		else
			printf("variable %s is undefined.\n", arrcrt->child0->val);
	case recCreate_label:
		recCreate_Type* reccrt = (recCreate_Type *)structPtr;
		E_enventry* x = S_look(venv, Symbol(reccrt->child0->val));
		bool recFlag = true;
		if (x && x->label == varEnv_label)
		{
			if (x->type->label != TyRec_label)
				printf("%s is not a record type.\n", reccrt->child0->val);
			else
			{
				TyFieldList* fields = ((TyRec *)(x->type))->fields;
				fieldCreate_list_Type* fList = reccrt->child1;
				if (fields->head->label == TyVoid_label)
				{
					if (fList_label == empty_fieldCreate_list_label)
						return expTy(NULL, x->type);
					else
						printf("redundant fields.\n");
				}
				else
				{
					if (fList_label == empty_fieldCreate_list_label)
						printf("fields required.\n");
					else
					{
						while (1)
						{
							TyField* tmp = fields->head;
							if (fList->label == single_fieldCreate_list_label)
							{
								if (fields->next)
								{
									printf("need more fields.\n");
									recFlag = false;
									break;
								}
								fieldCreate_Type* fcrt = fList->child0;
								struct expTy expty = transExp(venv, tenv, fcrt->child1);
								if (GetSymName(tmp->sym) != fcrt->child0->val)
								{
									printf("no such field as %s.\n", fcrt->child0->val);
									recFlag = false;
									break;
								}
								if (expty.type->label == tmp->type->label)
									break;
								else
								{
									printf("incorrect field type.\n");
									recFlag = false;
									break;
								}
							}
							else
							{
								if (!(fields->next))
								{
									printf("too many fields.\n");
									recFlag = false;
									break;
								}
								fieldCreate_Type* fcrt = fList->child0;
								struct expTy expty = transExp(venv, tenv, fcrt->child1);
								if (GetSymName(tmp->sym) != fcrt->child0->val)
								{
									printf("no such field as %s.\n", fcrt->child0->val);
									recFlag = false;
									break;
								}
								if (expty->type->label == tmp->type->label)
								{
									fields = fields->next;
									fList = fList->child1;
									continue;
								}
								else
								{
									printf("incorrect field type.\n");
									recFlag = false;
									break;
								}
							}
						}
					if (recFlag)
						return expTy(NULL, x->type);
					}
				}
			}
		}
		else
			printf("variable %s is undefined.\n", reccrt->child0->val);
	case assignment_label:
		assignment_Type* assign = (assignment_Type *)structPtr;
		struct expTy left = transVar(venv, tenv, assign->child0);
		struct expTy right = transExp(venv, tenv, assign->child1);
		if (left.type->label != right.type->label)
		{
			printf("illegal assignment.\n");
		}
		else
			return expTy(NULL, new TyVoid());
		break;
	case ifThenElse_label:
		ifThenElse_Type* ifthenelse = (ifThenElse_Type *)structPtr;
		struct expTy cond = transExp(venv, tenv, ifthenelse->child0);
		struct expTy thendo = transExp(venv, tenv, ifthenelse->child1);
		struct expTy elsedo = transExp(venv, tenv, ifthenelse->child2);
		if (cond.type->label != TyInt_label)
		{
			printf("illegal condition.\n");
			break;
		} 
		else
		{
			if (thendo.type->label == elsedo.type->label)
			{
				if (thendo.type->label == TyVoid_label)
					return expTy(NULL, new TyVoid());
				else
					return expTy(NULL, thendo.type);
			}
			else
			{
				printf("unmatched expression types.\n");
				break;
			}
		}
	case ifThen_label:	
		ifThen_Type* ifthen = (ifThen_Type *)structPtr;
		cond = tranExp(venv, tenv, ifthen->child0);
		thendo = tranExp(venv, tenv, ifthen->child1);
		if (cond.type->label != TyInt_label)
		{
			printf("illegal condition.\n");
			break;
		} 
		if (thendo.type->label != TyVoid_label)
			printf("the expressions shouldn't have a value.\n");
		else
			return expTy(NULL, new TyVoid());
	case whileExp_label:
		whileExp* whi = (whileExp *)structPtr;
		struct expTy cond = transExp(venv, tenv, whi->child0);
		struct expTy whiexp = transExp(venv, tenv, whi->child1);
		if (cond.type->label != TyInt_label)
		{
			printf("illegal condition.\n");
			break;
		}
		if (whiexp.type->label != TyVoid_label)
			printf("the expressions shouldn't have a value.\n");
		else
			return expTy(NULL, new TyVoid());
	case forExp_label:
		forExp_Type* forptr = (forExp_Type *)structPtr;
		struct expTy lowbound = transExp(venv, tenv, forptr->child1);
		struct expTy upbound = transExp(venv, tenv, forptr->child2);
		struct expTy forexp = transExp(venv, tenv, forptr->child3);
		if (lowbound.type->label != TyInt_label || upbound->label != TyInt_label)
		{
			printf("illegal bound for For-loop.\n");
			break;
		}
		if (forexp.type->label != TyVoid_label)
			printf("the expressions shouldn't have a value.\n");
		else
			return expTy(NULL, new TyVoid());
	case letExp_label:
		letExp_Type* let = (letExp_Type *)structPtr;
		transDec(venv, tenv, structPtr->child0);
		...
		struct expTy letexps = transExp(venv, tenv, let->child2);
		return expTy(NULL, letexps.type);
	case breakExp_label:
		return expTy(NULL, new TyVoid());
	}
}

void transDec(S_table, venv, S_table tenv, Type* structPtr)
{
	/* to-do */

}
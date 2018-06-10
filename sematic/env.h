#include <string>
#include "tiger.h"
#include "type.h"

#define varEnv_label 1
#define funEnv_label 2

struct E_enventry {
	int label;
};

struct E_varEntry : public E_enventry{
	Ty* type;
	E_varEntry(Ty* ty)
	{
		label = varEnv_label;
		type = ty;
	}
};

struct E_funEntry : public E_enventry{
	TyList* formals;
	Ty* result;
	E_funEntry(TyList* formals, Ty* result)
	{
		label = funEnv_label;
		this->formals = formals;
		this->result = result;
	} 
};

S_table E_base_tenv(void)
{
	S_table t = S_empty();
	TyInt* intType = new TyInt();
	TyString* strType = new TyString();
	S_enter(t, Symbol("int"), intType);
	S_enter(t, Symbol("string"), strType);
	return t;
}

S_table E_base_venv(void)
{
	S_table t = S_empty();
	TyInt* intType = new TyInt();
	TyString* strType = new TyString();
	
	TyList* printFunList = new TyList(strType);
	E_funEntry* printFun = new E_funEntry(printFunList, new TyVoid());
	S_enter(t, Symbol("print"), printFun);

	TyList* flushFunList = new TyList(new TyVoid());
	E_funEntry* flushFun = new E_funEntry(flushFunList, new TyVoid());
	S_enter(t, Symbol("flush"), flushFun);

	TyList* getcharFunList = new TyList(new TyVoid());
	E_funEntry* getcharFun = new E_funEntry(getcharFunList, strType);
	S_enter(t, Symbol("getchar"), getcharFun);

	TyList* ordFunList = new TyList(strType);
	E_funEntry* ordFun = new E_funEntry(ordFunList, intType);
	S_enter(t, Symbol("ord"), ordFun);

	TyList* chrFunList = new TyList(intType);
	E_funEntry* chrFun = new E_funEntry(chrFunList, strType);
	S_enter(t, Symbol("chr"), chrFun);

	TyList* sizeFunList = new TyList(strType);
	E_funEntry* sizeFun = new E_funEntry(sizeFunList, intType);
	S_enter(t, Symbol("size"), sizeFun);

	TyList* substrFunList = new TyList(strType);
	appendTyList(substrFunList, intType);
	appendTyList(substrFunList, intType);
	E_funEntry* substrFun = new E_funEntry(substrFunList, strType);
	S_enter(t, Symbol("substring"), substrFun);

	TyList* concatFunList = new TyList(strType);
	appendTyList(concatFunList, strType);
	E_funEntry* concatFun = new E_funEntry(concatFunList, strType);
	S_enter(t, Symbol("concat"), concatFun);

	TyList* notFunList = new TyList(intType);
	E_funEntry notFun = new E_funEntry(notFunList, intType);
	S_enter(t, Symbol("not"), notFun);

	TyList* exitFunList = new TyList(intType);
	E_funEntry exitFun = new E_funEntry(exitFunList, new TyVoid());
	S_enter(t, Symbol("exit"), exitFun);

	return t;
}
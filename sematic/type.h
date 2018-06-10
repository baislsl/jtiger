#include "tiger.h"
#include "symbol.h"

using namespace std;

#define TyNil_label 0
#define TyInt_label 1
#define TyString_label 2
#define TyVoid_label 3
#define TyRec_label 4
#define TyArr_label 5
#define TyName_label 6

struct Ty{
	int label;
};

struct TyList{
	Ty* head;
	TyList* next;
	TyList(Ty* type)
	{
		head = type;
		next = NULL;
	}
};

void appendTyList(TyList* List, Ty* type)
{
	TyList* tmpList = List;
	while (tmpList->next)
		tmpList = tmpList->next;
	TyList* newTy = TyList(type);
	tmpList->next = newTy;
}

struct TyField{
	SymPtr sym;
	Ty* typr;
	TyField(SymPtr sym, Ty* type)
	{
		this->sym = sym;
		this->typr = type;
	}
};

struct TyFieldList{
	TyField* head;
	TyFieldList* next;
	TyFieldList(SymPtr sym, Ty* type)
	{
		head = TyField(sym, type);
		next = NULL;
	}
};

void appendTyFieldList(TyFieldList* List, SymPtr sym, Ty* type)
{
	TyFieldList* tmpList = List;
	while (tmpList->next)
		tmpList = tmpList->next;
	TyFieldList* newField = TyFieldList(sym, type);
	tmpList->next = newField;
}

struct TyNil : public Ty{
	TyNil(void) { label = TyNil_label; }
};

struct TyInt : public Ty{
	TyInt(void) { label = TyInt_label; }
};

struct TyString : public Ty{
	TyString(void) { label = TyString_label; }
};

struct TyVoid : public Ty{
	TyVoid(void) { label = TyVoid_label; }
};

struct TyRec : public Ty{
	TyFieldList* fields;
	TyRec(TyFieldList* fields) 
	{
		label = TyRec_label;
		this->fields = fields; 
	}
};

struct TyArr : public Ty{
	Ty* type;
	TyArr(Ty* type) 
	{ 
		label = TyArr_label; 
		this->type = type;
	}
};

struct TyName : public Ty{
	SymPtr sym;
	Ty* type;
	TyName(SymPtr sym, Ty* type) 
	{ 
		label = TyName_label;
		this->sym = sym;
		this->type = type;
	}
}
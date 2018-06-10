#include <iostream>
#include <cstdlib>
#include <string>
#include "tiger.h"
#include "table.h"

using namespace std;

extern exp_Type* absyn_root;

typedef struct Symbol *SymPtr;
struct Symbol{
	string name;
	SymPtr next;
};

static SymPtr mkSymbol(string name, SymPtr next)
{
	SymPtr s = (SymPtr)malloc(sizeof(struct Symbol));
	s->name = name;
	s->next = next;
	return s;
}

#define SIZE 109

static SymPtr symboltable[SIZE];

static unsigned int hash(const char *s0)
{
	unsigned int h = 0;
	char *s;
	for (s = (char *)s0; *s; s++)
		h = h * 65599 + *s;
	return h;
}

SymPtr Symbol(string name)
{
	int index = hash(name.c_str()) % SIZE;
	SymPtr syms = symboltable[index];
	SymPtr sym;
	for (sym = syms; sym; sym = sym->next)
		if (sym->name == name)
			return sym;
	sym = mkSymbol(name, syms);
	symboltable[index] = sym;
	return sym;
}

string GetSymName(SymPtr sym) { return sym->name; }

typedef struct TAB_table_ *S_table;
static struct Symbol mark = {"\"m\"", 0};

S_table S_empty(void) { return TAB_empty(); }

void S_enter(S_table t, SymPtr sym, void* value) { TAB_enter(t, (void *)sym, value); }

void* S_look(S_table t, SymPtr sym) { return TAB_look(t, (void *)sym); }

void S_begin(S_table t) { S_enter(t, &mark, NULL); }

void S_end(S_table t) 
{
	SymPtr sym;
	do {
		sym = TAB_pop(t);
	} while (sym != &mark);
}

void S_dump(S_table t, void (*show)(S_symbol sym, void *binding))
{ TAB_dump(t, (void (*)(void *, void *)) show); }

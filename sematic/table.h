#include <stdio.h>

#define TAB_SIZE 127

typedef struct binder_ *binder;
struct binder_{
	void *key;
	void *value;
	binder next;
	void* prevTop;
};

typedef struct TAB_table_ *TAB_table;
struct TAB_table_{
	binder table[TABSIZE];
	void *top;
};

static binder Binder(void* key, void* value; binder next, void* prevTop)
{
	binder bdr = malloc(sizeof(struct binder_));
	b->key = key;
	b->value = value;
	b->next = next;
	b->prevTop = prevTop;
	return b;
}

TAB_table TAB_empty(void);
void TAB_enter(TAB_table t, void* key, void* value);
void* TAB_look(TAB_table t, void* key);
void* TAB_pop(TAB_table t);
void TAB_dump(TAB_table t, void (*show)(void* key, void* value));

TAB_table TAB_empty(void)
{
	TAB_table t = malloc(sizeof(struct TAB_table_));
	t->top = NULL;
	for (int i = 0; i < TAB_SIZE; ++i)
		t->table[i] = NULL;
	return t;
}

void TAB_enter(TAB_table t, void* key, void* value)
{
	int index;
	index = ((unsigned int)key) % TAB_SIZE;
	t->table[index] = Binder(key, value, t->table[index], t->top);
	t->top = key;
}

void* TAB_look(TAB_table t, void* key)
{
	int index;
	bindex = bdr;
	index = ((unsigned int)key) % TAB_SIZE;
	for (bdr = t->table[index]; bdr; bdr = bdr->next)
		if (bdr->key == key)
			return bdr->value;
	return NULL;
}

void* TAB_pop(TAB_table t)
{
	void *top;
	binder bdr;
	int index;
	top = t->top;
	index = ((unsigned int)top) % TAB_SIZE;
	bdr = t->table[index];
	t->table[index] = bdr->next;
	t->top = bdr->prevTop;
	return bdr->key;
}

void TAB_dump(TAB_table t, void (*show)(void *key, void *value))
{
	void *top = t->top;
	int index = ((unsigned int)top) % TAB_SIZE;
	binder bdr = t->table[index];
	if (!bdr)
		return;
	t->table[index] = bdr->next;
	t->top = bdr->prevTop;
	show(bdr->key, bdr->value);
	TAB_dump(t, show);
	t->top = top;
	t->table[index] = bdr;
}
package me.baislsl.tiger;

import me.baislsl.tiger.symbol.Symbol;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable<T extends Symbol> {

    private Map<String, T> map = new HashMap<>();
    private SymbolTable<? extends T> parent;

    public SymbolTable() {
        this.parent = null;
    }

    public SymbolTable(SymbolTable<? extends T> parent) {
        this.parent = parent;
    }

    public T put(String name, T symbol) {
        return map.put(name, symbol);
    }

    public QueryResult<T> query(String name) {
        if (map.containsKey(name)) {
            return new QueryResult<>(map.get(name), 0);
        }

        if (parent == null) return null;
        QueryResult<? extends T> r = parent.query(name);
        if (r == null) return null;
        return new QueryResult<>(r.symbol, r.depth + 1);

    }

    static class QueryResult<S extends Symbol> {
        public S symbol;
        public int depth;

        public QueryResult(S symbol, int depth) {
            this.symbol = symbol;
            this.depth = depth;
        }
    }


}

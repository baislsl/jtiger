package me.baislsl.tiger;

import me.baislsl.tiger.symbol.*;

public class TigerEnv {

    private SymbolTable<TypeSymbol> typeTable;
    private SymbolTable<FieldSymbol> fieldTable;
    private SymbolTable<FunSymbol> funcTable;

    public TigerEnv() {
        typeTable = new SymbolTable<>();
        fieldTable = new SymbolTable<>();
        funcTable = new SymbolTable<>();
        loadSystemFunc(funcTable);
        loadSystemType(typeTable);
    }

    public TigerEnv(TigerEnv parent) {
        typeTable = new SymbolTable<>(parent.typeTable);
        fieldTable = new SymbolTable<>(parent.fieldTable);
        funcTable = new SymbolTable<>(parent.funcTable);
    }

    private static void loadSystemFunc(SymbolTable<FunSymbol> funcTable) {
        for (SystemFunSymbol s : SystemFunSymbol.symbols) {
            funcTable.put(s.name(), s);
        }
    }

    private static void loadSystemType(SymbolTable<TypeSymbol> typeTable) {
        typeTable.put("int", PrimitiveType.INT);
        typeTable.put("string", PrimitiveType.STRING);
    }


    public SymbolTable<TypeSymbol> getTypeTable() {
        return typeTable;
    }

    public SymbolTable<FunSymbol> getFuncTable() {
        return funcTable;
    }

    public SymbolTable<FieldSymbol> getFieldTable() {
        return fieldTable;
    }
}

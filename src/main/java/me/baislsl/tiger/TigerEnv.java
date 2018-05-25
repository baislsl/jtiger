package me.baislsl.tiger;

import me.baislsl.tiger.symbol.FieldSymbol;
import me.baislsl.tiger.symbol.FunSymbol;
import me.baislsl.tiger.symbol.TypeSymbol;

public class TigerEnv {

    private SymbolTable<TypeSymbol> typeTable;
    private SymbolTable<FieldSymbol> fieldTable;
    private SymbolTable<FunSymbol> funcTable;

    public TigerEnv() {
        typeTable = new SymbolTable<>();
        fieldTable = new SymbolTable<>();
        funcTable = new SymbolTable<>();

    }

    public TigerEnv(TigerEnv parent) {
        typeTable = new SymbolTable<>(parent.typeTable);
        fieldTable = new SymbolTable<>(parent.fieldTable);
        funcTable = new SymbolTable<>(parent.funcTable);
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

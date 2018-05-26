package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncDecGenTest {

    @Test
    void generateClass() {
        FunDec funDec = new FunDec();
        funDec.id = new Token("myprint");
        FieldDec f1 = new FieldDec(); f1.id = new Token("f1"); f1.tyId = new Token("int");
        funDec.decs.add(f1);
        FieldDec f2 = new FieldDec(); f2.id = new Token("f2"); f2.tyId = new Token("string");
        funDec.decs.add(f2);
        Call c = new Call();
        c.id = new Token("print");
        c.exps.add(new StringLit("Hello World"));
        c.type = Type.VOID;
        funDec.exp = c;
        TigerEnv env = new TigerEnv();
        FuncDecGen.generateClass(env, funDec, "string");

    }
}
package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgramMainGenTest {

    @Test
    void gen() {
        Program p = new Program();
        p.exp = new LetExp();
        ProgramMainGen.gen(p);
    }


    @Test
    void helloWorld() {
        Program p = new Program();
        Call c = new Call();
        c.id = new Token("print");
        c.exps.add(new StringLit("Hello World"));
        c.type = Type.VOID;
        p.exp = c;
        ProgramMainGen.gen(p, "HelloWorld");
    }


    @Test
    void helloWorld2() {
        Program p = new Program();
        LetExp letExp = new LetExp();
        letExp.className = "LetDecTest"; // name to be generate


        // var N: = 8
        VarDec varDec = new VarDec();
        varDec.id = new Token("N");
        varDec.tyId = new Token("int");
        varDec.exp = new IntLit(new Token("123"));

        // type List = {a: string, next: List}
        TyDec tyDec = new TyDec();
        tyDec.tyId = new Token("List");
        tyDec.ty = new RecTy();
        ((RecTy) tyDec.ty).decs.add(new FieldDec(new Token("a"), new Token("string")));
        ((RecTy) tyDec.ty).decs.add(new FieldDec(new Token("b"), new Token("List")));

        //  function myprint() void := print("Hello World")
        FunDec funDec = new FunDec();
        funDec.id = new Token("myprint");
        Call c = new Call();
        c.id = new Token("print");
        c.exps.add(new StringLit("Hello World"));
        c.type = Type.VOID;
        funDec.exp = c;
        funDec.setRetType(Type.VOID);

        letExp.decs.add(varDec);
        letExp.decs.add(tyDec);
        letExp.decs.add(funDec);

        // TODO: use func call instead
        letExp.exps.add(c);
        p.exp = letExp;
        ProgramMainGen.gen(p, "HelloWorld");
    }
}
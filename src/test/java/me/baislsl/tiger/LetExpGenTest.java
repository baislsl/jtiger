package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * let
 *  var N := 8
 *  type List = { a : string, next : List }
 *  function myprint() void := print("Hello World")
 *
 * in:
 *  myprint()
 *
 *
 *
 *
 */

class LetExpGenTest {
    @Test
    void generateClass() {
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
        tyDec.ty.type = new ObjectType("List");
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

        LetExpGen.generateClass(new TigerEnv(), letExp, new ObjectType("StringTestRoot" ));



    }
}
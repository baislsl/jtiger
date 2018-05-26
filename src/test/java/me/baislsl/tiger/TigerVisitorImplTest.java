package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TigerVisitorImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    /**
     * let
     *  a := "Hello World"
     * in
     *  let
     *     function printa(){
     *         print(a)
     *     }
     *  in
     *      printa()
     *  end
     * end
     *
     */
    @Test
    void visitIdOnlyLValue() {

        FunDec f = new FunDec();
        f.id = new Token("printa");
        f.exp = new Call();
        f.exp.type = Type.VOID;
        ((Call) f.exp).id = new Token("print");
        Lvalue lv = new IdOnlyLvalue(new Token("a"));
        lv.type = Type.STRING;
        ((Call) f.exp).exps.add(lv);

        Call call = new Call(); // printa();
        call.id = new Token("printa");
        call.type = Type.VOID;

        LetExp letExp = new LetExp();
        letExp.className = "TestVisitOnlyLet0";
        letExp.decs.add(f);
        letExp.exps.add(call);
        letExp.type = Type.VOID;

        // a := "Hello World"
        VarDec varDec = new VarDec();
        varDec.tyId = new Token("string");
        varDec.exp = new StringLit("Hello World");
        varDec.id = new Token("a");

        LetExp letExpTop = new LetExp();
        letExpTop.className = "TestVisitOnlyLetTop";
        letExpTop.decs.add(varDec);
        letExpTop.exps.add(letExp);

        Program p = new Program();
        p.exp = letExpTop;

        ProgramMainGen.gen(p, "TestVisitIdOnlyLValue");
    }


}
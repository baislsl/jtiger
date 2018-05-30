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
     *  in
     *  let
     *       function printa(){
     *       print(a)
     *      }
     *  in
     *      printa()
     *  end
     * end
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


    /**
     * let
     *  a = "Hello World"
     *  repeat = 5
     * in
     *  for i := 0 to repeat:
     *      print(a)
     *
     * end
     *
     */
    @Test
    void testForLoop() {
        // a := "Hello World"
        VarDec varDec = new VarDec();
        varDec.tyId = new Token("string");
        varDec.exp = new StringLit("Hello World");
        varDec.id = new Token("a");

        VarDec repeat = new VarDec();
        repeat.tyId = new Token("int");
        repeat.exp = new IntLit(new Token("5"));
        repeat.id = new Token("repeat");

        // print(a)
        Call call = new Call();
        call.id = new Token("print");
        call.type = Type.VOID;
        call.exps.add(new IdOnlyLvalue(new Token("a")));

        // for i:= 0 to repeat
        //      print(a)
        ForExp forExp = new ForExp();
        forExp.id = new Token("i");
        forExp.fromExp = new IntLit(new Token("0"));
        forExp.toExp = new IdOnlyLvalue(new Token("repeat"));
        forExp.doExp = call;
        forExp.type = Type.VOID;

        LetExp letExp = new LetExp();
        letExp.decs.add(varDec);
        letExp.decs.add(repeat);
        letExp.exps.add(forExp);
        letExp.type = Type.VOID;

        Program p = new Program();
        p.exp = letExp;


        ProgramMainGen.gen(p, "TestForLoop");

    }


    /**
     * let
     *  a = "Hello World"
     *  b = "\nHello baislsl"
     *  repeat = 5
     * in
     *  for i := 0 to repeat:
     *      if i = 4:
     *         then print(a)
     *      else print(concat(a, b)
     *
     *
     *
     * end
     *
     */
    @Test
    void testForLoop2() {
        // a := "Hello World"
        VarDec varDec = new VarDec();
        varDec.tyId = new Token("string");
        varDec.exp = new StringLit("Hello World");
        varDec.id = new Token("a");

        VarDec varDecB = new VarDec();
        varDecB.tyId = new Token("string");
        varDecB.exp = new StringLit("\nHello baislsl");
        varDecB.id = new Token("b");

        VarDec repeat = new VarDec();
        repeat.tyId = new Token("int");
        repeat.exp = new IntLit(new Token("5"));
        repeat.id = new Token("repeat");

        // print(a)
        Call call = new Call();
        call.id = new Token("print");
        call.type = Type.VOID;
        call.exps.add(new IdOnlyLvalue(new Token("a")));


        // print(concat(a, b))
        Call c1 = new Call();
        c1.id = new Token("concat");
        c1.type = Type.STRING;
        c1.exps.add(new IdOnlyLvalue(new Token("a")));
        c1.exps.add(new IdOnlyLvalue(new Token("b")));
        Call c1pr = new Call();
        c1pr.id = new Token("print");
        c1pr.exps.add(c1);
        c1pr.type = Type.VOID;

        // i == 4
        InfixExp infixExp = new InfixExp();
        infixExp.exp1 = new IdOnlyLvalue(new Token("i"));
        infixExp.exp1.type = Type.INT;
        infixExp.exp2 = new IntLit(new Token("4"));
        infixExp.type = Type.INT;
        infixExp.infixOp = new Token("=");


        IfThenElse ifThenElse = new IfThenElse();
        ifThenElse.ifExp = infixExp;
        ifThenElse.thenExp = call;
        ifThenElse.elseExp = c1pr;
        ifThenElse.type = Type.VOID;

        // for i:= 0 to repeat
        ForExp forExp = new ForExp();
        forExp.id = new Token("i");
        forExp.fromExp = new IntLit(new Token("0"));
        forExp.toExp = new IdOnlyLvalue(new Token("repeat"));
        forExp.doExp = ifThenElse;
        forExp.type = Type.VOID;

        LetExp letExp = new LetExp();
        letExp.decs.add(varDec);
        letExp.decs.add(varDecB);
        letExp.decs.add(repeat);
        letExp.exps.add(forExp);
        letExp.type = Type.VOID;

        Program p = new Program();
        p.exp = letExp;


        ProgramMainGen.gen(p, "TestForLoop");

    }

}
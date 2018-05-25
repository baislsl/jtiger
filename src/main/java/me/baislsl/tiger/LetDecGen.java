package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;

import java.util.ArrayList;
import java.util.List;

public class LetDecGen {

    private TigerEnv env;
    private LetExp letExp;
    private String parent;
    private String className;

    private ClassGen cg;
    private ConstantPoolGen cp;

    private List<TyDec> tyDecs = new ArrayList<>();
    private List<VarDec> varDecs = new ArrayList<>();
    private List<FunDec> funDecs = new ArrayList<>();

    private LetDecGen(TigerEnv env, LetExp letExp, String parent, String className) {
        this.env = env;
        this.letExp = letExp;
        this.parent = parent;
        this.className = className;

        cg = new ClassGen(className, "java.lang.Object", "<generated>",
                Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        cp = cg.getConstantPool();

        for (Dec d : letExp.decs) {
            if (d instanceof TyDec) tyDecs.add((TyDec) d);
            else if (d instanceof VarDec) varDecs.add((VarDec) d);
            else funDecs.add((FunDec) d);
        }
    }

    private void updateEnv() {
        for (TyDec d : tyDecs) {



        }

        for (VarDec v : varDecs) {

        }

        for (FunDec f : funDecs) {

        }


    }

    private void generateField() {

    }


}

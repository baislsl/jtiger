package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.DecFunSymbol;
import me.baislsl.tiger.symbol.FieldSymbol;
import me.baislsl.tiger.symbol.GenerateTypeSymbol;
import me.baislsl.tiger.symbol.UserTypeSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LetExpGen {

    private TigerEnv env;
    private LetExp letExp;
    private String parent;
    private String className;

    private ClassGen cg;
    private ConstantPoolGen cp;

    private List<TyDec> tyDecs = new ArrayList<>();
    private List<VarDec> varDecs = new ArrayList<>();
    private List<FunDec> funDecs = new ArrayList<>();

    public static void generateClass(TigerEnv env, LetExp letExp, String parent) {
        LetExpGen gen = new LetExpGen(env, letExp, parent);
        gen.updateEnv();
        gen.generateField();
        gen.generateConstructor();
        gen.generateInvoke();
        try {
            gen.cg.getJavaClass().dump(Util.classPath + letExp.className + ".class");
        } catch (IOException e) {
            throw new CompileException(e);
        }
    }

    private LetExpGen(TigerEnv env, LetExp letExp, String parent) {
        this.env = env;
        this.letExp = letExp;
        this.parent = parent;
        this.className = letExp.className;

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
        env.getTypeTable().put(className, new GenerateTypeSymbol(letExp));
        for (TyDec d : tyDecs) {
            env.getTypeTable().put(d.tyId.name, new UserTypeSymbol(d));
            TypeClassGen.generateClass(env, d);
        }
        for (VarDec v : varDecs) {
            FieldDec fieldDec = new FieldDec();
            fieldDec.id = v.id;
            fieldDec.tyId = v.tyId;
            env.getFieldTable().put(v.id.name, new FieldSymbol(fieldDec));
        }
        for (FunDec f : funDecs) {
            env.getFuncTable().put(f.id.name, new DecFunSymbol(f));
            FuncDecGen.generateClass(env, f, className);
        }
    }

    private void generateField() {
        for (VarDec v : varDecs) {
            FieldGen fg = new FieldGen(Const.ACC_PUBLIC, v.type(), v.id.name, cp);
            cg.addField(fg.getField());
        }
    }


    private void generateConstructor() {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID,
                new Type[]{env.getTypeTable().query(parent).symbol.type()},
                new String[]{Util.parentFieldName},
                "<init>", cg.getClassName(), il, cp);

        InstructionFactory factory = new InstructionFactory(cp);

        // super()
        il.append(InstructionConst.THIS);
        il.append(new INVOKESPECIAL(cp.addMethodref(cg.getSuperclassName(),
                "<init>", "()V")));

        TigerVisitor visitor = new TigerVisitorImpl(env, cg, mg, il);

        // this.parent = parent
        Type parentType = env.getTypeTable().query(parent).symbol.type();
        il.append(InstructionConst.THIS);
        il.append(InstructionFactory.createLoad(parentType, 1));
        il.append(factory.createPutField(className, Util.parentFieldName, parentType));

        // vardec init
        for (VarDec v : varDecs) {
            if (v.exp == null) continue;
            il.append(InstructionConst.THIS);
            v.exp.accept(visitor);
            il.append(factory.createPutField(className, v.id.name, v.type()));
        }

        il.append(InstructionConst.RETURN);
        cg.addMethod(mg.getMethod());
    }

    private void generateInvoke() {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC,
                letExp.type(),  // ret
                Type.NO_ARGS,   // arg
                new String[0],  // arg name
                Util.invokeFuncName, cg.getClassName(), il, cp);

        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);
        for (int i = 0; i < letExp.exps.size(); i++) {
            letExp.exps.get(i).accept(visitor);
            if (i != letExp.exps.size() - 1 && letExp.exps.get(i).type() != Type.VOID) {
                il.append(InstructionConst.POP);
            }
        }
        cg.addMethod(mg.getMethod());
    }

}

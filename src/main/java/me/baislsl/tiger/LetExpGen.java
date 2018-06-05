package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.DecFunSymbol;
import me.baislsl.tiger.symbol.ClassFieldSymbol;
import me.baislsl.tiger.symbol.GenerateTypeSymbol;
import me.baislsl.tiger.symbol.UserTypeSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LetExpGen {

    private TigerEnv env;
    private LetExp letExp;
    private ObjectType parent;
    private String className;

    private ClassGen cg;
    private ConstantPoolGen cp;

    private List<TyDec> tyDecs = new ArrayList<>();
    private List<VarDec> varDecs = new ArrayList<>();
    private List<FunDec> funDecs = new ArrayList<>();

    // env parentStack should not contain env
    public static void generateClass(TigerEnv env, LetExp letExp, ObjectType parent) {
        LetExpGen gen = new LetExpGen(env, letExp, parent);
        gen.updateEnv();
        gen.generateParentField();
        gen.generateField();
        gen.generateConstructor();
        gen.generateInvoke();
        try {
            gen.cg.getJavaClass().dump(JVMSpec.classPath + letExp.className + ".class");
        } catch (IOException e) {
            throw new CompileException(e);
        }
    }

    private LetExpGen(TigerEnv env, LetExp letExp, ObjectType parent) {
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
            if(d.ty instanceof IdOnlyTy) { // alias
                env.getTypeTable().put(d.tyId.name,
                        env.getTypeTable().query(((IdOnlyTy) d.ty).id.name).symbol);
            } else if(d.ty instanceof RecTy) {  // generate new class for this type
                env.getTypeTable().put(d.tyId.name, new UserTypeSymbol(d));
                TypeClassGen.generateClass(env, d);
            } else {    // arrTy
                env.getTypeTable().put(d.tyId.name, new UserTypeSymbol(d));
            }
        }
        for (VarDec v : varDecs) {
            env.getFieldTable().put(v.id.name, new ClassFieldSymbol(v));
        }
        for (FunDec f : funDecs) {
            env.getFuncTable().put(f.id.name, new DecFunSymbol(f));
            FuncDecGen.generateClass(new TigerEnv(env, className), f, new ObjectType(className));
        }
    }

    private void generateParentField() {
        FieldGen fg = new FieldGen(Const.ACC_PUBLIC, parent,
                JVMSpec.parentFieldName, cp);
        cg.addField(fg.getField());
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
                new Type[]{parent},
                new String[]{JVMSpec.parentFieldName},
                "<init>", cg.getClassName(), il, cp);

        InstructionFactory factory = new InstructionFactory(cp);

        // super()
        il.append(InstructionConst.THIS);
        il.append(new INVOKESPECIAL(cp.addMethodref(cg.getSuperclassName(),
                "<init>", "()V")));

        TigerVisitor visitor = new TigerVisitorImpl(env, cg, mg, il);

        // this.parent = parent
        Type parentType = parent;
        il.append(InstructionConst.THIS);
        il.append(InstructionFactory.createLoad(parentType, 1));
        il.append(factory.createPutField(className, JVMSpec.parentFieldName, parentType));

        // vardec init
        for (VarDec v : varDecs) {
            if (v.exp == null) continue;
            il.append(InstructionConst.THIS);
            v.exp.accept(visitor);
            il.append(factory.createPutField(className, v.id.name, v.type()));
        }

        il.append(InstructionConst.RETURN);
        mg.setMaxStack();   // TODO:
        cg.addMethod(mg.getMethod());
    }

    private void generateInvoke() {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC,
                letExp.type(),  // ret
                Type.NO_ARGS,   // arg
                new String[0],  // arg name
                JVMSpec.invokeFuncName, cg.getClassName(), il, cp);

        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);
        for (int i = 0; i < letExp.exps.size(); i++) {
            letExp.exps.get(i).accept(visitor);
            if (i != letExp.exps.size() - 1 && letExp.exps.get(i).type() != Type.VOID) {
                il.append(InstructionConst.POP);
            }
        }

        if(!letExp.exps.isEmpty() && letExp.exps.get(letExp.exps.size() - 1).type() != Type.VOID) {
            il.append(InstructionFactory.createReturn(letExp.exps.get(letExp.exps.size() - 1).type()));
        } else {
            il.append(InstructionConst.RETURN);
        }

        mg.setMaxStack();   // TODO:
        cg.addMethod(mg.getMethod());
    }

}

package me.baislsl.tiger;

import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FunDec;
import me.baislsl.tiger.symbol.ClassFieldSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FuncDecGen {
    private TigerEnv env;
    private FunDec funDec;
    private ObjectType parent;

    private ClassGen cg;
    private ConstantPoolGen cp;


    private FuncDecGen(TigerEnv env, FunDec funDec, ObjectType parent) {
        funDec.parent = parent.getClassName();
        this.funDec = funDec;
        this.env = env;
        this.parent = parent;
        cg = new ClassGen(funDec.id.name, "java.lang.Object", "<generated>",
                Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        cp = cg.getConstantPool();
    }

    // env should not contain func in stack
    public static void generateClass(TigerEnv env, FunDec funDec, ObjectType parent) {
        FuncDecGen gen = new FuncDecGen(env, funDec, parent);
        gen.updateFieldTable();
        gen.generateParentField();
        gen.generateParamField();
        gen.generateConstructor();
        gen.generateInvoke();
        try {
            String classPath = JVMSpec.classPath + gen.cg.getClassName() + ".class";
            gen.cg.getJavaClass().dump(classPath);
            JarBuilder.add(new File(classPath));
        } catch (IOException e) {
            throw new CompileException(e);
        }
    }

    private void updateFieldTable() {
        for (FieldDec f : funDec.decs) {
            env.getFieldTable().put(f.id.name, new ClassFieldSymbol(f));
        }
    }

    private void generateParentField() {
        FieldGen fg = new FieldGen(Const.ACC_PUBLIC, parent,
                JVMSpec.parentFieldName, cp);
        cg.addField(fg.getField());
    }


    private void generateParamField() {
        for (FieldDec f : funDec.decs) {
            FieldGen fg = new FieldGen(Const.ACC_PUBLIC, env.getTypeTable().query(f.tyId.name).symbol.type(),
                    f.id.name, cp);
            cg.addField(fg.getField());
        }
    }

    private void generateConstructor() {
        InstructionList il = new InstructionList();
        List<Type> argType = new ArrayList<>();
        argType.add(parent);
        List<String> argName = new ArrayList<>();
        argName.add(JVMSpec.parentFieldName);
        for (FieldDec fd : funDec.decs) {
            argName.add(fd.id.toString());
            argType.add(env.getTypeTable().query(fd.tyId.name).symbol.type());
        }
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID,
                argType.toArray(new Type[0]),
                argName.toArray(new String[0]),
                "<init>", cg.getClassName(), il, cp);

        InstructionFactory factory = new InstructionFactory(cp);

        // super()
        il.append(InstructionConst.THIS);
        il.append(new INVOKESPECIAL(cp.addMethodref(cg.getSuperclassName(),
                "<init>", "()V")));

        for (int i = 0; i < argType.size(); i++) {
            il.append(InstructionConst.THIS);
            il.append(InstructionFactory.createLoad(argType.get(i), i + 1));
            il.append(factory.createPutField(cg.getClassName(), argName.get(i), argType.get(i)));
        }
        il.append(InstructionConst.RETURN);
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
    }

    private void generateInvoke() {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC,
                funDec.type(),  // ret
                Type.NO_ARGS,   // arg
                new String[0],  // arg name
                JVMSpec.invokeFuncName, cg.getClassName(), il, cp);

        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);
        funDec.exp.accept(visitor);
        if (funDec.exp.type() != Type.VOID) {
            if (funDec.type() != Type.VOID) {
                il.append(InstructionFactory.createReturn(funDec.exp.type()));
            } else {
                il.append(InstructionConst.POP);
                il.append(InstructionFactory.RETURN);
            }
        } else {
            il.append(InstructionFactory.RETURN);
        }
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
    }


}

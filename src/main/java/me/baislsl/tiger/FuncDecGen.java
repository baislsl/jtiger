package me.baislsl.tiger;

import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FunDec;
import me.baislsl.tiger.structure.Token;
import me.baislsl.tiger.symbol.FieldSymbol;
import me.baislsl.tiger.symbol.TypeSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuncDecGen {
    private TigerEnv env;
    private FunDec funDec;
    private String header;
    private String parent;

    private ClassGen cg;
    private ConstantPoolGen cp;


    private FuncDecGen(TigerEnv env, FunDec funDec, String parent) {
        this.funDec = funDec;
        this.env = new TigerEnv(env);
        this.parent = parent;
        cg = new ClassGen( funDec.id.name, "java.lang.Object", "<generated>",
                Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        cp = cg.getConstantPool();
    }

    public static void generateClass(TigerEnv env, FunDec funDec, String parent) {
        FuncDecGen gen = new FuncDecGen(env, funDec,  parent);
        gen.updateFieldTable();
        gen.generateParentField();
        gen.generateParamField();
        gen.generateConstructor();
        gen.generateInvoke();
        try{
            gen.cg.getJavaClass().dump(Util.classPath + gen.cg.getClassName() + ".class");
        } catch (IOException e) {
            throw new CompileException(e);
        }
    }

    private void updateFieldTable() {
        for(FieldDec f : funDec.decs) {
            env.getFieldTable().put(f.id.name, new FieldSymbol(f));
        }
    }

    private void generateParentField() {
        FieldGen fg = new FieldGen(Const.ACC_PUBLIC, env.getTypeTable().query(parent).symbol.type(),
                Util.parentFieldName, cp);
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
        argType.add(env.getTypeTable().query(parent).symbol.type());
        List<String> argName = new ArrayList<>();
        argName.add(Util.parentFieldName);
        for(FieldDec fd : funDec.decs) {
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
        cg.addMethod(mg.getMethod());
    }

    private void generateInvoke() {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC,
                funDec.type(),  // ret
                Type.NO_ARGS,   // arg
                new String[0],  // arg name
                Util.invokeFuncName, cg.getClassName(), il, cp);

        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);
        funDec.exp.accept(visitor);
        cg.addMethod(mg.getMethod());
    }


}

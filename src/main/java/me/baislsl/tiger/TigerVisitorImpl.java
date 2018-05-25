package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.*;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.util.ArrayList;
import java.util.List;

public class TigerVisitorImpl implements TigerVisitor {

    private TigerEnv env;
    private ClassGen cg;
    private MethodGen mg;
    private InstructionFactory factory;
    private InstructionList il;
    private SymbolTable<TypeSymbol> typeTable;
    private SymbolTable<FieldSymbol> fieldTable;
    private SymbolTable<FunSymbol> funcTable;

    public TigerVisitorImpl(TigerEnv env, ClassGen cg, MethodGen mg, InstructionList il) {
        this.env = new TigerEnv(env);
        this.cg = cg;
        this.mg = mg;
        factory = new InstructionFactory(cg.getConstantPool());
        this.il = il;
        typeTable = env.getTypeTable();
        fieldTable = env.getFieldTable();
        funcTable = env.getFuncTable();
    }

    @Override
    public void visit(ArrCreate e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(ArrTy e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(Assignment e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(Call e) {
        SymbolTable.QueryResult<FunSymbol> r = funcTable.query(e.id.name);
        if (r == null) {
            throw new CompileException("Can not find function " + e.id.name);
        }
        FunSymbol f = r.symbol;
        if (e.exps.size() != f.paramsType().size())
            throw new CompileException("Func parameter number not match");
        if (f.isSystemFunc()) {
            for (Exp param : e.exps) {
                param.accept(this);
            }
            il.append(factory.createInvoke(TigerFuncLink.class.getName(), f.name(),
                    f.retType(), f.paramsType().toArray(new Type[0]), Const.INVOKESTATIC));
        } else {
            // func(params) -> new func(params).invoke()
            il.append(factory.createNew(new ObjectType(f.name())));
            il.append(InstructionConst.DUP);
            List<Type> params = new ArrayList<>();
            params.add(env.getTypeTable().query(cg.getClassName()).symbol.type());
            params.addAll(f.paramsType());
            il.append(InstructionConst.THIS);
            for (Exp param : e.exps) {
                param.accept(this);
            }
            il.append(factory.createInvoke(f.name(), "<init>",
                    Type.VOID, params.toArray(new Type[0]), Const.INVOKESPECIAL));

            il.append(factory.createInvoke(f.name(), Util.invokeFuncName, f.retType(), Type.NO_ARGS,
                    Const.INVOKEVIRTUAL));

        }
    }

    @Override
    public void visit(FieldCreate e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(FieldDec e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(FieldExp e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(ForExp e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(FunDec e) {
        // fun dec can only appear in let block
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(IfThen e) {
        e.ifExp.accept(this);
        BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(br);
        e.thenExp.accept(this);
        br.setTarget(il.append(InstructionConst.NOP));
    }

    @Override
    public void visit(IfThenElse e) {
        e.ifExp.accept(this);
        BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        il.append(br);
        e.thenExp.accept(this);
        BranchInstruction gt = InstructionFactory.createBranchInstruction(Const.GOTO, null);
        il.append(gt);
        br.setTarget(il.append(InstructionConst.NOP));
        e.elseExp.accept(this);
        gt.setTarget(il.append(InstructionConst.NOP));
    }

    @Override
    public void visit(InfixExp e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(IntLit e) {
        il.append(factory.createConstant(Integer.valueOf(e.value.name)));
    }

    @Override
    public void visit(LetExp e) {
        e.className = LetNameFactory.newLetName();
        LetExpGen.generateClass(env, e, cg.getClassName());
        env.getTypeTable().put(e.className, new GenerateTypeSymbol(e));

        // let  -> new Let00(parent).invoke()
        il.append(factory.createNew(new ObjectType(e.className)));
        il.append(InstructionConst.DUP);
        il.append(InstructionConst.THIS);
        il.append(factory.createInvoke(e.className, "<init>",
                Type.VOID,
                new Type[]{env.getTypeTable().query(cg.getClassName()).symbol.type()},
                Const.INVOKESPECIAL));
        il.append(factory.createInvoke(e.className, Util.invokeFuncName, e.type(), Type.NO_ARGS,
                Const.INVOKEVIRTUAL));


    }

    @Override
    public void visit(Negation e) {
        if (e.exp.type != Type.INT) {
            throw new CompileException("Can not negation on a non int value");
        }
        il.append(factory.createConstant(0));
        e.exp.accept(this);
        il.append(InstructionFactory.createBinaryOperation("-", Type.INT));
    }

    @Override
    public void visit(Nil e) {
        il.append(InstructionFactory.createNull(e.type));
    }

    @Override
    public void visit(Program e) {
        throw new CompileException(new UnsupportedOperationException());
    }

    @Override
    public void visit(RecCreate e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(RecTy e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(SeqExp e) {
        for (int i = 0; i < e.exps.size(); i++) {
            e.exps.get(i).accept(this);
            if (i != e.exps.size() - 1 && e.exps.get(i).type != Type.VOID) {
                il.append(InstructionConst.POP);
            }
        }
    }

    @Override
    public void visit(StringLit e) {
        il.append(factory.createConstant(e.value));
    }

    @Override
    public void visit(Subscript e) {
        throw new CompileException(new UnsupportedOperationException());
    }

    @Override
    public void visit(TyDec e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(VarDec e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(While e) {
        InstructionHandle begin = il.append(InstructionConst.NOP);
        e.whileExp.accept(this);
        BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        e.doExp.accept(this);
        BranchInstruction gt = InstructionFactory.createBranchInstruction(Const.GOTO, begin);
        il.append(gt);
        br.setTarget(il.append(InstructionConst.NOP));
    }

}

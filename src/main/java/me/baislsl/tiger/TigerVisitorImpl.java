package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.*;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.util.*;

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
        this.env = new TigerEnv(env, cg.getClassName());
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
        if(e.lv instanceof IdOnlyLvalue) {  // Lvalue -> id
            IdOnlyLvalue lv = (IdOnlyLvalue)e.lv;
            SymbolTable.QueryResult<FieldSymbol> r = fieldTable.query(lv.token.name);
            if (r.symbol.isLocalVariable()) {    // local value
                LocalFieldSymbol symbol = (LocalFieldSymbol) r.symbol;
                e.exp.accept(this);
                il.append(InstructionFactory.createStore(symbol.type(), symbol.index()));
            } else {    // field of class
                ClassFieldSymbol symbol = (ClassFieldSymbol) r.symbol;
                il.append(InstructionConst.THIS);
                il.append(factory.createPutField(cg.getClassName(), symbol.name(), symbol.type()));
            }
        } else if(e.lv instanceof Subscript) {  // Lvalue -> Subsript
            throw new CompileException("Unsupported for Subscript");
        } else {    //  Lvalue -> FieldExp
                    // lvalue.id
            FieldExp exp = (FieldExp) e.lv;
            exp.lvalue.accept(this);
            // should get the type of return value
            e.exp.accept(this);
            Type t = exp.lvalue.type();
            // TODO: bug ?
            il.append(factory.createPutField(t.toString(), exp.id.name, e.exp.type()));
        }
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
    }

    @Override
    public void visit(FieldDec e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(FieldExp e) {
    }

    @Override
    public void visit(ForExp e) {
        LocalVariableGen lg = mg.addLocalVariable(e.id.name, Type.INT, null, null);
        lg.setStart(il.append(InstructionConst.NOP));
        e.fromExp.accept(this);
        il.append(InstructionFactory.createStore(Type.INT, lg.getIndex()));
        e.toExp.accept(this);
        InstructionHandle loop = il.append(InstructionConst.DUP);
        il.append(InstructionFactory.createBinaryOperation("-", Type.INT));
        BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFGT, null);
        e.doExp.accept(this);
        if (e.doExp.type() != Type.VOID) {
            il.append(InstructionConst.POP);
        }
        il.append(new GOTO(loop));
        br.setTarget(il.append(InstructionConst.POP)); // pop out the "toExp" value
        lg.setEnd(il.getEnd());
    }

    @Override
    public void visit(FunDec e) {
        // fun dec can only appear in let block
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(IdOnlyTy e) {
        throw new CompileException("Unsupported to compile " + e.getClass().getSimpleName());
    }

    @Override
    public void visit(IdOnlyLvalue e) {
        SymbolTable.QueryResult<FieldSymbol> r = fieldTable.query(e.token.name);
        if (r.symbol.isLocalVariable()) {    // local value
            LocalFieldSymbol symbol = (LocalFieldSymbol) r.symbol;
            il.append(InstructionFactory.createLoad(symbol.type(), symbol.index()));
        } else {    // field of class
            ClassFieldSymbol symbol = (ClassFieldSymbol) r.symbol;
            int depth = r.depth;
            // TODO: 需要记录下parent的类型
            il.append(InstructionConst.THIS);
            il.append(factory.createGetField(cg.getClassName(), symbol.name(), symbol.type()));
        }
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

    private static Map<String, Short> branchMap = new HashMap<>();

    static {
        branchMap.put("<", Const.IFLT);
        branchMap.put(">", Const.IFGT);
        branchMap.put("<=", Const.IFLE);
        branchMap.put(">=", Const.IFGE);
        branchMap.put("=", Const.IFEQ);
        branchMap.put("<>", Const.IFNE);
    }

    @Override
    public void visit(InfixExp e) {
        // *, /, +, -,
        // =, <>, >, <, >=, <=,
        // &, |
        String oper = e.infixOp.name;
        if (Arrays.asList("*", "/", "+", "-").contains(oper)) {
            e.exp1.accept(this);
            e.exp2.accept(this);
            il.append(InstructionFactory.createBinaryOperation(oper, Type.INT));
        } else if (Arrays.asList("=", "<>", ">", "<", ">=", "<=").contains(oper)) {
            e.exp1.accept(this);
            e.exp2.accept(this);
            if (e.exp1.type().equals(Type.INT)) {
                il.append(InstructionFactory.createBinaryOperation("-", Type.INT));
            } else {    //  invoke String::compareTo
                il.append(factory.createInvoke("java.lang.String",
                        "compareTo", Type.INT, new Type[]{Type.STRING}, Const.INVOKEVIRTUAL));
            }
            BranchInstruction br = InstructionFactory.createBranchInstruction(branchMap.get(oper), null);
            il.append(br);
            il.append(factory.createConstant(0));
            GOTO g = new GOTO(null);
            il.append(g);
            br.setTarget(il.append(factory.createConstant(1)));
            g.setTarget(il.append(InstructionConst.NOP));
        } else if (oper.equals("&")) {    // "&", "|"
            e.exp1.accept(this);
            BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
            il.append(br);
            e.exp2.accept(this);
            br.setTarget(il.append(InstructionConst.NOP));
        } else {    // "|"
            e.exp1.accept(this);
            BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFNE, null);
            il.append(br);
            e.exp2.accept(this);
            br.setTarget(il.append(InstructionConst.NOP));
        }
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

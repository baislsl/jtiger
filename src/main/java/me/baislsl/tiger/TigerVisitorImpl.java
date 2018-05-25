package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.FieldSymbol;
import me.baislsl.tiger.symbol.FunSymbol;
import me.baislsl.tiger.symbol.TypeSymbol;
import me.baislsl.tiger.symbol.UserTypeSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

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

    }

    @Override
    public void visit(ArrTy e) {

    }

    @Override
    public void visit(Assignment e) {

    }

    @Override
    public void visit(Call e) {
        for (Exp param : e.exps) {
            param.accept(this);
        }

        SymbolTable.QueryResult<FunSymbol> r = funcTable.query(e.id.name);
        if (r == null) {
            throw new CompileException("Can not find function " + e.id.name);
        }
        FunSymbol f = r.symbol;
        if (f.isSystemFunc()) {
            il.append(factory.createInvoke(TigerFuncLink.class.getName(), f.name(),
                    f.retType(), f.paramsType().toArray(new Type[0]), Const.INVOKESTATIC));
        } else {
            // TODO:
        }
    }

    @Override
    public void visit(FieldCreate e) {

    }

    @Override
    public void visit(FieldDec e) {

    }

    @Override
    public void visit(FieldExp e) {

    }

    @Override
    public void visit(ForExp e) {

    }

    @Override
    public void visit(FunDec e) {

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

    }

    @Override
    public void visit(IntLit e) {
        il.append(factory.createConstant(Integer.valueOf(e.value.name)));
    }

    @Override
    public void visit(LetExp e) {

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

    }

    @Override
    public void visit(VarDec e) {

    }

    @Override
    public void visit(While e) {
        InstructionHandle begin = il.append(InstructionConst.NOP);
        e.whileExp.accept(this);
        BranchInstruction br = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        e.doExp.accept(this);
        BranchInstruction gt = InstructionFactory.createBranchInstruction(Const.GOTO, begin);
        br.setTarget(il.append(InstructionConst.NOP));
    }

}

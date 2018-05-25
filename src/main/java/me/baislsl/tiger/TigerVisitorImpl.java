package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

public class TigerVisitorImpl implements TigerVisitor {

    private TigerEnv env;
    private ClassGen cg;
    private MethodGen mg;
    private InstructionFactory factory;
    private InstructionList il;

    public TigerVisitorImpl(TigerEnv env, ClassGen cg, MethodGen mg, InstructionList il) {
        this.env = new TigerEnv(env);
        this.cg = cg;
        this.mg = mg;
        factory = new InstructionFactory(cg.getConstantPool());
        this.il = il;
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

    }

    @Override
    public void visit(IfThenElse e) {

    }

    @Override
    public void visit(InfixExp e) {

    }

    @Override
    public void visit(IntLit e) {

    }

    @Override
    public void visit(LetExp e) {

    }

    @Override
    public void visit(Negation e) {

    }

    @Override
    public void visit(Nil e) {

    }

    @Override
    public void visit(Program e) {


    }

    @Override
    public void visit(RecCreate e) {

    }

    @Override
    public void visit(RecTy e) {

    }

    @Override
    public void visit(SeqExp e) {

    }

    @Override
    public void visit(StringLit e) {

    }

    @Override
    public void visit(Subscript e) {

    }

    @Override
    public void visit(Token e) {

    }

    @Override
    public void visit(TyDec e) {

    }

    @Override
    public void visit(VarDec e) {

    }

    @Override
    public void visit(While e) {

    }

}

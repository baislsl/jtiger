package me.baislsl.tiger.structure;

public interface TigerVisitor {
    void visit(ArrCreate e);
    void visit(ArrTy e);
    void visit(Assignment e);
    void visit(Call e);
    void visit(FieldCreate e);
    void visit(FieldDec e);
    void visit(FieldExp e);
    void visit(ForExp e);
    void visit(FunDec e);
    void visit(IfThen e);
    void visit(IfThenElse e);
    void visit(InfixExp e);
    void visit(IntLit e);
    void visit(LetExp e);
    void visit(Negation e);
    void visit(Nil e);
    void visit(Program e);
    void visit(RecCreate e);
    void visit(RecTy e);
    void visit(SeqExp e);
    void visit(StringLit e);
    void visit(Subscript e);
    void visit(TyDec e);
    void visit(VarDec e);
    void visit(While e);






}

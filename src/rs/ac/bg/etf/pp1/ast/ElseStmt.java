// generated with ast extension for cup
// version 0.8
// 29/5/2021 23:42:58


package rs.ac.bg.etf.pp1.ast;

public class ElseStmt extends Else1 {

    private ElseZakrpi ElseZakrpi;
    private Statement Statement;

    public ElseStmt (ElseZakrpi ElseZakrpi, Statement Statement) {
        this.ElseZakrpi=ElseZakrpi;
        if(ElseZakrpi!=null) ElseZakrpi.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ElseZakrpi getElseZakrpi() {
        return ElseZakrpi;
    }

    public void setElseZakrpi(ElseZakrpi ElseZakrpi) {
        this.ElseZakrpi=ElseZakrpi;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseZakrpi!=null) ElseZakrpi.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseZakrpi!=null) ElseZakrpi.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseZakrpi!=null) ElseZakrpi.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseStmt(\n");

        if(ElseZakrpi!=null)
            buffer.append(ElseZakrpi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseStmt]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 29/5/2021 23:42:58


package rs.ac.bg.etf.pp1.ast;

public class NoElseStmt extends Else1 {

    private ElseZakrpi ElseZakrpi;

    public NoElseStmt (ElseZakrpi ElseZakrpi) {
        this.ElseZakrpi=ElseZakrpi;
        if(ElseZakrpi!=null) ElseZakrpi.setParent(this);
    }

    public ElseZakrpi getElseZakrpi() {
        return ElseZakrpi;
    }

    public void setElseZakrpi(ElseZakrpi ElseZakrpi) {
        this.ElseZakrpi=ElseZakrpi;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseZakrpi!=null) ElseZakrpi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseZakrpi!=null) ElseZakrpi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseZakrpi!=null) ElseZakrpi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoElseStmt(\n");

        if(ElseZakrpi!=null)
            buffer.append(ElseZakrpi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoElseStmt]");
        return buffer.toString();
    }
}

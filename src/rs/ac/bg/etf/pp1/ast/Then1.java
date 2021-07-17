// generated with ast extension for cup
// version 0.8
// 29/5/2021 23:42:58


package rs.ac.bg.etf.pp1.ast;

public class Then1 implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private ThenZakrpi ThenZakrpi;
    private Statement Statement;

    public Then1 (ThenZakrpi ThenZakrpi, Statement Statement) {
        this.ThenZakrpi=ThenZakrpi;
        if(ThenZakrpi!=null) ThenZakrpi.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ThenZakrpi getThenZakrpi() {
        return ThenZakrpi;
    }

    public void setThenZakrpi(ThenZakrpi ThenZakrpi) {
        this.ThenZakrpi=ThenZakrpi;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ThenZakrpi!=null) ThenZakrpi.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ThenZakrpi!=null) ThenZakrpi.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ThenZakrpi!=null) ThenZakrpi.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Then1(\n");

        if(ThenZakrpi!=null)
            buffer.append(ThenZakrpi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Then1]");
        return buffer.toString();
    }
}

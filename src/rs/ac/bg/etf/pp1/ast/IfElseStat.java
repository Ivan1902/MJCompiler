// generated with ast extension for cup
// version 0.8
// 29/5/2021 23:42:58


package rs.ac.bg.etf.pp1.ast;

public class IfElseStat extends Statement {

    private If1 If1;
    private Condition Condition;
    private Then1 Then1;
    private Else1 Else1;

    public IfElseStat (If1 If1, Condition Condition, Then1 Then1, Else1 Else1) {
        this.If1=If1;
        if(If1!=null) If1.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.Then1=Then1;
        if(Then1!=null) Then1.setParent(this);
        this.Else1=Else1;
        if(Else1!=null) Else1.setParent(this);
    }

    public If1 getIf1() {
        return If1;
    }

    public void setIf1(If1 If1) {
        this.If1=If1;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public Then1 getThen1() {
        return Then1;
    }

    public void setThen1(Then1 Then1) {
        this.Then1=Then1;
    }

    public Else1 getElse1() {
        return Else1;
    }

    public void setElse1(Else1 Else1) {
        this.Else1=Else1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(If1!=null) If1.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(Then1!=null) Then1.accept(visitor);
        if(Else1!=null) Else1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(If1!=null) If1.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(Then1!=null) Then1.traverseTopDown(visitor);
        if(Else1!=null) Else1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(If1!=null) If1.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(Then1!=null) Then1.traverseBottomUp(visitor);
        if(Else1!=null) Else1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseStat(\n");

        if(If1!=null)
            buffer.append(If1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Then1!=null)
            buffer.append(Then1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Else1!=null)
            buffer.append(Else1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfElseStat]");
        return buffer.toString();
    }
}

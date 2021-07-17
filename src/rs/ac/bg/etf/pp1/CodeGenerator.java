package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private int adr = 0;
	private int adr2 = 0;
	private int relop;
	private Boolean cond = false;
	private int nivoUgnjezdenosti = -1;
	LinkedList<List<Integer>> adreseZaZakrpu = new LinkedList<List<Integer>>();
	LinkedList<List<Integer>> adreseZaThen = new LinkedList<List<Integer>>();
	LinkedList<List<Integer>> adreseZaElse = new LinkedList<List<Integer>>();
	LinkedList<Boolean> poslednjiDeo = new LinkedList<Boolean>();
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(PrintStat printStmt) {
		if(printStmt.getExpr().struct == Tab1.intType) {
			Code.loadConst(5); // na kojoj sirini treba da se ispise print
			Code.put(Code.print);
		}
		else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(PrintStatWithNum printStmt) {
		Code.loadConst(printStmt.getNum());
		Code.put(Code.print);
	}
	
	public void visit(FactNum cnst) {
		Code.loadConst(cnst.getNumConst());
	}
	
	public void visit(FactBool cnst) {
		Boolean b = cnst.getBoolConst();
		Code.loadConst(b == true ? 1 : 0);
		
		
	}
	
	public void visit(FactChar cnst) {
		Code.loadConst(cnst.getCharConst());
	}
	
	public void visit(FactDesign factDesign) {
		Code.load(factDesign.getDesignator().obj);
	}
	
	public void visit(FactDesignWithoutActPars factDesign) {
		int offset = factDesign.getDesignator().obj.getAdr();
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(FactDesignWithActPars factDesign) {
		int offset = factDesign.getDesignator().obj.getAdr();
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(FactNew factNew) {
		Code.put(Code.new_);
	}
	
	public void visit(FactNewArray factNewArray) {
		Code.put(Code.newarray);
		if(factNewArray.getType().struct.equals(Tab1.charType)) {
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	public void visit(MethTypeName methodTypeName) {
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
public void visit(MethVoidName methodTypeName) {
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	}
	
	public void visit(MethodDeclWithFormPars methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(MethodDeclWithoutFormPars methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(DesigStatAssingopExpr desigStatAssingopExpr) {
		Designator desig = desigStatAssingopExpr.getDesignator();
		if(desig instanceof DesignatorExpr) {
			desig = (DesignatorExpr) desigStatAssingopExpr.getDesignator();
			if(desig.obj.getType().compatibleWith(Tab1.charType)) {
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
		}
		else {
			Code.store(desig.obj);

		}
	}

	
	public void visit(DesigStatPlusPlus desigStatPlusPlus) {
		Designator desig = desigStatPlusPlus.getDesignator();
		Code.load(desig.obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(desig.obj);
	}
	
	public void visit(DesigStatMinusMinus desigStatMinusMinus) {
		Designator desig = desigStatMinusMinus.getDesignator();
		Code.load(desig.obj);
		Code.loadConst(1);
		Code.put(Code.neg);
		Code.put(Code.add);
		Code.store(desig.obj);
	}
	
	public void visit(ReadStat readStat) {
		Designator desig = readStat.getDesignator();
		if(desig.obj.getType().equals(Tab1.charType)) {
			Code.put(Code.bread);
		}
		else {
			Code.put(Code.read);
		}
		Code.store(desig.obj);
	}
	
	public void visit(AddopTermListMore addopTermListMore) {
		if(addopTermListMore.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
		else {
			Code.put(Code.neg);
			Code.put(Code.add);
		}
	}
	
	public void visit(Expr11 expr11) {
		Code.put(Code.neg);
	}
	
	
	public void visit(DesignatorName designator) {
		Code.load(designator.obj);
	}
	
	public void visit(TermNoTerminate term) {
		if(term.getMulop() instanceof MulMulop) {
			Code.put(Code.mul);
		}
		if(term.getMulop() instanceof DivMulop) {
			Code.put(Code.div);
		}
		if(term.getMulop() instanceof ModMulop) {
			Code.put(Code.rem);
		}
	}
	
	
	
	public void visit(Condition1 condition1) {
		//Code.loadConst(0);
		if(cond == true) Code.putFalseJump(Code.eq, 0);
		else Code.putFalseJump(relop, 0);
		adr = Code.pc - 2;
	}
	
	public void visit(If1 if1) {
		nivoUgnjezdenosti++;
		adreseZaZakrpu.add(new LinkedList<Integer>());
		adreseZaThen.add(new LinkedList<Integer>());
		adreseZaElse.add(new LinkedList<Integer>());
		IfElseStat stmt = (IfElseStat) if1.getParent();
		if(stmt.getCondition() instanceof ConditionTerminate) {
			poslednjiDeo.add(true);
		}
		else {
			poslednjiDeo.add(false);
		}
	}
	
	public void visit(CondFactMore condFact) {
		Relop op = condFact.getRelop();
		if(op instanceof RelopEqEq) {
			relop = Code.eq;
		}
		if(op instanceof RelopNotEq) {
			relop = Code.ne;
		}
		if(op instanceof RelopGraterEq) {
			relop = Code.ge;
		}
		if(op instanceof RelopGreater) {
			relop = Code.gt;
		}
		if(op instanceof RelopLess) {
			relop = Code.lt;
		}
		if(op instanceof RelopLessEq) {
			relop = Code.le;
		}
		
		if(!(poslednjiDeo.getLast())) {
			// nisam u poslednjem delu || dela
			if(!(condFact.getParent().getParent() instanceof Condition)) {
				// nije poslednji u nizu && iskaza
				Code.putFalseJump(relop, 0);
				adreseZaZakrpu.getLast().add(Code.pc - 2);
			}
			else {
				// poslednji u nizu && iskaza
				Code.putFalseJump(Code.inverse[relop], 0);
				adreseZaThen.getLast().add(Code.pc - 2);
			}
		}
		else {
			// poslednji deo || dela
			Code.putFalseJump(relop, 0);
			adreseZaElse.getLast().add(Code.pc - 2);
		}
		
		
	}
	
	public void visit(CondFactTerminate condFact) {
		if(!(poslednjiDeo.getLast())) {
			// nisam u poslednjem delu || dela
			if(!(condFact.getParent().getParent() instanceof Condition)) {
				// nije poslednji u nizu && iskaza
				Code.loadConst(1);
				Code.putFalseJump(Code.eq, 0);
				adreseZaZakrpu.getLast().add(Code.pc - 2);
			}
			else {
				// poslednji u nizu && iskaza
				Code.loadConst(1);
				Code.putFalseJump(Code.inverse[Code.eq], 0);
				adreseZaThen.getLast().add(Code.pc - 2);
			}
		}
		else {
			// poslednji deo || dela
			Code.loadConst(1);
			Code.putFalseJump(Code.eq, 0);
			adreseZaElse.getLast().add(Code.pc - 2);
		}
	}
	
	public void visit(OrOperator or) {
		for(int x : adreseZaZakrpu.getLast()) {
			Code.fixup(x);
		}
		adreseZaZakrpu.getLast().clear();
		if(or.getParent().getParent() instanceof IfElseStat) {
			//poslednjiDeo.getLast() = true;
			poslednjiDeo.set(poslednjiDeo.size() - 1, true);
		}
	}
	
	public void visit(ThenZakrpi then1) {
	
		
		for(int x : adreseZaThen.getLast()) {
			Code.fixup(x);
		}
		adreseZaThen.getLast().clear();
		
	}
	
	public void visit(Then1 then) {
		IfElseStat parent = (IfElseStat) then.getParent();
		if(parent.getElse1() instanceof ElseStmt) {
			Code.putJump(0);
			adr2 = Code.pc - 2;
		}
	
	}
	
	public void visit(ElseZakrpi else1) {
	
		
		for(int x : adreseZaElse.getLast()) {
			Code.fixup(x);
		}
		adreseZaElse.getLast().clear();
		//cond = false;
		
	}
	
	public void visit(ElseStmt els) {
		Code.fixup(adr2);
	}
	
	public void visit(NoElseStmt else1) {
		
		//Code.fixup(adr2);
	
	}
	public void visit(IfElseStat stmt) {
		//poslednjiDeo = false;
		//poslednjiDeo.set(nivoUgnjezdenosti, false);
		nivoUgnjezdenosti--;
		poslednjiDeo.removeLast();
		adreseZaElse.removeLast();
		adreseZaThen.removeLast();
		adreseZaZakrpu.removeLast();
		
	}

}

package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	boolean errorDetected = false;
	int nVars;
	Obj currentType = null;
	boolean mainExsist = false;
	Obj currentMethod = null;
	ArrayList<CompilerError> listaSemantickihGresaka = new ArrayList<CompilerError>();
	
	void dodajGresku(int line, String message, CompilerError.CompilerErrorType type) {
		listaSemantickihGresaka.add(new CompilerError(line, message, type));
	}
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
		dodajGresku(line, message, CompilerError.CompilerErrorType.SEMANTIC_ERROR);
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(ProgName progName) {
		progName.obj = Tab1.insert(Obj.Prog, progName.getProgName(), Tab1.noType);
		Tab1.openScope();
	}
	
	public void visit(Program program) {
		nVars = Tab1.currentScope.getnVars();
		Tab1.chainLocalSymbols(program.getProgName().obj);
		Tab1.closeScope();
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab1.find(type.getTypeName());
		if(typeNode == Tab1.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u Tabeli simbola ", type);
			type.struct = Tab1.noType;
			currentType = typeNode;
		}
		else {
			if(Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				currentType = typeNode;
			}
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				type.struct = Tab1.noType;
				currentType = typeNode;
			}
		}
	}
	
	public void visit(VarIdentif varIdent) {
		Obj variable = Tab1.currentScope().findSymbol(varIdent.getVarName());
		if(variable == null) { //ovde ne smem da poredim sa Tab1.noObj jer ako udjem u findSymbol vidim da ona vraca null a ne noObj
			if(varIdent.getBracketOption() instanceof Brackets) {
				// ako je u pitanju niz 
				Obj varNode = Tab1.insert(Obj.Var, varIdent.getVarName(), new Struct(Struct.Array, currentType.getType()));
				report_info("Deklarisana promenljiva " + varIdent.getVarName() + " koja je tipa :" + currentType.getName() +"[]", varIdent);	
			}
			else {
				// ako nije u pitanju niz
				Obj varNode = Tab1.insert(Obj.Var, varIdent.getVarName(), currentType.getType());
				report_info("Deklarisana promenljiva " + varIdent.getVarName() + " koja je tipa :" + currentType.getName(), varIdent);
			}
		}
		else {
			report_error("Error: Ponovo deklarisana promenljiva " + varIdent.getVarName() + " koja je tipa :" + currentType.getName(), varIdent);
		}
	}
	
	public void visit(VarDecl varDecl) {
		currentType = null;
	}

	public void visit(ConstNum constIdent) {
		Obj cnst = Tab1.find(constIdent.getConstName());
		if(cnst == Tab1.noObj) {
			report_info("Deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);
			Obj varNode = Tab1.insert(Obj.Con, constIdent.getConstName(), currentType.getType());
			varNode.setAdr(constIdent.getValue());
		}
		else {
			report_error("Error: Ponovo deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);	
		}
	}
	
	public void visit(ConstBool constIdent) {
		Obj cnst = Tab1.find(constIdent.getConstName());
		if(cnst == Tab1.noObj) {
			report_info("Deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);
			Obj varNode = Tab1.insert(Obj.Con, constIdent.getConstName(), currentType.getType());
			varNode.setAdr(constIdent.getValue() ? 1 : 0);
		}
		else {
			report_error("Error: Ponovo deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);	
		}
	}
	
	public void visit(ConstChar constIdent) {
		Obj cnst = Tab1.find(constIdent.getConstName());
		if(cnst == Tab1.noObj) {
			report_info("Deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);
			Obj varNode = Tab1.insert(Obj.Con, constIdent.getConstName(), currentType.getType());
			varNode.setAdr(constIdent.getValue());
		}
		else {
			report_error("Error: Ponovo deklarisana konstanta " + constIdent.getConstName() + " koja je tipa :" + currentType.getName(), constIdent);	
		}
	}
	
	public void visit(ConstDecl constDecl) {
		currentType = null;
	}
	
	public void visit(MethTypeName methodTypeName) {
		Obj obj = Tab1.find(methodTypeName.getMethName());
		if(obj == Tab1.noObj) {
			currentMethod = Tab1.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
			methodTypeName.obj = currentMethod;
			if(methodTypeName.getMethName().equals("main")) {
				mainExsist = true;
			}
			report_info("Deklarisana metoda:"+methodTypeName.getMethName(), methodTypeName);
			Tab1.openScope();
		}
		else {
			methodTypeName.obj = Tab1.noObj;
			report_error("Vec deklarisana metoda "+methodTypeName.getMethName(), methodTypeName);
			currentMethod = obj;
			Tab1.openScope();
		}
	}
	
	public void visit(MethVoidName methodTypeName) {
		Obj obj = Tab1.find(methodTypeName.getMethName());
		if(obj == Tab1.noObj) {
			//currentMethod = Tab1.insert(Obj.Meth, methodTypeName.getMethName(), Tab1.voidType);
			currentMethod = Tab1.insert(Obj.Meth, methodTypeName.getMethName(), Tab1.noType);
			methodTypeName.obj = currentMethod;
			if(methodTypeName.getMethName().equals("main")) {
				mainExsist = true;
			}
			report_info("Deklarisana metoda:"+methodTypeName.getMethName(), methodTypeName);
			Tab1.openScope();
		}
		else {
			methodTypeName.obj = Tab1.noObj;
			report_error("Vec deklarisana metoda "+methodTypeName.getMethName(), methodTypeName);
			currentMethod = obj;
			Tab1.openScope();
		}	
	}
	
	
	public void visit(MethodDeclWithoutFormPars methodDecl) {
		Tab1.chainLocalSymbols(currentMethod);
		Tab1.closeScope();
		currentMethod = null;
	}
	
	public void visit(MethodDeclWithFormPars methodDecl) {
		Tab1.chainLocalSymbols(currentMethod);
		Tab1.closeScope();
		currentMethod = null;
	}
	
	public void visit(FormPar formPar) {
		Obj obj = Tab1.currentScope().findSymbol(formPar.getName());
		if(obj == null) { //ovde ne smem da poredim sa Tab1.noObj jer ako udjem u findSymbol vidim da ona vraca null a ne noObj
			if(formPar.getBracketOption() instanceof Brackets) {
				// ako je u pitanju niz 
				Obj varNode = Tab1.insert(Obj.Var, formPar.getName(), new Struct(Struct.Array, currentType.getType()));
				report_info("Deklarisan argument fje " + formPar.getName() + " koji je tipa :" + currentType.getName() +"[]", formPar);		
			}
			else {
				// ako nije u pitanju niz
				Obj varNode = Tab1.insert(Obj.Var, formPar.getName(), currentType.getType());
				report_info("Deklarisan argument " + formPar.getName() + " koji je tipa :" + currentType.getName(), formPar);
			}
		}
		else {
			report_error("Error: Ponovo deklarisan argument " + formPar.getName() + " koji je tipa :" + currentType.getName(), formPar);
		}
	}
	
	public void visit(DesignatorIdent designator) {
		Obj obj = Tab1.find(designator.getName());
		if(obj == Tab1.noObj) {
			designator.obj = Tab1.noObj;
			report_error("Error: Greska na liniji " + designator.getLine() + " ime "+designator.getName()+ " nije deklarisano!", designator);
		}
		else {
			designator.obj = obj;
		}
	}
	
	public void visit(DesignatorExpr designator) {
		if(designator.getDesignatorName().obj.getType().getKind() == Struct.Array) {
			if(designator.getExpr().struct == Tab1.intType) {
				designator.obj = new Obj(Obj.Elem,"",designator.getDesignatorName().obj.getType().getElemType());
				// PROVERI DA LI JE MOGLO I JEDNOSTAVNIJE OD OVOGA
			}
		}
		else {
			report_error("Error: Identifikator nije niz! na liniji "+designator.getLine(), designator);
		}
	}
	
	public void visit(DesignatorDot designator) {
		// ovo je za vise od A nivoa
	}
	
	public void visit(DesignatorName designator) {
		designator.obj = designator.getDesignator().obj;
	}
	
	
	
	public void visit(DesigStatAssingopExpr desigStat) {
		Obj obj = desigStat.getDesignator().obj;
		if(obj.getKind() == Obj.Var || obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld) {
		    if(desigStat.getDesignator().obj.getType().compatibleWith(desigStat.getExpr().struct)) { // ako su designator i expr obicni tipovi
				//report_info("Designator i expr su odgovarajuceg tipa na liniji "+ desigStat.getLine(), desigStat);
			}
			else {
				report_error("Error: Designator i expr nisu odgovarajuceg tipa na liniji "+ desigStat.getLine(), desigStat);	
			}
		}
		else {
			report_error("Error: Designator nije odgovarajuceg tipa na liniji " + desigStat.getLine(), desigStat);
		}
	}
	
	public void visit(FactDesign factDesign) {
		factDesign.struct = factDesign.getDesignator().obj.getType();
	}
	
	public void visit(FactNum factNum) {
		factNum.struct = Tab1.intType;
	}
	
	public void visit(FactChar factChar) {
		factChar.struct = Tab1.charType;
	}
	
	public void visit(FactBool factBool) {
		factBool.struct = Tab1.boolType;
	}
	
	public void visit(TermNoTerminate term) {
		if(!term.getFactor().struct.compatibleWith(term.getTerm().struct) && !term.getFactor().struct.compatibleWith(Tab1.intType)) {
			term.struct = term.getFactor().struct;
			report_error("Error: Nekompatibilni tipovi u smeni Term na liniji "+term.getLine(), term);
			//report_error("$$$"+term.getFactor().struct.getKind() + term.getTerm().struct.getKind(), null);
		}
		else {
			term.struct = term.getFactor().struct;
		}
	}
	
	public void visit(TermTerminate term) {
		term.struct = term.getFactor().struct;
	}
	
	public void visit(Expr11 expr) {
		expr.struct = expr.getTerm().struct;
	}
	
	public void visit(Expr12 expr) {
		expr.struct = expr.getTerm().struct;
	}
	
//	public void visit(NoTernaryOperator expr) {
//		expr.struct = expr.getExpr1().struct;
//	}
	
	// ovde uopste ne proveravam condition uslov, vec samo then i else grane
//	public void visit(IfElseCondition expr) {
//		if(expr.getThen1().struct.compatibleWith(expr.getElse1().struct)) {
//			expr.struct = expr.getThen1().struct;
//		}
//		else {
//			report_error("Error: Then i else grane nisu istog tipa!",null);
//			expr.struct = Tab1.noType;
//		}
//	}
	
	//public void visit(Condition1 condition1) {
		//condition1.struct = condition1.getExpr1().struct;
	//}
	
	public void visit(Then1 then1) {
		//then1.struct = then1.getExpr1().struct;
	}
	
	public void visit(Else1 else1) {
		//else1.struct = else1.getExpr1().struct;
	}
	
	public void visit(FactNewArray factNewArray) {
		factNewArray.struct = new Struct(Struct.Array, factNewArray.getType().struct);
	}
	
	public void visit(FactExpr factExpr) {
		factExpr.struct = factExpr.getExpr().struct;
	}
	
	public void visit(DesigStatPlusPlus desigStat) {
		Obj obj = desigStat.getDesignator().obj;
		if(obj.getKind() == Obj.Var || obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld) {
			if(obj.getType().getKind() == Struct.Int) {
				//report_info("Operacija ++ je ok", desigStat);
			}
			else {
				report_error("Error: Designator nije tipa int", desigStat);
			}
		}
		else {
			report_error("Error: Designator ne oznacava to sto treba", desigStat);
		}
		
	}
	
	public void visit(DesigStatMinusMinus desigStat) {
		Obj obj = desigStat.getDesignator().obj;
		if(obj.getKind() == Obj.Var || obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld) {
			if(obj.getType().getKind() == Struct.Int) {
				//report_info("Operacija -- je ok", desigStat);
			}
			else {
				report_error("Error: Designator nije tipa int", desigStat);
			}
		}
		else {
			report_error("Error: Designator ne oznacava to sto treba", desigStat);
		}
	}
	
	public void visit(ReadStat stat) {
		Obj obj = stat.getDesignator().obj;
		if(obj.getKind() == Obj.Var || obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld) {
			if(obj.getType().getKind() == Struct.Int || obj.getType().getKind() == Struct.Char || obj.getType().getKind() == Struct.Bool) {
				//report_info("***Operacija READ je ok", stat);
			}
			else {
				report_error("Error: Designator nije dobrog tipa na liniji "+ stat.getLine(), stat);
			}
		}
		else {
			report_error("Error: Designator ne oznacava to sto treba na liniji "+ stat.getLine(), stat);
		}
	}
	
	public void visit(PrintStat stat) {
		Struct struct = stat.getExpr().struct;
		if(struct.getKind() == Struct.Int || struct.getKind() == Struct.Char || struct.getKind() == Struct.Bool) {
			//report_info("////Operacija PRINT je ok", stat);
		}
		else {
			report_error("Error: Expr nije dobrog tipa na liniji "+ stat.getLine(), stat);
		}	
	}
	
	public void visit(PrintStatWithNum stat) {
		Struct struct = stat.getExpr().struct;	
		if(struct.getKind() == Struct.Int || struct.getKind() == Struct.Char || struct.getKind() ==  Struct.Bool) {
			//report_info("////Operacija PRINT je ok", stat);
		}
		else {
			report_error("Error: Expr nije dobrog tipa na liniji "+ stat.getLine(), stat);
		}
	}
	
	public void visit(CondFactMore cond) {
		if(cond.getExpr().struct.compatibleWith(cond.getExpr1().struct)) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Losi argumenti za poredjenje  na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	public void visit(CondFactTerminate cond) {
		if(cond.getExpr().struct.getKind() == Struct.Bool) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Argument nije tipa boolean na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	
	public void visit(CondTermMore cond) {
		if(cond.getCondFact().struct.getKind() == Struct.Bool && cond.getCondTerm().struct.getKind() == Struct.Bool) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Argumenti nisu tipa boolean na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	public void visit(CondTermTerminate cond) {
		if(cond.getCondFact().struct.getKind() == Struct.Bool) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Argumenti nisu tipa boolean na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	public void visit(ConditionMore cond) {
		if(cond.getCondTerm().struct.getKind() == Struct.Bool && cond.getCondition().struct.getKind() == Struct.Bool) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Argumenti nisu tipa boolean na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	public void visit(ConditionTerminate cond) {
		if(cond.getCondTerm().struct.getKind() == Struct.Bool) {
			cond.struct = Tab1.boolType;
		}
		else {
			//report_error("Argumenti nisu tipa boolean na liniji " + cond.getLine(), null);
			cond.struct = Tab1.noType;
		}
	}
	
	public void visit(Condition1 cond) {
		cond.struct = cond.getCondition().struct;
	}
	
	public void visit(IfElseStat stmt) {
		if(stmt.getCondition().struct.getKind() != Struct.Bool) {
			report_error("Condition nije tipa boolean na liniji " + stmt.getLine(), stmt);
		}
	}
	
	
	public boolean passed() {
		return !errorDetected;
	}
}



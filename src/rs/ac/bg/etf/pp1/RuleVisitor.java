package rs.ac.bg.etf.pp1;

import org.apache.log4j.*;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor{

	int printCallCount = 0;
	int varDeclCount = 0;
	
	Logger log = Logger.getLogger(getClass());
	
	/*public void visit(VarDecl varDecl) {
		 varDeclCount++;
	}

	public void visit(PrintStat PrintStat) { 
		printCallCount++;
		 log.info("Prepoznata naredba print!");
	}*/

}

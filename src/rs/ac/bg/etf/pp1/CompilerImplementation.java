package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.test.Compiler;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.etf.pp1.mj.runtime.Code;

public class CompilerImplementation implements Compiler {

	@Override
	public List<CompilerError> compile(String sourceFilePath, String outputFilePath) {
		Logger log = Logger.getLogger(CompilerImplementation.class);

		Reader br = null;
		try {
			File sourceCode = new File(sourceFilePath);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);

			MJParser p = new MJParser(lexer);
			Symbol s = p.parse(); // pocetak parsiranja

			Program prog = (Program) (s.value);
			Tab1.init();
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v);

			// log.info("Print count calls = "+ v.printCallCount);
			// log.info("Deklarisanih promenljivih ima = "+ v.varDeclCount);

			log.info("===================================");
			Tab1.dump();
			if (v.mainExsist == false) {
				log.error("Error: Nije deklarisana main funckija!");
			} else {
				log.info("Deklarisana je main funkcija!");
			}
			if (v.passed()) {
				File objFile = new File(outputFilePath);
				if (objFile.exists())
					objFile.delete();

				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Uspesno izvrsena semanticka analiza!!!");
			} else {
				log.error("Error: Neuspesno izvrsena semanticka analiza!!!");
			}
			
			List<CompilerError> konacnaLista = new ArrayList<>();
			konacnaLista.addAll(lexer.listaLeksickihGresaka);
			konacnaLista.addAll(p.listaSintaksnihGresaka);
			konacnaLista.addAll(v.listaSemantickihGresaka);
			return konacnaLista;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
		}
		return null;

	}

}

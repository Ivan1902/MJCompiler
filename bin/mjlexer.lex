
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.test*;
import java.util.ArrayList;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
	
	public ArrayList<CompilerError> listaLeksickihGresaka = new ArrayList<CompilerError>();
	
	void dodajGresku(int line, String message, CompilerError.CompilerErrorType type) {
		listaLeksickihGresaka.add(new CompilerError(line, message, type));
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROGRAM, yytext());}
"break"		{ return new_symbol(sym.BREAK, yytext());}
"class"		{ return new_symbol(sym.CLASS, yytext());}
"enum"		{ return new_symbol(sym.ENUM, yytext());}
"else"		{ return new_symbol(sym.ELSE, yytext());}
"const"		{ return new_symbol(sym.CONST, yytext());}
"if" 		{ return new_symbol(sym.IF, yytext());}	
"switch"	{ return new_symbol(sym.SWITCH, yytext());}
"do"		{ return new_symbol(sym.DO, yytext());}
"while"		{ return new_symbol(sym.WHILE, yytext());}
"new"		{ return new_symbol(sym.NEW, yytext());}
"print"		{ return new_symbol(sym.PRINT, yytext());}
"read"		{ return new_symbol(sym.READ, yytext());}
"return"	{ return new_symbol(sym.RETURN, yytext());}
"void"		{ return new_symbol(sym.VOID, yytext());}
"extends"	{ return new_symbol(sym.EXTENDS, yytext());}
"continue"	{ return new_symbol(sym.CONTINUE, yytext());}
"case"		{ return new_symbol(sym.CASE, yytext());}

"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-"			{ return new_symbol(sym.MINUS, yytext()); }
"*"			{ return new_symbol(sym.MUL, yytext()); }
"/"			{ return new_symbol(sym.DIV, yytext()); }
"%"			{ return new_symbol(sym.MOD, yytext()); }
"=="		{ return new_symbol(sym.EQEQ, yytext()); }
"!="		{ return new_symbol(sym.NOTEQ, yytext()); }
">"			{ return new_symbol(sym.GREATER, yytext()); }
">="		{ return new_symbol(sym.GREATEREQ, yytext()); }
"<"			{ return new_symbol(sym.LESS, yytext()); }	
"<="		{ return new_symbol(sym.LESSEQ, yytext()); }
"&&"		{ return new_symbol(sym.ANDAND, yytext()); }
"||"		{ return new_symbol(sym.OROR, yytext()); }
"="			{ return new_symbol(sym.EQ, yytext()); }
"++"		{ return new_symbol(sym.PLUSPLUS, yytext()); }
"--"		{ return new_symbol(sym.MINUSMINUS, yytext()); }
";"			{ return new_symbol(sym.SEMI, yytext()); }
","			{ return new_symbol(sym.COMMA, yytext()); }
"."			{ return new_symbol(sym.DOT, yytext()); }
"("			{ return new_symbol(sym.LPAREN, yytext()); }
")"			{ return new_symbol(sym.RPAREN, yytext()); }
"["			{ return new_symbol(sym.LBRACKET, yytext()); }
"]"			{ return new_symbol(sym.RBRACKET, yytext()); }
"{"			{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"?"			{ return new_symbol(sym.QUESTION, yytext()); }
":"			{ return new_symbol(sym.DOTDOT, yytext()); }


"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }


[0-9]+ 		 { return new_symbol(sym.NUM, new Integer (yytext())); }
"true"|"false" 			{ return new_symbol(sym.BOOL, new Boolean(yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 			{return new_symbol (sym.IDENT, yytext()); }
"'"."'" 		{ return new_symbol(sym.CHAR, new Character(yytext().charAt(1))); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1));
	dodajGresku((yyline + 1), "Leksicka greska ("+yytext()+")", CompilerError.CompilerErrorType.LEXICAL_ERROR); }

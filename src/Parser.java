import java.util.LinkedList;
import java.util.ListIterator;

public class Parser {
	Scanner scanner;
	
	Parser() {
		scanner = new Scanner();
	}
	
	public boolean parse(String inputFile) {
		LinkedList<Token> tokens = scanner.scan(inputFile);
		ListIterator<Token> iter = tokens.listIterator();
		
		return classStatement(iter);
	}
	
	private boolean check(Token.TypePrintable src1, Token.TypePrintable src2) {
		return src1 == src2;
	}
	
	// Class_statement -> class Id { main() { statements } }
	private boolean classStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.KeywordList.CLASS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.IdList.ID)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_CURLY)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.KeywordList.MAIN)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.R_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_CURLY)
			return false;

		if (!statements(iter))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.R_CURLY)
			return false;
		
		return true;
	}
	
	// Statements -> (Def_statement|If_statement|Print_statement|While_statement|For_statement|Operate_statement)[Statements]
	private boolean statements(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		lexeme = iter.previous();
		System.out.println(lexeme.getLexeme());
		
		while (lexeme.getToken() instanceof Token.CommentList)
			lexeme = iter.next();
		lexeme = iter.previous();
		
		if (lexeme.getToken() instanceof Token.KeywordList) {
			if (((Token.KeywordList)lexeme.getToken()).isTypeKeywords()) {
				if (!defStatement(iter))
					return false;
			}
			if (lexeme.getToken() == Token.KeywordList.IF) {
				if (!ifStatement(iter))
					return false;
			} else if (lexeme.getToken() == Token.KeywordList.PRINT) {
				if (!printStatement(iter))
					return false;
			} else if (lexeme.getToken() == Token.KeywordList.WHILE) {
				if (!whileStatement(iter))
					return false;
			} else if (lexeme.getToken() == Token.KeywordList.FOR) {
				if (!forStatement(iter))
					return false;
			}
		} else if (lexeme.getToken() == Token.IdList.ID) {
			if (!operateStatement(iter))
				return false;
		} else {
			return false;
		}
		
		lexeme = iter.next();
		lexeme = iter.previous();
		if ((lexeme.getToken() instanceof Token.KeywordList) || (lexeme.getToken() == Token.IdList.ID)) {
			if (!statements(iter))
				return false;
		}
		
		return true;
	}
	
	// While_statement -> while (factor relOp factor) { statements }
	private boolean whileStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.KeywordList.WHILE)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isFactor(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isRelOp(lexeme))
			return false;
		
		lexeme = iter.next();
		if (!isFactor(lexeme))
			return false;
		
		lexeme = iter.next();
		if (lexeme.getToken() != Token.SymbolList.R_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		if (lexeme.getToken() != Token.SymbolList.L_CURLY)
			return false;

		if (!statements(iter))
			return false;
		
		lexeme = iter.next();
		if (lexeme.getToken() != Token.SymbolList.R_CURLY)
			return false;
		
		return true;
	}
	
	// For_statement -> for (Def_statement; factor relOp factor; Operate_statement) { statements }
	private boolean forStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		if (lexeme.getToken() != Token.KeywordList.FOR)
			return false;
		
		lexeme = iter.next();
		if (lexeme.getToken() != Token.SymbolList.L_PARENTHESIS)
			return false;
		
		if (!defStatement(iter))
			return false;
		
		lexeme = iter.next();
		if (lexeme.getToken() != Token.SymbolList.SEMICOLON)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isFactor(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isRelOp(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isFactor(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.SEMICOLON)
			return false;
		
		if (!operateStatement(iter))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_CURLY)
			return false;
		
		if (!statements(iter))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.R_CURLY)
			return false;
		
		return true;
	}
	
	// Operate_statement -> Id (Unary_op | = id op id) 
	private boolean operateStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.IdList.ID)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!(lexeme.getToken() instanceof Token.OperatorList))
			return false;
		
		if (((Token.OperatorList)lexeme.getToken()).isUnaryOp())
			return true;
		else if (lexeme.getToken() != Token.OperatorList.ASSIGN) {
			lexeme = iter.next();
			System.out.println(lexeme.getLexeme());
			if (lexeme.getToken() != Token.IdList.ID)
				return false;
			
			lexeme = iter.next();
			System.out.println(lexeme.getLexeme());
			if (!(lexeme.getToken() instanceof Token.OperatorList))
				return false;
			
			lexeme = iter.next();
			System.out.println(lexeme.getLexeme());
			if (lexeme.getToken() != Token.IdList.ID)
				return false;
			
			return true;
		} else {
			return false;
		}
	}
	
	// Def_statement -> Type_keywords id [=Literal];
	private boolean defStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isTypeKeywords(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isId(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() == Token.OperatorList.ASSIGN) {
			lexeme = iter.next();
			System.out.println(lexeme.getLexeme());
			if (lexeme.getToken() instanceof Token.LiteralList) {
				lexeme = iter.next();
				System.out.println(lexeme.getLexeme());
				if (lexeme.getToken() == Token.SymbolList.SEMICOLON)
					return true;
				else
					return false;
			} else
				return false;
		} else if (lexeme.getToken() == Token.SymbolList.SEMICOLON) {
			return true;
		} else {
			lexeme = iter.previous();
		}
		return true;
	}
	
	// If_statement -> if (rel_statement) { statements } [else if_statement] [else { statements }]
	private boolean ifStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.KeywordList.IF)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.L_PARENTHESIS)
			return false;
		
		if (!relStatement(iter))
			return false;
		
		if (!statements(iter))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() == Token.KeywordList.ELSE) {
			lexeme = iter.next();
			System.out.println(lexeme.getLexeme());
			if (lexeme.getToken() == Token.KeywordList.IF) {
				lexeme = iter.previous();
				if (!ifStatement(iter))
					return false;
			} else {
				if (lexeme.getToken() != Token.SymbolList.L_CURLY)
					return false;
				
				if (!statements(iter))
					return false;
				
				if (lexeme.getToken() != Token.SymbolList.R_CURLY)
					return false;
			}
		} else {
			lexeme = iter.previous();
		}
		return true;
	}

	// Rel_statement -> factor relOp factor
	private boolean relStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isFactor(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isRelOp(lexeme))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!isFactor(lexeme))
			return false;
		
		return true;
	}
	
	 // Print_statement -> out.println (Literal|Id);
	private boolean printStatement(ListIterator<Token> iter) {
		Token lexeme = iter.next();	
		System.out.println(lexeme.getLexeme());	
		if (lexeme.getToken() != Token.SymbolList.L_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (!((lexeme.getToken() == Token.IdList.ID) || (lexeme.getToken() instanceof Token.LiteralList)))
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.R_PARENTHESIS)
			return false;
		
		lexeme = iter.next();
		System.out.println(lexeme.getLexeme());
		if (lexeme.getToken() != Token.SymbolList.SEMICOLON)
			return false;
		
		return true;
	}
	
	// factor -> id|Numeric_literal
	private boolean isFactor(Token lexeme) {
		if (lexeme.getToken() == Token.IdList.ID)
			return true;
		else if (lexeme.getToken() instanceof Token.LiteralList)
			return ((Token.LiteralList)lexeme.getToken()).isNumeric();
		return false;
	}
	
	// repOp -> relOp
	private boolean isRelOp(Token lexeme) {
		if (!(lexeme.getToken() instanceof Token.OperatorList))
			return false;
		
		return ((Token.OperatorList)lexeme.getToken()).isRelationalOp();
	}
	
	// typeKeywords -> typeKeywords
	private boolean isTypeKeywords(Token lexeme) {
		if (!(lexeme.getToken() instanceof Token.KeywordList))
			return false;
		
		return ((Token.KeywordList)lexeme.getToken()).isTypeKeywords();
	}
	
	// id -> id
	private boolean isId(Token lexeme) {
		return lexeme.getToken() instanceof Token.IdList;
	}
}

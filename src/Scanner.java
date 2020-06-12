import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import java.io.BufferedReader;

public class Scanner {
	private LinkedList<Token> tokens;
	
	public LinkedList<Token> scan(String inputFile) {
		String context = "";
		try {
			context = readFile(inputFile);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

		tokens = tokenize(context);
		ListIterator<Token> iter = tokens.listIterator();
		
		while (iter.hasNext()) {
			Token token = iter.next();
			Token curr = token;
			
			if (token.getLexeme().trim().length() == 0) {
				iter.remove();
				continue;
			}
			
			// DFA Start
			char lex = token.getLexeme().charAt(0);
			switch (lex) {
			case '(':
				token.setToken(Token.SymbolList.L_PARENTHESIS);
				break;
			case ')':
				token.setToken(Token.SymbolList.R_PARENTHESIS);
				break;
			case '{':
				token.setToken(Token.SymbolList.L_CURLY);
				break;
			case '}':
				token.setToken(Token.SymbolList.R_CURLY);
				break;
			case '[':
				token.setToken(Token.SymbolList.L_BRACKET);
				break;
			case ']':
				token.setToken(Token.SymbolList.R_BRACKET);
				break;
			case ';':
				token.setToken(Token.SymbolList.SEMICOLON);
				break;
			case '\'':
				token.setToken(Token.SymbolList.SINGLE_QUOTER);
				token = curr = iter.next();
				while (iter.hasNext()) {
					String temp = curr.getLexeme();
					if (temp.charAt(0) == '\'')
						break;
					if (token != curr) {
						token.setLexeme(token.getLexeme() + temp);
						iter.remove();
					}
					else
						token.setLexeme(temp);
					curr = iter.next();
				}
				token.setToken(Token.LiteralList.STRING);
				if (curr.getLexeme().charAt(0) == '\'')
					curr.setToken(Token.SymbolList.SINGLE_QUOTER);
				break;
			case '"':
				token.setToken(Token.SymbolList.DOUBLE_QUOTER);
				token = curr = iter.next();
				while (iter.hasNext()) {
					String temp = curr.getLexeme();
					if (temp.charAt(0) == '"')
						break;
					if (token != curr) {
						token.setLexeme(token.getLexeme() + temp);
						iter.remove();
					}
					else
						token.setLexeme(temp);
					curr = iter.next();
				}
				token.setToken(Token.LiteralList.STRING);
				if (curr.getLexeme().charAt(0) == '"')
					curr.setToken(Token.SymbolList.DOUBLE_QUOTER);
				break;
			case '/':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '/') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						iter.remove();
						
						while (iter.hasNext()) {
							curr = iter.next();
							String temp = curr.getLexeme();
							
							if (temp.charAt(0) == '\n') {
								iter.remove();
								break;
							}
							
							token.setLexeme(token.getLexeme() + temp);
							iter.remove();
						}
						token.setToken(Token.CommentList.SINGLE);
					} else if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.DIV_ASSIGN);
						iter.remove();
					} else {
						token.setToken(Token.OperatorList.DIVISION);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.DIVISION);
				}
				break;
			case '*':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.MUL_ASSIGN);
						iter.remove();
					} else {
						token.setToken(Token.OperatorList.MULTIPLICATION);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.MULTIPLICATION);
				}
				break;
			case '%':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.MOD_ASSIGN);
						iter.remove();
					} else {
						token.setToken(Token.OperatorList.MODULO);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.MODULO);
				}
				break;
			case '<':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.LESS_EQUAL);
						iter.remove();
					} else {
						token.setToken(Token.OperatorList.LESS);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.LESS);
				}
				break;
			case '>':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.GREATER_EQUAL);
						iter.remove();
					} else {
						token.setToken(Token.OperatorList.GREATER);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.GREATER);
				}
				break;
			case '+':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.ADD_ASSIGN);
						iter.remove();
					} else if (curr.getLexeme().charAt(0) == '+') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.INCREMENT);
						iter.remove();
					} else if (Character.isDigit(curr.getLexeme().charAt(0))) {
						numberScan(iter, token, true);
					} else {
						token.setToken(Token.OperatorList.ADDITION);
						curr = iter.previous();
					}
				}
				else
					token.setToken(Token.OperatorList.ADDITION);
				break;
			case '-':
				if (iter.hasNext()) {
					curr = iter.next();
					if (curr.getLexeme().charAt(0) == '=') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.SUB_ASSIGN);
						iter.remove();
					} else if (curr.getLexeme().charAt(0) == '-') {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						token.setToken(Token.OperatorList.DECREMENT);
						iter.remove();
					} else if (Character.isDigit(curr.getLexeme().charAt(0))) {
						curr = iter.previous();
						numberScan(iter, token, true);
					} else {
						token.setToken(Token.OperatorList.SUBTRACTION);
						curr = iter.previous();
					}
				} else {
					token.setToken(Token.OperatorList.SUBTRACTION);
				}
				break;
			case '=':
				token.setToken(Token.OperatorList.ASSIGN);
				break;
			default:
				if (Character.isDigit(token.getLexeme().charAt(0))) {
					curr = iter.previous();
					numberScan(iter, token, false);
				} else {
					switch (token.getLexeme().charAt(0)) {
					case 'c':
						if (token.getLexeme().equals(Token.KeywordList.CLASS.getId()))
							token.setToken(Token.KeywordList.CLASS);
						else
							checkString(iter, token);
						break;
					case 'i':
						if (token.getLexeme().equals(Token.KeywordList.INT.getId()))
							token.setToken(Token.KeywordList.INT);
						else if (token.getLexeme().equals(Token.KeywordList.IF.getId()))
							token.setToken(Token.KeywordList.IF);
						else
							checkString(iter, token);
						break;
					case 'e':
						if (token.getLexeme().equals(Token.KeywordList.ELSE.getId()))
							token.setToken(Token.KeywordList.ELSE);
						else
							checkString(iter, token);
						break;
					case 'w':
						if (token.getLexeme().equals(Token.KeywordList.WHILE.getId()))
							token.setToken(Token.KeywordList.WHILE);
						else
							checkString(iter, token);
						break;
					case 'm':
						if (token.getLexeme().equals(Token.KeywordList.MAIN.getId()))
							token.setToken(Token.KeywordList.MAIN);
						else
							checkString(iter, token);
						break;
					case 'o':
						if (token.getLexeme().equals("out")) {
							curr = iter.next();
							if (curr.getLexeme().equals(".")) {
								token.setLexeme(token.getLexeme() + curr.getLexeme());
								iter.remove();
								curr = iter.next();
								if (curr.getLexeme().equals("println")) {
									token.setLexeme(token.getLexeme() + curr.getLexeme());
									token.setToken(Token.KeywordList.PRINT);
								} else {
									token.setToken(Token.IllegalList.KEYWORD);
								}
								iter.remove();
							} else {
								curr = iter.previous();
								checkString(iter, token);
							}
						} else {
							checkString(iter, token);
						}
						break;
					default:
						checkString(iter, token);
					}
				}
			}
			
			if (token.getToken() instanceof Token.IllegalList)
				break;
		}
		
		return tokens;
	}
	
	private void numberScan(ListIterator<Token> iter, Token token, boolean isPrevToken) {
		Token curr = iter.next();
		int index = 0;
		
		if (curr.getLexeme().charAt(index) == '0') {
			if (curr.getLexeme().length() == 1) {
				curr = iter.next();
				if (curr.getLexeme().charAt(0) == '.') {
					token.setLexeme(token.getLexeme() + curr.getLexeme());
					iter.remove();
					floatScan(iter, token, true);
				} else {
					curr = iter.previous();
					curr = iter.previous();
					curr = iter.next();
					token.setToken(Token.LiteralList.INTEGER);
					
					if (isPrevToken) {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						iter.remove();
					}
				}
				return;
			}
			
			index++;
			char ch = curr.getLexeme().charAt(index);
			if (Character.isDigit(ch) || ch == 'l' || ch == 'L' || ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F') {
				int idx = 0;
				for (char c : curr.getLexeme().substring(index).toCharArray()) {
					if (idx == curr.getLexeme().substring(index).length() - 1) {
						
						if (c == 'l' || c == 'L')
							token.setToken(Token.LiteralList.INTEGER);
						else if (c == 'd' || c == 'D' || c == 'f' || c == 'F')
							token.setToken(Token.LiteralList.FLOATING);
						else if (Character.isDigit(c))
							token.setToken(Token.LiteralList.INTEGER);
						else
							token.setToken(Token.IllegalList.LITERAL);

						if (isPrevToken) {
							token.setLexeme(token.getLexeme() + curr.getLexeme());
							iter.remove();
						}
					} else if (!Character.isDigit(c)) {
						if (c == 'e' || c == 'E') {
							floatScan(iter, token, false);
							return;
						} else {
							token.setToken(Token.IllegalList.LITERAL);
							
							if (isPrevToken) {
								token.setLexeme(token.getLexeme() + curr.getLexeme());
								iter.remove();
							}
							return;
						}
					}
					idx++;
				}
				curr = iter.next();
				if (curr.getLexeme().charAt(0) == '.') {
					token.setLexeme(token.getLexeme() + curr.getLexeme());
					iter.remove();
					floatScan(iter, token, true);
				}
			} else if (ch == 'x' || ch == 'X') {
				int idx = 0;
				for (char c : curr.getLexeme().substring(index + 1).toCharArray()) {
					if ((idx == curr.getLexeme().substring(index + 1).length() - 1) && (c == 'l' || c == 'L')) {
						token.setToken(Token.LiteralList.INTEGER);

						if (isPrevToken) {
							token.setLexeme(token.getLexeme() + curr.getLexeme());
							iter.remove();
						}
						return;
					} else if ((!Character.isDigit(c)) && (!(c >= 'A' && c <= 'F'))) {
						token.setToken(Token.IllegalList.LITERAL);

						if (isPrevToken) {
							token.setLexeme(token.getLexeme() + curr.getLexeme());
							iter.remove();
						}
						return;
					}
					index++;
				}
				token.setToken(Token.LiteralList.INTEGER);
			} else if (ch == 'b' || ch == 'B') {
				for (char c : curr.getLexeme().substring(index + 1).toCharArray()) {
					if (!(c == '0' || c == '1')) {
						token.setToken(Token.IllegalList.LITERAL);
						return;
					}
				}
				token.setToken(Token.LiteralList.INTEGER);
			}
		}
		else {
			if (curr.getLexeme().length() == 1) {
				curr = iter.next();
				if (curr.getLexeme().charAt(0) == '.') {
					token.setLexeme(token.getLexeme() + curr.getLexeme());
					iter.remove();
					floatScan(iter, token, true);
				} else {
					curr = iter.previous();
					curr = iter.previous();
					curr = iter.next();
					token.setToken(Token.LiteralList.INTEGER);

					if (isPrevToken) {
						token.setLexeme(token.getLexeme() + curr.getLexeme());
						iter.remove();
					}
				}
				return;
			}
			
			index++;
			char ch = curr.getLexeme().charAt(index);
			if (Character.isDigit(ch)) {
				int idx = 0;
				for (char c : curr.getLexeme().substring(index).toCharArray()) {
					if (idx == curr.getLexeme().substring(index).length() - 1) {
						if (c == 'l' || c == 'L')
							token.setToken(Token.LiteralList.INTEGER);
						else if (c == 'd' || c == 'D' || c == 'f' || c == 'F')
							token.setToken(Token.LiteralList.FLOATING);
						else if (!Character.isDigit(c))
							token.setToken(Token.IllegalList.LITERAL);
						else
							token.setToken(Token.LiteralList.INTEGER);

						if (isPrevToken) {
							token.setLexeme(token.getLexeme() + curr.getLexeme());
							iter.remove();
						}
					} else if (!Character.isDigit(c)) {
						if (c == 'e' || c == 'E') {
							floatScan(iter, token, false);
							return;
						} else {
							token.setToken(Token.IllegalList.LITERAL);

							if (isPrevToken) {
								token.setLexeme(token.getLexeme() + curr.getLexeme());
								iter.remove();
							}
							return;
						}
					}
					idx++;
				}
			} else {
				token.setToken(Token.IllegalList.LITERAL);

				if (isPrevToken) {
					token.setLexeme(token.getLexeme() + curr.getLexeme());
					iter.remove();
				}
			}
			
			curr = iter.next();
			if (curr.getLexeme().charAt(0) == '.') {
				token.setLexeme(token.getLexeme() + curr.getLexeme());
				iter.remove();
				floatScan(iter, token, true);
			} else {
				curr = iter.previous();
				curr = iter.previous();
				curr = iter.next();
				token.setToken(Token.LiteralList.INTEGER);

				if (isPrevToken) {
					token.setLexeme(token.getLexeme() + curr.getLexeme());
					iter.remove();
				}
			}
		}
	}

	private void floatScan(ListIterator<Token> iter, Token token, boolean isPrevToken) {
		// TODO: Floating Scanning
		Token curr = iter.next();
		token.setToken(Token.LiteralList.FLOATING);
		
		if (isPrevToken) {
			token.setLexeme(token.getLexeme() + curr.getLexeme());
			iter.remove();
		}
	}
	
	private void checkString(ListIterator<Token> iter, Token token) {
		Token curr = token;
		
		while (Character.isAlphabetic(curr.getLexeme().charAt(0)) ||
			   Character.isDigit(curr.getLexeme().charAt(0)) ||
			   curr.getLexeme().charAt(0) == '$' ||
			   curr.getLexeme().charAt(0) == '_') {
			if (token != curr) {
				token.setLexeme(token.getLexeme() + curr.getLexeme());
				iter.remove();
			}
			curr = iter.next();
		}
		
		token.setToken(Token.IdList.ID);
		curr = iter.previous();
	}
	
	private String readFile(String filename) throws FileNotFoundException, IOException  {
		String context = "";
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		String line = "";
		while ((line = reader.readLine()) != null)
			context += (line.trim() + '\n');
		reader.close();
		
		return context;
	}
	
	private LinkedList<Token> tokenize(String inputString) {
		String delimeter = "((?<= )|(?= ))|((?<=[\\W+])|(?=[\\W+]))";
		
		LinkedList<Token> tokens = new LinkedList<Token>();
		String[] lexemes = inputString.split(delimeter);
		for (String lexeme : lexemes) {
			if (lexeme.length() == 0)
				continue;
			
			Token lex = new Token(lexeme);
			tokens.add(lex);
		}
		
		return tokens;
	}
}

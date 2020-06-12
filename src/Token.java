public class Token {
	private TypePrintable tokenName;
	private String lexeme;
	
	public Token(String str) {
		lexeme = str;
	}
	public Token(String str, TypePrintable type) {
		lexeme = str;
		tokenName = type;
	}
	
	public String getLexeme() { return this.lexeme; }
	public TypePrintable getToken() { return this.tokenName; }
	public void setLexeme(String lex) { this.lexeme = lex; }
	public void setToken(TypePrintable token) { this.tokenName = token; }
	
	public interface TypePrintable {
		String getTypeAsString();
	}
	public enum CommentList implements TypePrintable {
		SINGLE("Single Line", "//"),
		MULTIPLE_START("Multiple Line Start", "/*"),
		MULTIPLE_END("Multiple Line End", "*/");
		
		final String type;
		final String identifier;
		CommentList(final String type, final String id) {
			this.type = type;
			this.identifier = id;
		}
		
		public String getId() { return identifier; }
		public String getTypeAsString() { return type + " Comment"; }
	}
	public enum IdList implements TypePrintable {
		ID("Id");
		
		final String type;
		IdList(final String type) { this.type = type; }
		public String getTypeAsString() { return type; }
	}
	public enum IllegalList implements TypePrintable {
		ID("Id"),
		KEYWORD("Keyword"),
		LITERAL("Literal"),
		OPERATOR("Operator"),
		SYMBOL("Symbol"),
		COMMENT("Comment"),
		NOTYPE("");
		
		final String type;
		IllegalList(final String type) { this.type = type; }
		public String getTypeAsString() { return "Illegal " + type; }
	}
	public enum KeywordList implements TypePrintable{
		ABSTRACT("Abstract", "abstract"),
		ASSERT("Assert", "assert"),
		BOOLEAN("Boolean", "boolean"),
		BREAK("Break", "break"),
		BYTE("Byte", "byte"),
		CASE("Case", "case"),
		CATCH("Catch", "catch"),
		CHAR("Char", "char"),
		CLASS("Class", "class"),
		CONST("Const", "const"),
		CONTINUE("Continue", "continue"),
		DEFAULT("Default", "default"),
		DO("Do", "do"),
		DOUBLE("Double", "double"),
		ELSE("Else", "else"),
		ENUM("Enum", "enum"),
		EXTENDS("Extends", "extends"),
		FINAL("Final", "final"),
		FINALLY("Finally", "finally"),
		FLOAT("Float", "float"),
		FOR("For", "for"),
		GOTO("Goto", "goto"),
		IF("If", "if"),
		IMPLEMENTS("Implements", "implements"),
		IMPORT("Import", "import"),
		INSTANCEOF("Instanceof", "instanceof"),
		INT("Int", "int"),
		INTERFACE("Interface", "interface"),
		LONG("Long", "long"),
		NATIVE("Native", "native"),
		NEW("New", "new"),
		MAIN("Main", "main"),
		PACKAGE("Package", "package"),
		PRIVATE("Private", "private"),
		PROTECTED("Protected", "protected"),
		PUBLIC("Public", "public"),
		RETURN("Return", "return"),
		SHORT("Short", "short"),
		STATIC("Static", "static"),
		STRICTFP("Strictfp", "strictfp"),
		SUPER("Super", "super"),
		SWITCH("Switch", "switch"),
		SYNCHRONIZED("Synchronized", "synchronized"),
		THIS("This", "this"),
		THROW("Throw", "throw"),
		THROWS("Throws", "throws"),
		TRANSIENT("Trasient", "trasient"),
		TRY("Try", "try"),
		VOID("Void", "void"),
		VOLATILE("Volatile", "volatile"),
		WHILE("While", "while"),
		TRUE("true", "true"),
		FALSE("false", "false"),
		NULL("null", "null"),
		PRINT("out.println", "out.println");
		
		final String type;
		final String identifier;
		KeywordList(final String type, final String id) {
			this.type = type;
			this.identifier = id;
		}
		
		public String getId() { return identifier; }
		public String getTypeAsString() { return type + " Keyword"; }
		public boolean isTypeKeywords() {
			boolean result = false;
			
			switch (identifier) {
			case "byte":
			case "short":
			case "int":
			case "long":
			case "double":
			case "float":
				result = true;
				break;
			default:
				result = false;
				break;
			}
			return result;
		}
	}
	public enum LiteralList implements TypePrintable {
		INTEGER("Integer"),
		FLOATING("Floating"),
		STRING("String");
		final String type;
		
		LiteralList(final String type) { this.type = type; }
		public String getTypeAsString() { return type + " Literal"; }
		public boolean isNumeric() {
			boolean result = false;
			
			switch (type) {
			case "Integer":
			case "Floating":
				result = true;
				break;
			default:
				result = false;
				break;
			}
			return result;
		}
	}
	public enum OperatorList implements TypePrintable {
		ASSIGN("Assignment", "="),
		ADDITION("Plus", "+"),
		ADD_ASSIGN("Add and assign", "+="),
		SUBTRACTION("Minus", "-"),
		SUB_ASSIGN("Sub and assign", "-="),
		MULTIPLICATION("Multiple", "*"),
		MUL_ASSIGN("Mul and assign", "*="),
		DIVISION("Devide", "/"),
		DIV_ASSIGN("Div and assign", "/="),
		MODULO("Moduler", "%"),
		MOD_ASSIGN("Mod and assign", "%="),
		
		INCREMENT("Increment", "++"),
		DECREMENT("Decrement", "--"),
		LESS("Less than", "<"),
		LESS_EQUAL("Less than or equal to", "<="),
		GREATER("Greater than", ">"),
		GREATER_EQUAL("Greater than or euqal to", ">="),
		
		LOGICAL_AND("And", "&&"),
		LOGICAL_OR("Or", "||"),
		LOGICAL_NOT("Not", "!"),
		BITWISE_AND("Bitwise And", "&"),
		BAND_ASSIGN("Bitwise And and assign", "&="),
		BITWISE_OR("Bitwise Or", "|"),
		BOR_ASSIGN("Bitwise Or and assign", "|="),
		BITWISE_XOR("Bitwise Xor", "^"),
		BXOR_ASSIGN("Bitwise Xor and assign", "^="),
		L_SHIFT("Left shift", "<<"),
		LSF_ASSIGN("Left shift and assign", "<<="),
		R_SHIFT("Right shift", ">>"),
		RSF_ASSIGN("Right shift and assign", ">>=");
		
		final String type;
		final String identifier;
		OperatorList(final String type, final String id) {
			this.type = type;
			this.identifier = id;
		}
		
		public String getId() { return identifier; }
		public String getTypeAsString() { return type + " Operator"; }
		public boolean isRelationalOp() { 
			boolean result = false;
			switch (identifier) {
			case "<":
			case "<=":
			case ">":
			case ">=":
			case "==":
				result = true;
				break;
				
			default:
				result = false;
				break;
			}
			
			return result;
		}
		public boolean isUnaryOp() {
			boolean result = false;
			switch (identifier) {
			case "++":
			case "--":
				result = true;
				break;
			default:
				result = false;
				break;
			}
			
			return result;
		}
	}
	public enum SymbolList implements TypePrintable {
		L_PARENTHESIS("Left Parenthesis", "("),
		R_PARENTHESIS("Right Parenthesis", ")"),
		L_CURLY("Left Curly Brace", "{"),
		R_CURLY("Right Curly Brace", "}"),
		L_BRACKET("Left Square Bracket", "["),
		R_BRACKET("Right Square Bracket", "]"),
		SEMICOLON("Semicolon", ";"),
		SINGLE_QUOTER("Single quoter", "'"),
		DOUBLE_QUOTER("Double quoter", "\"");
		
		final String type;
		final String identifier;
		SymbolList(final String type, final String id) {
			this.type = type;
			this.identifier = id;
		}
		
		public String getId() { return identifier; }
		public String getTypeAsString() { return this.type + " Symbol"; }
	}
}

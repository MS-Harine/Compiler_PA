import java.util.Iterator;
import java.util.LinkedList;

public class Tester {
	public static void main(String[] args) {
		testParser("testcase_ok.txt");
	}
	
	public static void testParser(String filename) {
		Parser parser = new Parser();
		boolean result = parser.parse(filename);
		System.out.println(result);
	}
	
	public static void testScanner(String filename) {
		Scanner scanner = new Scanner();
		LinkedList<Token> list;
		System.out.println("Test Scanner");
		
		list = scanner.scan(filename);
		Iterator<Token> iter = list.iterator();
		while (iter.hasNext()) {
			Token t = iter.next();
			System.out.println(t.getLexeme() + "\t" + t.getToken().getTypeAsString());
			if (t.getToken() instanceof Token.IllegalList)
				break;
		}
	}
}

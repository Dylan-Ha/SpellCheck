package cs146F20.Ha.project4;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class Test {

	@org.junit.jupiter.api.Test
	// Test the Red Black Tree
	public void test() {
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert("D");
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");
		assertEquals("DBACFEHGIJ", makeString(rbt));
		String str = "Color: 1, Key:D Parent: \n" + "Color: 1, Key:B Parent: D\n" + "Color: 1, Key:A Parent: B\n"
				+ "Color: 1, Key:C Parent: B\n" + "Color: 1, Key:F Parent: D\n" + "Color: 1, Key:E Parent: F\n"
				+ "Color: 0, Key:H Parent: F\n" + "Color: 1, Key:G Parent: H\n" + "Color: 1, Key:I Parent: H\n"
				+ "Color: 0, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(rbt));

	}
	
	public static String makeString(RedBlackTree t) {
		class MyVisitor implements RedBlackTree.Visitor {
			String result = "";

			public void visit(RedBlackTree.Node n) {
				result = result + n.key;
			}
		}
		;
		MyVisitor v = new MyVisitor();
		t.preOrderVisit(v);
		return v.result;
	}

	public static String makeStringDetails(RedBlackTree t) {
		{
			class MyVisitor implements RedBlackTree.Visitor {
				String result = "";

				public void visit(RedBlackTree.Node n) {
					if (n.parent == null) {
						result = result + "Color: " + n.color + ", Key:" + n.key + " Parent: \n";
					} else if (!(n.key).equals(""))
						result = result + "Color: " + n.color + ", Key:" + n.key + " Parent: " + n.parent.key + "\n";

				}
			}
			;
			MyVisitor v = new MyVisitor();
			t.preOrderVisit(v);
			return v.result;
		}
	}

	// tester for spell checking
	@org.junit.jupiter.api.Test
	public void spellCheckTest() throws IOException {
		RedBlackTree dictionary = new RedBlackTree();
		RedBlackTree poemTree = new RedBlackTree();
		BufferedReader document = new BufferedReader(new FileReader("data/Dictionary.txt"));
		BufferedReader poem = new BufferedReader(new FileReader("data/Poem.txt"));
		long a = System.nanoTime();
		while (document.readLine() != null) {
			dictionary.insert(document.readLine());
		}
		long b = System.nanoTime();
		long ba = b-a;
		System.out.println("Dictionary creation: " + ba + " nanoseconds");
		long c = System.nanoTime();
		while (poem.readLine() != null) {
			dictionary.lookUp(poem.readLine());
		}
		long d = System.nanoTime();
		long dc = d-c;
		System.out.println("Search: " + dc + " nanoseconds");
	}
}

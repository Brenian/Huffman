import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {

	private Tree tree;
	private Map<String, Character> codesToVals = new HashMap<String, Character>();
	private Map<Character, Integer> frequencies = new HashMap<Character, Integer>();
	private String[] codes = new String[1000000];
	private int count = 0;

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		int[] arrOfFreqs = new int[1000000];
		for (char c : text.toCharArray()) {
			arrOfFreqs[c]++;
		}
		PriorityQueue<Tree> nodes = new PriorityQueue<Tree>();
		for (int i = 0; i < arrOfFreqs.length; i++) {
			if (arrOfFreqs[i] > 0)
				nodes.offer(new Leaf(arrOfFreqs[i], (char) i));
		}
		while (nodes.size() > 1) {
			Tree nodeA = nodes.poll();
			Tree nodeB = nodes.poll();
			nodes.offer(new Node(nodeA, nodeB));
		}
		tree = nodes.poll();
		generateCodeMap(tree, new StringBuilder());
	}

	public void generateCodeMap(Tree tree, StringBuilder prefix) {
		if (tree instanceof Node) {
			Node node = (Node) tree;
			prefix.append("0");
			generateCodeMap(node.leftChild, prefix);
			prefix.deleteCharAt(prefix.length() - 1);
			prefix.append("1");
			generateCodeMap(node.rightChild, prefix);
			prefix.deleteCharAt(prefix.length() - 1);
		} else if (tree instanceof Leaf) {
			Leaf leaf = (Leaf) tree;
			// valsToCodes.put(leaf.val, prefix.toString());
			codesToVals.put(prefix.toString(), leaf.val);
			frequencies.put(leaf.val, leaf.freq);
			codes[leaf.val] = prefix.toString();
			count++;
			System.out.println("#" + count + " : " + leaf.val + " : " + prefix
					+ " : " + leaf.freq);
		}
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		StringBuilder encoded = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			encoded.append(codes[c]);
		}
		return encoded.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		StringBuilder temp = new StringBuilder();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < encoded.length(); i++) {
			temp.append(encoded.charAt(i));
			if (codesToVals.containsKey(temp.toString())) {
				result.append(codesToVals.get(temp.toString()));
				temp = new StringBuilder();
			}
		}
		return result.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}

	public static void main(String[] args0) {
		String test = "Peter Piper picked a peck of pickled peppers. A peck of pickled peppers Peter Piper picked. If Peter Piper picked a peck of pickled peppers, Where's the peck of pickled peppers Peter Piper picked?";
		HuffmanCoding hc = new HuffmanCoding(test);
		String encoded = hc.encode(test);
		System.out.println(hc.decode(encoded));
	}
}

abstract class Tree implements Comparable<Tree> {
	public final int freq;

	public Tree(int freq) {
		this.freq = freq;
	}

	public int compareTo(Tree tree) {
		return freq - tree.freq;
	}
}

class Leaf extends Tree {
	public final char val;

	public Leaf(int freq, char val) {
		super(freq);
		this.val = val;
	}
}

class Node extends Tree {
	public final Tree leftChild, rightChild;

	public Node(Tree l, Tree r) {
		super(l.freq + r.freq);
		leftChild = l;
		rightChild = r;
	}
}

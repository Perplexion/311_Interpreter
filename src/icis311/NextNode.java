package icis311;

public class NextNode extends StatementNode {
	
	private Node var;
	private Node f;
	public NextNode(Node n) {
		super(n);
		var=n;
	}
	
	public void forset(Node x) {
		f=x;
	}
	public Node getFor() {
		return f;
	}
	public String toString() {
		String x;
		x=("Next "+var);
		return x;
	}

}

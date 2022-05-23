package icis311;

public class GosubNode extends StatementNode{
	private String nodename;
	private Node v;
	public GosubNode(Node n) {
		super(n);
		v=n;
	}
	
	public Node getGosub() {
		return v;
	}
	public void setVname(String s) {
		nodename=s;
	}
	public String getVname() {
		return nodename;
	}
	
	public String toString() {
		String x;
		x=("Gosub "+v);
		return x;
	}

}

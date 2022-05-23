package icis311;

public class VariableNode extends Node {

	private String name;
	
	private Node e;
	public VariableNode(String n) {
		name=n;
		
	}
	public String getVar() {
		return name;
	}
	public void setVar(Node v) {
		e=v;
	}
	public Node getVal() {
		return e;
	}
	
	
	
	public void setN(String x) {
		name=x;
	}
	
	
	
	public String toString() {
		String x;
		x=(name);
		return x;
	}
	
	
}

package icis311;

public class IfNode extends StatementNode {
	private Node e1,e2,o,then; //o is bool operation node
	public IfNode(Node n) {
		super(n);
		e1=n;
	}
	
	public void setExpression(Node e) {
		e2=e;
	}
	public void setOp(Node op) {
		o=op;
	}
	public Node getLeft() {
		return e1;
	}
	public Node getRight() {
		return e2;
	}
	public Node getOp() {
		return o;
	}
	public void setThen(Node t) {
		then =t;
	}
	

	public String toString() {
		String x;
		x=("IF("+e1+o+e2+")");
		return x;
	}
}

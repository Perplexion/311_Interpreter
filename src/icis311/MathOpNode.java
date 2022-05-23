package icis311;

public class MathOpNode extends Node {
	
	
	
	
	
	public enum MathOp{
		add, subtract, multiply, divide
	}
	
	
	private Node a, b;
	private MathOp m;
	

	public MathOpNode(MathOp n) {
		a=null;
		b=null;
		m=n;
	}
	public void setL(Node l) {
		a=l;
	}
	public void setR(Node r) {
		b=r;
	}
	public String getOp() {
		if(m==MathOp.add) {
			return "+";
		}
		if(m==MathOp.subtract) {
			return "-";
		}
		if(m==MathOp.multiply) {
			return "*";
		}
		if(m==MathOp.divide) {
			return "/";
		}
		return null;
		
	}
	public Node getL() {
		return a;
	}
	public Node getR() {
		return b;
	}
	
	public String toString() {
		String x;
		x=(a+"("+m+")"+b);
		return x;
	}
}

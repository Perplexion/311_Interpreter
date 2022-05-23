package icis311;

public class ForNode extends StatementNode{

	private Node exp;
	private Node to;
	private Node increment;
	private Node afterN;
	public ForNode(Node n) {
		super(n);
		exp=n;
	}
	
	
	public Node getForEx() {
		return exp;
		
	}
	public void setTo(Node t) {
		to=t;
	}
	
	public void setStep(Node s) {
		increment=s;
	}
	public void setAfterNext(Node n) {
		afterN=n;
	}
	public Node getInc() {
		return increment;
	}
	public Node getTo() {
		return to;
	}
	public Node getAftNext() {
		return afterN;
	}
	
	public String toString() {
		String x;
		x=("For "+exp+" "+" to "+to+" "+"Step "+increment);
		return x;
	}
	
}

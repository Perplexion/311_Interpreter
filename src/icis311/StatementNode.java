package icis311;

public class StatementNode extends Node{

	public Node stat;
	private StatementNode next=null;
	public StatementNode(Node s) {
		
		stat=s;
		
		
	}
	public Node getStatement() {
		return stat;
	}
	
	public void setNext(StatementNode n) {
		next=n;
	}
	public StatementNode getNext() {
		return next;
	}
	
	
	public String toString() {
		String x;
		x=("(Statement):"+stat);
		return x;
	}
}

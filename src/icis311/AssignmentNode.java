package icis311;

public class AssignmentNode extends StatementNode {

	
	
	private VariableNode vn;
	private Node n;
	public AssignmentNode(Node no) {
		super(no);
		n=no;
		vn=null;
	}
	
	public void setVar(VariableNode var) {
		vn=var;
	}
	public VariableNode getVar() {
		return vn;
	}
	public Node getexp() {
		return n;
	}
	//public void setAssignValue(Node no) {
	//	n=no;
		
//	}
	
	public String toString() {
		String x;
		x=("(Variable)"+vn+"="+n);
		return x;
	}
	
}

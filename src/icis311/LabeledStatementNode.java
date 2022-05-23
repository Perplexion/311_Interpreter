package icis311;

public class LabeledStatementNode extends StatementNode {
	
	private String label;
	private StatementNode no;
	
	public LabeledStatementNode(StatementNode n) {
		super(n);
		no=n;
	}
	
	
	public void setLabel(String s) {
		label=s;
	}
	public String getLabel() {
		return label;
	}
	public StatementNode getSN() {
		return no;
	}
	
	
	public String toString() {
		String x;
		x=(label+super.stat);
		return x;
	}

}

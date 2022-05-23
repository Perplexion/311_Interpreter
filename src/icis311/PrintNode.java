package icis311;

import java.util.ArrayList;

public class PrintNode extends StatementNode{

	private ArrayList<Node> list=new ArrayList<Node>();
	
	
	public PrintNode(Node n) {
		super(n);
		
	}
	
	
	public void addPrint(Node n) {
		list.add(n);
		
		
	}
	
	public String toString() {
		StringBuilder st=new StringBuilder();
		
		String x="Print Node: \n";
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			st.append("\n");
		}
		x=st.toString();
		return x;
	}
	
}

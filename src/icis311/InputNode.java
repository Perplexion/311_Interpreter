package icis311;

import java.util.ArrayList;

public class InputNode extends StatementNode {

	private Node c;
	
	private ArrayList<Node> list=new ArrayList<Node>();
	public InputNode(Node n) {
		super(n);
		c=n;
		list.add(c);
	}
	
	
	public void addVInput(Node v) {
		list.add(v);
	}
	public ArrayList<Node> getInputList(){
		return list;
	}
	public void setInput(Node n,int i) {
		list.set(i, n);
	}
	
	public String toString() {
		StringBuilder st=new StringBuilder();
		
		String x="Input Node: \n";
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			st.append("\n");
		}
		x=st.toString();
		return x;
	}
	
}

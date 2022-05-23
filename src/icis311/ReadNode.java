package icis311;

import java.util.ArrayList;

public class ReadNode extends StatementNode {
	
	private ArrayList<Node> list=new ArrayList<Node>();
	
	public ReadNode(Node n) {
		super(n);
		
	}
	
	public void addRead(Node v) {
		list.add(v);
	}
	public ArrayList<Node> getRead(){
		return list;
	}
	public void setRead(Node r,int e) {
		list.set(e, r);
	}
	
	public String toString() {
		StringBuilder st=new StringBuilder();
		
		String x="Read Node: \n";
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			st.append("\n");
		}
		x=st.toString();
		return x;
	}

}

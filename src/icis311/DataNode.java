package icis311;

import java.util.ArrayList;

public class DataNode extends StatementNode {
	
	private ArrayList<Node> list=new ArrayList<Node>();
	
	
	public DataNode(Node d) {
		super(d);
		
		
	}
	
	public void addData(Node a) {
		list.add(a);
	}
	public ArrayList<Node> getData(){
		return list;
	}
	
	public String toString() {
		StringBuilder st=new StringBuilder();
		
		String x="Data Node: \n";
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			st.append("\n");
		}
		x=st.toString();
		return x;
	}
	
	

}

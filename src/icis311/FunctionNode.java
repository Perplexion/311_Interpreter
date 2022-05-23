package icis311;

import java.util.ArrayList;

public class FunctionNode extends Node{
	
	private String fn;
	private ArrayList<Node> list=new ArrayList<Node>();
	
	public FunctionNode(String n) {
		fn=n;
	}
	
	public void addPara(Node s) {
		list.add(s);
	}
	
	public ArrayList<Node> getFNs(){
		return list;
	}
	public String getF() {
		return fn;
	}
	
	public String toString() {
		String x;
		x=(fn+"(");
		StringBuilder st=new StringBuilder();
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			if(i==list.size()-1) {
				
				break;
			}
			st.append(",");
		}
		st.append(")");
		x=st.toString();
		return x;
	}

}

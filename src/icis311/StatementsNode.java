package icis311;
import java.util.ArrayList;
public class StatementsNode extends Node {

	
	private ArrayList<StatementNode> list=new ArrayList<StatementNode>();
	
	
	
	public void addStat(StatementNode n) {
		list.add(n);
	}
	public ArrayList<StatementNode> getList(){
		return list;
	}
	public void replaceStat(StatementNode n,int i) {
		list.set(i, n);
	}
	public void removeStat(int x) {
		list.remove(x);
	}
	
	public String toString() {
		StringBuilder st=new StringBuilder();
		
		String x="";
		st.append(x);
		for(int i=0;i<list.size();i++) {
			st.append(list.get(i));
			st.append("\n");
		}
		x=st.toString();
		return x;
	}
	
}

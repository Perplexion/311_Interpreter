package icis311;

public class IntegerNode extends Node {

	private int number;
	
	public IntegerNode(int n) {
		number=n;
	}
	
	
	public int getIntNum() {
		return number;
	}
	
	public String toString() {
		String x;
		x=("(Int):"+number);
		return x;
	}
	
	
}

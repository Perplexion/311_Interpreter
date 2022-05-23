package icis311;

public class FloatNode extends Node {
	
private float number;
	
	public FloatNode(float n) {
		number=n;
	}
	
	
	public float getFloatNum() {
		return number;
	}
	
	public String toString() {
		String x;
		x=("(Float):"+number);
		return x;
	}
	

}

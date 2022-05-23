package icis311;
//By Andy Vuu
public class Token {
	private String val;
	public enum tokenType{
		Number, Plus, 
		Minus, Times, 
		Div, Decimal, 
		LessThan, GreaterThan, 
		Equals, LessEquals, 
		MoreEquals, NotEquals, 
		EndofLine, PRINT, 
		LABEL, IDENTIFIER, 
		If, Else, 
		Public, Static, 
		Void, STRING, LParen, RParen, Comma,
		Variable, READ, DATA,INPUT,RETURN, GOSUB,
		FOR,NEXT,STEP,TO, RANDOM, LEFT$, RIGHT$,
		MID$,NUM$,VAL,FVAL, Then
		
		
	}
	
	private tokenType type;
	
	
	public Token(String v, tokenType t){
		type=t;
		val=v;
	}
	
	public String getV(){
		return val;
	}
	
	public tokenType getT() {
		return type;
	}
	
	public String toString() {
		String x;
		x=("("+type+": "+val+")");
		return x;
	}

	
	
}

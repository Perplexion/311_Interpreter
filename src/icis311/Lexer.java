package icis311;
import java.util.HashMap;
import icis311.Token.tokenType;
import icis311.Token;

import java.util.ArrayList;

//By Andy Vuu
public class Lexer {

	private String input;
	private HashMap<String,Token> hm= new HashMap<String, Token>();
	private HashMap<String,Token> fm= new HashMap<String, Token>();
	private int pos;
	
	Lexer(String s){
		input=s;
		this.pos=0;
	}
	

	public ArrayList<Token> Lex(String s) {
		input=s;
		ArrayList<Token> t= new ArrayList<Token>();
		char c;
		hashM();
		
		
		
		
		if(pos>= input.length()) {
			t.add(new Token("NULL",tokenType.EndofLine));
			return t;
		}
		
		c=input.charAt(pos);
		
		if(Character.isAlphabetic(c))
			recId(t);
			
		
		if(Character.isDigit(c)==true || c=='='||c=='+'||c=='-' ||c=='*'||c=='/'||c=='(')
			recOpNum(t);
		
		
		
		/*
		for(int n=0;n<t.size();n++)
			System.out.println(t.get(n));
		*/
	
		
		
		return t;
		
		
		
	}
	
	public void hashM() {
		hm.put("PRINT",new Token("PRINT",tokenType.PRINT));
		hm.put("IF",new Token("IF",tokenType.If));
		hm.put(":", new Token("LABEL",tokenType.LABEL));
		hm.put("", new Token("IDENTIFLIER",tokenType.IDENTIFIER));
		hm.put("READ",new Token("READ",tokenType.READ));
		hm.put("DATA",new Token("DATA",tokenType.DATA));
		hm.put("INPUT",new Token("INPUT",tokenType.INPUT));
		hm.put("RETURN", new Token("RETURN",tokenType.RETURN));
		hm.put("GOSUB", new Token("GOSUB",tokenType.GOSUB));
		hm.put("FOR", new Token("FOR",tokenType.FOR));
		hm.put("NEXT", new Token("NEXT",tokenType.NEXT));
		
		fm.put("RANDOM", new Token("RANDOM",tokenType.RANDOM));
		fm.put("LEFT$", new Token("LEFT$",tokenType.LEFT$));
		fm.put("RIGHT$", new Token("RIGHT$",tokenType.RIGHT$));
		fm.put("MID$", new Token("MID$",tokenType.MID$));
		fm.put("NUM$", new Token("NUM$",tokenType.NUM$));
		fm.put("VAL", new Token("VAL",tokenType.VAL));
		fm.put("VAL%", new Token("VAL%",tokenType.FVAL));
		fm.put("Then", new Token("Then",tokenType.Then));
	}
	
	public boolean isCompare(char x, char y) {
		
		
		if(x=='=')
			return true;
		if(x=='<'&& y=='=')
			return true;
		if(x=='>'&& y=='=')
			return true;
		if(x=='<'&& y=='>')
			return true;
		if(x=='<')
			return true;
		if(x=='>')
			return true;
		
		return false;
		
		
	}
	public boolean fmCheck(String s) { //function map check
		for(String x: fm.keySet()) {
			
			if(s.compareTo(x)==0) {
				return true;
			}
		}
		return false;
	}
	public boolean hashCheck(String s) {
		for(String x: hm.keySet()) {
			String y=x+"%";
			String z=x+"$";
			if(s.compareTo(x)==0||s.compareTo(y)==0||s.compareTo(z)==0) {
				return true;
			}
		}
		return false;
	}
	public void recId(ArrayList<Token> t) {
		char i='\0';
		/*
		i=input.charAt(pos);
		while(i==' ') {		//ignore whitespaces in the front		
			pos++;
			i=input.charAt(pos);
		}
		*/
		
				String ident=""; 
				while (pos<input.length()) { 
					i=input.charAt(pos);
					
					if(Character.isAlphabetic(i)==false && Character.isDigit(i)==false && i!='_' && i!='%'&& i!='$'
							&&i!='"') {	//if not a letter, number, or any of these symbols
						pos++;
						break;
					}
					StringBuilder st=new StringBuilder();
					st.append(ident);
					st.append(i);
					ident=st.toString();
					pos+=1;
				}
				
			//	pos+=ident.length();
				
				if(ident.indexOf('"')==0 && ident.substring(ident.indexOf('"')+1).lastIndexOf('"')!=-1) {
					ident=ident.substring(0, ident.lastIndexOf('"')+1);
					t.add(new Token (ident, tokenType.STRING)) ;
				}
				if(hashCheck(ident)==true) { //check if ident matches one of the known words
					if(ident.indexOf('%')!=-1)
						ident=ident.substring(0, ident.indexOf('%'));
					if(ident.indexOf('$')!=-1)
						ident=ident.substring(0, ident.indexOf('$'));
					if(ident.compareTo("PRINT")==0) {
						t.add(hm.get(ident));
						recOpNum(t);
					}
					if(ident.compareTo("DATA")==0) {
						t.add(hm.get(ident));
						recOpNum(t);					
					}
					if(ident.compareTo("READ")==0) {
						t.add(hm.get(ident));
						varList(t);
					}
					if(ident.compareTo("INPUT")==0) {
						t.add(hm.get(ident));
						stringCheck(t);		//check for a string at the beginning and add it to the token list if found
						varList(t);			//adds variables with comma tokens to the list
					}
					if(ident.compareTo("GOSUB")==0) {
						t.add(hm.get(ident));
						varList(t);			//varList also works with only one identifier
					}
					if(ident.compareTo("FOR")==0) {
						t.add(hm.get(ident));
						recId(t);		
					}
					if(ident.compareTo("NEXT")==0) {
						t.add(hm.get(ident));
						varList(t);		
					}
					if(ident.compareTo("IF")==0) {
						t.add(hm.get(ident));
						recOpNum(t);
					}
					if(ident.compareTo("RETURN")==0) {
						t.add(hm.get(ident));
						
					}
					
				}
				else if(fmCheck(ident)==true) {    //check the other hash map
					if(input.charAt(pos-1)=='(') {
						t.add(fm.get(ident));
						t.add(new Token("(",tokenType.LParen));
						recOpNum(t);
					}
				}
				
				
				else if(input.charAt(pos-1)==':') {
					t.add(new Token(ident,tokenType.LABEL));
					recId(t);				//"can currently only work with whitespace after the colon"  *fixed
					
					
				}
				else {
					t.add(new Token(ident,tokenType.IDENTIFIER));
					if(i=='=') {
						t.add(new Token("=",tokenType.Equals));
						recOpNum(t);
					}
					
					if(i=='+') {
						t.add(new Token("+",tokenType.Plus));
						recOpNum(t);
					}
					if(i=='-') {
						t.add(new Token("-",tokenType.Minus));
						recOpNum(t);
					}
					if(i=='*') {
						t.add(new Token("*",tokenType.Times));
						recOpNum(t);
					}
					if(i=='/') {
						t.add(new Token("/",tokenType.Div));
						recOpNum(t);
					}
					if(i=='<') {
						t.add(new Token("<",tokenType.LessThan));
						recOpNum(t);
					}
					if(i=='>') {
						t.add(new Token(">",tokenType.GreaterThan));
						recOpNum(t);
					}
					
				}
				if(pos==input.length())	{	//t.get(t.size()-1)!=new Token("NULL",tokenType.EndofLine)
					t.add(new Token("NULL",tokenType.EndofLine));
					pos++;
				}
	}
	
	
	public void recOpNum(ArrayList<Token> t) { //state machine 
		char c, d;
		int state=1;
		
		while(pos<input.length()) {
			thenCheck(t);   //checks for a "then" and its label
			
			stringCheck(t); //check for a string for the DATA keyword
			if(pos==input.length()) { //exit the while if stringCheck hits the input length
				break;
			}
			
			c= input.charAt(pos);
			
			if(pos!=input.length()-1)
				d=input.charAt(pos+1); //look ahead to account for operators
			else
				d='\0';
					
					
			if(Character.isDigit(c)==false && c!='+' && c!='-' && c!='/' && c!='*' 
					&& c!=' ' && c!='\t' && c!='\n'&& c!='.' && c!='"' && c!='(' 
					&& c!=')' && isCompare(c,d)==false && c!='_'&&  c!='<'&& c!='>' && c!=',' && Character.isAlphabetic(c)==false) {
				System.out.println(state);
				throw new ArithmeticException("Character not allowed: "+c);
			
			}
		
			
			
			switch(state) {
				case 1:
					if(c==' '||c=='\t') {
						state=1;
						break;
					}
					if(c=='(') {
						state=1;
						t.add(new Token (Character.toString(c),tokenType.LParen));
						break;
					}
					if(c==')') {
						state=-1;
						t.add(new Token (Character.toString(c),tokenType.RParen));
						break;
					}
					if(c==',') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Comma));
						break;
						
					}
					
					if(Character.isDigit(c)==true) {
						state=2;
						t.add(new Token(Character.toString(c),tokenType.Number));
						break;
					}
					if(Character.isAlphabetic(c)==true) {
						state=2;
						identAdd(t); //add an identifier starting with the character until a whitespace or comparison operator
						break;
					}
					
					else if(c=='-') {
						state=2;
						t.add(new Token(Character.toString(c),tokenType.Minus));
						break;
					}
				
					else { state=-1;
					t.add(new Token("NULL",tokenType.EndofLine));
					 }
					break;
				case 2:
					
					if(c==' '||c=='\t') {
						state=2;
						break;
					}
					
					if (Character.isDigit(c)==true) {
						state=2;
						t.add(new Token(Character.toString(c),tokenType.Number));
						break;
					}
					if(c==')') {
						state=2;
						t.add(new Token (Character.toString(c),tokenType.RParen));		
						
						break;
					}
					if(c==',') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Comma));
						break;
						
					}
					if(c=='T'&& d=='O') {
							state=1;
							pos++;
							t.add(new Token("TO",tokenType.TO));
							break;
					}
					
					if(stepCheck(t)==true) {
						state=1;
						break;
					}
					
					if(c=='.') {
						state=3;
						t.add(new Token(Character.toString(c),tokenType.Decimal));
						break;
					}
					else if(c=='+') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Plus));
						break;
					}
					else if(c=='-') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Minus));
						break;
					}
					else if(c=='*') {
						
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Times));
						break;
						
					}
					else if(c=='/') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Div));
						break;
					}
					
					
					
					else if(isCompare(c,d)==true) {
						if(c=='=') 
							t.add(new Token(Character.toString(c),tokenType.Equals));
						
						else if(c=='<' && d=='=') {
							pos++;
							t.add(new Token("<=",tokenType.LessEquals));
						}
						else if(c=='>'&& d=='=') {
							pos++;
							t.add(new Token (">=",tokenType.MoreEquals));
						}
						else if(c=='<'&& d=='>') {
							pos++;
							t.add(new Token ("<>",tokenType.NotEquals));
						}
						else if(c=='<') 
							t.add(new Token(Character.toString(c),tokenType.LessThan));
						
						else if(c=='>') 
							t.add(new Token (Character.toString(c),tokenType.GreaterThan));

						
						else if(c=='(') 
							t.add(new Token (Character.toString(c),tokenType.LParen));
						
						else if(c==')') 
							t.add(new Token (Character.toString(c),tokenType.RParen));
						
						state=1;	
						break;
					}
					
					else { state=-1;
					t.add(new Token("NULL",tokenType.EndofLine));
					}
					break;
				case 3:
					if(c==' '||c=='\t') {
						state=3;
						break;
					}
				
					if(Character.isDigit(c)==true) {
						state=4;
						t.add(new Token(Character.toString(c),tokenType.Number));
					}
					else { state=-1;
					t.add(new Token("NULL",tokenType.EndofLine)); }
					break;
				case 4:
					if(c==' '||c=='\t') {
						state=4;
						break;
					}
					if(c==',') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Comma));
						break;
	
					}
					if(Character.isDigit(c)==true) {
						state=4;
						t.add(new Token(Character.toString(c),tokenType.Number));
						break;
					}
					else if(c=='+') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Plus));
						break;
					}
					else if(c=='-') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Minus));
						break;
					}
					else if(c=='*') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Times));
						break;
					}
					else if(c=='/') {
						state=1;
						t.add(new Token(Character.toString(c),tokenType.Div));
						break;
					}
					else if(c=='(') {
						t.add(new Token (Character.toString(c),tokenType.LParen));
						state=1;
						break;
					}
					else if(c==')') {
						t.add(new Token (Character.toString(c),tokenType.RParen));
						state=1;
						break;
					}
					else if(isCompare(c,d)==true) {
						if(c=='=') {
							state=1;
							t.add(new Token(Character.toString(c),tokenType.Equals));
							break;
						}
						else if(c=='<' && d=='=') {
							pos++;
							t.add(new Token("<=",tokenType.LessEquals));
							state=1;
							break;
						}
						else if(c=='>'&& d=='=') {
							pos++;
							t.add(new Token (">=",tokenType.MoreEquals));
							state=1;
							break;
						}
						else if(c=='<'&& d=='>') {
							pos++;
							t.add(new Token ("<>",tokenType.NotEquals));
							state=1;
							break;
						}
						else if(c=='<') {
							t.add(new Token(Character.toString(c),tokenType.LessThan));
							state=1;
							break;
						}
						else if(c=='>') {
							t.add(new Token (Character.toString(c),tokenType.GreaterThan));
							state=1;
							break;
						}
						
						
						state=1;
						break;
					}
					
					
					
					
					
					else {
						state=-1;
						
						t.add(new Token("NULL",tokenType.EndofLine));
					}
					break;
				case -1:
					break;
				
		
		}
			//System.out.print(state);
			pos++;
			if(pos==input.length()) {
				t.add(new Token("NULL",tokenType.EndofLine));
				pos++;
			}
		}
		System.out.print(" ");
		
		
		
		
	}
	
	
	public void stringCheck(ArrayList<Token> t) { //method checks for a string and adds it a string token otherwise nothing happens
		char i='\0';
		int p=pos;
		String ident=""; 
		
		if(p<input.length()) {
			while(input.charAt(p)==' ') {	//ignore whitespaces in front of first double quotes
				p++;
			}
			if(input.charAt(p)=='"' ) {
				p++;
				ident+='"';
				while (p<input.length()) { 
					i=input.charAt(p);
					
					if(i=='"') {	//when it's the next double quotes add in a double quotes and add the new token
						ident+='"';
						p++;
						t.add(new Token (ident, tokenType.STRING)) ;
						pos=p;
						break;
					}
					StringBuilder st=new StringBuilder();
					st.append(ident);
					st.append(i);
					ident=st.toString();
					p+=1;
					
					}
				
				}
			
			
			
			
		}
		
		
		
		
		
		}
	
	public void varList(ArrayList<Token> t){	//adds variables with commas to token list
		char i='\0';
		
		String var="";
		
		
		
		while(pos<input.length()) {
			i=input.charAt(pos);
			if(i==',') {
				t.add(new Token(var,tokenType.IDENTIFIER)); //add var token with its string value
				t.add(new Token(",", tokenType.Comma));
				var="";										//reset var to nothing to receive next string value
				pos++;
				i=input.charAt(pos);
			}
			
			var+=i;
			pos++;
			if(pos==input.length()) {
				t.add(new Token(var,tokenType.IDENTIFIER));
			}
		}
		
		
	}
	
	public boolean stepCheck(ArrayList<Token> t) { //check for a "STEP"
		char i='\0';
		int p=pos;
		String ident=""; 
		
		while (p<input.length()) { 
			i=input.charAt(p);
			StringBuilder st=new StringBuilder();
			st.append(ident);
			st.append(i);
			ident=st.toString();
			
			
			if(ident.compareTo("STEP")==0) {	
				
				p++;
				t.add(new Token (ident, tokenType.STEP)) ;
				pos=p;
				return true;
			}
			p+=1;
			
			}
		
		return false;
	}
	
	public void identAdd(ArrayList<Token> t) { //add an identifier when called from state machine
		char i='\0';
		int p=pos;
		String ident=""; 
		
		
		
		while(p<input.length()) {
			
			if(input.charAt(p)==' ' || input.charAt(p)=='=' || input.charAt(p)=='<' || input.charAt(p)=='>' 
					|| input.charAt(p)=='+' || input.charAt(p)=='-' || input.charAt(p)=='*' || input.charAt(p)=='/') {
				
				t.add(new Token (ident, tokenType.IDENTIFIER)) ;
				pos=p-1;
				break;
			}
			i=input.charAt(p);
			StringBuilder st=new StringBuilder();
			st.append(ident);
			st.append(i);
			ident=st.toString();
			p+=1;
			if(p==input.length()) {
				t.add(new Token (ident, tokenType.IDENTIFIER)) ;
			}
			
		}
	}
	
	public void thenCheck(ArrayList<Token> t) { //checks for Then and a label
		
		char i='\0';
		int p=pos;
		String ident=""; 
		
		while(input.charAt(p)==' ') {	//ignore whitespaces in front of first letter
			p++;
		}
		
		while(p<input.length()) {

			i=input.charAt(p);
			StringBuilder st=new StringBuilder();
			st.append(ident);
			st.append(i);
			ident=st.toString();
			
			if(ident.compareTo("Then")==0) {
				p++;
				t.add(new Token (ident,tokenType.Then));
				break;
			}
			
			p++;
		}
		
		if(ident.compareTo("Then")==0) {
			
			while(input.charAt(p)==' ') {	//ignore whitespaces in front of first letter
				p++;
			}
			ident=""; 
			while(p<input.length()) {
				i=input.charAt(p);
				StringBuilder st=new StringBuilder();
				st.append(ident);
				st.append(i);
				ident=st.toString();
				
				
				p++;
			}
			t.add(new Token(ident,tokenType.LABEL));
			pos=p;
			
			
			
			
			
		}

	}
	/*
	public void goLabel(ArrayList<Token> t) {
		char i='\0';
		int p=pos;
		String ident=""; 
		
		while(input.charAt(p)==' ') {	//ignore whitespaces in front of first letter
			p++;
		}
		
		while(p<input.length()) {
			i=input.charAt(p);
			StringBuilder st=new StringBuilder();
			st.append(ident);
			st.append(i);
			ident=st.toString();
			
			
			p++;
			
		}
		
		t.add(new Token(ident,tokenType.LABEL));
		pos=p;
		
		
		
	}
	*/
		
		
	}

	
	
	
	


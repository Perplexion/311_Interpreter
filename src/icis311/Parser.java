package icis311;

import java.util.ArrayList;

import icis311.Token.tokenType;
import icis311.IntegerNode;
import icis311.MathOpNode;
import icis311.MathOpNode.MathOp;
import icis311.Node;
public class Parser {

	private ArrayList<Token> t;
	
	public Parser(ArrayList<Token> col) {
		
		t=col;
	}
	
	
	public Token matchAndRemove(Token tok) {
			Token out;
			Token n=new Token ("null",tokenType.EndofLine);
			if(t.size()==0)		//when the array list is empty 
				return null;
			/*
			if(t.get(0).getT()==n.getT()) {
				t.remove(0);
				return matchAndRemove(tok);
			} */
			if (t.get(0).getT()==tok.getT()) {
				out=t.get(0);
				t.remove(0);
				return out;
			}
				return null;
				
		}
		
	
	
	
	public StatementsNode parse() {
		
		return Statements();
		//return expression();
		
	}
	
	public Node expression() {
		
		MathOpNode x;
		Node lt=Term();
		if(lt!=null) {
			Token i=matchAndRemove(new Token("+",tokenType.Plus));
			
			if(i!=null) {
				x=new MathOpNode(MathOp.add	);
				Node rt=expression(); //recursive call with the base case being a null i and j token
				x.setL(lt);
				x.setR(rt);
				return x;
			}
			Token j=matchAndRemove(new Token("-",tokenType.Minus));
			if(j!=null) {
				x=new MathOpNode(MathOp.subtract);
				Node rt=expression();
				x.setL(lt);
				x.setR(rt);
				return x;
			}
			
			return lt;
		}
		else
			lt=functionInvocation();
			return lt;
		
		
	}
	
	
	
	public Node Factor() {
		
		Token id=matchAndRemove(new Token("ident",tokenType.IDENTIFIER));
		if(id!=null) {
			VariableNode v=new VariableNode(id.getV());
			return v;
		}
		
		/*		//why did I write this?
		Token par=matchAndRemove(new Token("(",tokenType.LParen));
		if(par!=null) {
			Token nu=matchAndRemove(new Token("NUM",tokenType.Number));
			if(nu!=null) {
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(r!=null) {
					IntegerNode z=new IntegerNode(Integer.parseInt(nu.getV()));
					return z;
				}
					
			}
			
		}
		*/
		Token d=matchAndRemove(new Token("NUM",tokenType.Number));
		Token de=matchAndRemove(new Token(".",tokenType.Decimal));
		if(d!=null && de!=null) {
			Token a=matchAndRemove(new Token("NUM",tokenType.Number));
			String dec=d.getV()+de.getV();
			while(a!=null){
				dec+=a.getV();
				a=matchAndRemove(new Token("NUM",tokenType.Number));
			}
			FloatNode decimal= new FloatNode(Float.parseFloat(dec));
			return decimal;
			
		}

		
		else if(d!=null) {
			String num="";
			while(d!=null) { //allows integer node to be more than a single digit
											
				num+=d.getV();
				d=matchAndRemove(new Token("NUM",tokenType.Number));
			}
			
			IntegerNode z= new IntegerNode(Integer.parseInt(num));
			return z;
		}
		
		
		
		Token j=matchAndRemove(new Token("-",tokenType.Minus));
		if(j!=null ) {
			Token n=matchAndRemove(new Token("NUM",tokenType.Number));
			if(n!=null) {
				String v;
				v=j.getV()+n.getV();
				FloatNode z= new FloatNode(Float.parseFloat(v));
				return z;
			}
			
		}
		
		
		
		Token k=matchAndRemove(new Token("(",tokenType.LParen));
		if(k!=null) {
			//t.remove(0);
			Node e=expression();
			matchAndRemove(new Token(")",tokenType.RParen));
			
			return e;
		}
		else {
			return null;
			//throw new ArithmeticException("Strange token: "+t.get(0).getT());
			
		}
	}
	
	public Node Term() {
		Node l=Factor();
		
		Token o=matchAndRemove(new Token("*",tokenType.Times));
		if(o!=null) {
			
			MathOpNode p=new MathOpNode(MathOp.multiply);
			Node r= Term();		//base case of null o or n token
			p.setL(l);
			p.setR(r);
			
			return p;
		}
		
			
			
			
		Token n=matchAndRemove(new Token("/",tokenType.Div));
		if(n!=null) {
			MathOpNode q=new MathOpNode(MathOp.divide);
			Node r= Term();
			q.setL(l);
			q.setR(r);
			
			return q;
		}
		
		return l;
		
		
		
		
		
		
		
		
	}
	
	public StatementsNode Statements() {
		StatementsNode n=new StatementsNode();
		StatementNode s=Statement();
		while(s!=null) {
			n.addStat(s);
			matchAndRemove(new Token("null",tokenType.EndofLine));
			s=Statement();
			
		}
		//System.out.println("n");
		
		return n;
	}
	
	public StatementNode Statement() {
		Token l=matchAndRemove(new Token("label",tokenType.LABEL));
		if(l!=null) {
			StatementNode s=Statement();
			LabeledStatementNode lab=new LabeledStatementNode(s);	//works properly only with a space after the colon of the "label"
			lab.setLabel(l.getV());
			return lab;
		}
		
		
		
		IfNode ef=IfStatement();
		if(ef!=null) {
			return ef;
		}
		
		NextNode nex=NextStatement();
		if(nex!=null) {
			return nex;
		}
		
		ForNode f=ForStatement();
		if(f!=null) {
			return f;
		}
		
		GosubNode g=GosubStatement();
		if(g!=null) {
			return g;
		}
		
		StatementNode ret=ReturnStatement();
		if(ret!=null) {
			return ret;
		}
		
		PrintNode p=PrintStatement();
		if(p!=null) {
			return p;
		}
			
		AssignmentNode a=Assignment();
		if(a!=null) {
			
			return a;
		}
		
		Node e=expression();
		if(e!=null) {
			StatementNode s=new StatementNode(e);
			return s;
		}
		
		DataNode d=DataStatement();
		if(d!=null) {
			return d;
		}
		
		ReadNode r=ReadStatement();
		if(r!=null) {
			return r;
		}
		
		InputNode i=InputStatement();
		if(i!=null) {
			return i;
		}
		
		
		
		return null;
	}
	public IfNode IfStatement() {
		Token x=matchAndRemove(new Token("IF",tokenType.If));
		if(x!=null) {
			matchAndRemove(new Token("(",tokenType.LParen));
			Node e=expression();
			if(e!=null) {
				Node o=boolop();
				if(o!=null) {
					Node e1=expression();
					matchAndRemove(new Token(")",tokenType.RParen));
					if(e1!=null) {
						Token th=matchAndRemove(new Token("Then",tokenType.Then));
						if(th!=null) {
							Token l=matchAndRemove(new Token("label",tokenType.LABEL));
							
							if(l!=null) {
								
								LabeledStatementNode lab=new LabeledStatementNode(null);	
								lab.setLabel(l.getV());
								
								IfNode i=new IfNode(e);
								i.setExpression(e1);
								i.setOp(o);
								i.setThen(lab);
								return i;
								
								
								
							}
							
							
							
							
							
						}
						
						
					}
				}
			}
		}
			
		return null;
	}
	
	public NextNode NextStatement() {
		Token n=matchAndRemove(new Token("next",tokenType.NEXT));
		if(n!=null) {
			Node f=Variable();
			Token end=matchAndRemove(new Token("endofline",tokenType.EndofLine));	//check for the end of line
			if(f!=null && end!=null) {
				NextNode nex=new NextNode(f);
				return nex;
			}
		}
		return null;
		
	}
	
	public ForNode ForStatement() {
		Token f=matchAndRemove(new Token("for",tokenType.FOR));	//looks for FOR token
		if(f!=null) {
			Node e=Assignment(); //checks the first assignment
			if(e!=null ) {
				Token t=matchAndRemove(new Token("to",tokenType.TO)); //checks for a TO token
					if(t!=null) {
						Node to=Factor();								//looks at the number after TO
						if(to!=null) {
							ForNode fo=new ForNode(e);			//create the for node
							fo.setTo(to);						//set the upper limit
							Token s=matchAndRemove(new Token("step",tokenType.STEP));		//check for a STEP
							if(s!=null) {
								Node inc=Factor();				//look after the STEP
								fo.setStep(inc);				//set the increment
							}
							else {
								fo.setStep(new IntegerNode(1));	//set the increment to 1 if no STEP given
							}
							return fo;		//return for node
						}
						
					}
				
			}
		}
		return null;
	}
	
	public GosubNode GosubStatement() {
		Token go=matchAndRemove(new Token("gosub",tokenType.GOSUB));
		if(go!=null) {
			Node v=Variable(); //look for an identifier
			if(v!=null) {
				if(v instanceof VariableNode)	{
					VariableNode vp=(VariableNode)v;
					String s=vp.getVar();
					GosubNode g=new GosubNode(v);
					g.setVname(s);
					return g;
				}
				
					GosubNode gos=new GosubNode(v);
					return gos;
				
				
				
			}
		}
		return null;
	}
	
	
	
	public StatementNode ReturnStatement() {
		Token r=matchAndRemove(new Token("return",tokenType.RETURN));
		if(r!=null) {							
			
				ReturnNode ret=new ReturnNode();
				StatementNode s=new StatementNode(ret);
				return s;
			
			
		}
		return null;
	}
	
	
	
	public InputNode InputStatement() {
		Token i=matchAndRemove(new Token("input",tokenType.INPUT));
		if(i!=null) {
			InputNode in=InputList();
			return in;
		}
		return null;
	}
	
	public InputNode InputList() {
		Node s=String();
		Node v=Variable();
		
		if(s!=null) {
			InputNode i=new InputNode(s);
			
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {			//only the first "parameter" would be a string
				v=Variable();			//rest of the list are variables
				i.addVInput(v);
				c=matchAndRemove(new Token(",",tokenType.Comma));
			}
			return i;
			
		}
		if(v!=null) {
			InputNode i=new InputNode(v);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				v=Variable();
				i.addVInput(v);
				c=matchAndRemove(new Token(",",tokenType.Comma));
			}
			return i;
		}
		return null;
		
		
		
		
		
	}
	
	public ReadNode ReadStatement() {
		Token r=matchAndRemove(new Token("read",tokenType.READ));
		if(r!=null) {
			ReadNode rn=ReadList();
			return rn;
		}
		return null;
		
	}
	
	public ReadNode ReadList() {
		Node v=Variable();
		if(v!=null) {
			ReadNode r=new ReadNode(v);
			r.addRead(v);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				v=Variable();
				r.addRead(v);
				c=matchAndRemove(new Token(",",tokenType.Comma));
			}
			return r;
		}
		return null;
	}
	
	public DataNode DataStatement() {
		Token d=matchAndRemove(new Token("data",tokenType.DATA));
		if(d!=null) {
			DataNode dn=DataList();
			return dn;
		}
		return null;
	}
	public DataNode DataList() {
		Node s=String();
		Node f=Factor();
		if(s!=null) {
			DataNode d=new DataNode(s);
			d.addData(s);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				f=Factor();
				if(f!=null) {
					d.addData(f);
				}
				s=String();
				if(s!=null) {
					d.addData(s);
				}
				c=matchAndRemove(new Token(",",tokenType.Comma));
				
			}
			return d;
		}
		if(f!=null) {
			DataNode d=new DataNode(f);
			d.addData(f);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				f=Factor();
				if(f!=null) {
					d.addData(f);
				}
				s=String();
				if(s!=null) {
					d.addData(s);
				}
				c=matchAndRemove(new Token(",",tokenType.Comma));
				
			}
			return d;
		}
		return null;
	}
	
	public PrintNode PrintStatement() {
		Token p=matchAndRemove(new Token("print",tokenType.PRINT));
		if(p!=null) {
			PrintNode pr=PrintList();
			return pr;
		}
		return null;
		
	}
	
	public PrintNode PrintList() {
		Node e=expression();
		Node s=String();
		if(e!=null) {
			PrintNode p=new PrintNode(e);
			p.addPrint(e);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				e=expression();
				if(e!=null) {
					p.addPrint(e);
				}
				
				s=String();
				if(s!=null) {
					p.addPrint(s);
				}
				
				c=matchAndRemove(new Token(",",tokenType.Comma));
			}
			return p;
		}
		
		if(s!=null) {
			PrintNode p=new PrintNode(s);
			p.addPrint(s);
			
			Token c=matchAndRemove(new Token(",",tokenType.Comma));
			while(c!=null) {
				e=expression();
				if(e!=null) {
					p.addPrint(e);
				}
				
				s=String();
				if(s!=null) {
					p.addPrint(s);
				}
				
				c=matchAndRemove(new Token(",",tokenType.Comma));
			}
			return p;
		}
		
		
		
		return null;
		
	}
	public AssignmentNode Assignment() {
		Token v=matchAndRemove(new Token("var",tokenType.IDENTIFIER));
		Token e=matchAndRemove(new Token("=",tokenType.Equals));
		if(v!=null && e!=null) {
			
			Node ex=expression();
			VariableNode vn=new VariableNode(v.getV());
			AssignmentNode n=new AssignmentNode(ex);
			n.setVar(vn);
			return n;
		}
		return null;
	}
	
	public Node String() {
		Token s=matchAndRemove(new Token("string",tokenType.STRING));
		if(s!=null) {
			StringNode e=new StringNode(s.getV());
			return e;
		}
		return null;
	}
	public Node Variable() {	//separate variable method to parse for variables
		Token id=matchAndRemove(new Token("ident",tokenType.IDENTIFIER));
		if(id!=null) {
			VariableNode v=new VariableNode(id.getV());
			return v;
		}
		return null;
		
	}
	
	public FunctionNode functionInvocation() {
		Token a=matchAndRemove(new Token("rand",tokenType.RANDOM));
		if(a!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(r!=null) {
					FunctionNode f=new FunctionNode(a.getV());
					return f;
					
				}
			}
				
			
		}
		
		
		Token b=matchAndRemove(new Token("LEFT$",tokenType.LEFT$));
		if(b!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node s=String();
				Token c1=matchAndRemove(new Token(",",tokenType.Comma));
				Node f=Factor();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(r!=null && s!=null && c1!=null && f.getClass()==IntegerNode.class) {
					FunctionNode fun=new FunctionNode(b.getV());
					fun.addPara(s);
					fun.addPara(f);
					return fun;
				}
			}
				
			
		}
		
		Token c=matchAndRemove(new Token("RIGHT$",tokenType.RIGHT$));
		if(c!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node s=String();
				Token c1=matchAndRemove(new Token(",",tokenType.Comma));
				Node f=Factor();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(r!=null && s!=null && c1!=null && f.getClass()==IntegerNode.class) {
					FunctionNode fun=new FunctionNode(c.getV());
					fun.addPara(s);
					fun.addPara(f);
					return fun;
				}
			}
				
			
		}
		
		Token d=matchAndRemove(new Token("MID$",tokenType.MID$));
		if(d!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node s=String();
				Token c1=matchAndRemove(new Token(",",tokenType.Comma));
				Node f=Factor();
				Token c2=matchAndRemove(new Token(",",tokenType.Comma));
				Node f2=Factor();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(r!=null && s!=null && c1!=null && c2!=null && f.getClass()==IntegerNode.class && f2.getClass()==IntegerNode.class) {
					FunctionNode fun=new FunctionNode(d.getV());
					fun.addPara(s);
					fun.addPara(f);
					fun.addPara(f2);
					return fun;
				}
			}
				
			
		}
		Token e=matchAndRemove(new Token("NUM$",tokenType.NUM$));
		if(e!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node f=Factor();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(f!=null && r!=null ) {
					FunctionNode fun=new FunctionNode(e.getV());
					fun.addPara(f);
					return fun;
				}
			}
			
		}
		
		Token f=matchAndRemove(new Token("VAL",tokenType.VAL));
		if(f!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node s=String();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(s!=null && r!=null ) {
					FunctionNode fun=new FunctionNode(f.getV());
					fun.addPara(s);
					return fun;
				}
			}
			
		}
		
		Token g=matchAndRemove(new Token("VAL%",tokenType.FVAL));
		if(g!=null) {
			Token l=matchAndRemove(new Token("(",tokenType.LParen));
			if(l!=null) {
				Node s=String();
				Token r=matchAndRemove(new Token(")",tokenType.RParen));
				if(s!=null && r!=null ) {
					FunctionNode fun=new FunctionNode(g.getV());
					fun.addPara(s);
					return fun;
				}
			}
			
		}
		return null;
		
	}
	
	public Node boolop() {
		Token e=matchAndRemove(new Token("<>",tokenType.NotEquals));
		if(e!=null) {
			Node b= new BooleanOperationNode(e.getV());
			return b;
		}
		Token f=matchAndRemove(new Token("=",tokenType.Equals));
		if(f!=null) {
			Node b= new BooleanOperationNode(f.getV());
			return b;
		}
		Token g=matchAndRemove(new Token(">=",tokenType.MoreEquals));
		if(g!=null) {
			Node b= new BooleanOperationNode(g.getV());
			return b;
		}
		Token h=matchAndRemove(new Token("<=",tokenType.LessEquals));
		if(h!=null) {
			Node b= new BooleanOperationNode(h.getV());
			return b;
		}
		Token i=matchAndRemove(new Token("<",tokenType.LessThan));
		if(i!=null) {
			Node b= new BooleanOperationNode(i.getV());
			return b;
		}
		Token j=matchAndRemove(new Token(">",tokenType.LessThan));
		if(j!=null) {
			Node b= new BooleanOperationNode(j.getV());
			return b;
		}
		return null;
		
	}
	
	
	
	
	
	
}

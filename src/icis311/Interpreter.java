package icis311;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.*;
public class Interpreter {
	
	private HashMap<String,Integer> a=new HashMap<String, Integer>();
	private HashMap<String,Float> b=new HashMap<String, Float>();
	private HashMap<String,String> c=new HashMap<String, String>();
	
	private HashMap<String,Node> n=new HashMap<String, Node>();
	
	private ArrayList<Node> data=new ArrayList<Node>();		//of type object
	private StatementsNode tran;
	private Stack<Node> node_stack=new Stack<Node>();
	
	private ArrayList<StatementNode> tlist=new ArrayList<StatementNode>();
	
	public Interpreter(StatementsNode t) {
		tran=t;
		//tlist.addAll(tran.getList());
	}
	
	public void initialize() {
		labelsearch();
		forwalk();
		datawalk();
		next();
		
		tlist.addAll(tran.getList());
		interpret(tlist.get(0));
		
		
		
		
		
		
		//System.out.println(tran); //shows the changes to the statementsnode
		
	}
	
	public void labelsearch() {	
		ArrayList<Node> list=new ArrayList<Node>();
		list.addAll(tran.getList());	//this list is now the tran list
		
		for(int i=0;i<tran.getList().size();i++) {
			
			if(list.get(i).getClass()==LabeledStatementNode.class) {
				
				LabeledStatementNode ls=(LabeledStatementNode) list.get(i); //already confirmed to be a LabeledStatementNode
				String key=ls.getLabel();		
				StatementNode sn=ls.getSN();		
				
				tran.replaceStat(sn, i);	//replace labeled statement with child
				n.put(key, sn);
				
			
			}
			
			
		}
		
	}
	
	public void forwalk() {
		ArrayList<Node> list=new ArrayList<Node>();
		list.addAll(tran.getList());
		
		for(int i=0;i<tran.getList().size();i++) {
			
			if(list.get(i).getClass()==ForNode.class) {		//look for FOR statements
				for(int j=i;j<tran.getList().size();j++) {	//look for corresponding NEXT statement
					if(list.get(j).getClass()==NextNode.class) {
						
						NextNode n=(NextNode)list.get(j);
						ForNode x=(ForNode)list.get(i);
						
						if(j!=tran.getList().size()-1) {	//if it's not the end after next
							x.setAfterNext(list.get(j+1));	//set references
						}
						n.forset(x);
						
						tran.replaceStat(n, j);
						tran.replaceStat(x, i);
						
					}
				
				}
				
			}
			
		}
	}
	
	public void datawalk() {
		ArrayList<Node> list=new ArrayList<Node>();
		list.addAll(tran.getList());
		
		for(int i=0;i<tran.getList().size();i++) {
			
			if(list.get(i).getClass()==DataNode.class) {
				DataNode dn=(DataNode) list.get(i);		//confirmed to be data node
				data.addAll(dn.getData()); //add the data to the collection
				tran.removeStat(i);  //remove the DataNode
				
				
				
			}
			
			
			
		}
		
		
	}
	
	public void next() { // sets next element for each item
		
		for(int i=0;i<tran.getList().size();i++) {	//by default the final statement's next is null
			
			if(tran.getList().get(i).getClass()==GosubNode.class) {	//set the gosub next by looking at the hashmap reference
				GosubNode g=(GosubNode)tran.getList().get(i);	//confirmed to be gosub node
				String sn=g.getVname();			//get the identifier string value
				for(String x: n.keySet()) {
					if(sn.compareTo(x)==0) {			//search the hashmap for the corresponding string value
						if(n.get(sn) instanceof StatementNode) {		//I promise the hashmap value is a statement node
							StatementNode s=(StatementNode)n.get(sn);	
							s.setNext(s);			//set the next element
						}
															
						
					}
				
				}

			}
			else if(i!=tran.getList().size()-1) {
				StatementNode s=tran.getList().get(i);		//set the next element
				s.setNext(tran.getList().get(i+1));
			}
			
		}
				
		
	}
	
	public void interpret(StatementNode s) {
		StatementNode cn=s;
		int tc=0;
		
		while(cn!=null) {
			
			if(cn.getClass()==ReadNode.class) {
				ReadNode r=(ReadNode)cn;
				ArrayList<Node> rn= new ArrayList<Node>();
				rn.addAll(r.getRead());
				
				for(int i=0;i<rn.size();i++) {
				
					
					if(rn.get(i).getClass()==VariableNode.class) {
						
						VariableNode rnv=(VariableNode)rn.get(i);
						
						if(rnv.getVar().endsWith("%")==true && data.get(i).getClass()==FloatNode.class) { //check the ending of the variables
							FloatNode df=(FloatNode)data.get(i);
							rnv.setVar(df); 	//set var node equal to data
							b.put(rnv.getVar(), df.getFloatNum());
						}
						else if(rnv.getVar().endsWith("$")==true && data.get(i).getClass()==StringNode.class) {
							StringNode si=(StringNode)data.get(i);
							rnv.setVar(si);
							c.put(rnv.getVar(), si.getString());
						}
						
						else if(data.get(i).getClass()==IntegerNode.class ) {
							IntegerNode di=(IntegerNode)data.get(i);
							rnv.setVar(di); 							//set the node in VarNode to equal the one in data node
							a.put(rnv.getVar(), di.getIntNum());
						}
						else {
							throw new ArithmeticException("type mismatch with data and read \n");
						}
					
						rn.set(i,rnv);
						r.setRead(rnv, i);
					}
					
					
					
				}
				
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				else
					break;
				
				
			}
			
			
			else if(cn.getClass()==AssignmentNode.class) {
				AssignmentNode as=(AssignmentNode)cn;
				VariableNode v=as.getVar();		//get variable node from assignment node
				int vi; float vf;
				String sv=v.getVar();			//gets the variable
				if(EvalIntMathOp(as.getexp())!=null) {
					vi=EvalIntMathOp(as.getexp());
					a.put(sv,vi);
				}
				else if(EvalFloatMathOp(as.getexp())!=null) {
					vf=EvalFloatMathOp(as.getexp());
					b.put(sv, vf);
				}
				
				
				
				
				
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				else
					break;
				
			}
			
			
			else if(cn.getClass()==InputNode.class) {
				InputNode in=(InputNode)cn;
				ArrayList<Node> i=in.getInputList();
				System.out.println(i.get(0));
				
				Scanner uv= new Scanner(System.in);
				int var1; String var2; float var3;
				
				for(int x=1;x<i.size();x++) {
					if(i.get(x).getClass()==VariableNode.class) {		//find the variable type then ask for user input
																	//create an AssignmentNode and replace within the inputnode
						VariableNode vn=(VariableNode)i.get(x);
						String vname=vn.getVar();
						
						if(vname.endsWith("$")==true) {
							System.out.println("enter string value");	//prompt the user to input their values for
																		//each variable
							var2=uv.nextLine();
							StringNode es=new StringNode(var2);
							AssignmentNode v2=new AssignmentNode(es);	//create and set assignmentnode
							v2.setVar(vn);
							in.setInput(v2, x);				//replace the variable node with assignment node
						}
						else if(vname.endsWith("%")==true) {
							System.out.println("enter float value");
							var3=uv.nextFloat();
							uv.nextLine();
							FloatNode fn=new FloatNode(var3);
							AssignmentNode v2=new AssignmentNode(fn);
							v2.setVar(vn);
							in.setInput(v2, x);
							
						}
						else {
							System.out.println("enter int value");
							var1=uv.nextInt();
							uv.nextLine();
							IntegerNode fn=new IntegerNode(var1);
							AssignmentNode v2=new AssignmentNode(fn);
							v2.setVar(vn);
							in.setInput(v2, x);
							
						}
						
						
					}

					
				}
				tlist.set(tc, in); //replace the input node with the new input node
				
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				
				
			}
			else if(cn.getClass()==PrintNode.class) {
				PrintNode p=(PrintNode)cn;
				System.out.println(p); //use printnode tostring
				

				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				else
					break;
				
				
			}
			else if(cn.getStatement().getClass()==FunctionNode.class) {	//runs through functions
				FunctionNode f=(FunctionNode)cn.getStatement();
				ArrayList<Node> fns=f.getFNs();
				
				if(f.getF().compareTo("RANDOM")==0) {
					Random rand = new Random();
					int int_rand=rand.nextInt();
					System.out.println(int_rand);
				}
				if(f.getF().compareTo("LEFT$")==0) {
					String lefts; int lefti;
					if(fns.get(0).getClass()==StringNode.class) {
						StringNode fnstring=(StringNode)fns.get(0);
						lefts=fnstring.getString();
						
						if(fns.get(1).getClass()==IntegerNode.class) {
							IntegerNode fni=(IntegerNode)fns.get(1);
							lefti=fni.getIntNum();
							lefts=lefts.substring(0, lefti+1);
							System.out.println("LEFT$= "+lefts);
							
						}
					}
				}
				if(f.getF().compareTo("RIGHT$")==0) {
					String rights; int righti;
					if(fns.get(0).getClass()==StringNode.class) {
						StringNode fnstring=(StringNode)fns.get(0);
						rights=fnstring.getString();
						
						if(fns.get(1).getClass()==IntegerNode.class) {
							IntegerNode fni=(IntegerNode)fns.get(1);
							righti=fni.getIntNum();
							rights=rights.substring(rights.length()-righti, rights.length());
							System.out.println("RIGHT$= "+rights);
							
						}
					}
				}
				if(f.getF().compareTo("MID$")==0) {
					String mids; int m2; int m3;
					if(fns.get(0).getClass()==StringNode.class) {
						StringNode fnstring=(StringNode)fns.get(0);
						mids=fnstring.getString();
						
						if(fns.get(1).getClass()==IntegerNode.class) {
							IntegerNode fni=(IntegerNode)fns.get(1);
							m2=fni.getIntNum();
							
							if(fns.get(2).getClass()==IntegerNode.class) {
								fni=(IntegerNode)fns.get(2);
								m3=fni.getIntNum();
								
								mids=mids.substring(m2, m2+m3);
								System.out.println("MID$= "+mids);
								
							}
							
						}
					}
					
				}
				if(f.getF().compareTo("NUM$")==0) {
					if(fns.get(0).getClass()==IntegerNode.class) {
						IntegerNode num_nod=(IntegerNode)fns.get(0);
						int num=num_nod.getIntNum();
						
						String num_str=String.valueOf(num);
						System.out.println(num_str);
					}
					if(fns.get(0).getClass()==FloatNode.class) {
						FloatNode num_nod=(FloatNode)fns.get(0);
						float num=num_nod.getFloatNum();
						
						String num_str=String.valueOf(num);
						System.out.println(num_str);
					}
				}
				if(f.getF().compareTo("VAL")==0) {
					if(fns.get(0).getClass()==StringNode.class) {
						StringNode str=(StringNode)fns.get(0);
						String st=str.getString();
						st=st.substring(1, st.length()-1);  //account for double quotes
						int str_i=Integer.parseInt(st);
						System.out.println(str_i);
					}
					
					
					
				}
				if(f.getF().compareTo("VAL%")==0) {
					if(fns.get(0).getClass()==StringNode.class) {
						StringNode str=(StringNode)fns.get(0);
						String st=str.getString();
						st=st.substring(1, st.length()-1);  //account for double quotes
						float str_f=Float.parseFloat(st);
						System.out.println(str_f);
					}
					
				}
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				else
					break;
				
				
			}
			
			else if(cn.getStatement().getClass()==MathOpNode.class) {
				MathOpNode mo=(MathOpNode)cn.getStatement();
				System.out.println(EvalIntMathOp(mo));
				
				System.out.println(EvalFloatMathOp(mo));
				
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				
				
			}
			
			else if(cn.getStatement().getClass()==IfNode.class) {
				IfNode ino=(IfNode)cn.getStatement();
				Integer l=null, r=null; Float lf=null,rf=null;
				if(EvalIntMathOp(ino.getLeft())!=null) {
					l=EvalIntMathOp(ino.getLeft());
				}
				else if(EvalFloatMathOp(ino.getLeft())!=null) {
					lf=EvalFloatMathOp(ino.getLeft());
				}
				if(EvalIntMathOp(ino.getRight())!=null) {
					r=EvalIntMathOp(ino.getRight());
				}
				else if(EvalFloatMathOp(ino.getRight())!=null) {
					rf=EvalFloatMathOp(ino.getRight());
				}
				String op="";
				if(ino.getOp().getClass()==BooleanOperationNode.class) {
					BooleanOperationNode bo=(BooleanOperationNode)ino.getOp();
					op=bo.getBooOp();
				}
				if(op.equals("=")==true) {
					if(l!=null && r!=null) {
						if(l==r) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
					else if(lf!=null && rf!=null) {
						if(lf==rf) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
				}
				if(op.equals("<")==true) {
					if(l!=null && r!=null) {
						if(l<r) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
					else if(lf!=null && rf!=null) {
						if(lf<rf) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
				}
				if(op.equals(">")==true) {
					if(l!=null && r!=null) {
						if(l>r) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
					else if(lf!=null && rf!=null) {
						if(lf>rf) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
				}
				if(op.equals(">=")==true) {
					if(l!=null && r!=null) {
						if(l>=r) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
					else if(lf!=null && rf!=null) {
						if(lf>=rf) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
				}
				if(op.equals("<=")==true) {
					if(l!=null && r!=null) {
						if(l<=r) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
					else if(lf!=null && rf!=null) {
						if(lf<=rf) {
									
							if(ino.getNext().getClass()==LabeledStatementNode.class) {
								LabeledStatementNode tl=(LabeledStatementNode)ino.getNext(); //search the hash map with string key for the node
																							  //set the currentnode to that node
								Node cn_next=n.get(tl.getLabel());
								if(cn_next.getClass()==StatementNode.class) {
									cn=(StatementNode)cn_next;
								}
							}
						}
					}
				}

			}
			
			else if(cn.getStatement().getClass()==GosubNode.class) {
				node_stack.push(cn.getNext());
				GosubNode go_n=(GosubNode)cn.getStatement();
				
				if(n.get(go_n.getVname()).getClass()==StatementNode.class) {
					cn=(StatementNode)n.get(go_n.getVname());
				}
				
				
			}
			
			else if(cn.getStatement().getClass()==ReturnNode.class) {
				if(node_stack.peek().getClass()==StatementNode.class) {
					cn=(StatementNode)node_stack.pop();
				}
			}
			else if(cn.getStatement().getClass()==ForNode.class) {
				ForNode temp_f=(ForNode)cn.getStatement();
				
				
				if(temp_f.getForEx().getClass()==AssignmentNode.class) { 
					
					AssignmentNode temp_an=(AssignmentNode)temp_f.getForEx();
					VariableNode temp_v=temp_an.getVar();				
					
					String svar=temp_v.getVar();
					
				
						if(a.containsKey(svar)==true) {		//found the variable
							int hash_i=a.get(svar);
							
							if(temp_f.getTo().getClass()==IntegerNode.class) {
								IntegerNode i_to=(IntegerNode)temp_f.getTo();
								int limit=i_to.getIntNum();
								
								if(hash_i>limit) {
									if(temp_f.getAftNext().getClass()==StatementNode.class) {		//if limit is exceeded
																							//current node is now after NEXT
										cn=(StatementNode)temp_f.getAftNext();
										a.remove(svar);										//variable removed from the hash
									}
									
								}
								
								
								else if(temp_f.getInc().getClass()==IntegerNode.class) {
									IntegerNode increm= (IntegerNode)temp_f.getInc();
									int inc=increm.getIntNum();		//increment set to 1 if none given, in the parser
									hash_i+=inc;					//increment and replace
									a.replace(svar, hash_i);
								}
								
								
							}
							
							
							
							
							
							
						}
						else if(temp_an.getexp().getClass()==IntegerNode.class) {			//create if not in the hash
							
							IntegerNode i_temp=(IntegerNode)temp_an.getexp();  //get the IntegerNode that the first variable is set to
							
							a.put(temp_v.getVar(), i_temp.getIntNum()); 		//create the int variable to the map first
							
						}
					
					
					
					
					
				}
			}
			
			else if(cn.getStatement().getClass()==NextNode.class) {
				NextNode neno=(NextNode)cn.getStatement();
				if(neno.getFor().getClass()==ForNode.class) {
					ForNode nen_f=(ForNode)neno.getFor();
					cn=nen_f;								//set current node to the For node
				}
				
			}
			
			
			
			else {
				if(cn.getNext()!=null) {
					cn=cn.getNext();
				}
				else
					break;
			}
			
			
		}
		
		
	
		
		
		
	
	}
	
	public Integer EvalIntMathOp(Node n) {
		if(n.getClass()==IntegerNode.class) {
			IntegerNode ni=(IntegerNode)n;
			return ni.getIntNum();
		}
		if(n.getClass()==VariableNode.class) {			//if it's an Int VariableNode
			VariableNode vn=(VariableNode)n;
			
			if(vn.getVal().getClass()==IntegerNode.class) {
				IntegerNode vi=(IntegerNode)vn.getVal()	;
				return vi.getIntNum();						//return the int number
			}
			
		}
		if(n.getClass()==MathOpNode.class) {
			MathOpNode mn=(MathOpNode)n;
			String m=mn.getOp();
			Node L,R;
			L=mn.getL();
			R=mn.getR();
			
			int L_int, R_int;
			L_int=EvalIntMathOp(L);
			R_int=EvalIntMathOp(R);
			
			if(m.compareTo("+")==0) {
				return L_int + R_int;
			}
			if(m.compareTo("-")==0) {
				return L_int - R_int;
			}
			if(m.compareTo("*")==0) {
				return L_int * R_int;
			}
			if(m.compareTo("/")==0) {
				return L_int / R_int;
			}
			
		}
		return null;
		
	}
	public Float EvalFloatMathOp(Node n) {
		if(n.getClass()==FloatNode.class) {
			FloatNode ni=(FloatNode)n;
			return ni.getFloatNum();
		}
		if(n.getClass()==VariableNode.class) {			//if it's an float VariableNode
			VariableNode vn=(VariableNode)n;
			
			if(vn.getVal().getClass()==FloatNode.class) {
				FloatNode vi=(FloatNode)vn.getVal()	;
				return vi.getFloatNum();						//return the float number
			}
			
		}
		if(n.getClass()==MathOpNode.class) {
			MathOpNode mn=(MathOpNode)n;
			String m=mn.getOp();
			Node L,R;
			L=mn.getL();
			R=mn.getR();
			
			float L_Float, R_Float;
			L_Float=EvalFloatMathOp(L);
			R_Float=EvalFloatMathOp(R);
			
			if(m.compareTo("+")==0) {
				return L_Float + R_Float;
			}
			if(m.compareTo("-")==0) {
				return L_Float - R_Float;
			}
			if(m.compareTo("*")==0) {
				return L_Float * R_Float;
			}
			if(m.compareTo("/")==0) {
				return L_Float / R_Float;
			}
			
		}
		return null;
		
	}
	
	
	
	
}
	
	


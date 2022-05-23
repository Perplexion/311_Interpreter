package icis311;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

//By Andy Vuu
public class Basic {
	
	public static void main(String args[]) {
		
		if(args.length>1)
			throw new IndexOutOfBoundsException("Only one arg. allowed");
		ArrayList<ArrayList<Token>> output=new ArrayList<ArrayList<Token>>();
		ArrayList<Token> out=new ArrayList<Token>();
		ArrayList<Node> tran=new ArrayList<Node>();
		List<String> file=new ArrayList<String>();
		
		Path p=Paths.get(args[0]);
		try {
			file=Files.readAllLines(p);
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<file.size();i++) {
			Lexer x=new Lexer(file.get(i));
			//output.add(i, x.Lex(file.get(i)));
			out.addAll(x.Lex(file.get(i)));
			
			
			}
		
		
		System.out.println();
		/*
		for(int n=0;n<output.size();n++) {
			System.out.println(output.get(n));
		} 
		*/
		
		for(int n=0;n<out.size();n++) {
			//System.out.println(out.get(n));
		} 
		System.out.println();
		
		Parser par=new Parser(out);
		StatementsNode t=par.parse();
		System.out.println(t);
		
		Interpreter in=new Interpreter(t);
		in.initialize();
		
		/*
		for(int i=0;i<output.size();i++) {
			Parser par=new Parser(output.get(i));
			Node test=par.parse();
			tran.add(i,test);
			System.out.println(test);
		}
			*/
		
}
	
	
}	

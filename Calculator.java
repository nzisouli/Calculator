package ce325.hw1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.StringBuilder;
import java.io.*;
import java.util.Scanner; 
import java.util.Arrays;
import java.lang.Character;
import java.lang.String;

public class Calculator{
	
	public static TreeNode makeTree(String expression, int[] parenthesis, int[] operands){
		int i, counter, index;

		//If expression starts with left parenthesis and it closes in the end of the expression remove outer parenthenses 
		if (parenthesis[0] == 1){
			counter = 1;
			for (i = 1; i < expression.length(); i++){
				counter += parenthesis[i];
				if (counter == 0){
					break;
				}
			}
			if (i == expression.length()-1){
				expression = expression.substring(1,i);
				parenthesis = Arrays.copyOfRange(parenthesis, 1, i);
				operands = Arrays.copyOfRange(operands, 1, i);
			}
		}

		//Check for operands out of parentheses starting for the one with lowest priority
		for (index = 1; index < 6; index++){
			counter = 0;
			for (i = expression.length()-1; i >= 0 ; i--){
				counter += parenthesis[i];
				//If found operand out of parentheses split left and right part of expression and recurse
				if ((operands[i] == index) && (counter == 0)){
					TreeNode left = makeTree(expression.substring(0,i), Arrays.copyOfRange(parenthesis, 0, i), Arrays.copyOfRange(operands, 0, i));
					TreeNode right = makeTree(expression.substring(i+1), Arrays.copyOfRange(parenthesis, i+1, expression.length()), Arrays.copyOfRange(operands, i+1, expression.length()));

					TreeNode op = new TreeNode(left, right, expression.charAt(i));
					return op;
				}
			}
		}
		//If no operands found in expression there is only a value
		TreeNode number = new TreeNode(null, null, Double.parseDouble(expression));
		return number;
	}


	public static void main(String[] args){

		//Scan the expression (accepting new line characters)
		String str = new String();
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter math expression (Press double enter to continue): ");
		String input = new String();
		while (scan.hasNextLine() ){
			str = scan.nextLine().toString();
			if( !(str.equals(new String()))){
				input= input + str;
			}
			else{
				break;
			}
		}
		System.out.println("Entered expression:" + input);
		
		//Deleting whitespace
		int i = 0;
		StringBuilder sb = new StringBuilder(input);
		while(i < sb.length()){
			if (sb.charAt(i) == ' ' || sb.charAt(i) == '\n' || sb.charAt(i) == '\t' || sb.charAt(i) == '\r'){
				sb.deleteCharAt(i);
			}
			else{
				i ++;
			}
		}
		String expression = new String(sb.toString());
		
		int[] parenthesis; //track parentheses in current expression (1 = left parenthesis, -1 = right parenthsis, 0 = other)
		int[] operands; //track operands in current expression in an ascending priority manner
		parenthesis = new int[expression.length()];
		operands = new int[expression.length()];

		i = 0;
		while(i < expression.length()){
			//Find parenthesis
			if(expression.charAt(i) == '('){
				parenthesis[i] = 1;
			}
			else if(expression.charAt(i) == ')'){
				parenthesis[i] = -1;
			}
			else{
				parenthesis[i] = 0;
			}


			//Find operands
			if(expression.charAt(i) == '+'){
				operands[i] = 1;
			}
			else if(expression.charAt(i) == '-'){
				operands[i] = 2;
			}
			else if(expression.charAt(i) == '*' || expression.charAt(i) == 'x'){
				operands[i] = 3;
			}
			else if(expression.charAt(i) == '/'){
				operands[i] = 4;
			}
			else if(expression.charAt(i) == '^'){
				operands[i] = 5;
			}
			else{
				operands[i] = 0;
			}
			i++;
		}

		//Checking for errors in expression
		int counter = 0;
		String curr = new String();
		for (i = 0; i < expression.length(); i++){
			counter += parenthesis[i];
			//if counter reaches a negative value it means that there is an extra right parenthesis resulting in error
			if (counter < 0 ){
				System.out.println("Syntax error");
				return;
			}

			//Check for invalid characters
			curr += expression.charAt(i);
			if(Character.isDigit(expression.charAt(i))== false  && (expression.charAt(i) != '+') && (expression.charAt(i) != '-') && (expression.charAt(i) != '*')
				&& (expression.charAt(i) != '/') && (expression.charAt(i) != 'x') && (expression.charAt(i) != '^') && (expression.charAt(i) != '(')
				&& (expression.charAt(i) != ')') && (expression.charAt(i) != '.')){
				System.out.println("Invalid input.Exiting..");
				System.out.println(i);
				return;
			}
			curr = curr.substring(0, curr.length() - 1);
		}
		//If counter != 0 it means that there is an open parenthesis
		if(counter != 0){
			System.out.println("Syntax error");
			return;
		}

		//Create tree
		TreeNode root = makeTree(expression, parenthesis, operands);
		Tree t = new Tree(root);

		//Print expression from tree
		String output = t.toString(t.getRoot());
		System.out.println("Your expression: " + output);

		//Calculate and print result
		double result = t.calculate(t.getRoot());
		System.out.println("Result = " + result);

		//Create .dot and .png files
		try {        
		  PrintWriter pfile = new PrintWriter("ArithmeticExpression.dot");
		  pfile.println(t.toDotString(t.getRoot()));
		  pfile.close();
		  System.out.println("PRINT DOT FILE OK!");
		         
		  Process p = Runtime.getRuntime().exec("dot -Tpng ArithmeticExpression.dot " +
		                                        "-o ArithmeticExpression.png");
		  p.waitFor();
		  System.out.println("PRINT PNG FILE OK!");
		}
		catch(Exception ex) {
		  System.err.println("Unable to write dotString!!!");
		  ex.printStackTrace();
		  System.exit(1);
		}
	}
}
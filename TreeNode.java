package ce325.hw1;

import java.lang.String;

public class TreeNode{
	private TreeNode left, right;
	private double value;
	private char operand; 
	private String type;

	//Constructor if the node contains an operand
	public TreeNode(TreeNode left, TreeNode right, char operand){
		this.left = left;
		this.right = right;
		this.operand = operand;
		this.type = "op";

	}

	//Constructor if the node contains a value
	public TreeNode(TreeNode left, TreeNode right, double value){
		this.left = left;
		this.right = right;
		this.value = value;
		this.type = "num";
	}

	public TreeNode getLeft(){
		return left;
	}

	public TreeNode getRight(){
		return right;
	}

	public String getType(){
		return type;
	}

	public double getValue(){
		return value;
	}

	public char getOperand(){
		return operand;
	}

	//Return either operand or value according to type
	public String toString(){
		String out="";
		if (type == "op"){
			return out + operand;
		}
		else{
			return out + value ;
		}
	}
}
package ce325.hw1;

import java.lang.Math;

public class Tree {
	private TreeNode root;

	public Tree(TreeNode root){
		this.root = root;
	}

	public TreeNode getRoot(){
		return root;
	}


	//Returns the equivalent expression traversing the tree in in-order
	public String toString(TreeNode node){
		String out = "";
		out += "(";
		TreeNode left = node.getLeft();
		if (left != null){
			out += toString(left);
		}
		out += node.toString();
		TreeNode right = node.getRight();
		if(right!= null){
			out += toString(right);
		}
		out += ")";

		return out;
	}

	//Returns the string to create .dot file traversing the tree in pre-order
	public String toDotString(TreeNode node){
		String out = "";
		if (node == root){
			out += "digraph ArithmeticExpressionTree {\nlabel=\"Arithmetic Expression\n\"";
		}
		out += node.hashCode();
		out += " [label=\"";
		out += node.toString();
		out += "\", shape=circle, color=black]\n";

		TreeNode left = node.getLeft();
		if (left != null){
			out += node.hashCode();
			out += " -> ";
			out += left.hashCode();
			out += "\n";
			out += toDotString(left);
		}

		TreeNode right = node.getRight();
		if(right!= null){
			out += node.hashCode();
			out += " -> ";
			out += right.hashCode();
			out += "\n";
			out += toDotString(right);
		}

		if (node == root){
			out += "\n}";
		}
		return out;
	}

	//Calculates the result traversing the tree in pre-order
	public double calculate(TreeNode node){
		char op;
		double l, r, result = 0;
		if (node.getType() == "op"){
			TreeNode left = node.getLeft();
			TreeNode right = node.getRight();
			op = node.getOperand();
			l = calculate(left);
			if (op == '+'){
				result = l+calculate(right);
			}
			else if(op == '-'){
				result = l-calculate(right);
			}
			else if(op == '*' || op == 'x'){
				result = l*calculate(right);
			}
			else if(op == '^'){
				result = Math.pow(l,calculate(right));
			}
			else if(op == '/'){
				result = l/calculate(right);
			}
		}
		else{
			result = node.getValue();
		}
		return result;
	}
	
}
package assignment1;

import java.util.LinkedList;

//ProgramNode class, represents the top node of the syntax tree
class ProgramNode {
 private LinkedList<BlockNode> beginBlockNodes;
 private LinkedList<BlockNode> endBlockNodes;
 private LinkedList<BlockNode> otherBlockNodes;
 private LinkedList<FunctionDefinitionNode> functionNodes;

 public ProgramNode() {
     this.beginBlockNodes = new LinkedList<>();
     this.endBlockNodes = new LinkedList<>();
     this.otherBlockNodes = new LinkedList<>();
     this.functionNodes = new LinkedList<>();
 }
 
 public void addOtherNode(BlockNode node) {
	    this.otherBlockNodes.add(node);
	}

 public LinkedList<FunctionDefinitionNode> getFunctionNodes() {
     return functionNodes;
 }

 public LinkedList<BlockNode> getOtherBlockNodes() {
     return otherBlockNodes;
 }
}


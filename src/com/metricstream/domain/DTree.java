package com.metricstream.domain;

/**
 * 
 * @author debdipta.h
 *
 *
 *Definition of the DTree.
 */

public class DTree {
	private DTreeNode root;
	
	 public DTree(){
	}
	
	DTree(DTreeNode node){
		this.root=node;
	}
	
	
  public void setRoot(DTreeNode node){	
	this.root=node;  
  }
  
  public DTreeNode getRoot(){
	  return root;
  }  
   
  public DTreeNode createNode(String name,String value,String type){
	 DTreeNode newnode=new DTreeNode(name,value,type);
//	 childnodelist.add(newnode);
	 return newnode;
  }
  
  public DTreeNode createNode(String name,String value,String type,DTreeNode parent){
	  DTreeNode newnode=new DTreeNode(name,value,type,parent);
//	  childnodelist.add(newnode);
	  return newnode;
  }
  
}

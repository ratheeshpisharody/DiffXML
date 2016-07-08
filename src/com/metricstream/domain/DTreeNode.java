package com.metricstream.domain;
/**
 *@author debdipta.h
 * 
 * Definition of node for DTree.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DTreeNode {
	private String nodename;
	private DTreeNode parentnode;
	private String value;
	private String type;
    private ArrayList<DTreeNode> children;
    private String signature;
    private int attributesize;
    private HashSet<String> hashvalue;
    private  ArrayList<DTreeNode> leaves;
    
    DTreeNode(){
    	children=new ArrayList<DTreeNode>();
        hashvalue=new HashSet<String>();
        attributesize=0;
    }
    
    DTreeNode(String nodename,String value,String type){
    	this.nodename=nodename;
    	this.value=value;
    	this.type=type;
    	signature=nodename+"/"+type;
    	children=new ArrayList<DTreeNode>();
    	hashvalue=new HashSet<String>();
    	attributesize=0;
    	leaves=new ArrayList<DTreeNode>();
    }
    
    DTreeNode(String nodename,String value,String type,DTreeNode parent){
    	this.nodename=nodename;
    	this.value=value;
        this.parentnode=parent;
        this.type=type;
        this.signature=parent.getSignature()+"/"+nodename+"/"+type;
    	children=new ArrayList<DTreeNode>();
    	hashvalue=new HashSet<String>();
    	attributesize=0;
    	leaves=new ArrayList<DTreeNode>();
    }
    
    
    public ArrayList<DTreeNode> getChildren(){
    	return children;
    }
    
    
    public DTreeNode getParent(){
    	 return parentnode;
     }
	
    public void addChildren(DTreeNode node){
    	this.children.add(node);
    }
    
    
    public String getNodeName(){
    	return nodename;
    }
    
    
    public String getNodeValue(){
    	return value;
    }
    
    public String getType(){
    	return type;
    }
    
    public String getSignature(){
    	return signature;
    }
    
    public void setHash(Set<String> hashval){
    	this.hashvalue.addAll(hashval);
    
    }
    
    public HashSet<String> getHash(){
    	return hashvalue;
    }
    
    public void updateAttributeSize(int attributesize){
    	this.attributesize=attributesize;
    }
    
    public int getAttributeSize(){
    	return attributesize;
    }
    
    public void addleaf(DTreeNode leaf){
    	this.leaves.add(leaf);
    }
    public void addleaves(ArrayList<DTreeNode>leaves){
    	this.leaves.addAll(leaves);
    }
    
    public ArrayList<DTreeNode> getLeaves(){
    	return leaves;
    }
    
    public void update(String value){
    	this.value=value;	
    }
    
    public void delete(){
    	DTreeNode parent=this.parentnode;
    	parent.children.remove(this);
    	this.parentnode=null;
       for(DTreeNode child:children){
    	   child.parentnode=null;
       }
       this.leaves.clear();
       this.children.clear();
    }
    
    public void insert(DTreeNode parent){
    	parent.children.add(this);
    	this.parentnode=parent;
    	parent.addleaves(this.getLeaves());
    }
    
    public void removeChildren(ArrayList<DTreeNode> list){
    	this.children.removeAll(list);
    }
}

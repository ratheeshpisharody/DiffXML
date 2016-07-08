package com.metricstream.Util;
/**
 * @author debdipta.h
 * 
 */

import java.util.Stack; 

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.metricstream.constants.nodeType;
import com.metricstream.domain.DTree;
import com.metricstream.domain.DTreeNode;

public class parseXMLHandler extends DefaultHandler {
	private Stack<DTreeNode> elements=new Stack<DTreeNode>();
	private Stack<String> texts=new Stack<String>();
	private DTree xmltree;
	private hashGenerator hashgen;
	
	public DTree getTree(){
		return xmltree;
	}
	
	public void startDocument(){
		xmltree=new DTree();
		hashgen=new hashGenerator();
		System.out.println("Starting parsing the document");
	}
	
	public void endDocument(){
		System.out.println("Ended parsing the document");
	}
	
	
	public void startElement(String uri,String localName,String qName, Attributes attributes){
	 DTreeNode newnode;
		if(elements.isEmpty()){
			newnode=xmltree.createNode(qName, "", nodeType.Element.toString());
	    }
		else
			newnode=xmltree.createNode(qName, "", nodeType.Element.toString(),elements.peek());
		elements.push(newnode);
		newnode.updateAttributeSize(attributes.getLength());
		for(int i=0;i<attributes.getLength();i++){
			System.out.println(attributes.getQName(i));
			DTreeNode attrnode=xmltree.createNode(attributes.getQName(i), attributes.getValue(i),nodeType.Attribute.toString(),newnode);
			hashgen.generateHash(attrnode);
			newnode.addChildren(attrnode);
			newnode.addleaf(attrnode);
		}
	
	}
	
	
	
	public void endElement(String uri, String localName, String qName){
         
//		System.out.println("end tag");
		if(this.elements.peek().getNodeName()==qName){
		DTreeNode parnode=this.elements.pop();
//		 System.out.print("\t"+name+"\t");
		
		 while(!texts.isEmpty()){
		    	DTreeNode textnode=xmltree.createNode("",texts.pop(),nodeType.Text.toString(),parnode);
		    	hashgen.generateHash(textnode);
		    	parnode.addChildren(textnode);
		    	parnode.addleaf(textnode);
		    }
		 
		 if(elements.isEmpty()){
			xmltree.setRoot(parnode);
		 }
		 else{			 
			 this.elements.peek().addChildren(parnode);
			 this.elements.peek().addleaves(parnode.getLeaves());
			 }
	   
	    hashgen.generateHash(parnode);
//	    this.elements.peek().addleaves(parnode.getLeaves());
    }
   }
	
	public void characters(char ch[], int start, int length){
		String data=new String(ch,start,length);
		String datarep=data;
//		String x=datarep.replaceAll("\\r\\n|\\n|\\r|\\t","").trim();
         if(!datarep.replaceAll("\\r\\n|\\n|\\r|\\t","").trim().equals(""))
		  this.texts.push(data);
	}
}
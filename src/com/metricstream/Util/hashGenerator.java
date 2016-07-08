package com.metricstream.Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import com.metricstream.domain.DTreeNode;

public class hashGenerator {
	
	public void generateHash(DTreeNode node){
		if(node.getType()=="Element")
			node.setHash(elementHash(node));
		else if(node.getType()=="Attribute")
			node.setHash(attrHash(node));
		else if(node.getType()=="Text")
			node.setHash(textHash(node));
		else{
			System.err.println("Cannot determine the nodetype");
			System.exit(1);
	}
}		
	
   private Set<String> elementHash(DTreeNode node){
	   byte[] digest=new byte[0];
	   Set<String> finaldigest=new HashSet<String>();
	   try{
		   MessageDigest md=MessageDigest.getInstance("MD5");
		   md.update((byte) 1);
		   md.update(node.getNodeName().getBytes("UnicodeBigUnmarked"));
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update((byte)node.getAttributeSize());
		   md.update((byte)node.getChildren().size());
		   digest=md.digest();
		   finaldigest.add(toString(digest));
		   for(DTreeNode childnode:node.getChildren()){
			   finaldigest.addAll(childnode.getHash());
		   }
		 }
	   catch(NoSuchAlgorithmException e){
		   System.err.println("The algorithm for encryption of attribute is not supported");
		   e.printStackTrace();
	   }
	   catch(UnsupportedEncodingException e){
		   System.err.println("The encoding provided for attribute is not supported");
		   e.printStackTrace();
	   } 
	  return finaldigest;
   }   
   
   private Set<String> attrHash(DTreeNode node){
	   byte[] digest=new byte[0];
	   Set<String> finaldigest=new HashSet<String>();
	   try{
		   MessageDigest md=MessageDigest.getInstance("MD5");
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update((byte) 2);
		   md.update(node.getNodeName().getBytes("UnicodeBigUnmarked"));
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update(node.getNodeValue().getBytes("UnicodeBigUnmarked"));
		   digest=md.digest();
		   finaldigest.add(toString(digest));
		   
	   }
	   catch(NoSuchAlgorithmException e){
		   System.err.println("The algorithm for encryption of attribute is not supported");
		   e.printStackTrace();
	   }
	   catch(UnsupportedEncodingException e){
		   System.err.println("The encoding provided for attribute is not supported");
		   e.printStackTrace();
	   }
	   return finaldigest;
   }
   
   private Set<String> textHash(DTreeNode node){
	   byte[] digest=new byte[0];
	   Set<String> finaldigest=new HashSet<String>();
	   try{
		   MessageDigest md=MessageDigest.getInstance("MD5");
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update((byte) 0);
		   md.update((byte) 3);
		   md.update(node.getNodeValue().getBytes("UnicodeBigUnmarked"));
		   digest=md.digest();
		   finaldigest.add(toString(digest));
	   }
	   
	   catch(UnsupportedEncodingException e){
		   System.err.println("The encoding provided is not a valid one");
		   e.printStackTrace();
	   }
	   
	   catch(NoSuchAlgorithmException e){
		   System.err.println("The encryption algorithm is not supported.");
	   }
	   
	   return finaldigest;
   }
   
   public String toString(byte[] array) {
       String str = "";
       for (int i = 0; i < array.length; i++) str += array[i];
       return str;
   }
}

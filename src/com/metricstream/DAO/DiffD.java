package com.metricstream.DAO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.metricstream.Exceptions.distanceException;
import com.metricstream.Util.DiffUtil;
import com.metricstream.Util.xmlUtil;
import com.metricstream.constants.nodeType;
import com.metricstream.domain.DTree;
import com.metricstream.domain.DTreeNode;

public class DiffD {
	private void removeEqualSubTree(DTree basetree,DTree xprtdtree){
		HashMap<HashSet<String>,DTreeNode> basemap=new HashMap<HashSet<String>,DTreeNode>();
		HashMap<HashSet<String>,DTreeNode> xprtdmap=new HashMap<HashSet<String>,DTreeNode>();
		DTreeNode xprtdroot=xprtdtree.getRoot();
		DTreeNode baseroot=basetree.getRoot();
		for(DTreeNode node:baseroot.getChildren())
			basemap.put(node.getHash(),node);
		for(DTreeNode node:xprtdroot.getChildren())
			xprtdmap.put(node.getHash(),node);
		for(Set<String> hashval:xprtdmap.keySet()){
			if(basemap.containsKey(hashval)){
			 xprtdroot.getChildren().remove(xprtdmap.get(hashval));
			 baseroot.getChildren().remove(basemap.get(hashval));
			 xprtdroot.getLeaves().removeAll(xprtdmap.get(hashval).getLeaves());
			 baseroot.getLeaves().removeAll(basemap.get(hashval).getLeaves());
			}
		}
		
	}
	
	public void diff(DTree basetree,DTree xprtdtree) throws distanceException{
		if(!basetree.getRoot().getHash().equals(xprtdtree.getRoot().getHash())){
			removeEqualSubTree(basetree,xprtdtree);
		DiffUtil newdiff=new DiffUtil();
		xmlUtil newxml=new xmlUtil();
//		print(newdiff.findMinimumMatch(basetree, xprtdtree));
		newxml.writeToxml(newdiff.findMinimumMatch(basetree, xprtdtree),"E:\\diff.xml");
		;
		}
		else{ 
			System.out.println("There are no differences");
		   System.exit(0);
	     }

  }
	
	public void print(DTreeNode node){
		System.out.println(node.getNodeName()+" "+node.getType());
		for(DTreeNode child:node.getChildren()){
			String type=child.getType();
		if(type.equals(nodeType.Attribute.toString()))
			System.out.println("  "+child.getNodeName()+":"+child.getNodeValue());
		else if (type.equals(nodeType.Element.toString()))
			print(child);
		else if (type.equals(nodeType.Text.toString()))
			System.out.println("   "+child.getNodeValue());
		}
	}
	
}
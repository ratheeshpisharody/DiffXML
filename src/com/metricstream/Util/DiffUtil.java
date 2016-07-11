package com.metricstream.Util;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.metricstream.Exceptions.distanceException;
import com.metricstream.constants.nodeType;
import com.metricstream.domain.DTree;
import com.metricstream.domain.DTreeNode;
import com.metricstream.domain.MatchPair;

/**
 * 
 * @author debdipta.h
 *
 *Diffing logic for the XMLs.
 */

public class DiffUtil {
	private ArrayList<DTreeNode>  deleted;
	
	public DiffUtil(){
		deleted=new ArrayList<DTreeNode>();
	}
	
/*finding the minimum match and storing distance between nodes in distance table.
 * Calculates distance between two nodes in a leaf to root manner.
 * Uses min cost max flow algorithm to calculate the distances.
 */
	public DTreeNode findMinimumMatch(DTree baseTree,DTree xportedTree) throws distanceException{
//		ArrayList<MatchPair> minmatch=new ArrayList<MatchPair>();
		HashMap<MatchPair,Integer> distanceTable=new HashMap<MatchPair,Integer>();
		Set<DTreeNode> baselements=new LinkedHashSet<DTreeNode>(baseTree.getRoot().getLeaves());
		Set<DTreeNode> xprtelements=new LinkedHashSet<DTreeNode>(xportedTree.getRoot().getLeaves());
		DTreeNode baseroot=baseTree.getRoot();
		DTreeNode xprtroot=xportedTree.getRoot();
		Set<DTreeNode> tempelements=new HashSet<DTreeNode>();
		
		do{
			for(DTreeNode basenode:baselements){
     			for(DTreeNode xprtnode:xprtelements){
					if(basenode.getSignature().equals(xprtnode.getSignature()))
					   distanceTable.put(new MatchPair(basenode,xprtnode),Dist(basenode,xprtnode,distanceTable));
				}
			}
			
		 for(DTreeNode bsnode:baselements){
			if(!bsnode.getType().equals(nodeType.Attribute.toString())&& bsnode.getParent()!=null) 
			  tempelements.add(bsnode.getParent());	  
		 }
		 baselements.clear();
		 baselements.addAll(tempelements);
		 tempelements.clear();
		 
		 for(DTreeNode xnode:xprtelements){
		  if(!xnode.getType().equals(nodeType.Attribute.toString())&& xnode.getParent()!=null) 
			 tempelements.add(xnode.getParent());
		 }
		 xprtelements.clear();
		 xprtelements.addAll(tempelements);
		 tempelements.clear();
						
		}while(!baselements.isEmpty()&& !xprtelements.isEmpty());
		
/*		for(MatchPair mpair:distanceTable.keySet())
			System.out.println(mpair.getfirstnode().getNodeName()+mpair.getfirstnode().getNodeValue()+"/"+mpair.getsecondnode().getNodeName()+mpair.getsecondnode().getNodeValue()+"/"+distanceTable.get(mpair));*/
		return editScript(baseroot,xprtroot,distanceTable);
	}

//Distance function for calculating distance between two nodes.	
   private int Dist(DTreeNode firstnode,DTreeNode secondnode,HashMap<MatchPair,Integer> distanceTable) throws distanceException{
	   int dist=-1;
	   if(!firstnode.getType().equals(nodeType.Element.toString())&& !secondnode.getType().equals(nodeType.Element.toString())){
		   if(firstnode.getSignature().equals(secondnode.getSignature())){
			   if(firstnode.getNodeValue().equals(secondnode.getNodeValue())){
				   dist=0;
			   }
			   else 
				   dist=1;
		   } 
	   }
	   
	   else{
		  for(DTreeNode fnode:firstnode.getChildren()){
			  for(DTreeNode snode:secondnode.getChildren()){
				 if(fnode.getSignature().equals(snode.getSignature())){
					 MatchPair nodepair=new MatchPair(fnode,snode);
//					 System.out.println(distanceTable.containsKey(nodepair)+fnode.getNodeName()+snode.getNodeName());
					 if(!distanceTable.containsKey(nodepair)){
					    if(fnode.getType().equals(nodeType.Element.toString()) && snode.getType().equals(nodeType.Element.toString()))
						 distanceTable.put(nodepair,0);
					    
					    else 
					    	throw new distanceException("Distance between"+fnode.getSignature()+"and"+snode.getSignature()+"not found");
					 }  
						 dist=max(dist,distanceTable.get(nodepair)); 
					  
				 }
			  }
		  }
	   }
	  return dist;
   }

private int max(int x,int y){
	if(x>=y)
		return x;
    return y;
}

/*This function does the necessary changes to the base DTree to get the updated one. 
*If the new root is a totally new one the base root is replaced with the new one.
*returns the root node of the diffed DTree.
*/
 private DTreeNode editScript(DTreeNode baseroot,DTreeNode xprtroot,HashMap<MatchPair,Integer> distanceTable){
	 MatchPair pair=new MatchPair(baseroot,xprtroot);
	 HashSet<DTreeNode> basexcselements=new HashSet<DTreeNode>();
	 HashSet<DTreeNode> xprtexcselements;
	 int flagx;
	 DTreeNode resultant=baseroot;
	 if(!distanceTable.keySet().contains(pair)){
	   resultant=xprtroot;
	 }
	 else if(distanceTable.get(pair)==0 && baseroot.getParent()!=null && adjustedAttributes(baseroot,xprtroot,distanceTable)==true){
		     deleted.add(pair.getfirstnode());
//			 System.out.println("No changes");
			 resultant=null;
	 }
	 else{ 
		   for(DTreeNode nodebase:baseroot.getChildren()){ 
			   flagx=0;
			   for(DTreeNode nodexprt:xprtroot.getChildren()){
				if(nodexprt.status()!='Y'){
			     MatchPair piar=new MatchPair(nodebase,nodexprt);
			     if(distanceTable.containsKey(piar)){
			    	 flagx=1;
				   if(!piar.getfirstnode().getType().equals(nodeType.Element.toString())&& !piar.getsecondnode().getType().equals(nodeType.Element.toString())){
					if(distanceTable.get(piar)==1)
						piar.getfirstnode().update(piar.getsecondnode().getNodeValue());
					else if(nodebase.getType().equals(nodeType.Attribute.toString())){
						basexcselements.add(nodebase);
					}
					else
						deleted.add(piar.getfirstnode());
						
				   }
				  else
					  editScript(piar.getfirstnode(),piar.getsecondnode(),distanceTable);
				  nodexprt.updateStatus('Y'); 
				  break;
			     }     
			   }
			  }	
			  if(flagx==0)
			   basexcselements.add(nodebase);
			  nodebase.removeChildren(deleted);
			 
			 }
		   for(DTreeNode basenodex:basexcselements)
			   basenodex.delete();
			   
		   xprtexcselements=new HashSet<DTreeNode>(getxprtnodes(distanceTable.keySet()));
		   for(DTreeNode nodexprt:xprtroot.getChildren()){
			   if(!xprtexcselements.contains(nodexprt)&& nodexprt.status()=='N')
				   nodexprt.insert(baseroot);
		   }
		   
		 
	 }
 return resultant;
 }
 
 /*
  * get the second nodes from the MatchPairs of the distanceTable.
  */
 private Set<DTreeNode> getxprtnodes(Set<MatchPair> nodes){
	 Set<DTreeNode> secondnodes=new HashSet<DTreeNode>(); 
	 for(MatchPair mpair:nodes)
		 secondnodes.add(mpair.getsecondnode());
//		 System.out.println("__"+mpair.getsecondnode().getNodeName()+"/");}
	 return secondnodes;
 }
 
 /*
  * get the first nodes of the MatchPairs of the distanceTable.
  */
 
 private Set<DTreeNode> getbasenodes(Set<MatchPair> nodes){
	 Set<DTreeNode> fnodes=new HashSet<DTreeNode>(); 
	 for(MatchPair mpair:nodes)
		 fnodes.add(mpair.getfirstnode());
	 return fnodes;
 }
 
/*
 * Adjusts the attributes of a node.
 * Returns true if there is no change between the attributes of a tag i.e. there is no updation or insertion of new attributes.
 * Returns false otherwise and also does the required changes to the DTree Structure. 
 */
private boolean adjustedAttributes(DTreeNode baseroot,DTreeNode xprtnode,HashMap<MatchPair,Integer> distanceTable){
	Set<DTreeNode> secondnodes=new HashSet<DTreeNode>(getxprtnodes(distanceTable.keySet())); 
	Set<DTreeNode> fnodes=new HashSet<DTreeNode>(getbasenodes(distanceTable.keySet()));
	boolean bool=true;
	for(DTreeNode child:baseroot.getChildren()){
		if(child.getType().equals(nodeType.Attribute.toString())&& !fnodes.contains(child)){
			child.delete();
			bool=false;
		}
	}
	for(DTreeNode child:xprtnode.getChildren()){
		if(child.getType().equals(nodeType.Attribute.toString())&& !secondnodes.contains(child)){
			child.insert(baseroot);
			bool=false;
		}
	}
	return bool;
}
}

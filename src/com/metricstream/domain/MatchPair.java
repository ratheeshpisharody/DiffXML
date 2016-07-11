package com.metricstream.domain;
/**
 * 
 * @author debdipta.h
 *
 */

public class MatchPair {
	private DTreeNode firstnode;
	private DTreeNode secondnode;
	
	public MatchPair(DTreeNode firstnode,DTreeNode secondnode){
		this.firstnode=firstnode;
		this.secondnode=secondnode;
	}
	
    
	public DTreeNode getfirstnode(){
		return firstnode;
	}
	
	public DTreeNode getsecondnode(){
		return secondnode;
	}
	
	@Override
	public int hashCode(){
		return firstnode.getSignature().hashCode()^secondnode.getSignature().hashCode()^System.identityHashCode(this.getfirstnode().getParent())^System.identityHashCode(this.getsecondnode().getParent());
		
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null)
			return false;
		if(!(o instanceof MatchPair))
			return false;
		MatchPair pair=(MatchPair)o;
		DTreeNode tfnode=this.getfirstnode();
		DTreeNode tsnode=this.getsecondnode();
		DTreeNode pfnode=pair.getfirstnode();
		DTreeNode psnode=pair.getsecondnode();
		if(tfnode.getParent()!=null && pfnode.getParent()!=null){
		if(tfnode.getSignature().equals(pfnode.getSignature())&& tfnode.getParent().equals(pfnode.getParent())&& tsnode.getSignature().equals(psnode.getSignature())&& tsnode.getParent().equals(psnode.getParent()))
			return true;
		}
		else{  
			if(tfnode.getSignature().equals(pfnode.getSignature()) && tsnode.getSignature().equals(psnode.getSignature()))
				return true;
		}
			
		return false;
	}
}

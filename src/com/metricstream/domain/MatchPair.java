package com.metricstream.domain;

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
		return firstnode.getSignature().hashCode()^secondnode.getSignature().hashCode();
		
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null)
			return false;
		if(!(o instanceof MatchPair))
			return false;
		MatchPair pair=(MatchPair)o;
		if((this.getfirstnode().getSignature().equals(pair.firstnode.getSignature()))&&(this.getsecondnode().getSignature().equals(pair.secondnode.getSignature())))
			return true;
		return false;
	}
}

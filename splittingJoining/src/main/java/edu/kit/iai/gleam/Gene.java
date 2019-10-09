package edu.kit.iai.gleam;

import java.util.List;

public class Gene {
	public Integer genId;
	public List<Integer> IntList;
	public List<Double> Dlist;
	
	public Integer getGenId() {
		return genId;
	}
	public void setGenId(Integer genId) {
		this.genId = genId;
	}
	public List<Integer> getIntList() {
		return IntList;
	}
	public void setIntList(List<Integer> intList) {
		IntList = intList;
	}
	public List<Double> getDlist() {
		return Dlist;
	}
	public void setDlist(List<Double> dlist) {
		Dlist = dlist;
	}
	
}

package edu.kit.iai.gleam;

import java.util.List;

public class Chromosome {
public Integer parentIndex;
 public Integer childIndex;
 public Integer chrLength;
 public List<Gene> genList;
 
 public Integer getParentIndex() {
		return parentIndex;
	}
	public void setParentIndex(Integer parentIndex) {
		this.parentIndex = parentIndex;
	}
	public Integer getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(Integer childIndex) {
		this.childIndex = childIndex;
	}
	public Integer getChrLength() {
		return chrLength;
	}
	public void setChrLength(Integer chrLength) {
		this.chrLength = chrLength;
	}
	public List<Gene> getGenList() {
		return genList;
	}
	public void setGenList(List<Gene> genList) {
		this.genList = genList;
	}

 
}

package edu.kit.iai.gleam;

import java.util.List;

public class Result {
	public Integer retStatus; 
	public Integer chrNumber;
	public Integer resValNumber;
	public List<ResultList> Reslist;
	
	public Integer getRetStatus() {
		return retStatus;
	}
	public void setRetStatus(Integer retStatus) {
		this.retStatus = retStatus;
	}
	public Integer getChrNumber() {
		return chrNumber;
	}
	public void setChrNumber(Integer chrNumber) {
		this.chrNumber = chrNumber;
	}
	public Integer getResValNumber() {
		return resValNumber;
	}
	public void setResValNumber(Integer resValNumber) {
		this.resValNumber = resValNumber;
	}
/*	public List<ResultList> getReslist() {
		return Reslist;
	}
	public void setReslist(List<ResultList> reslist) {
		this.Reslist = reslist;
	}
	*/
	
}

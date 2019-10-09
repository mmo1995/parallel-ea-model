package edu.kit.iai.gleam;
import java.util.List;
public class ChromosomeList {
	public Integer chrNumber;
	public Integer maxIntNumber;
	public Integer maxDoubleNumber;
	public List<Chromosome> chormomses;
	public Integer getChrNumber() {
		return chrNumber;
	}
	public void setChrNumber(Integer chrNumber) {
		this.chrNumber = chrNumber;
	}
	public Integer getMaxIntNumber() {
		return maxIntNumber;
	}
	public void setMaxIntNumber(Integer maxIntNumber) {
		this.maxIntNumber = maxIntNumber;
	}
	public Integer getMaxDoubleNumber() {
		return maxDoubleNumber;
	}
	public void setMaxDoubleNumber(Integer maxDoubleNumber) {
		this.maxDoubleNumber = maxDoubleNumber;
	}
	public List<Chromosome> getChormomses() {
		return chormomses;
	}
	public void setChormomses(List<Chromosome> chormomses) {
		this.chormomses = chormomses;
	}
	
}

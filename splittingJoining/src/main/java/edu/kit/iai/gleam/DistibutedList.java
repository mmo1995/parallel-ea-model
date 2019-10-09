package edu.kit.iai.gleam;

import java.util.List;

public class DistibutedList {
	//public Integer NrOfContainer;
	public List<String> DistibutedList;

   /* public Integer getNrOfContainer() {
		return NrOfContainer;
	}
	public void setNrOfContainer(Integer nrOfContainer) {
		NrOfContainer = nrOfContainer;
	}*/
	public List<String> getDistibutedList() {
		return DistibutedList;
	}
	public void setDistibutedList(String distibutedList) {
		DistibutedList.add(distibutedList);
	}
}

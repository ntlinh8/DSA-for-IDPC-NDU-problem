package IDPCDU2;

import java.util.ArrayList;
import java.util.List;

public class Domain {
	public int nameDomain; //Dung de dinh danh domain
	public int[] nodeList;
	
	
	public Domain(int number, int[] nodeList) {
		this.nameDomain = number;
		this.nodeList = new int [nodeList.length];
		for(int i = 0; i < nodeList.length; i++) {
			this.nodeList[i] = nodeList[i];
		}
	}
	

	
	
	//Tinh toan so canh di sang domain khac
	//Input: Node & Domain can den
	//Out put: list cac edge va 
	
	

}

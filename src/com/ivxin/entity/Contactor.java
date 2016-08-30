package com.ivxin.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Contactor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ArrayList<String> numberList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getNumberList() {
		return numberList;
	}
	public void setNumberList(ArrayList<String> numberList) {
		this.numberList = numberList;
	}
	
	
}

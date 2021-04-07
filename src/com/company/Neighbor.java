package com.company;

public class Neighbor implements Comparable{
    //编号
	private int id;
    //相似度
	private double value;
	public Neighbor(int id,double value) {
		this.id=id;
		this.value=value;
	}
	public int getID(){
		return id;
	}
	public double getValue(){
		return value;
	}
	public int compareTo(Object o) {//降序排列
		// TODO Auto-generated method stub
		if(o instanceof Neighbor){
			Integer ID=((Neighbor) o).id;
			Double VALUE=((Neighbor) o).value;
			return VALUE.compareTo(value);
		}
		else{
			return 2;
		}
	}
}


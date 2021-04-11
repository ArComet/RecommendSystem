package com.recommend;

public class Item implements Comparable{
	private int id;//编号
	private double value;//推荐度
	Item(int id, double value) {
		this.id=id;
		this.value=value;
	}

	int getID(){
		return id;
	}

	double getValue(){
		return value;
	}

	public int compareTo(Object o) {//降序排列
		// TODO Auto-generated method stub
		if(o instanceof Item){
			Integer ID=((Item) o).id;
			Double VALUE=((Item) o).value;
			return VALUE.compareTo(value);
		}
		else{
			return 2;
		}
	}
}


package com.recommend;

import java.util.*;

/*
 * 基于用户协同过滤的即时动态推荐算法
 * 度量用户间相似性的方法选用：带修正的余弦相似性
 * */
public class ItemBaseCF extends UserBaseCF{

	//推荐物品即最近邻居
	public int[] Similar(int itemID,int size){
		Neighbor[] neighbors = getNeighbor(itemID);
		int[] items = new int[size];
		for (int i=0; i<size; i++){
			if (i<numOfneighbor[itemID])
				items[i]=neighbors[i].getID();
			else
				items[i]=-1;
		}
		return items;
	}

	public ItemBaseCF(){
		super();
	}

	public int addItem(){
		return super.addUser();
	}

	public int updateUserScoreOfItem(int ItemID,int UserID,double UserScore){
		return super.updateItemScoreOfUser(ItemID,UserID,UserScore);
	}

	public int updateUserScoreOfItem(int ItemID,Map<Integer, Double> ScoreMap){
		return super.updateItemScoreOfUser(ItemID,ScoreMap);
	}
}
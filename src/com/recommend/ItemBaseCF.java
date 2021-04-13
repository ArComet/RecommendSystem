package com.recommend;

import java.util.*;

/*
 * �����û�Эͬ���˵ļ�ʱ��̬�Ƽ��㷨
 * �����û��������Եķ���ѡ�ã�������������������
 * */
public class ItemBaseCF extends UserBaseCF{

	//�Ƽ���Ʒ������ھ�
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
package com.recommend;

import java.util.*;

/*
 * 基于用户协同过滤的即时动态推荐算法
 * 度量用户间相似性的方法选用：带修正的余弦相似性
 * */
public class UserBaseCF extends BaseData{
	//用户的近邻数
	private static final int UN=10;

	/*
	 * 维护用户平均打分
	 * num 打分次数 sum 打分分数和
	 * */
	private int[] num=new int[MAXUSERSIZE];
	private double[] sum=new double[MAXUSERSIZE];
	private double getAverage(int userID){
		return 1.0*sum[userID]/num[userID];
	}

	//用户近邻
	private Neighbor[][] neighbor =new Neighbor[MAXUSERSIZE +1][UN+1];//每个用户的最近的UN个邻居

	public void setNeighbor(Neighbor[][] neighbor){
		this.neighbor=neighbor;
	}

	public Neighbor[][] getNeighbor(){
		return neighbor;
	}


	//向量自身和
	private double Sum(double[] arr, int len){
		double sum=0.0;
		for(int i=0; i<len; i++) sum+=arr[i];
		return sum;
	}
	//向量点积
	private double vectorMultiple(double[] arr1, double[] arr2, int len){
		double sum=0.0;
		for(int i=0;i<len;i++) sum+=arr1[i]*arr2[i];
		return sum;
	}
	//Pearson相似度
	private double Pearson(double[] x, double[] y, int len){
		double sumX=Sum(x,len);
		double sumY=Sum(y,len);
		double sumXX= vectorMultiple(x,x,len);
		double sumYY= vectorMultiple(y,y,len);
		double sumXY= vectorMultiple(x,y,len);
		double upside=sumXY-sumX*sumY/(len);
		double downside=Math.sqrt((sumXX-Math.pow(sumX, 2)/len)*(sumYY-Math.pow(sumY, 2)/len));
		return upside/downside;
	}

	//从稀疏矩阵中提取向量并处理
	private double[] getVectorOfMap(Map<Integer,Double> map, Set<Integer> keys, double filling){
		double[] vector = new double[MAXITEMSIZE];
		int k=0;
		for (Integer key:keys)
			vector[k++]=map.getOrDefault(key,filling);
		return vector;
	}

	//求user的近邻，求NofUser数组
	private void findNeighbor(int userID){
		Set<Integer> keys = score[userID].keySet();
		Queue<Neighbor> neighborList = new PriorityQueue<>();

		for(int id = 0; id< UserSize; id++){
			if(id==userID) continue;
			double[] V1 = getVectorOfMap(score[userID],keys,getAverage(userID));
			double[] V2 = getVectorOfMap(score[id],keys,getAverage(id));
			double sim = Pearson(V1,V2,keys.size());
			neighborList.add(new Neighbor(id,sim));
			if (neighborList.size()>UN) neighborList.remove();
		}

		int k=0;
		for (Neighbor it:neighborList){
			neighbor[userID][k++]=it;
		}
	}
	
	//根据最近邻居给出预测评分
	private double predictByNeighbor(int userID, int itemID){//这里的userID为用户输入，减1后为数组下标！
		double sum1=0;
	    double sum2=0;
	    for(int i=0; i<Math.min(UN, UserSize-1); i++){//对最近的UN个邻居进行处理
	    	int neighborID=neighbor[userID][i].getID();
	        double sim=neighbor[userID][i].getValue();
	        sum1+=sim*(score[neighborID].get(itemID)-getAverage(userID));
	        sum2+=Math.abs(sim);
	    }
	    return getAverage(userID)+sum1/sum2;
	}

	//根据最近邻居给出推荐物品
	public int[] Recommending(int userID, int size){
		Queue<Item> items = new PriorityQueue<>();
		Set<Integer> unique = new HashSet<>();//去重
		for(int i=0; i<Math.min(UN, UserSize-1); i++){//对最近的UN个邻居进行处理
			int neighborID=neighbor[userID][i].getID();
			for (Integer key:score[neighborID].keySet()){
				if (score[userID].containsKey(key)) continue;
				if (unique.contains(key)) continue;
				items.add(new Item(key,predictByNeighbor(userID,key)));
				unique.add(key);
				if (items.size()>size){
					unique.remove(items.peek().getID());
					items.remove();
				}
			}
		}

		int[] itemList = new int[size];
		int k=0;
		while (k<size && !items.isEmpty()){
			itemList[k++] = items.peek().getID();
			items.remove();
		}
		return itemList;
	}

	public UserBaseCF(){
		super();
	}

	@Override
	public int updateItemScoreOfUser(int UserID,int ItemID,double ItemScore){
		try {
			if (!score[UserID].containsKey(ItemID))
				num[UserID]++;
			else
				sum[UserID]-=score[UserID].get(ItemID);
			sum[UserID]+=ItemScore;
			score[UserID].replace(ItemID,ItemScore);
			findNeighbor(UserID);
			return 0;
		}catch (Exception e){
			return -1;
		}
	}

	@Override
	public int updateItemScoreOfUser(int UserID, Map<Integer, Double> ScoreMap){
		try {
			num[UserID]=ScoreMap.size();
			sum[UserID]=0;
			for (Integer key : ScoreMap.keySet())
				sum[UserID]+=ScoreMap.get(key);
			score[UserID]=ScoreMap;
			findNeighbor(UserID);
			return 0;
		}catch (Exception e){
			return -1;
		}
	}
}
package com.recommend;

import java.util.*;

/*
 * �����û�Эͬ���˵ļ�ʱ��̬�Ƽ��㷨
 * �����û��������Եķ���ѡ�ã�������������������
 * */
public class UserBaseCF extends BaseData{
	//�û��Ľ�����
	private static final int UN=10;

	/*
	 * ά���û�ƽ�����
	 * num ��ִ��� sum ��ַ�����
	 * */
	private int[] num = new int[MAXUSERSIZE];
	private double[] sum = new double[MAXUSERSIZE];
	private double getAverage(int userID){
		if (num[userID]==0) return 0;
		return 1.0*sum[userID]/num[userID];
	}

	//�û�����
	private Neighbor[][] neighbor = new Neighbor[MAXUSERSIZE][UN];//ÿ���û��������UN���ھ�
	int [] numOfneighbor = new int[MAXUSERSIZE];
	public void setNeighbor(Neighbor[][] neighbor){
		this.neighbor=neighbor;
	}

	public Neighbor[][] getNeighbor(){
		return neighbor;
	}
	public Neighbor[] getNeighbor(int userid){
		return neighbor[userid];
	}


	//���������
	private double Sum(double[] arr, int len){
		double sum=0.0;
		for(int i=0; i<len; i++) sum+=arr[i];
		return sum;
	}
	//�������
	private double vectorMultiple(double[] arr1, double[] arr2, int len){
		double sum=0.0;
		for(int i=0;i<len;i++) sum+=arr1[i]*arr2[i];
		return sum;
	}
	//Pearson���ƶ�
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

	//��ϡ���������ȡ����������
	private double[] getVectorOfMap(Map<Integer,Double> map, Set<Integer> keys, double filling){
		double[] vector = new double[MAXITEMSIZE];
		int k=0;
		for (Integer key:keys)
			vector[k++]=map.getOrDefault(key,filling);
		return vector;
	}

	//��user�Ľ��ڣ���NofUser����
	private int findNeighbor(int userID){
		Set<Integer> keys = score[userID].keySet();
		Queue<Neighbor> neighbors = new PriorityQueue<>();

		for(int id = 0; id< UserSize; id++){
			if(id==userID) continue;
			double[] V1 = getVectorOfMap(score[userID],keys,getAverage(userID));
			double[] V2 = getVectorOfMap(score[id],keys,getAverage(id));
			double sim = Pearson(V1,V2,keys.size());
			neighbors.add(new Neighbor(id,sim));
			if (neighbors.size()>UN) neighbors.remove();
		}

		int cnt=0;
		while (!neighbors.isEmpty()){
			neighbor[userID][cnt++]=neighbors.peek();
			neighbors.remove();
		}
		return cnt;
	}
	
	//��������ھӸ���Ԥ������
	private double predictByNeighbor(int userID, int itemID){//�����userIDΪ�û����룬��1��Ϊ�����±꣡
		double sum1=0;
	    double sum2=0;
	    for(int i=0; i<numOfneighbor[userID]; i++){//�������UN���ھӽ��д���
	    	int neighborID=neighbor[userID][i].getID();
	        double sim = neighbor[userID][i].getValue();
	        double nei = getAverage(neighborID);
	        if (score[neighborID].containsKey(itemID))
	        	nei = score[neighborID].get(itemID);
	        sum1+=sim*(nei-getAverage(userID));
	        sum2+=Math.abs(sim);
	    }
	    return getAverage(userID)+sum1/sum2;
	}

	//��������ھӸ����Ƽ���Ʒ
	public int[] Recommending(int userID,int size){
		Queue<Item> items = new PriorityQueue<>();
		Set<Integer> unique = new HashSet<>();//ȥ��
		for(int i=0; i<numOfneighbor[userID]; i++){//������ھӽ��д���
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

		for (int i=0; i<size; i++){
			if (!items.isEmpty()){
				itemList[i] = items.peek().getID();
				items.remove();
			}
			else
				itemList[i] = -1;
		}

		return itemList;
	}

	public UserBaseCF(){
		super();
	}

	@Override
	public int addUser(){
		score[UserSize]=new HashMap<Integer, Double>();
		num[UserSize]=0;
		sum[UserSize]=0;
		return UserSize++;
	}

	@Override
	public int updateItemScoreOfUser(int UserID,int ItemID,double ItemScore){
		try {
			if (!score[UserID].containsKey(ItemID)){
				num[UserID]++;
				sum[UserID]+=ItemScore;
				score[UserID].put(ItemID,ItemScore);
			}
			else{
				sum[UserID]-=score[UserID].get(ItemID);
				sum[UserID]+=ItemScore;
				score[UserID].replace(ItemID,ItemScore);
			}
			for (int uid=0; uid<UserSize; uid++){
				if (score[uid].containsKey(ItemID)){
					numOfneighbor[uid]=findNeighbor(uid);
				}
			}
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

			for (int uid=0; uid<UserSize; uid++){
				numOfneighbor[uid]=findNeighbor(uid);
			}

			return 0;
		}catch (Exception e){
			return -1;
		}
	}
}
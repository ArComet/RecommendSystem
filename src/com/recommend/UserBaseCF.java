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
	private int[] num=new int[MAXUSERSIZE];
	private double[] sum=new double[MAXUSERSIZE];
	private double getAverage(int userID){
		return 1.0*sum[userID]/num[userID];
	}

	//�û�����
	private Neighbor[][] neighbor =new Neighbor[MAXUSERSIZE +1][UN+1];//ÿ���û��������UN���ھ�

	public void setNeighbor(Neighbor[][] neighbor){
		this.neighbor=neighbor;
	}

	public Neighbor[][] getNeighbor(){
		return neighbor;
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
	
	//��������ھӸ���Ԥ������
	private double predictByNeighbor(int userID, int itemID){//�����userIDΪ�û����룬��1��Ϊ�����±꣡
		double sum1=0;
	    double sum2=0;
	    for(int i=0; i<Math.min(UN, UserSize-1); i++){//�������UN���ھӽ��д���
	    	int neighborID=neighbor[userID][i].getID();
	        double sim=neighbor[userID][i].getValue();
	        sum1+=sim*(score[neighborID].get(itemID)-getAverage(userID));
	        sum2+=Math.abs(sim);
	    }
	    return getAverage(userID)+sum1/sum2;
	}

	//��������ھӸ����Ƽ���Ʒ
	public int[] Recommending(int userID, int size){
		Queue<Item> items = new PriorityQueue<>();
		Set<Integer> unique = new HashSet<>();//ȥ��
		for(int i=0; i<Math.min(UN, UserSize-1); i++){//�������UN���ھӽ��д���
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
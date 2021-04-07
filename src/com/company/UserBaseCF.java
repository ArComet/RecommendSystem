package com.company;

import java.util.*;

/*
 * 基于用户的动态协同过滤推荐算法
 * 度量用户间相似性的方法选用：带修正的余弦相似性
 * */
class UserBaseCF extends BaseData{
	//用户的近邻数
	private static final int UN=10;

	//用户实际打分物品数（求平均分）
	private int[] num=new int[MAXUSERSIZE];
	//用户实际打分物品分数和（求平均分）
	private int[] sum=new int[MAXUSERSIZE];
	//平均分
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

	//数据预处理
	//处理评分矩阵的稀疏问题
	//user对没有被评分的item，会打几分
	public void dealMatrix(){
		for(int i = 0; i< usersize; i++){
			//计算总和并统计数量
			for(int j = 0; j< itemsize; j++){
				if(score[i][j]!=0) num[i]++;
				sum[i]+=score[i][j];
			}
			//用平均值代替空值
			for(int j = 0; j< itemsize; j++){
				if(score[i][j]==0)
					score[i][j]=1.0*sum[i]/num[i];
			}
		}
		//test
		for(int i = 0; i< usersize; i++){
			for(int j = 0; j< itemsize; j++){
				System.out.print(score[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	//向量自身和
	private double Sum(double[] arr){
		double sum=0.0;
		for(double it:arr) sum+=it;
		return sum;
	}
	//向量点积
	private double vectorMultiple(double[] arr1, double[] arr2, int len){
		double sum=0.0;
		for(int i=0;i<len;i++) sum+=arr1[i]*arr2[i];
		return sum;
	}
	//Pearson相似度
	static final double eps = 1e-18;
	private double Pearson(double[] x,double[] y){
		int len = Math.min(x.length,y.length);
		double sumX=Sum(x);
		double sumY=Sum(y);
		double sumXX= vectorMultiple(x,x,len);
		double sumYY= vectorMultiple(y,y,len);
		double sumXY= vectorMultiple(x,y,len);
		double upside=sumXY-sumX*sumY/(len+eps);
		double downside=Math.sqrt((sumXX-Math.pow(sumX, 2)/len)*(sumYY-Math.pow(sumY, 2)/(len+eps)));
		return upside/(downside+eps);
	}

	//求user的近邻，求NofUser数组
	private void findNeighbor(int userID){
		Queue<Neighbor> neighborList = new PriorityQueue<>();

		for(int id=0; id<usersize; id++){
			if(id==userID) continue;
			double sim=Pearson(score[userID],score[id]);
			neighborList.add(new Neighbor(id,sim));
			if (neighborList.size()>UN)
				neighborList.remove();
		}

		int k=0;
		for (Neighbor it:neighborList){
			neighbor[userID][k++]=it;
		}
	}
	
	//根据最近邻居给出预测评分
	public double predict(int userID, int itemID){//这里的userID为用户输入，减1后为数组下标！
		double sum1=0;
	    double sum2=0;
	    for(int i=0;i<Math.min(UN,usersize-1);i++){//对最近的UN个邻居进行处理
	    	int neighborID=neighbor[userID][i].getID();
	        double neib_sim=neighbor[userID][i].getValue();
	        sum1+=neib_sim*(score[neighborID][itemID]-getAverage(userID));
	        sum2+=Math.abs(neib_sim);
	    }
	    return getAverage(userID)+sum1/sum2;
	}

	UserBaseCF(){
		super();
	}

	UserBaseCF(double[][] score){
		super(score);
		dealMatrix();
		for (int userID=0; userID<usersize; userID++){
			findNeighbor(userID);
		}
	}

	@Override
	public int addUser(double[] userVector){
		try {
			score[usersize++]=userVector;
			findNeighbor(itemsize);
			return usersize;
		}catch (Exception e){
			return -1;
		}
	}
}
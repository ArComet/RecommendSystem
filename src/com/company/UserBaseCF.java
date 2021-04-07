package com.company;

import java.util.*;

/*
 * �����û��Ķ�̬Эͬ�����Ƽ��㷨
 * �����û��������Եķ���ѡ�ã�������������������
 * */
class UserBaseCF extends BaseData{
	//�û��Ľ�����
	private static final int UN=10;

	//�û�ʵ�ʴ����Ʒ������ƽ���֣�
	private int[] num=new int[MAXUSERSIZE];
	//�û�ʵ�ʴ����Ʒ�����ͣ���ƽ���֣�
	private int[] sum=new int[MAXUSERSIZE];
	//ƽ����
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

	//����Ԥ����
	//�������־����ϡ������
	//user��û�б����ֵ�item����򼸷�
	public void dealMatrix(){
		for(int i = 0; i< usersize; i++){
			//�����ܺͲ�ͳ������
			for(int j = 0; j< itemsize; j++){
				if(score[i][j]!=0) num[i]++;
				sum[i]+=score[i][j];
			}
			//��ƽ��ֵ�����ֵ
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
	//���������
	private double Sum(double[] arr){
		double sum=0.0;
		for(double it:arr) sum+=it;
		return sum;
	}
	//�������
	private double vectorMultiple(double[] arr1, double[] arr2, int len){
		double sum=0.0;
		for(int i=0;i<len;i++) sum+=arr1[i]*arr2[i];
		return sum;
	}
	//Pearson���ƶ�
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

	//��user�Ľ��ڣ���NofUser����
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
	
	//��������ھӸ���Ԥ������
	public double predict(int userID, int itemID){//�����userIDΪ�û����룬��1��Ϊ�����±꣡
		double sum1=0;
	    double sum2=0;
	    for(int i=0;i<Math.min(UN,usersize-1);i++){//�������UN���ھӽ��д���
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
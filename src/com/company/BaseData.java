package com.company;

public class BaseData {
    //用户和物品上限
    static final int MAXUSERSIZE = 1000;
    static final int MAXITEMSIZE = 1000;
    //用户和物品实际数量
    int usersize, itemsize;
    //用户评分矩阵
    double[][] score = new double[MAXUSERSIZE+1][MAXITEMSIZE+1];

    BaseData(){
        usersize = 0;
        itemsize = 0;
    }

    BaseData(double[][] score){
        this.score = score;
        usersize = score.length;
        itemsize = usersize==0?0:score[0].length;
    }

    public int addUser(double[] userVector){
        usersize++;
        score[usersize]=userVector;
        return usersize;
    }

    public int addItem(){
        itemsize++;
        return itemsize;
    }

    public int addItem(double[] itemVector){
        try {
            itemsize++;
            for (int i=1; i<=usersize; i++){
                score[i][itemsize]=itemVector[i];
            }
            return itemsize;
        }catch (Exception e){
            return -1;
        }
    }

    public double[][] getScore(){
        return score;
    }
}

package com.recommend;

import java.util.HashMap;
import java.util.Map;

public class BaseData {
    //用户和物品上限
    static final int MAXUSERSIZE = 1000000;
    static final int MAXITEMSIZE = 100;

    //用户和物品实际数量
    //userID范围 [0,UserSize)
    int UserSize;

    //用户评分矩阵
    Map<Integer, Double>[] score;

    public BaseData(){
        UserSize = 0;
        score = new Map[MAXUSERSIZE];
    }

    public int addUser(){
        score[UserSize]=new HashMap<Integer, Double>();
        return UserSize++;
    }

    //set 修改用户对一个物品的打分
    public int updateItemScoreOfUser(int UserID, int ItemID, double ItemScore){
        try {
            score[UserID].replace(ItemID,ItemScore);
            return 0;
        }catch (Exception e){
            return -1;
        }
    }

    //set 批量修改用户对物品的打分表
    public int updateItemScoreOfUser(int UserID, Map<Integer, Double> ScoreMap){
        try {
            score[UserID]=ScoreMap;
            return 0;
        }catch (Exception e){
            return -1;
        }
    }

    //get 获取用户的打分表
    public Map<Integer, Double> getScoreMapofUser(int UserID){
        return score[UserID];
    }

}

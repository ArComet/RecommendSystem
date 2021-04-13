package com.sample;

import com.recommend.Neighbor;
import com.recommend.UserBaseCF;

/**********
 * 即时动态推荐算法
 * 根据用户近期浏览商品
 * 基于稀疏矩阵的优化加速
 */

public class sample_user {
    public static void main(String[] args){
        //原始用户评分数据
        double[][] matrix={
                {10,9,7,0,0},
                {8,1,0,0,0},
                {0,0,7,8,9},
                {0,0,0,8,9},
                {10,0,0,0,10},
        };
        //创建用户协同过滤聚类器
        UserBaseCF cf = new UserBaseCF();
        //导入数据
        for (int i=0; i<5; i++){
            int uid = cf.addUser();
            for (int j=0; j<5; j++){
                if (matrix[i][j]==0) continue;//0表示未评分，跳过
                //修改 uid用户 对 j商品 的评分
                cf.updateItemScoreOfUser(uid,j,matrix[i][j]);
            }
        }
        //获取用户近邻（测试用）
        Neighbor[][] Neighbor = cf.getNeighbor();

        System.out.print("Neighbor of user 4:[");
        for (int i=0; i<4; i++)
            System.out.print(Neighbor[4][i].getID()+",");
        System.out.println("]");

        //获取uid为4的用户的大小为4的推荐商品列表
        int[] itemList = cf.Recommending(3,4);

        System.out.print("Recommending for user 4:[");
        for (int i=0; i<4; i++)
            System.out.print(itemList[i]+",");
        System.out.println("]");
    }
}

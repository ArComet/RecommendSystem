package com.sample;

import com.recommend.Neighbor;
import com.recommend.UserBaseCF;

/**********
 * 即时动态推荐算法
 * 根据用户近期浏览商品
 * 基于稀疏矩阵的优化加速
 */

public class Main {
    public static void main(String args[]){
        //原始用户评分数据
        double[][] matrix={
                {10,9,7,0,0},
                {8,1,0,0,0},
                {0,0,7,8,9},
                {0,0,0,8,9},
                {10,0,0,0,10},
        };

        UserBaseCF cf = new UserBaseCF();

        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                cf.updateItemScoreOfUser(i,j,matrix[i][j]);
            }
        }

        Neighbor[][] Neighbor = cf.getNeighbor();

        System.out.print("Neighbor of user 0:[");
        for (int i=0; i<3; i++) System.out.print(Neighbor[0][i]+",");
        System.out.println("]");

        int[] itemlist = cf.Recommending(0,3);

        System.out.print("Recommending for user 0:[");
        for (int i=0; i<3; i++) System.out.print(itemlist[i]+",");
        System.out.println("]");
    }
}

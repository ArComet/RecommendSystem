package com.sample;

import com.recommend.Neighbor;
import com.recommend.UserBaseCF;

import java.util.Map;

/**********
 * 即时动态推荐算法
 * 根据用户近期浏览商品
 * 基于稀疏矩阵的优化加速
 */

public class Main {
    public static void main(String[] args){
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
            int uid = cf.addUser();
            for (int j=0; j<5; j++){
                if (matrix[i][j]==0) continue;
                cf.updateItemScoreOfUser(uid,j,matrix[i][j]);
            }
        }

        Neighbor[][] Neighbor = cf.getNeighbor();

        System.out.print("Neighbor of user 4:[");
        for (int i=0; i<4; i++) System.out.print(Neighbor[4][i].getID()+",");
        System.out.println("]");

        int[] itemlist = cf.Recommending(4,3);

        System.out.print("Recommending for user 4:[");
        for (int i=0; i<3; i++) System.out.print(itemlist[i]+",");
        System.out.println("]");
    }
}

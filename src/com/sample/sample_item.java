package com.sample;

import com.recommend.ItemBaseCF;

/**********
 * 即时动态推荐算法
 * 根据用户近期浏览商品
 * 基于稀疏矩阵的优化加速
 */

public class sample_item {
    public static void main(String[] args){
        //原始用户评分数据
        double[][] matrix={
                {10,9,7,0,0},
                {8,1,0,0,0},
                {0,0,7,8,9},
                {0,0,0,8,9},
                {10,0,0,0,10},
        };
        //创建物品协同过滤聚类器
        ItemBaseCF cf = new ItemBaseCF();
        //导入数据
        for (int j=0; j<5; j++){
            int id = cf.addItem();
            for (int i=0; i<5; i++){
                if (matrix[i][j]==0) continue;//0表示未评分，跳过
                //修改 i用户 对 id商品 的评分
                cf.updateUserScoreOfItem(i,id,matrix[i][j]);
            }
        }
        //获取id为0的商品的大小为4的相似商品列表
        int[] itemList = cf.Similar(0,4);

        System.out.print("Similar with item 0:[");
        for (int i=0; i<4; i++)
            System.out.print(itemList[i]+",");
        System.out.println("]");
    }
}

# 基于协同过滤的推荐算法

## 示例说明

### sample_user：用户协同过滤

初始化聚类器

```java
import com.recommend.UserBaseCF;
//创建用户协同过滤聚类器
UserBaseCF cf = new UserBaseCF();
```

添加用户并导入打分数据

```java
        //原始用户评分数据
        double[][] matrix={
                {10,9,7,0,0},
                {8,1,0,0,0},
                {0,0,7,8,9},
                {0,0,0,8,9},
                {10,0,0,0,10},
        };
		//导入数据
        for (int i=0; i<5; i++){
            int uid = cf.addUser();
            for (int j=0; j<5; j++){
                if (matrix[i][j]==0) continue;//0表示未评分，跳过
                //修改 uid用户 对 j商品 的评分
                cf.updateItemScoreOfUser(uid,j,matrix[i][j]);
            }
        }
```


查询用户的近邻

```java
import com.recommend.Neighbor;
//获取用户近邻（测试用）
Neighbor[][] Neighbor = cf.getNeighbor();
```

获取用户的推荐物品列表

```java
//获取uid为4的用户的大小为4的推荐商品列表
int[] itemlist = cf.Recommending(4,3);
```

### sample_item：物品协同过滤

初始化聚类器

```java
import com.recommend.ItemBaseCF;
//创建物品协同过滤聚类器
ItemBaseCF cf = new ItemBaseCF();
```

添加物品并导入打分数据

```java
        //原始用户评分数据
        double[][] matrix={
                {10,9,7,0,0},
                {8,1,0,0,0},
                {0,0,7,8,9},
                {0,0,0,8,9},
                {10,0,0,0,10},
        };
        //导入数据
        for (int j=0; j<5; j++){
            int id = cf.addItem();
            for (int i=0; i<5; i++){
                if (matrix[i][j]==0) continue;//0表示未评分，跳过
                //修改 i用户 对 id商品 的评分
                cf.updateUserScoreOfItem(i,id,matrix[i][j]);
            }
        }
```

获取某商品的相似商品列表

```java
//获取id为0的商品的大小为4的相似商品列表
int[] itemList = cf.Similar(0,4);
```



## 接口文档

### UserBaseCF

创建聚类器  

| UserBaseCF() |            |            |
| ------------ | ---------- | ---------- |
| return       | UserBaseCF | 聚类器对象 |

添加用户  


| addUser() |      |          |
| --------- | ---- | -------- |
| return    | int  | 该用户id |


添加/修改用户打分

| updateItemScoreOfUser(UserID, ItemID, ItemScore) |        |                    |
| ------------------------------------------------ | ------ | ------------------ |
| UserID                                           | int    | 用户id             |
| ItemID                                           | int    | 物品id             |
| ItemScore                                        | double | 用户对该物品的打分 |
| return                                           | int    | 0：成功，-1：失败  |


批量添加/修改用户打分

| updateItemScoreOfUser(UserID, ScoreMap) |                      |                                                |
| --------------------------------------- | -------------------- | ---------------------------------------------- |
| UserID                                  | int                  | 用户id                                         |
| ScoreMap                                | Map<Integer, Double> | 用户打分表，键值：物品id，值：用户对该物品打分 |
| return                                  | int                  | 0：成功，-1：失败                              |


获取用户的推荐物品列表

| Recommending(userID,  size) |       |                              |
| --------------------------- | ----- | ---------------------------- |
| userID                      | int   | 用户id                       |
| size                        | int   | 推荐列表大小                 |
| return                      | int[] | 物品id的列表（按推荐度排序） |

### ItemBaseCF

创建聚类器  

| ItemBaseCF() |            |            |
| ------------ | ---------- | ---------- |
| return       | ItemBaseCF | 聚类器对象 |

添加物品  

| addUser() |      |          |
| --------- | ---- | -------- |
| return    | int  | 该物品id |

添加/修改用户打分

| updateUserScoreOfItem(ItemID, UserID, UserScore) |        |                    |
| ------------------------------------------------ | ------ | ------------------ |
| ItemID                                           | int    | 物品id             |
| UserID                                           | int    | 用户id             |
| ItemScore                                        | double | 物品被该用户的打分 |
| return                                           | int    | 0：成功，-1：失败  |

批量添加/修改用户打分

| updateUserScoreOfItem(ItemID, ScoreMap) |                      |                                                |
| --------------------------------------- | -------------------- | ---------------------------------------------- |
| ItemID                                  | int                  | 物品id                                         |
| ScoreMap                                | Map<Integer, Double> | 物品打分表，键值：用户id，值：物品被用户的打分 |
| return                                  | int                  | 0：成功，-1：失败                              |

获取用户的推荐物品列表

| Similar(ItemID,  size) |       |                              |
| ---------------------- | ----- | ---------------------------- |
| ItemID                 | int   | 物品id                       |
| size                   | int   | 相似物品列表大小             |
| return                 | int[] | 物品id的列表（按相似度排序） |

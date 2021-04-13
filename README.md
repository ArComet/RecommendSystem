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

### sample_item：物品协同过滤初始化聚类器

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
| return       | 聚类器对象 | UserBaseCF |

添加用户  


| addUser() |          |      |
| --------- | -------- | ---- |
| return    | 该用户id | int  |


添加/修改用户打分

| updateItemScoreOfUser(UserID, ItemID, ItemScore) |                    |        |
| ------------------------------------------------ | ------------------ | ------ |
| UserID                                           | 用户id             | int    |
| ItemID                                           | 物品id             | int    |
| ItemScore                                        | 用户对该物品的打分 | double |
| return                                           | 0：成功，-1：失败  | int    |


批量添加/修改用户打分

| updateItemScoreOfUser(UserID, ScoreMap) |                                                |                      |
| --------------------------------------- | ---------------------------------------------- | -------------------- |
| UserID                                  | 用户id                                         | int                  |
| ScoreMap                                | 用户打分表，键值：物品id，值：用户对该物品打分 | Map<Integer, Double> |
| return                                  | 0：成功，-1：失败                              | int                  |


获取用户的推荐物品列表

| Recommending(userID,  size) |                              |       |
| --------------------------- | ---------------------------- | ----- |
| userID                      | 用户id                       | int   |
| size                        | 推荐列表大小                 | int   |
| return                      | 物品id的列表（按推荐度排序） | int[] |

### ItemBaseCF

创建聚类器  

| ItemBaseCF() |            |            |
| ------------ | ---------- | ---------- |
| return       | 聚类器对象 | ItemBaseCF |

添加物品  

| addUser() |          |      |
| --------- | -------- | ---- |
| return    | 该物品id | int  |

添加/修改用户打分

| updateUserScoreOfItem(ItemID, UserID, UserScore) |                    |        |
| ------------------------------------------------ | ------------------ | ------ |
| ItemID                                           | 物品id             | int    |
| UserID                                           | 用户id             | int    |
| ItemScore                                        | 物品被该用户的打分 | double |
| return                                           | 0：成功，-1：失败  | int    |

批量添加/修改用户打分

| updateUserScoreOfItem(ItemID, ScoreMap) |                                                |                      |
| --------------------------------------- | ---------------------------------------------- | -------------------- |
| ItemID                                  | 物品id                                         | int                  |
| ScoreMap                                | 物品打分表，键值：用户id，值：物品被用户的打分 | Map<Integer, Double> |
| return                                  | 0：成功，-1：失败                              | int                  |

获取用户的推荐物品列表

| Similar(ItemID,  size) |                              |       |
| ---------------------- | ---------------------------- | ----- |
| ItemID                 | 物品id                       | int   |
| size                   | 相似物品列表大小             | int   |
| return                 | 物品id的列表（按相似度排序） | int[] |



# 机器学习算法——协同过滤推荐算法

## 推荐系统的概念

推荐系统(Recommendation System, RS)，简单来说就是根据用户的日常行为，预测用户的喜好，为用户提供更多完善的服务。举个简单的例子，在天猫商城，我们浏览一本书之后，系统会为我们推荐购买了这本书的其他用户购买的其他的书籍。

推荐系统在很多方面都有很好的应用，尤其在现在的个性化方面发挥着重要的作用。

## 推荐算法的分类

推荐算法使用了一系列不同的技术，主要可以分为以下两类：

- 基于内容（content-based）的推荐：主要依据的是推荐项的性质，适合**用户焦点比较集中**的使用场景，比如垂直领域的内容推送。（非社交系统）
- 基于协同过滤（collaborative filtering）的推荐：主要是依据用户或者项之间的相似性，比较适合**给用户带点新鲜的东西**的使用场景，比如逛淘宝。（社交系统）
  - 基于项的协同过滤推荐：主要是依据项与项之间的相似性。
  - 基于用户的协同过滤推荐：主要是依据用户与用户之间的相似性。

## 相似度的度量方法

相似度的度量方法主要有以下三种：

- **欧式距离**
  欧式距离是使用较多的相似性的度量方法，在kMeans中就使用到欧式距离作为相似项的发现。 
- **皮尔逊相关系数(Pearson Correlation)**
  皮尔逊相似性的度量对量级不敏感。
- **余弦相似度(Cosine Similarity)**
  计算两个向量夹角的余弦，余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，夹角等于0，说明两个向量相等，这就叫余弦相似性。

## UserCF的原理（基于用户的协同过滤推荐）

1. 分析各个用户对 item 的评价（衡量的维度可以是浏览记录、购买记录等），列出用户和 item 之间的矩阵。
2. 依据用户对 item 的评价计算得出所有用户之间的相似度（采用余弦相似度度量公式）。
3. 选出与当前用户最相似的 N 个用户。
4. 将这 N 个用户评价最高并且当前用户又没有浏览过的 item 推荐给当前用户。

## ItemCF的原理（基于项的协同过滤推荐）

1. 分析各个用户对 item 的浏览记录，列出 item 和用户之间的矩阵。
2. 依据浏览记录了分析得出所有 item 之间的相似度（任然采用余弦相似度度量公式）。
3. 对于当前用户正在浏览的 item，找出与之相似度最高的item。
4. 将这 N 个 item 推荐给用户。

## 基于内容的推荐算法的原理

1. 分析用户和 item 之间的属性（例如：用户喜欢买鼠标买外设，某个商品也具有外设这个属性）。
2. 把用户和 item 所拥有的属性分解成两个向量，计算这两个向量的相似度。
3. 找出用户和 item 之间相似度最高的前 N 个 item。
4. 将这个 N 个 item推荐给用户。
   用户和 item 分解成的向量如图：



## 参考实例

基于用户的协同过滤电影推荐算法

https://github.com/nareoivy/CollaborativeFiltering

基于用户的协同过滤算法实现的商品推荐系统

https://github.com/MrQuJL/product-recommendation-system



## Mahout机器学习框架

Mahout http://mahout.apache.org/

mahout 详解 https://blog.csdn.net/qq_36864672/article/details/78796295

Mahout 个性化推荐系统 https://www.jianshu.com/p/db3d1a983774

Mahout（机器学习引擎）https://blog.csdn.net/qq_31641743/article/details/106720404

基于Mahout的图书推荐系统 https://github.com/acumen1005/Recommendation

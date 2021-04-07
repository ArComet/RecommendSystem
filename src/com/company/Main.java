package com.company;

public class Main {
    public static void main(String args[]){
        double[][] matrix={
                {1,2,3,4,5},
                {1,4,3,2,1},
                {1,4,3,2,1},
        };
        UserBaseCF cf=new UserBaseCF(matrix);
        double predictRes = cf.predict(2,4);
        System.out.println(predictRes);

    }
}

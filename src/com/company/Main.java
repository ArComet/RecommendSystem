package com.company;

import java.io.*;

public class Main {
    public static final int USERSIZE=943;
    public static final int ITEMSIZE=1682;
    public static final int UN=10;//某一user的最近邻居数

    public static void main(String args[]) throws Exception{

        UserBaseCF cf=new UserBaseCF();
        if(cf.readFile("bin/ml-data_0/u1.base")){
            System.out.println("请等待，正在分析");
            cf.dealRate();//得到DealedOfRate

            cf.getNofUser();//得到NofUser
			/* test
			System.out.println(cf.rate[1][11]);
			System.out.println(cf.DealedOfRate[1][11]);
			System.out.println(cf.num[1]);
			System.out.println(cf.average[1]);
			System.out.println(cf.rate[1][10]);
			System.out.println(cf.DealedOfRate[1][10]);
		    */
            for(int i=1;i<=UN;i++){
                System.out.println(cf.NofUser[1][i].getID()+":"+cf.NofUser[1][i].getValue());
            }


            //测试
            //读文件
            File inputFile=new File("bin/ml-data_0/u1.test");
            BufferedReader reader=null;
            if(!inputFile.exists()||inputFile.isDirectory())
                throw new FileNotFoundException();
            reader=new BufferedReader(new FileReader(inputFile));

            //写文件
            File outputFile=new File("bin/testResult.txt");
            FileWriter writer=null;
            if(!outputFile.exists())
                if(!outputFile.createNewFile())
                    System.out.println("输出文件创建失败");
            writer=new FileWriter(outputFile);
            String title ="UserID"+"\t"+"ItemID"+"\t"+"OriginalRate"+"\t"+"PredictRate"+"\r\n";
            writer.write(title);
            writer.flush();
            String[] part=new String[3];
            String tmpToRead="";
            String tmpToWrite="";
            while((tmpToRead=reader.readLine())!=null){
                part=tmpToRead.split("\t");
                int userID=Integer.parseInt(part[0]);
                int itemID=Integer.parseInt(part[1]);
                double originalRate=Double.parseDouble(part[2]);
                double predictRate=cf.predict(userID, itemID);
                cf.x.add(originalRate);
                cf.y.add(predictRate);
                tmpToWrite=userID+"\t"+itemID+"\t"+originalRate+"\t"+predictRate+"\r\n";
                writer.write(tmpToWrite);
                writer.flush();
            }
            System.out.println("分析完成，请打开工程目录下bin文件夹中的testResult.txt");
            System.out.println("利用RMSE分析结果为"+cf.analyse(cf.x, cf.y));

        }
        else
            System.out.println("失败");

    }
}

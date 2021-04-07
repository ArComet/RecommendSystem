package com.company;

import java.io.*;

public class Main {
    public static final int USERSIZE=943;
    public static final int ITEMSIZE=1682;
    public static final int UN=10;//ĳһuser������ھ���

    public static void main(String args[]) throws Exception{

        UserBaseCF cf=new UserBaseCF();
        if(cf.readFile("bin/ml-data_0/u1.base")){
            System.out.println("��ȴ������ڷ���");
            cf.dealRate();//�õ�DealedOfRate

            cf.getNofUser();//�õ�NofUser
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


            //����
            //���ļ�
            File inputFile=new File("bin/ml-data_0/u1.test");
            BufferedReader reader=null;
            if(!inputFile.exists()||inputFile.isDirectory())
                throw new FileNotFoundException();
            reader=new BufferedReader(new FileReader(inputFile));

            //д�ļ�
            File outputFile=new File("bin/testResult.txt");
            FileWriter writer=null;
            if(!outputFile.exists())
                if(!outputFile.createNewFile())
                    System.out.println("����ļ�����ʧ��");
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
            System.out.println("������ɣ���򿪹���Ŀ¼��bin�ļ����е�testResult.txt");
            System.out.println("����RMSE�������Ϊ"+cf.analyse(cf.x, cf.y));

        }
        else
            System.out.println("ʧ��");

    }
}

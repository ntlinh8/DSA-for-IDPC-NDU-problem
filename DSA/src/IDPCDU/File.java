package IDPCDU;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class File {
	public static int TASK_NUM = 1;
	public static int SRC;                       //begin node
    public static int DES;                       //end node
    public static int srcDomain, desDomain;		 //begin domain and end domain
	public static int NODES_NUM;                 //tong so node
    public static int DOMAINS_NUM;               //tong so domain
    public static int [][] matrixEdge;           // canh giua node i va j la matrixEdge[i][j]
    //public static Domain[] domainList;			// ma tran domain trong do index = domain number
    public static List<Domain> domainList = new ArrayList<Domain>();//domain thu i thi o vi tri thu i - 1
    public static List<Edge> edgeList = new ArrayList<Edge>();
	public File() {
		// TODO Auto-generated constructor stub
	}
	
	public static void readFile(String s) {
		BufferedReader bReader;
        int counter = 0;
        
        try  {
        	bReader = new BufferedReader(new FileReader("/Users/admin/Documents/Java/DSA/src/IDPCDU/" + s + ".txt"));
        	String line = bReader.readLine();
        	
        	while(line != null){
        		if (counter == 0) {
        			String [] splitedString= line.split("\\s+");
        			NODES_NUM = Integer.parseInt(splitedString[0]);
        			matrixEdge = new int [NODES_NUM+1][NODES_NUM+1];
        			DOMAINS_NUM = Integer.parseInt(splitedString[1]);
        			//System.out.print("Domain number " + DOMAINS_NUM);
        			//domainList = new Domain[DOMAINS_NUM+1];
        			//domainList2 = new ArrayList<Domain>(DOMAINS_NUM);
        			/*for(int i = 0; i < domainList.length; i++) {
        				System.out.print(domainList[i].nameDomain + " ");
        				
        			}
        			System.out.println(" ");*/
        
                    counter++;
                    line = bReader.readLine();
                    //System.out.println("NODES_NUM " + NODES_NUM);
                    //System.out.println("DOMAINS_NUM " + DOMAINS_NUM);
                    //System.out.println(" ");

        		}
        		 else if (counter == 1){
                     String [] splitedString= line.split("\\s+");
                     SRC = Integer.parseInt(splitedString[0]);
                     DES = Integer.parseInt(splitedString[1]);
                     counter++;
                     line = bReader.readLine();
                     //System.out.println("SRC " +SRC);
                     //System.out.println("DES " + DES);
                     //System.out.println(" ");
        		 }else if (counter < (DOMAINS_NUM + 2)) {
        			 String [] splitedString= line.split("\\s+");
        			 int [] nodeList = new int [splitedString.length];
        			 //System.out.print("Domain: "+ (counter - 1) + " ");
        			 //System.out.print("Counter: " + counter);
        			 //System.out.println("splitedString.length " + splitedString.length);
        			 for(int i = 0; i < splitedString.length; i++) {
        				 nodeList[i] = Integer.parseInt(splitedString[i]);
        				 //System.out.print(nodeList[i] + " ");
        				 if(nodeList[i] == SRC) {
        					 srcDomain = counter -1;
        					 //System.out.print("SrcDomain: " + srcDomain);
        				 } else if(nodeList[i] == DES) {
        					 desDomain = counter -1;
        					 //System.out.print("DesDomain: " + desDomain);
        				 }
        			 }
        			 Domain d = new Domain(counter -1, nodeList);//domain 1 o vi tri 0
        			 //System.out.print("numbename " + d.nameDomain +" ");
        			 
        			 domainList.add(d);
        			 //System.out.print(domainList.get(0).nameDomain+ " ");
        			 counter++;
        			 line = bReader.readLine();   
                     
        			 
        		 }else {
        			 int u, t, w;
                     String [] splitedString= line.split("\\s+");
                     u = Integer.parseInt(splitedString[0]);
                     //System.out.print(u + " ");
                     t = Integer.parseInt(splitedString[1]);
                     //System.out.print(d + " ");
                     w = Integer.parseInt(splitedString[2]);
                     //System.out.print(w + " ");
                     //System.out.println(" ");
                     matrixEdge[u][t] = w;
                     Edge edge = new Edge (u, t, w);
                     edgeList.add(edge);
                     counter++;
                     line = bReader.readLine();           
        		 }
        	}
        	bReader.close();
        }catch(Exception e) {
        	System.out.println("Loi doc file");
        	e.printStackTrace();
        }
        
        /*for(int i = 1; i < NODES_NUM+1; i++) {
       	 for(int j = 1; j < NODES_NUM+1; j++) {
       		 System.out.print(matrixEdge[i][j]+" ");
       	 }
       	 System.out.println(" ");
        }*/
	}

	
	
	
	

}

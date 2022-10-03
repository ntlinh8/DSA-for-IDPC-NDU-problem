package IDPCDU;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class File {
	public static int TASK_NUM = 1;
	public static int SRC;                       					//begin node
    public static int DES;                       					//end node
    public static int srcDomain, desDomain;		 					//begin domain and end domain
	public static int NODES_NUM;                 					//tong so node
    public static int DOMAINS_NUM;             					    //tong so domain
    public static int [][] matrixEdge;           					// canh giua node i va j la matrixEdge[i][j]
    //public static Domain[] domainList;							// ma tran domain trong do index = domain number
    public static List<Domain> domainList = new ArrayList<Domain>();	//Domain bắt đầu từ d0 -> dn. Domain đầu tiên là d0 ở vị trí 0
    public static List<Edge> edgeList = new ArrayList<Edge>();
	public File() {
		// TODO Auto-generated constructor stub
	}
	
	public static void readFile(String s) {
		BufferedReader bReader;
        int counter = 0;
        
        try  {
        	bReader = new BufferedReader(new FileReader("C:\\Users\\GDCV\\Documents\\git\\DSA-IDPCNDU\\DSA\\src\\IDPCDU\\" + s + ".txt"));
        	String line = bReader.readLine();
        	
        	while(line != null){
        		if (counter == 0) {
        			String [] splitedString= line.split("\\s+");
        			NODES_NUM = Integer.parseInt(splitedString[0]);
        			matrixEdge = new int [NODES_NUM+1][NODES_NUM+1];
        			DOMAINS_NUM = Integer.parseInt(splitedString[1]);
                    counter++;
                    line = bReader.readLine();
                    
        		}else if (counter == 1){
                     String [] splitedString= line.split("\\s+");
                     SRC = Integer.parseInt(splitedString[0]);
                     DES = Integer.parseInt(splitedString[1]);
                     counter++;
                     line = bReader.readLine();
                     
        		 }else if (counter < (DOMAINS_NUM + 2)) {
        			 String [] splitedString= line.split("\\s+");
        			 int [] nodeList = new int [splitedString.length];
        			 for(int i = 0; i < splitedString.length; i++) {
        				 nodeList[i] = Integer.parseInt(splitedString[i]);
        				 if(nodeList[i] == SRC) {
        					 srcDomain = counter -1;
        				 } else if(nodeList[i] == DES) {
        					 desDomain = counter -1;
        				 }
        			 }
        			 Domain d = new Domain(counter -1, nodeList);
        			 domainList.add(d);
        			 counter++;
        			 line = bReader.readLine();   
        		 }else {
        			 int u, t, w;
                     String [] splitedString= line.split("\\s+");
                     u = Integer.parseInt(splitedString[0]);
                     t = Integer.parseInt(splitedString[1]);
                     w = Integer.parseInt(splitedString[2]);
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
	}

	
	
	
	

}

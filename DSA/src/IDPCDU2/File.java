package IDPCDU2;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class File {
	public static int TASK_NUM = 1;
	public static int SRC, DES, srcDomain, desDomain, NODES_NUM, DOMAINS_NUM;
    public static int [][] matrixNodeEdge, matrixDomainEdge;            // canh giua node i va j la matrixEdge[i][j]
    public static List<Domain> domainList = new ArrayList<Domain>();	//domain thu i thi o vi tri thu i - 1
    public static List<Edge> edgeList = new ArrayList<Edge>();
	public File() {
		
	}
	
	public static void readFile(String s) {
		BufferedReader bReader;
        int counter = 0;
        
        try  {
        	bReader = new BufferedReader(new FileReader("C:\\Users\\GDCV\\Documents\\git\\DSA-IDPCNDU\\DSA\\src\\IDPCDU2\\testdata\\" + s + ".txt"));
        	String line = bReader.readLine();
        	
        	while(line != null){
        		if (counter == 0) {
        			String [] splitedString= line.split("\\s+");
        			NODES_NUM = Integer.parseInt(splitedString[0]);
        			matrixNodeEdge = new int [NODES_NUM+1][NODES_NUM+1];
        			DOMAINS_NUM = Integer.parseInt(splitedString[1]);
        			matrixDomainEdge = new int [DOMAINS_NUM][DOMAINS_NUM];
                    counter++;
                    line = bReader.readLine();
        		}
        		 else if (counter == 1){
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
        					 srcDomain = counter -2;
        				 } else if(nodeList[i] == DES) {
        					 desDomain = counter -2;
        				 }
        			 }
        			 Domain d = new Domain(counter -2, nodeList);
        			 domainList.add(d);
        			 counter++;
        			 line = bReader.readLine();   
        			 
        		 }else {
        			 int u, t, w;
                     String [] splitedString= line.split("\\s+");
                     u = Integer.parseInt(splitedString[0]);
                     t = Integer.parseInt(splitedString[1]);
                     w = Integer.parseInt(splitedString[2]);
                     matrixNodeEdge[u][t] = w;
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
        
        /*
         * Hàm này nhằm mục đích tạo ra một graph đơn giản mô tả đường đi giữa các miền
         * Cụ thể: Giữa 2 miền d1, d2 có đường đi nếu matrixDomainEdge[d1][d2] = 1
         */
        for(int i = 0; i < File.DOMAINS_NUM-1; i++) {
        	Domain d1 = domainList.get(i);
        	int domainNumber1 = d1.nameDomain;
        	int[] nodeList1 = d1.nodeList;
        	
        	for(int j = 0; j < File.DOMAINS_NUM; j++) {
        		Domain d2 = domainList.get(j);
        		int domainNumber2 = d2.nameDomain;
        		int[] nodeList2 = d2.nodeList;
        		
        		for(int k = 0; k < nodeList1.length; k++) {
        			for(int l = 0; l < nodeList2.length; l++) {
        				int node1 = nodeList1[k];
        				int node2 = nodeList2[l];
        				int value = matrixNodeEdge[node1][node2];
        				
        				if(value != 0) {
        					matrixDomainEdge[domainNumber1][domainNumber2] = 1;
        					l = nodeList2.length;
        					k = nodeList1.length;
        				}
        			}
        		}
        	}
        }
        
        
	}

	
	
	
	

}

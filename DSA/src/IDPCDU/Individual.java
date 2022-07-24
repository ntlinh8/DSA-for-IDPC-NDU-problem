package IDPCDU;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;



public class Individual {
	public int[] individual; //tinh tu vi tri so 0, domain co gia tri tu 1 - domain number
	public static Random random = new Random();
	public int soVongLap = 10;
	public boolean isDeCode = true;
	//public List<Integer> nodeList = new ArrayList<Integer>() ;
	
	public Individual() {
		int length = random.nextInt(2, File.DOMAINS_NUM+1);
		
		//int length = 2;
		this.individual = new int[length];
		
		this.individual[0] = File.srcDomain;
		this.individual[length-1] = File.desDomain;
		
		if(length != 2) {
			
			List<Domain> d = new ArrayList<Domain>(File.DOMAINS_NUM - 2);
			//System.out.print("Size" + File.domainList.size());
			for(int i = 0; i < File.domainList.size(); i++) {
				if(File.domainList.get(i).nameDomain != File.srcDomain && File.domainList.get(i).nameDomain != File.desDomain) {
					d.add(File.domainList.get(i));
				}	
			}
			//System.out.print("Size" + d.size());
			
			
			for(int i = 0; i < length-2 ; i++) {
				if(!d.isEmpty()) {
					int index = random.nextInt(0, d.size());;
					this.individual[i+1] = d.get(index).nameDomain;
						//System.out.println("Remove " + d.get(index).nameDomain);
					d.remove(index);
				}
			}
		}
		
		
		
		
		this.isDeCode = true;
		
		/*System.out.print("I: ");
		for(int i = 0; i < length; i++ ) {
			System.out.print(this.individual[i] + " ");
		}
		System.out.println(" ");*/
	}
	
	
	
	public Individual(Individual c) {
		this.isDeCode = c.isDeCode;
		this.individual = new int[c.individual.length];
		for(int i = 0; i < c.individual.length; i++) {
			this.individual[i] = c.individual[i];
		}
	}
	
	
	
	
	public static List findEdgeOutDomain(Domain d1, Domain d2) {
		//int a = File.domainList.indexOf(d1);
		//int b = File.domainList.indexOf(d2);
		List<Edge> edgeOut = new ArrayList<Edge>();
		for(int i = 0; i < d1.nodeList.length; i++) {
			for(int j = 0; j< d2.nodeList.length; j++) {
				if(File.matrixEdge[d1.nodeList[i]][d2.nodeList[j]] != 0) {
					Edge edge = new Edge(d1.nodeList[i],d2.nodeList[j],File.matrixEdge[i][j]);
					edgeOut.add(edge);
					//System.out.print("Edge: " + d1.nodeList[i] + "-" + d2.nodeList[j] + ": " + File.matrixEdge[d1.nodeList[i]][d2.nodeList[j]] + " ||");
				}
			}
		}
		//System.out.print("Edge: " + );
		return edgeOut;
	}
	
	
	public static boolean CheckDeCode(Individual c) {
		c.isDeCode = true;
		List<Edge> edge = new ArrayList<Edge>();
		for(int i = 0; i < (c.individual.length - 1); i++) {
			edge = findEdgeOutDomain(File.domainList.get(c.individual[i]-1),File.domainList.get(c.individual[i+1]-1) );
			if(edge.isEmpty()) {
				c.isDeCode = false;
				break;
			}
		}
		
		return c.isDeCode;
		
	}
	
	public static int DeCode(Individual c) {
		int src = File.SRC;
		int totalCost = 0;
		for(int i = 0; i < (c.individual.length-1); i++) {
			Domain d1 = File.domainList.get(c.individual[i]-1);
			Domain d2 = File.domainList.get(c.individual[i+1]-1);
			int des = 0;
			//System.out.println(d1.nameDomain + "-" + d2.nameDomain + " ");
			/*if(d1.nameDomain == File.srcDomain) {
				src = File.SRC;
				//System.out.println("start: " +src);	
			}else{
				//System.out.println("start: " +src);
			}*/
			//System.out.println(d1.nameDomain + "-" + d2.nameDomain + " ");
			//Tim cac canh di tu d1 sang d2
			List<Edge> edge = findEdgeOutDomain(d1, d2);
			
			
			if(edge.isEmpty()) {
				return totalCost = 0;
			}else {
				//random ra 1 canh bat ky trong cac canh di tu d1 sang d2 de tinh toan
				
				int cost = Integer.MAX_VALUE;
				//int r = random.nextInt(edge.size());
				
				int desI = 0;
				for(int k = 0 ; k < edge.size(); k++) {
					desI = edge.get(k).des;
					int arrayValue[] = FindShortPathUsingDijkstra(d1, src, desI);
					int costI = arrayValue[arrayValue.length-1];
					
					if(costI < cost ) {
						cost = costI;
						des = desI;
						//System.out.println("end: " + des);
					}
				}
				
				
				
				/*des = edge.get(r).des;
				//System.out.println("end: " + des);
					
				int arrayValue[] = FindShortPathUsingDijkstra(d1, src, des);
				cost = arrayValue[arrayValue.length-1];
				*/
				
				
				if(cost != Integer.MAX_VALUE) {
				//System.out.print("TOTAL TotalCost = " + totalCost + " + "+ cost + " = ");
				totalCost = totalCost + cost;
						
				//System.out.println(totalCost);
				//System.out.println(" ");
				src = des;
				
				//break;
				}else {
					totalCost = Integer.MAX_VALUE;
					break;
				}

			}
			while(totalCost == Integer.MAX_VALUE) {
				totalCost = c.DeCode(c);
				
			}
		}
		if(totalCost != Integer.MAX_VALUE) {
			c.isDeCode = true;
		}
		return totalCost;
	}
	
	/*
	public static void Decode1(Individual c) {
		int src = File.SRC;
		int totalCost = 0;
		for(int i = 0; i < (c.individual.length-1); i++) {
			Domain d1 = File.domainList.get(c.individual[i]-1);
			Domain d2 = File.domainList.get(c.individual[i+1]-1);
			int des = 0;
			List<Edge> edge = findEdgeOutDomain(d1, d2);
			
		}
	}*/
	
	
	
	public static int CalculateFitness(Individual c) {
		int totalCost = 0;
		if(c.CheckDeCode(c) == true) {
			
			int src = File.SRC;
			
			
			for(int i = 0 ; i < c.individual.length-1; i++) {
				Domain d1 = File.domainList.get(c.individual[i]-1);
				Domain d2 = File.domainList.get(c.individual[i+1]-1);
				
				int des = 0;
				int cost = Integer.MAX_VALUE;
				
				List<Edge> edge = findEdgeOutDomain(d1, d2);
				
				int desI = 0;
				for(int k = 0 ; k < edge.size(); k++) {
					desI = edge.get(k).des;
					int arrayValue[] = FindShortPathUsingDijkstra(d1, src, desI);
					int costI = arrayValue[arrayValue.length-1];
					
					if(costI < cost ) {
						cost = costI;
						des = desI;
						//System.out.println("end: " + des);
					}
				}
				
				totalCost = totalCost + cost;
			}
		}
		return totalCost;
	}
	
	/*public static void CalculateBetweenDomain(Domain d1, Domain d2) {
		
		int des;
		//System.out.println(d1.nameDomain + "-" + d2.nameDomain + " ");
		if(d1.nameDomain == File.srcDomain) {
			src = File.SRC;
			//System.out.println("start: " +src);	
		}else{
			//System.out.println("start: " +src);
		}
		System.out.println(d1.nameDomain + "-" + d2.nameDomain + " ");
		//Tim cac canh di tu d1 sang d2
		List<Edge> edge = findEdgeOutDomain(d1, d2);
		
		//random ra 1 canh bat ky trong cac canh di tu d1 sang d2 de tinh toan
		
		int cost =0;
		int r = random.nextInt(edge.size());
		des = edge.get(r).des;
		//System.out.println("end: " + des);
			
		int arrayValue[] = FindShortPathUsingDijkstra(d1, src, des);
		cost = arrayValue[arrayValue.length-1];
		
		}*/
	
	public static int[] FindShortPathUsingDijkstra(Domain d, int src, int des) {
		int [] nodeList1 = new int [d.nodeList.length+1];
		nodeList1[d.nodeList.length] = des;
		for(int i = 0; i < (nodeList1.length -1); i++) {
			nodeList1[i] = d.nodeList[i];
			
		}
		//Khoi tao mang khoang cach ngan nhat cuoi cung
		int[] distance = new int[d.nodeList.length + 1];
		
		//Mang cho biet khoang cach ngan nhat da duoc tim ra hay chua
		boolean[] visited = new boolean[d.nodeList.length + 1];
		
		
		//Khoi tao cac mang
		for(int i=0; i< (d.nodeList.length + 1); i++)
		{
			distance[i] = Integer.MAX_VALUE;//initial distance is infinite
			visited[i] = false;//shortest distance for any node has not been found yet
		}
		
		//Tim ra vi tri cua src trong mang cac node cua domain
		int indexSrc = getIndex(d, src);
		int indexDes = getIndex(d, des);
		
		
		//Gan khoang cach toi chinh no la 0
		distance[indexSrc] = 0;
		//System.out.print("Length: " + (d.nodeList.length + 1));
		//System.out.print("Src Index: " + indexSrc);
		for(int i=0; i< (d.nodeList.length + 1); i++)
		{
			int closestVertex = getClosestVertex(distance, visited);//get the closest node
			//System.out.println("closestVertex: "+ closestVertex);
			
			//if closest node is infinite distance away, it means that no other node can be reached. So 
	        		
			if(distance[closestVertex] == Integer.MAX_VALUE) {	
				//System.out.print("Distance dung = " + nodeList1[closestVertex]);
				//System.out.print("Cost: " + distance[closestVertex]);
				return distance;
				
			}
			visited[closestVertex] = true;
				
			
			for(int j=0; j< (d.nodeList.length + 1); j++)
			{
				if(visited[j] == false)//shortest distance of the node j should not have been finalized
				{
					if(File.matrixEdge[nodeList1[closestVertex]][nodeList1[j]] != 0)
					{
						int dis = distance[closestVertex] + File.matrixEdge[nodeList1[closestVertex]][nodeList1[j]];
						if(dis < distance[j])//distance via closestVertex is less than the initial distance
							distance[j] = dis;
					}
				}		
			}
		}
		
		/*for(int i=0; i< (d.nodeList.length + 1); i++)
		{
			System.out.print("Distance: " + distance[i] +" ");
		
			
		}*/
		return distance;
	
		//return distance[distance.length-1];
		
	}
		
	
		public static int getClosestVertex(int[] distance, boolean[] visited)
		{
			int min = Integer.MAX_VALUE;
			int minIdx = 0;
			for(int i=0; i<distance.length; i++)
			{
				if(distance[i] < min)
					if(visited[i] == false)
					{
						min = distance[i];
						minIdx = i;
					}
			}
			return minIdx;
		}
		
		public static int getIndex(Domain d, int nodeNumber) {
			int indexSrc = 0;
			for(int i = 0; i <d.nodeList.length; i++ ) {
				if(d.nodeList[i] == nodeNumber) {
					indexSrc = i;
				}
			}
			return indexSrc;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

		/*public static int FindMin(int [] a, List<Edge> edgeList) {
		int min = Integer.MAX_VALUE;
		//List minlist = new List();
		int index = 0;
		for(int i = 0; i < a.length; i++) {

			if(a[i] != 0 && CheckEdge(i,edgeList) ==true &&  a[i] < min) {
				min = a[i];
				index = i;
			}
		}
		
		return index;
	}
	
	public static boolean CheckEdge(int number, List<Edge> list) {
		boolean result = true;
		for(Edge e : list) {
			if(e.des == number) {
				result = false;
			}
		}
		return result;
	}
	
	public static boolean CheckEdgeInDomain(int src, Domain d, int des) {
		boolean result = false;
		for(int i = 0; i < d.nodeList.length; i++) {
			if(File.matrixEdge[src][d.nodeList[i]] != 0) {	
			}
		}
		return result;
	}*/
	
		public static List<Integer> HammingDistance(Individual c1, Individual c2) {
			List<Integer> difLocation = new ArrayList<Integer>();
			for(int i = 0; i < c1.individual.length; i++) {
				if(c1.individual[i] != c2.individual[i]) {
					difLocation.add(i);	
				}
			}
			return difLocation;
		}
		
		public static void PrintIndividual(Individual c) {
			System.out.print("I: ");
			for(int i = 0; i < c.individual.length; i++ ) {
				System.out.print(c.individual[i] + " ");
			}
			System.out.println(" ");
		}
	

}
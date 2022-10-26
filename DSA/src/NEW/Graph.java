package NEW;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Graph {
    public static int n;   // n : số đỉnh
    public static int d;    // d : số miền
    public int[][] weight;   //   ma tran trọng số
    public static int startNode;  // Dinh bat dau
    public static int endNode;   // Dinh ket thuc
    public static int startDomain;  // Mien bat dau
    public static int endDomain; // Mien ket thuc
    public ArrayList<int[]> listDomain;  //  Danh sách các đỉnh thuộc miền tương ứng
    public int[] domainOfNode;  // domainOfNode[i] = j neu dinh i thuoc mien j
    public int[][] isConnectDomain;
    public static int best_finess = Integer.MAX_VALUE;

    public Graph (String path) {   //  path : Duong dan file
        BufferedReader bReader;
        int counter = 0;

        try  {
            bReader = new BufferedReader(new FileReader(path));
            String line = bReader.readLine();

            while(line != null) {
                if (counter == 0) {
                    String [] splitedString= line.split("\\s+");
                    this.n = Integer.parseInt(splitedString[0]);
                    this.weight = new int [this.n+1][this.n+1];
                    this.domainOfNode = new int[this.n +1];
                    this.d = Integer.parseInt(splitedString[1]);
                    this.isConnectDomain = new int [this.d][this.d];
                    this.listDomain = new ArrayList<int[]>(this.d);
                    counter++;
                    line = bReader.readLine();
                }
                else if (counter == 1){
                    String [] splitedString= line.split("\\s+");
                    this.startNode = Integer.parseInt(splitedString[0]);
                    this.endNode = Integer.parseInt(splitedString[1]);
                    counter++;
                    line = bReader.readLine();
                }else if (counter < (this.d + 2)) {
                    String [] splitedString= line.split("\\s+");
                    int [] nodeList = new int [splitedString.length];
                    for(int i = 0; i < splitedString.length; i++) {
                        nodeList[i] = Integer.parseInt(splitedString[i]);
                        if(nodeList[i] == this.startNode) {
                            this.startDomain = counter -2;
                        } else if(nodeList[i] == this.endNode) {
                            this.endDomain = counter -2;
                        }
                        this.domainOfNode[nodeList[i]] = counter - 2;
                    }
                    this.listDomain.add(counter - 2, nodeList);;
                    counter++;
                    line = bReader.readLine();

                }else {
                    int u, t, w;
                    String [] splitedString= line.split("\\s+");
                    u = Integer.parseInt(splitedString[0]);
                    t = Integer.parseInt(splitedString[1]);
                    w = Integer.parseInt(splitedString[2]);
                    this.weight[u][t] = w;
                    counter++;
                    line = bReader.readLine();
                }
            }
            bReader.close();
        }catch(Exception e) {
            System.out.println("Loi doc file");
            e.printStackTrace();
        }
        
//        for(int i = 0; i < this.listDomain.size(); i++) {
//        	int[] listNode = this.listDomain.get(i);
//        	System.out.println("Domain: " + i);
//        	for(int j = 0; j < listNode.length; j++) {
//        		System.out.print(listNode[j] + " ");
//        	}
//        }
        /*
         * Hàm này nhằm mục đích tạo ra một graph đơn giản mô tả đường đi giữa các miền
         * Cụ thể: Giữa 2 miền d1, d2 có đường đi nếu matrixDomainEdge[d1][d2] = 1
         */
        for(int i = 0; i < this.d-1; i++) {
        	int domainNumber1 = i;
        	int[] nodeList1 = this.listDomain.get(i);
        	
        	for(int j = 0; j < this.d; j++) {
        		int domainNumber2 = j;
        		int[] nodeList2 = this.listDomain.get(j);
        		
        		for(int k = 0; k < nodeList1.length; k++) {
        			for(int l = 0; l < nodeList2.length; l++) {
        				int node1 = nodeList1[k];
        				int node2 = nodeList2[l];
        				int value = weight[node1][node2];
        				
        				if(value != 0) {
        					this.isConnectDomain[domainNumber1][domainNumber2] = 1;
        					l = nodeList2.length;
        					k = nodeList1.length;
        				}
        			}
        		}
        	}
        }
    }

    public int[] findOrderDomain (double[] chromosome) {
    	int[] orderDomain = new int[Graph.d];
		int startD = this.startDomain;
		int endD = this.endDomain;
		int nextDomain= FindTheNextDomain(startD, chromosome, orderDomain);
		
		for(int i = 0; i < orderDomain.length; i++) {
			//System.out.print(nextDomain +" ");
			orderDomain[i] = startD;
			nextDomain = FindTheNextDomain(startD, chromosome, orderDomain);
			if(nextDomain == endD) {
				orderDomain[i+1] = nextDomain;
				break;
			}
			startD = nextDomain;
		}
//		System.out.println("order domain: ");
//		for(int i = 0; i < orderDomain.length; i++) {
//			System.out.print(orderDomain[i] +" ");
//		}
		return orderDomain;

    }
    
    /*
	 * Hàm để tìm domain tiếp theo từ domain cho trước đối với cá thể c
	 */
	public int FindTheNextDomain(int startDomain, double[] chromosome, int [] orderDomain) {
		int nextDomain = startDomain;
		double weight = 1.0;
		for(int i = 1; i < this.d; i++) {
			int currentDomain = i;
			if(this.isConnectDomain[startDomain][currentDomain]!= 0 && chromosome[currentDomain] < weight && domainIsTraversed(currentDomain, orderDomain) == false) {
				weight = chromosome[currentDomain];
				nextDomain = currentDomain;
			}
		}
		return nextDomain;
	}
    
	
	/*
	 * Hàm verify xem domain đã được đi qua hay chưa (đã tồn tại trong mảng chứa thứ tự của domain hay chưa)
	 */
	public boolean domainIsTraversed(int domain, int[] orderDomain) {
		boolean isTraversed = false;
		for(int i = 0; i < orderDomain.length; i++) {
			if (orderDomain[i] == domain) {
				isTraversed = true;
				break;
			}
		}
		return isTraversed;
	}
	
    public int[][] buildGraph(int[] orderDomain) {
        int[][] newWeightMatrix = new int[this.n+1][this.n+1];
        for (int i = 0; i < orderDomain.length - 1; i++) {
            int d1 = orderDomain[i];
            int[] listNoded1 = this.listDomain.get(d1);

            int d2 = orderDomain[i+1];
            int[] listNoded2 = this.listDomain.get(d2);

            // them cac canh trong mien d1 vao do thi moi
            for (int j = 0; j<listNoded1.length; j++) {
                int node1 = listNoded1[j];
                for (int k = 0; k<listNoded1.length; k++) {
                    int node2 = listNoded1[k];
                    if (this.weight[node1][node2] != 0) {
                        newWeightMatrix[node1][node2] = this.weight[node1][node2];
                    }
                }
            }

            // Them cac canh di tu mien d1 sang mien d2
            for (int j = 0; j<listNoded1.length; j++) {
                int node1 = listNoded1[j];
                for (int k = 0; k<listNoded2.length; k++) {
                    int node2 = listNoded2[k];
                    if (this.weight[node1][node2] != 0) {
                        newWeightMatrix[node1][node2] = this.weight[node1][node2];
                    }
                }
            }
        }
        return newWeightMatrix;
    }

    public int DijkstraAlgorithm (int[][] weightMatrix) {
		int fitness;
		int[] fitnessList = ArrayInitialization();
		int currentNode;
		int[] nodeTraveledList = new int [this.n+1];
		do {
			currentNode = FindTheNearestNode(fitnessList, nodeTraveledList);
			if(currentNode == Integer.MAX_VALUE) {
				fitness = fitnessList[endNode];
				return fitness;	
			}
			nodeTraveledList[currentNode] = 1;
//			System.out.println("Current Node is: " + currentNode);
			fitnessList = FindNeighboringNode(weightMatrix, fitnessList, currentNode, nodeTraveledList);
//			System.out.println("NodeList is: " + currentNode);
//			for(int i = 0; i < fitnessList.length; i++) {
//				System.out.print(fitnessList[i] + " ");
//			}
			// System.out.println("while loop");
		}while(currentNode != this.endNode);
		
		fitness = fitnessList[this.endNode];
		return fitness;	
    }
    
    /*
	 * Hàm khởi tạo mảng chứa tổng quãng đường khi đi từ src node đến các node còn lại
	 * Trả về mảng chứa các node từ 0 ; N trong đó tại src node khoảng cách là 0; các node còn lại khoảng cách max
	 */
	public int[] ArrayInitialization() {
		int[] array = new int[this.n+1];
		for(int i = 0; i < array.length; i++) {
			if(i == this.startNode) {
				array[i] = 0;
			}else {
				array[i] = Integer.MAX_VALUE;
			}
		}
		return array;
	}
	
	/*
	 * Tìm ra node có khoảng cách nhỏ nhất từ src node
	 * Trả về node có khoảng cách nhỏ nhất từ src node. Trong trường hợp chạy lần đầu, hàm trả ra src node
	 */
	public int FindTheNearestNode(int[] fitnessList, int[] nodeTraveledList) {
		int minNode = Integer.MAX_VALUE;
		int node = Integer.MAX_VALUE;
		for(int i = 0; i < fitnessList.length; i++) {
			int value = fitnessList[i];
//			if(value != 0) {
				if(value < minNode && nodeTraveledList[i] != 1) {
					minNode = value;
					node = i;
				}
//			}
			
		}
		return node;
	}
	
	/*
	 * Tìm các node lân cận node hiện tại và cập nhật fitness đến các nốt đó
	 * Đầu ra là mảng fitnessList sau khi đã được cập nhật giá trị của các node
	 */
	public int[] FindNeighboringNode(int [][] g0, int [] fitnessList, int currentNode, int[] nodeTraveledList) {
		for(int i = 0; i < this.n+1; i++) {
			int value = g0[currentNode][i];
			if(value != 0) {
				int neighborNode = i;
				int fitness = value + fitnessList[currentNode];
				if(fitness < fitnessList[neighborNode] && nodeTraveledList[neighborNode] != 1) {
					fitnessList[neighborNode] = fitness;
				}
			}
		}
		return fitnessList;
	}

    public int calculateFitness (double[] chromosome) {
        int [] orderDomain = findOrderDomain(chromosome);
        int[][] newWeightMatrix = buildGraph(orderDomain);
        int fitness = DijkstraAlgorithm(newWeightMatrix);
        return fitness;
    }
    
    
    
//    /*
//	 * Hàm để tìm thứ tự của các domain dựa theo trọng số
//	 * Return: Một mảng hiển thị thứ tự của domain bắt đầu từ 0 đến domain thứ n
//	 * Trường hợp không tìm thấy đường đi từ điểm đầu đến điểm cuối thì tại mảng thứ tự domain trả ra có 2 giá trị cuối cùng bằng nhau và khác 0
//	 */
//	public static int[] FindTheOrderOfDomain(Individual c) {
//		int[] orderDomain = new int[Graph.d];
//		int startDomain = Graph.startDomain;
//		int endDomain = Graph.endDomain;
//		int nextDomain= FindTheNextDomain(startDomain, c, orderDomain);
//		
//		for(int i = 0; i < orderDomain.length; i++) {
//			orderDomain[i] = startDomain;
//			nextDomain = FindTheNextDomain(startDomain, c, orderDomain);
//			if(nextDomain == endDomain) {
//				orderDomain[i+1] = nextDomain;
//				break;
//			}
//			startDomain = nextDomain;
//		}
////		System.out.println("order domain: ");
////		for(int i = 0; i < orderDomain.length; i++) {
////			System.out.print(orderDomain[i] +" ");
////		}
//		return orderDomain;
//	}
//	
//	/*
//	 * Hàm để tìm domain tiếp theo từ domain cho trước đối với cá thể c
//	 */
//	public static int FindTheNextDomain(int startDomain, Individual c, int [] orderDomain) {
//		int nextDomain = startDomain;
//		double priority = Graph.d;
//		for(int i = 0; i < Graph.d; i++) {
//			int currentDomain = i;
//			if(File.matrixDomainEdge[startDomain][currentDomain]!= 0 && c.chromosome[currentDomain] < priority && domainIsTraversed(currentDomain, orderDomain) == false) {
//				priority = c.chromosome[currentDomain];
//				nextDomain = currentDomain;
//			}
//		}
//		return nextDomain;
//	}
//	
//	/*
//	 * Hàm verify xem domain đã được đi qua hay chưa (đã tồn tại trong mảng chứa thứ tự của domain hay chưa)
//	 */
//	public static boolean domainIsTraversed(int domain, int[] orderDomain) {
//		boolean isTraversed = false;
//		for(int i = 0; i < orderDomain.length; i++) {
//			if (orderDomain[i] == domain) {
//				isTraversed = true;
//				break;
//			}
//		}
//		return isTraversed;
//	}
//	
//	/*
//	 * Hàm verify xem thứ tự các domain có thể decode được hay không?
//	 */
//	public static boolean isDecoded(int[] orderDomain) {
//		int length = orderDomain.length;
//		int endDomain = orderDomain[length-1];
//		int subEndDomain = orderDomain[length-2];
//		boolean isDecode = true;
//		if(endDomain == subEndDomain && endDomain != 0) {
//			isDecode= false;
//		}
//		return isDecode;
//	}
//	
//	
//	/*
//	 * Hàm có nhiệm vụ xây dựng một đồ thị G0 từ G bằng cách giữ lại toàn bộ đường đi trong các miền có đi qua
//	 * Và bổ sung thêm các đường dẫn liên miền theo đúng thứ tự miền sẽ đi qua
//	 */
//	public static int[][] BuildGraph(int[] orderDomain) {
//		int [][] g0 = new int [Graph.d +1][Graph.d + 1];
//		for(int i = 0; i < orderDomain.length-1; i++) {
//			if(orderDomain[i] == Graph.endDomain) {
//				break;
//			}else {
//				//Lấy ra domain d1 tại vị trí i
//				int d1Number = orderDomain[i];
//				Domain d1 = File.domainList.get(d1Number);
//				int [] d1NodeList = d1.nodeList;
//				//System.out.println("Domain d1 is: " + d1.nameDomain);
//				
//				//Lấy ra domain d2 tại vị trí i+1
//				int d2Number = orderDomain[i+1];
//				Domain d2 = File.domainList.get(d2Number);
//				int [] d2NodeList = d2.nodeList;
//				//System.out.println("Domain d2 is: " + d2.nameDomain);
//				
//				//Thêm toàn bộ các cạnh giữa các node trong domain d1 vào ma trận cạnh
//				for(int j = 0; j < d1NodeList.length; j++) {
//					for(int k = 0; k < d1NodeList.length; k++) {
//						int node1 = d1NodeList[j];
//						int node2 = d1NodeList[k];
//						int value = File.matrixNodeEdge[node1][node2];
//						if(value != 0) {
//							g0[node1][node2] = value;
//						}
//					}
//				}
//				//Done việc thêm cạnh trong miền d1
//				//================================================================
//				//Thêm toàn bộ các cạnh từ miền d1 đến miền d2 vào ma trận cạnh
//				for(int j = 0; j < d1NodeList.length; j++) {
//					for(int k = 0; k < d2NodeList.length; k++) {
//						int node1 = d1NodeList[j];
//						int node2 = d2NodeList[k];
//						int value = File.matrixNodeEdge[node1][node2];
//						if(value != 0) {
//							g0[node1][node2] = value;
//						}
//					}
//				}
//				//Done việc thêm cạnh nối giữa d1 và d2
//			}
//		}
////		System.out.println("Done build graph");
////		for(int i = 0; i < File.NODES_NUM+1; i++) {
////			System.out.println();
////			for(int j = 0; j < File.NODES_NUM+1; j++) {
////				System.out.print(g0[i][j]+" ");
////			}
////		}
//		return g0;
//		
//	}
//	
//	
//	public static void CalculateFiness(Individual c) {
//		int[] orderDomain = Individual.FindTheOrderOfDomain(c);
//		boolean isDecode = isDecoded(orderDomain);
//		if(isDecode == false) {
//			c.fitness = Integer.MAX_VALUE;
//		}else {
//			int [][] g0 = Individual.BuildGraph(orderDomain);
//			c.fitness = DijkstraAlgorithm(g0);
//		}
//	}
//	
//	/*
//	 * Hàm sử dụng giải thuật dijkstra để tìm ra đường đi ngắn nhất từ src node đến các đỉnh khác
//	 */
//	public static int DijkstraAlgorithm(int [][] g0) {
//		int fitness;
//		int endNode = Graph.endNode;
//		int[] fitnessList = Individual.ArrayInitialization();
//		int currentNode;
//		int[] nodeTraveledList = new int [Graph.n+1];
//		do {
//			currentNode = Individual.FindTheNearestNode(fitnessList, nodeTraveledList);
//			if(currentNode == Integer.MAX_VALUE) {
//				fitness = fitnessList[endNode];
//				return fitness;	
//			}
//			nodeTraveledList[currentNode] = 1;
////			System.out.println("Current Node is: " + currentNode);
//			fitnessList = Individual.FindNeighboringNode(g0, fitnessList, currentNode, nodeTraveledList);
////			System.out.println("NodeList is: " + currentNode);
////			for(int i = 0; i < fitnessList.length; i++) {
////				System.out.print(fitnessList[i] + " ");
////			}
//			// System.out.println("while loop");
//		}while(currentNode != endNode);
//		
//		fitness = fitnessList[endNode];
//		return fitness;	
//	}
//	
//	/*
//	 * Hàm khởi tạo mảng chứa tổng quãng đường khi đi từ src node đến các node còn lại
//	 * Trả về mảng chứa các node từ 0 ; N trong đó tại src node khoảng cách là 0; các node còn lại khoảng cách max
//	 */
//	public static int[] ArrayInitialization() {
//		int startNode = Graph.startNode;
//		int[] array = new int[Graph.n+1];
//		for(int i = 0; i < array.length; i++) {
//			if(i == startNode) {
//				array[i] = 0;
//			}else {
//				array[i] = Integer.MAX_VALUE;
//			}
//		}
//		return array;
//	}
//	
//	
//	/*
//	 * Tìm ra node có khoảng cách nhỏ nhất từ src node
//	 * Trả về node có khoảng cách nhỏ nhất từ src node. Trong trường hợp chạy lần đầu, hàm trả ra src node
//	 */
//	public static int FindTheNearestNode(int[] fitnessList, int[] nodeTraveledList) {
//		int minNode = Integer.MAX_VALUE;
//		int node = Integer.MAX_VALUE;
//		for(int i = 0; i < fitnessList.length; i++) {
//			int value = fitnessList[i];
////			if(value != 0) {
//				if(value < minNode && nodeTraveledList[i] != 1) {
//					minNode = value;
//					node = i;
//				}
////			}
//			
//		}
//		return node;
//	}
//	
//	
//	/*
//	 * Tìm các node lân cận node hiện tại và cập nhật fitness đến các nốt đó
//	 * Đầu ra là mảng fitnessList sau khi đã được cập nhật giá trị của các node
//	 */
//	public static int[] FindNeighboringNode(int [][] g0, int [] fitnessList, int currentNode, int[] nodeTraveledList) {
//		for(int i = 0; i < Graph.n +1; i++) {
//			int value = g0[currentNode][i];
//			if(value != 0) {
//				int neighborNode = i;
//				int fitness = value + fitnessList[currentNode];
//				if(fitness < fitnessList[neighborNode] && nodeTraveledList[neighborNode] != 1) {
//					fitnessList[neighborNode] = fitness;
//				}
//			}
//		}
//		return fitnessList;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	/*
//	 * Hàm để thu nhỏ ma trận đường đi giữa các domain, chỉ giữ lại đường đi giữa các domain theo thứ tự của cá thể
//	 * Argument: một mảng chứa thứ tự các domain
//	 * Return: Ma trận đường đi giữa các domain của cá thể I
//	 */
//	public static void ReductiveTheMatrixOfDomainEdge(int[] listDomain) {
//		int[][] smallListDomain = new int[Graph.d][Graph.d];
//		int i = 0;
//		int j = i + 1;
//		while(j < listDomain.length){
//			int d1Number = listDomain[i];
//			int d2Number = listDomain[j];
//			if(File.matrixDomainEdge[d1Number][d2Number] != 0) {
//				smallListDomain[d1Number][d2Number] = File.matrixDomainEdge[d1Number][d2Number];
//				i = j;
//			}
//			j += 1;
//		}
//	}
//	
//	
//	
	
	
	
}
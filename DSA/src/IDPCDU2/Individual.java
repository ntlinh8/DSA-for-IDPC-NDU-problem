package IDPCDU2;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.text.Document;

public class Individual {
	public int[] individual = new int[12];		//Mảng thể hiện độ ưu tiên của miền. VD: I = [0, 2, 1, 3, 4] thì 2 là độ ưu tiền của domain d1.
	public int fitness;
	public static Random random = new Random();
	public static int loopNumber = File.DOMAINS_NUM*2;
	
	public Individual() {
		for(int i = 0; i < this.individual.length; i++) {
			this.individual[i] = i;
		}
		for(int k = 0; k < Individual.loopNumber+1; k++) {
			for(int i = 0; i < this.individual.length; i++) {
				int p1 = random.nextInt(0, this.individual.length);
				int p2 = random.nextInt(0, this.individual.length);
				while(p1 == p2) {
					p2 = random.nextInt(0, this.individual.length);
				}
				int temp = this.individual[p1];
				this.individual[p1] = this.individual[p2];
				this.individual[p2] = temp;
			}
		}
	}
	
	public Individual(Individual c) {
		this.individual = c.individual;
		this.fitness = c.fitness;
	}
	
	/*
	 * Hàm để tìm thứ tự của các domain dựa theo trọng số
	 * Return: Một mảng hiển thị thứ tự của domain bắt đầu từ 0 đến domain thứ n
	 * Trường hợp không tìm thấy đường đi từ điểm đầu đến điểm cuối thì tại mảng thứ tự domain trả ra có 2 giá trị cuối cùng bằng nhau và khác 0
	 */
	public static int[] FindTheOrderOfDomain(Individual c) {
		int[] orderDomain = new int[File.DOMAINS_NUM];
		int count = 1;
		int startDomain = File.srcDomain;
		int endDomain = File.desDomain;
		int nextDomain= FindTheNextDomain(startDomain, c, orderDomain);
		
		for(int i = 0; i < orderDomain.length; i++) {
			orderDomain[i] = startDomain;
			nextDomain = FindTheNextDomain(startDomain, c, orderDomain);
			if(nextDomain == endDomain) {
				orderDomain[i+1] = nextDomain;
				break;
			}
			startDomain = nextDomain;
		}
		return orderDomain;
	}
	
	/*
	 * Hàm để tìm domain tiếp theo từ domain cho trước đối với cá thể c
	 */
	public static int FindTheNextDomain(int startDomain, Individual c, int [] orderDomain) {
		int nextDomain = startDomain;
		int priority = File.DOMAINS_NUM;
		for(int i = 0; i < File.DOMAINS_NUM; i++) {
			int currentDomain = i;
			if(File.matrixDomainEdge[startDomain][currentDomain]!= 0 && c.individual[currentDomain] < priority && isTraversed(currentDomain, orderDomain) == false) {
				priority = c.individual[currentDomain];
				nextDomain = currentDomain;
			}
		}
		return nextDomain;
	}
	
	/*
	 * Hàm verify xem domain đã được đi qua hay chưa (đã tồn tại trong mảng chứa thứ tự của domain hay chưa)
	 */
	public static boolean isTraversed(int domain, int[] orderDomain) {
		boolean isTraversed = false;
		for(int i = 0; i < orderDomain.length; i++) {
			if (orderDomain[i] == domain) {
				isTraversed = true;
				break;
			}
		}
		return isTraversed;
	}
	
	/*
	 * Hàm verify xem thứ tự các domain có thể decode được hay không?
	 */
	public static boolean isDecoded(int[] orderDomain) {
		int length = orderDomain.length;
		int endDomain = orderDomain[length-1];
		int subEndDomain = orderDomain[length-2];
		boolean isDecode = true;
		if(endDomain == subEndDomain && endDomain != 0) {
			isDecode= false;
		}
		return isDecode;
	}
	
	
	/*
	 * Hàm có nhiệm vụ xây dựng một đồ thị G0 từ G bằng cách giữ lại toàn bộ đường đi trong các miền có đi qua
	 * Và bổ sung thêm các đường dẫn liên miền theo đúng thứ tự miền sẽ đi qua
	 */
	public static int[][] BuildGraph(int[] orderDomain) {
		int [][] g0 = new int [File.NODES_NUM +1][File.NODES_NUM + 1];
		for(int i = 0; i < orderDomain.length; i++) {
			if(orderDomain[i] == File.desDomain) {
				break;
			}else {
				//Lấy ra domain d1 tại vị trí i
				int d1Number = orderDomain[i];
				Domain d1 = File.domainList.get(d1Number);
				int [] d1NodeList = d1.nodeList;
				
				//Lấy ra domain d2 tại vị trí i+1
				int d2Number = orderDomain[i+1];
				Domain d2 = File.domainList.get(d2Number);
				int [] d2NodeList = d2.nodeList;
				
				//Thêm toàn bộ các cạnh giữa các node trong domain d1 vào ma trận cạnh
				for(int j = 0; j < d1NodeList.length; j++) {
					for(int k = 0; k < d1NodeList.length; k++) {
						int value = File.matrixNodeEdge[j][k];
						if(value != 0) {
							g0[j][k] = value;
						}
					}
				}
				//Done việc thêm cạnh trong miền d1
				//================================================================
				//Thêm toàn bộ các cạnh từ miền d1 đến miền d2 vào ma trận cạnh
				for(int j = 0; j < d1NodeList.length; j++) {
					for(int k = 0; k < d2NodeList.length; k++) {
						int value = File.matrixNodeEdge[j][k];
						if(value != 0) {
							g0[j][k] = value;
						}
					}
				}
				//Done việc thêm cạnh nối giữa d1 và d2
			}
		}
		return g0;
	}
	
	
	public static void CalculateFiness(Individual c) {
		int[] orderDomain = Individual.FindTheOrderOfDomain(c);
		boolean isDecode = isDecoded(orderDomain);
		if(isDecode == false) {
			c.fitness = Integer.MAX_VALUE;
		}else {
			int [][] g0 = Individual.BuildGraph(orderDomain);
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Hàm để thu nhỏ ma trận đường đi giữa các domain, chỉ giữ lại đường đi giữa các domain theo thứ tự của cá thể
	 * Argument: một mảng chứa thứ tự các domain
	 * Return: Ma trận đường đi giữa các domain của cá thể I
	 */
	public static void ReductiveTheMatrixOfDomainEdge(int[] listDomain) {
		int[][] smallListDomain = new int[File.DOMAINS_NUM][File.DOMAINS_NUM];
		int i = 0;
		int j = i + 1;
		while(j < listDomain.length){
			int d1Number = listDomain[i];
			int d2Number = listDomain[j];
			if(File.matrixDomainEdge[d1Number][d2Number] != 0) {
				smallListDomain[d1Number][d2Number] = File.matrixDomainEdge[d1Number][d2Number];
				i = j;
			}
			j += 1;
		}
	}
	
	
	
	
	
	
	
	
	
}
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
	}
	
	/*
	 * Hàm để tìm thứ tự của các domain dựa theo trọng số
	 * Return: Một mảng hiển thị thứ tự của domain bắt đầu từ 0 đến domain thứ n
	 */
	public static int[] FindTheOrderOfDomain(Individual c) {
		int[] orderDomain = new int [File.DOMAINS_NUM];
		int count = 1;
		int startDomain = File.srcDomain;
		int endDomain = File.desDomain;
		
		int nextDomain = FindTheNextDomain(startDomain, c);
		orderDomain[count] = nextDomain;
		
		while(nextDomain != endDomain) {
			count++;
			startDomain = nextDomain;
			nextDomain = FindTheNextDomain(startDomain, c);
			orderDomain[count] = nextDomain;
		}
		return orderDomain;
	}
	
	/*
	 * Hàm để tìm domain tiếp theo từ domain cho trước đối với cá thể c
	 */
	public static int FindTheNextDomain(int startDomain, Individual c) {
		int nextDomain = startDomain;
		int priority = File.DOMAINS_NUM;
		for(int i = 0; i < File.DOMAINS_NUM; i++) {
			int currentDomain = i;
			if(File.matrixDomainEdge[startDomain][currentDomain]!= 0 && c.individual[currentDomain] < priority) {
				priority = c.individual[currentDomain];
				nextDomain = currentDomain;
			}
		}
		return nextDomain;
	}
	
	/*
	 * Hàm có nhiệm vụ xây dựng một đồ thị G0 từ G bằng cách giữ lại toàn bộ đường đi trong các miền có đi qua
	 * Và bổ sung thêm các đường dẫn liên miền theo đúng thứ tự miền sẽ đi qua
	 */
	public static void BuildGraph(int[] orderDomain) {
		
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
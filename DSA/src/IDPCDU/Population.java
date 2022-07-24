package IDPCDU;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
	public static int POP_SIZE;    
	public static Individual[] individualList;
	public static int [] fitness;
	public static Random random = new Random();
	public int bestFitness = 0;
	public static Individual Xleader;

	
	
	

	public Population(int size) {
		// TODO Auto-generated constructor stub
		this.POP_SIZE = size;
		individualList = new Individual[POP_SIZE];
		fitness = new int [POP_SIZE];
		
		for(int i = 0; i < individualList.length; i++) {
			Individual c;
			int cost;
			do{
				c = new Individual();
				//System.out.println("loop");
				cost = c.DeCode(c);
				//cost = c.CalculateFitness(c);
				//System.out.println("Fitness = " + cost);
			}while (cost == Integer.MAX_VALUE || cost == 0);
			
			this.individualList[i] = c;
			this.fitness[i] = cost;
			
		}
		
		this.Xleader = this.individualList[0];
		
		this.bestFitness = this.fitness[0];
		/*for(int i = 0; i < individualList.length; i++) {
			
			this.fitness[i] = this.individualList[i].DeCode(this.individualList[i]);
		}*/
	}
	
	public static void FindXLeader(Population p) {
		//int minFitness = 0;
		if(p.Xleader == null) {
			p.bestFitness = p.fitness[0];
		}
		
		
		for(int i = 0; i <POP_SIZE; i++ ) {
			if(p.fitness[i] < p.bestFitness && p.fitness[i] != 0 /*p.individualList[i].isDeCode == true && p.individualList[i].individual.length < p.Xleader.individual.length*/) {
				p.bestFitness = p.fitness[i];
				p.Xleader = p.individualList[i];
			}
		}
		/*
		System.out.print("I: ");
		for(int i = 0; i < File.DOMAINS_NUM; i++ ) {
			System.out.print(p.Xleader.individual[i] + " ");
		}
		System.out.println("Fitness = " + minFitness);
		System.out.println(" ");
		
		*/
	}
	
	public static Individual FindTheWorstIndividual(Population p) {
		int maxFitness = p.fitness[0];
		int index = 0;
		for(int i = 0; i <POP_SIZE; i++ ) {
			if(p.fitness[i]> maxFitness) {
				maxFitness = p.fitness[i];
				index = i;
			}
		}
		Individual worstIndividual = p.individualList[index];
		/*System.out.print("I: ");
		for(int i = 0; i < File.DOMAINS_NUM; i++ ) {
			System.out.print(p.Xleader.individual[i] + " ");
		}
		System.out.println("Fitness = " + maxFitness);
		System.out.println(" ");
		*/
		return worstIndividual;
		
	}
	
	
	public static void PopulationInitialize(){
		for(int i = 0 ; i < POP_SIZE; i++ ) {
			Individual c = new Individual();
			individualList[i] = c;			
			fitness[i] = c.DeCode(c);
		}
	}
	
	public static void PrintPopulation(Population p) {
		for(int i = 0; i < p.individualList.length; i++) {
			System.out.print("I: ");
			for(int j = 0; j < p.individualList[i].individual.length; j++ ) {
				System.out.print(p.individualList[i].individual[j] + " ");
				//System.out.println(" ");
				
			}
			System.out.println("Cost = " + p.fitness[i]);
			System.out.println("===");
		}
	}
	
	public static void ProbeAround(Population p) {
		for(int i = 0; i < Main.soVongLapKhoiTao; i++) {
			int maxFitness = p.fitness[0];
			int index = 0;
			for(int k = 0; k <POP_SIZE; k++ ) {
				if(p.fitness[i]> maxFitness) {
					maxFitness = p.fitness[k];
					index = k;
				}
			}
			Individual worstIndividual = p.individualList[index];
			int[] count = new int [File.DOMAINS_NUM + 1];
			
				
			for(int k = 0; k < p.POP_SIZE; k++) {
					
				if(p.individualList[k].equals(worstIndividual) == false) {
					for(int j = 1; j < (File.DOMAINS_NUM - 1); j++) {
						int value = p.individualList[k].individual[j];
						count[value]++;
					}
				}
			}
			
			int soMin = 2;
			int min = count[2];
			for(int k = 2; k < (File.DOMAINS_NUM); k++) {
				
				if(count[k] < min) {
					min = count[k];
					soMin = k;
				}
			}
			
			
			
		}
		
	}
	
	public static void ExplorationYourSelf(Individual c) {
		
			Random random = new Random();
			int index = random.nextInt(1,c.individual.length -2);
			
			int temp = c.individual[index];
			//System.out.println("Temp = " + temp);
			c.individual[index] = c.individual[index + 1];
			c.individual[index + 1] = temp;
			
			//Individual.PrintIndividual(c);
	}
	
	
	
	//Doi cho vi tri 2 mien bat ki cho nhau trong 1 ca 
	//Day la thay doi lon doi voi 1 ca the
	public static void ExplorationYourSelf_Big_Distances(Individual c) {
		if(c.individual.length == 3) {
			//Individual temp = new Individual(c);
			Random random = new Random();
			int index1;
			if(c.individual.length ==3) {
				index1 = 1;
			} else index1 = random.nextInt(1, c.individual.length-2);
			int index;
			do {
				index = random.nextInt(1, File.domainList.size()-2);
				//System.out.println("Day la vong lap vo han4");
			}while(c.individual[index1] == File.domainList.get(index).nameDomain);
			
			c.individual[index1] = File.domainList.get(index).nameDomain;
		}else {
			Random random = new Random();
			int index1 = random.nextInt(1,c.individual.length -2);
			int index2 = random.nextInt(1,c.individual.length -2);
			int temp1 = c.individual[index1];
			int temp2 = c.individual[index2];
			if(index1 < index2) {
				for(int i = index1; i < index2; i++) {
					c.individual[i] = c.individual[i+1];
				}
				c.individual[index2] = temp1;
			}else {
				//index 1 > index 2
				for(int i = index1; i > index2; i--) {
					c.individual[i] = c.individual[i-1];
				}
				c.individual[index2] = temp1;
			}
		}
	}
	
	
	public static void ExplorationYourSelf_Small_Distances(Individual c) {
		
		if(c.individual.length != File.DOMAINS_NUM) {
			Random r = new Random();
			int index1;
			if(c.individual.length == 3) {
				index1 = 1;
			}else index1 = r.nextInt(1, c.individual.length-2);
			
			boolean isExist = false;
			int index2;
			
			//do {
				index2 = r.nextInt(1, File.domainList.size()-2);
				for(int i = 1; i < c.individual.length-1; i++) {
					//System.out.print(c.individual[i] +" ");
					if(File.domainList.get(index2).nameDomain == c.individual[i]) {
						isExist = true;
						//System.out.println("Lap" + File.domainList.get(index2).nameDomain);
					}
				}
			//}while(!isExist);

			if(isExist == false) {
				c.individual[index1] = File.domainList.get(index2).nameDomain;
			}
		}
	
	}
	/*public static Individual ExplorationXleader(Population p, Individual c) {
		
			if(c.equals(p.Xleader) == false) {
				//Update theo 
				List<Integer> items = HammingDistance(c, p.Xleader);
				Random random = new Random();
				int index = 1;
				if(!items.isEmpty()) {
					index = items.get(random.nextInt(items.size()));
				}
				//System.out.println("Index: " + index);
				for(int j = 0; j < File.DOMAINS_NUM; j++) {
					if( c.individual[j] == p.Xleader.individual[index]) {
						c.individual[j] = c.individual[index];
					}
				}
				c.individual[index] = p.Xleader.individual[index];
			}
			//Individual.PrintIndividual(c);
			return c;
			
	}*/
	
	
	public static void ExploitationXleaderAndOther(Population p, Individual c) {

			if(c.equals(p.Xleader) == true) {
				Individual.PrintIndividual(c);
				Individual.PrintIndividual(p.Xleader);
				List<Integer> difLocation = HammingDistance(c, p.Xleader);
				for(int i = 0; i < difLocation.size(); i++) {
					System.out.println(difLocation.get(i) + " ");
					System.out.println("Size" +difLocation.size() + " ");
				}
				Random random = new Random();
				
				int index = difLocation.get(random.nextInt(difLocation.size()));
				//System.out.println("Index: " + index);
				for(int j = 0; j < File.DOMAINS_NUM; j++) {
					if( c.individual[j] == p.Xleader.individual[index]) {
						c.individual[j] = c.individual[index];
					}
				}
				c.individual[index] = p.Xleader.individual[index];
				
			}
			
			
			Individual k;
			do {
			Random random = new Random();
			int index = random.nextInt(p.individualList.length);
			//System.out.println("index = " + index);
			k = p.individualList[index];
			
			}while(k.individual.equals(p.Xleader.individual) == true|| k.individual.equals(c.individual) == true);	
			
			List<Integer> difLocation = HammingDistance(c, k);
			for(int i = 0; i < difLocation.size(); i++) {
				System.out.println(difLocation.get(i) + " ");
				System.out.println("Size" +difLocation.size() + " ");
			}
			//System.out.println("difLocation size k: " + difLocation.size());
			Random r = new Random();
			int indexNumber = 1;
			if(!difLocation.isEmpty()) {
				indexNumber = difLocation.get(r.nextInt(difLocation.size()));
			}
			//int indexNumber = difLocation.get(r.nextInt(difLocation.size()));
			//System.out.println("Index: " + index);
			for(int j = 0; j < c.individual.length; j++) {
				if( c.individual[j] == k.individual[indexNumber]) {
					c.individual[j] = c.individual[indexNumber];
				}
			}
			c.individual[indexNumber] = k.individual[indexNumber];
			//return c;
			//Individual.PrintIndividual(c);

			
		System.out.print("I: ");
		for(int i = 0; i < Xleader.individual.length; i++ ) {
			System.out.print(p.Xleader.individual[i] + " ");
		}
		System.out.println("Best Fitness = " + p.bestFitness);
		System.out.println(" ");
	}
	
	
	/*public static Individual ExploitationXleaderAnd2Other(Population p, Individual c) {

		if(c.individual.equals(p.Xleader.individual) == true) {
			List<Integer> difLocation = HammingDistance(c, p.Xleader);
			for(int i = 0; i < difLocation.size(); i++) {
				System.out.println(difLocation.get(i) + " ");
				System.out.println("Size" +difLocation.size() + " ");
			}
			Random random = new Random();
			
			int index = difLocation.get(random.nextInt(difLocation.size()));
			//System.out.println("Index: " + index);
			for(int j = 0; j < File.DOMAINS_NUM; j++) {
				if( c.individual[j] == p.Xleader.individual[index]) {
					c.individual[j] = c.individual[index];
				}
			}
			c.individual[index] = p.Xleader.individual[index];
			
		}
		
		
		Individual k1;
		do {
		Random random = new Random();
		int index = random.nextInt(p.individualList.length);
		k1 = p.individualList[index];
		
		}while(k1.individual.equals(p.Xleader.individual) == true || k1.individual.equals(c.individual) ==true );
		
		List<Integer> difLocationk1 = HammingDistance(c, k1);
		for(int i = 0; i < difLocationk1.size(); i++) {
			System.out.println(difLocationk1.get(i) + " ");
			System.out.println("Size" + difLocationk1.size() + " ");
		}
		Random r1 = new Random();
		int indexNumber = 1;
		if(!difLocationk1.isEmpty()) {
			int r1number = r1.nextInt(difLocationk1.size());
			System.out.print("r1number " + r1number);
			indexNumber = difLocationk1.get(r1number);
		}
		
		
		
		//System.out.println("Index: " + index);
		for(int j = 0; j < File.DOMAINS_NUM; j++) {
			if( c.individual[j] == k1.individual[indexNumber]) {
				c.individual[j] = c.individual[indexNumber];
			}
		}
		c.individual[indexNumber] = k1.individual[indexNumber];
		
		Individual k2;
		do {
		Random random = new Random();
		int index = random.nextInt(p.individualList.length);
		k2 = p.individualList[index];
		
		}while(k2.individual.equals(p.Xleader.individual) || k2.individual.equals(c.individual)|| k2.individual.equals(k1.individual));
		
		List<Integer> difLocationk2 = HammingDistance(c, k2);
		for(int i = 0; i < difLocationk2.size(); i++) {
			System.out.println(difLocationk2.get(i) + " ");
			System.out.println("Size" +difLocationk2.size() + " ");
		}
		Random r2 = new Random();
		int indexNumberk2 = 1;
		if(!difLocationk2.isEmpty()) {
			indexNumberk2 = difLocationk2.get(r2.nextInt(difLocationk2.size()));
		}
		
		//System.out.println("Index: " + index);
		for(int j = 0; j < File.DOMAINS_NUM; j++) {
			if( c.individual[j] == k2.individual[indexNumber]) {
				c.individual[j] = c.individual[indexNumber];
			}
		}
		c.individual[indexNumber] = k2.individual[indexNumber];
		//Individual.PrintIndividual(c);
		

		
	System.out.print("I: ");
	for(int i = 0; i < File.DOMAINS_NUM; i++ ) {
		System.out.print(p.Xleader.individual[i] + " ");
	}
	System.out.println("Best Fitness = " + p.bestFitness);
	System.out.println(" ");
		return c;
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
	
	
	//ok
	public static void Exploration(Individual c) {
		//Individual temp = new Individual(c);
		Random random = new Random();
		int index1;
		if(c.individual.length ==3) {
			index1 = 1;
		} else index1 = random.nextInt(1, c.individual.length-2);
		int index;
		do {
			index = random.nextInt(1, File.domainList.size()-2);
			//System.out.println("Day la vong lap vo han4");
		}while(c.individual[index1] == File.domainList.get(index).nameDomain);
		
		c.individual[index1] = File.domainList.get(index).nameDomain;
	}
	
	
	//ok
	public static void ExplorationAndOtherIndividual1(Individual c, Individual Xleader) {
		
		
		
		int index;
		if(Xleader.individual.length != 3) {
			index = random.nextInt(1, Xleader.individual.length - 2);
		}else index = 1;
		
		
		boolean isExist = false; //Gia tri domain tai index cua Xleader co hien huu trong c khong
		for(int i = 0; i < c.individual.length; i++) {
			if(c.individual[i] == Xleader.individual[index]) {
				isExist = true;
			}
		}
		//if ko ton tai trong c thi chen vao 1 vi tri bat ki trong c
		if(!isExist) {
			int indexC;
			if(c.individual.length != 3) {
			indexC = random.nextInt(1, c.individual.length - 2);
			}else indexC = 1;
				
			int[] temp = new int[c.individual.length];
			for(int i = 0; i < c.individual.length; i++) {
				temp[i] = c.individual[i];
			}
			
			c.individual = new int[c.individual.length+1];
			
			for(int i = 0; i < indexC; i++) {
				c.individual[i] = temp[i];
			}
			c.individual[indexC] = Xleader.individual[index];
			
			for(int i = (indexC + 1); i < c.individual.length; i++) {
				c.individual[i] = temp[i-1];
			}
		}else {
			
			
			if(c.individual.length >= Xleader.individual.length) {
				//int indexC = 0;
				for(int i = 0 ; i < c.individual.length; i++) {
					if(c.individual[i] == Xleader.individual[index]) {
						int temp = c.individual[index];
						c.individual[index] = Xleader.individual[index];
						c.individual[i] = temp;	
					}
				}
			}
		}
			/*else {
				int[] temp = new int[c.individual.length];
				for(int i = 0; i < c.individual.length; i++) {
					temp[i] = c.individual[i];
				}
				
				c.individual = new int[c.individual.length+1];
				
				for(int i = 0; i < c.individual.length-2; i++) {
					c.individual[i] = temp[i];
				}
				c.individual[c.individual.length-2] = Xleader.individual[index];
				c.individual[c.individual.length-1] = File.DOMAINS_NUM;
			
			}
				
				
		}*/
		//Buoc 2: tinh toan theo cj
		/*
		if(c.individual.length == cj.individual.length && c.individual.length != 3) {
			List<Integer> list = HammingDistance(c, cj);
			if(!list.isEmpty()) {
				int index2 = random.nextInt(list.size()-1);
				
				boolean isTontai = false;
				for(int i = 0; i < c.individual.length; i++) {
					if(c.individual[i] == cj.individual[index2]) {
						isTontai = true;
						int temp2 = c.individual[index2];
						c.individual[index2] = c.individual[i];
						c.individual[i] = temp2;
					}
				}
				
				if(!isTontai) {
					int index3 = random.nextInt(1, c.individual.length-2);
					//int tempC = c.individual[index3];
					
					int[] tempC = new int[c.individual.length];
					for(int i = 0; i < c.individual.length; i++) {
						tempC[i] = c.individual[i];
					}
					c.individual = new int[c.individual.length+1];
					for(int i = 0; i < index3; i++) {
						c.individual[i] = tempC[i];
					}
					c.individual[index3] = cj.individual[index2];
					for(int i = (index3+1); i < c.individual.length; i++) {
						c.individual[i] = tempC[i-1];
					}
				}	
			}
			
		}else if (c.individual.length > cj.individual.length){
			for(int j = 0; j < (c.individual.length - cj.individual.length); j++) {
				int index1 = random.nextInt(c.individual.length-1);
				int[] tempC = new int[c.individual.length];
				for(int i = 0; i < c.individual.length; i++) {
					tempC[i] = c.individual[i];
				}
				c.individual = new int[c.individual.length-1];
				for(int i = 0; i < index1; i++) {
					c.individual[i] = tempC[i];
				}
				for(int i = index1; i < c.individual.length; i++) {
					c.individual[i] = tempC[i+1];
				}
			}
		}
		*/	
	}
	
	
	
	
	
	public static void exploitation1(Individual c, Individual Xleader){
		if(c.individual.length == Xleader.individual.length){
			List<Integer> list = HammingDistance(c, Xleader);
			if(!list.isEmpty()) {
				int index = 0;
				if(list.size() != 1) {
					index = random.nextInt(list.size()-1);
				}//else index = 0;

				for(int i = 0; i < c.individual.length; i++) {
					if(c.individual[i] == Xleader.individual[index]) {
						c.individual[i] = c.individual[index];
					}
				}
				
				c.individual[index] = Xleader.individual[index];
			}
		}else if(c.individual.length > Xleader.individual.length) {
			
			
			for(int i = 0; i < c.individual.length; i++) {
				boolean isTontai = false;
				
				for(int j = 0; j < Xleader.individual.length; j++) {
					if(c.individual[i] == Xleader.individual[j]) {
						isTontai = true;
					}
				}
				
				if(!isTontai) {
					
					int[] temp = new int[c.individual.length];
					for(int j = 0; j < c.individual.length; j++) {
						temp[j] = c.individual[j];
					}
					
					c.individual = new int[c.individual.length-1];
					
					for(int j = 0; j < i; j++) {
						c.individual[j] = temp[j];
					}
					
					for(int j = i; j < c.individual.length; j++){
						c.individual[j] = temp[j+1];
					}
					break;
				}
				
				
				
				
			}
		}
	}
	

}

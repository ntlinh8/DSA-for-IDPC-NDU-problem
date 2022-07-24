package IDPCDU;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;

public class Main {
	
	//public static int REITERATE_TIME = 10000;
    //public static int LowestCost = 99999, time;
    public static int pop_size = 100;
	public static int dimension = 30;
	public static int REITERATE_TIME = 500;
	public static int xmin = 0, xmax; //range
	public static double P = 0.5, FP = 0.618, CF1, CF2, KF1, KF2;
	
	public static int cf1 = 0, cf2 = 0, kf1 = 0, kf2 = 0;
	public static int iterTimes = 10000;
	public static float K, Miu;
    public static Random random = new Random();
    public static int soVongLapKhoiTao = 5;
    

	public static void main(String[] args) {
		//FileWriter();
		//for(int m = 0; m < arr.length; m++) {
			//String s = arr[m];
			String s = "idpc_ndu_502_12_10949";
			System.out.println("======================");
			System.out.println("File: " + s);
			//Giai doan tien xu ly file
			File f = new File();
			f.readFile(s);
			System.out.println("Done xu ly data");
			
			for(int k = 0; k < 30; k++) {
				//System.out.println("KHOI TAO QUAN THE");
				final long startTime = System.currentTimeMillis();

				Population p = new Population(pop_size);
				

				//Tim gia tri tot nhat
				Population.FindXLeader(p);
				//System.out.println("Best Fitness: " + p.bestFitness);
				//System.out.println("======================");
				//Population.PrintPopulation(p);
				
				for (int j = 0; j < REITERATE_TIME; j++) {
					//System.out.println("======================");
					//System.out.println("GIAI DOAN KHAM PHA");
					//Giai doan kham pha -  
					//double K = Math.sin(2*random.nextDouble())+ 1;
					//double muy = K*(1-(j/REITERATE_TIME));
					
					for(int i = 0; i < pop_size; i++) {
						Individual c = p.individualList[i];
						
						float rand = random.nextFloat();
							if(P > rand ) {
								double l = random.nextDouble(2);
								//if(l <= muy) {
									//di chuyen nhieu
									Population.ExplorationYourSelf_Big_Distances(c);
								//}else {
									Population.ExplorationYourSelf_Small_Distances(c);
								//}

							}else {
								int index = 0;
								do {
									index = random.nextInt(0, p.individualList.length-1);
									
									//System.out.println("Day la vong lap vo han1");
								}while(c.individual.equals(p.individualList[index].individual) == true || p.Xleader.individual.equals(p.individualList[index].individual) == true);
								
								Individual cj = p.individualList[index];
								Population.ExplorationAndOtherIndividual1(c, p.Xleader);	
								Population.ExplorationAndOtherIndividual1(c, cj);
								
								
							}
							
						p.individualList[i] = c;
						c.CheckDeCode(c);
						//c.CalculateFitness(c);
						//c.PrintIndividual(c);
						p.fitness[i] = Individual.DeCode(c);
						p.FindXLeader(p);
					}
					//Individual.PrintIndividual(p.Xleader);
					//System.out.println("Best Fitness: " + p.bestFitness);
					//Population.PrintPopulation(p);
					
					
					//System.out.println("======================");
					//System.out.println("GIAI DOAN KHAI THAC");
				
					for(int i = 0; i <pop_size; i++) {
						Individual c = p.individualList[i];
						//Individual c = p.individualList[i];
						if(c.individual.length != 3) {
							Individual temp = c;
							Population.exploitation1(temp, p.Xleader);
							if(temp.DeCode(temp) < c.DeCode(c) && temp.DeCode(temp) != 0) {
								c = temp;
							}else {
								int index1 =0;
								do {
									index1 = random.nextInt(0, p.individualList.length-1);
								}while(c.individual.equals(p.individualList[index1].individual) == true || p.Xleader.individual.equals(p.individualList[index1].individual) == true);
								
								Individual cj = p.individualList[index1];
								
								int index2 =0;
								do {
									index2 = random.nextInt(0, p.individualList.length-1);
									//System.out.println("Day la vong lap vo han3");
								}while(c.individual.equals(p.individualList[index2].individual) == true || p.Xleader.individual.equals(p.individualList[index2].individual) == true || cj.individual.equals(p.individualList[index2].individual) == true);
								Individual ck = p.individualList[index2];
								
								
								Population.exploitation1(c, p.Xleader);	
								Population.exploitation1(c, cj);
								Population.exploitation1(c, ck);
							}
							
						}else Population.Exploration(c);
						
						p.individualList[i] = c;
						c.CheckDeCode(c);
						//c.CalculateFitness(c);
						p.fitness[i] = Individual.DeCode(c);
						p.FindXLeader(p);
						
						
						}
					//Population.PrintPopulation(p);
					//Individual.PrintIndividual(p.Xleader);
					//System.out.print(p.bestFitness +" ");
					
				}
				Individual.PrintIndividual(p.Xleader);
				//System.out.println(p.bestFitness);
				System.out.println(" ");
				System.out.println("Final result: ");
				System.out.println(p.bestFitness);
				final long endTime = System.currentTimeMillis();
				//System.out.println("StartTime " + (startTime));
				//System.out.println("EndTime " + (endTime));
				System.out.println((endTime - startTime));
			}
			
		}
}
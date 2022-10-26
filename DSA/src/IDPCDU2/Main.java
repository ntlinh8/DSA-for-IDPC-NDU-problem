package IDPCDU2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;

public class Main {
	
    //public static int LowestCost = 99999, time;
    public static int popSize = 100;
	public static int timerNumber = 10;
	
	public static int REITERATE_TIME = 500;
	public static int xmin = 0, xmax; //range
	public static double P = 0.5, FP = 0.618, CF1, CF2, KF1, KF2;
	public static int cf1 = 0, cf2 = 0, kf1 = 0, kf2 = 0;
	public static int iterTimes = 10000;
	public static double K, Miu;
    public static Random random = new Random();
    

	public static void main(String[] args) {
			String s = "idpc_ndu_1002_12_82252";
			System.out.println("======================");
			System.out.println("File: " + s);
			//Giai doan tien xu ly file
			File f = new File();
			f.readFile(s);
			System.out.println("Done xu ly data");
			
			long[] timeList = new long[timerNumber];
			int[] fitnessList = new int[timerNumber];
			
			for(int timer = 0; timer < timerNumber; timer++) {
				final long startTime = System.currentTimeMillis();
				
				Population p = new Population(popSize);
				for(int i = 0; i < REITERATE_TIME; i++) {
					
					//Prepare the data
					K = Math.sin(2*random.nextDouble())+ 1;
					Miu = K*(1-(i/REITERATE_TIME));
					CF1 = 1/FP*(random.nextDouble(0, 1));
					CF2 = 1/FP*(random.nextDouble(0, 1));
					KF1 = 1/FP*(random.nextDouble(0, 1));
					KF2 = 1/FP*(random.nextDouble(0, 1));
					
					
					//Exploration phase
					for(int j = 0; j < popSize; j++) {
						Individual leader = p.Xleader;
						Individual c = p.individualList[j];
						double rand = random.nextDouble(0.0, 1.0);
						if(P > rand ) {
							c.chromosome = CalculateIndividual(1.0, c.chromosome, "+" , Miu, c.chromosome);
						}else {
							Individual cj = p.individualList[random.nextInt(0, popSize)];
							double[] chro1 = CalculateIndividual(CF1, leader.chromosome, "-", CF1, c.chromosome);
							double[] chro2 = CalculateIndividual(CF2, cj.chromosome, "-", CF2, c.chromosome);
							double[] chro3 = CalculateIndividual(1.0, chro2, "+", 1.0, chro1);
							c.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, chro3);
						}
						c.CalculateFiness(c);
						p.individualList[j] = new Individual(c);
						Population.UpdatePopulation(p);
					}
					
					//Exploitation phase
					for(int j = 0; j < popSize; j++) {
						Individual leader = p.Xleader;
						Individual c = p.individualList[j];
						Individual base = new Individual();
						double[] r1 = CalculateIndividual(Miu, leader.chromosome, "-", Miu, c.chromosome);
						base.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, r1);
						Individual.CalculateFiness(base);
						Individual.CalculateFiness(c);
						
						if(c.fitness > base.fitness) {
							p.individualList[i] = new Individual(base);
							p.fitness[i] = base.fitness;
						}else {
							Individual cj = p.individualList[random.nextInt(0, popSize)];
							Individual ck = p.individualList[random.nextInt(0, popSize)];
							double[] chro1 = CalculateIndividual(KF1, leader.chromosome, "-", KF1, c.chromosome);
							double[] chro2 = CalculateIndividual(KF2, ck.chromosome, "-", KF2, cj.chromosome);
							double[] chro3 = CalculateIndividual(1.0, chro2, "+", 1.0, chro1);
							c.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, chro3);
						}
						c.CalculateFiness(c);
						p.individualList[j] = new Individual(c);
						Population.UpdatePopulation(p);
					}
//					System.out.println("Best Fitness lan " + timer + "-" + i + " la: " + p.bestFitness);
				}
				System.out.println("Best Fitness lan: " + timer +" la: " + p.bestFitness);
				final long endTime = System.currentTimeMillis();
				timeList[timer] = endTime - startTime;
				fitnessList[timer] = p.bestFitness;
			}
			System.out.println("==============");
			System.out.println("Average Fitness = " + CalculateAverageIntList(fitnessList));
			System.out.println("Best Fitness = " + FindMinItem(fitnessList));
			System.out.println("Average Time = " + CalculateAverageLongList(timeList));

	}
	
	public static double[] CalculateIndividual(double anpha, double[] chromosome1, String operator, double beta, double[] chromosome2) {
		double[] chromosome = new double [File.DOMAINS_NUM];
		if(operator=="+") {
			for(int i = 0 ; i < chromosome.length; i++) {
				chromosome[i] = anpha*chromosome1[i] + beta*chromosome2[i];
			}
		}else {
			for(int i = 0 ; i < chromosome.length; i++) {
				chromosome[i] = Math.abs(anpha*chromosome1[i] - beta*chromosome2[i]);
			}
		}
		return chromosome;
	}
	
	public static float CalculateAverageIntList(int[] a) {
		int total = 0;
		for(int i = 0; i < a.length; i++) {
			total += a[i];
		}
		return total/a.length;
	}
	public static long CalculateAverageLongList(long[] a) {
		long total = 0;
		for(int i = 0; i < a.length; i++) {
			total += a[i];
		}
		return total/a.length;
	}
	
	public static int FindMinItem(int[] a) {
		int min = a[0];
		for(int i = 1; i < a.length; i++) {
			if(a[i] < min && a[i] != 0) {
				min = a[i];
			}
		}
		return min;
	}
}
package IDPCDU2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
	public static int POP_SIZE;    
	public static Individual[] individualList;
	public static int [] fitness;
	public static Random random = new Random();
	public static int bestFitness;
	public static Individual Xleader;



	public Population(int size) {
		this.POP_SIZE = size;
		this.individualList = new Individual [this.POP_SIZE];
		this.fitness = new int [this.POP_SIZE];
		for(int i = 0; i < this.POP_SIZE; i++) {
			Individual c = new Individual();
			this.individualList[i]= c;
			Individual.CalculateFiness(c);
			this.fitness[i] = c.fitness;
		}
		FindXLeader(this);
	}
	
	public static void FindXLeader(Population p) {
		//int minFitness = 0;
		if(p.Xleader == null) {
			p.bestFitness = Integer.MAX_VALUE;
		}
		for(int i = 0; i <p.POP_SIZE; i++ ) {
			if(p.fitness[i] < p.bestFitness && p.fitness[i] != 0) {
				p.bestFitness = p.fitness[i];
				p.Xleader = p.individualList[i];
			}
		}
	}

	
	public static Individual[] getIndividualList() {
		return individualList;
	}

	public static void setIndividualList(Individual[] individualList) {
		Population.individualList = individualList;
	}
	
	public static void printPopulation(Population p) {
		for(int i = 0; i < p.individualList.length; i++) {
			Individual c = p.individualList[i];
			System.out.print("I: ");
			for(int j = 0; j < c.chromosome.length; j++) {
				System.out.print(c.chromosome[j] +" ");
			}
			Individual.CalculateFiness(c);
			System.out.print("fitness is: " + c.fitness);
			System.out.println();
		}
	}
	
	public static void UpdatePopulation(Population p) {
		for(int i = 0; i < p.POP_SIZE; i++) {
			Individual c = p.individualList[i];
			Individual.CalculateFiness(c);
			p.fitness[i] = c.fitness;
		}
		FindXLeader(p);
	}
}

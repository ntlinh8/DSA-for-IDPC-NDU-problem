package IDPCDU2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
	public static int POP_SIZE;    
	public static Individual[] individualList;
	public static int [] fitness;
	public static Random random = new Random();
	public int bestFitness;
	public static Individual Xleader;

	
	
	

	public Population(int size) {
		this.POP_SIZE = size;
		this.individualList = new Individual [this.POP_SIZE];
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
			p.bestFitness = p.fitness[0];
			p.Xleader = p.individualList[0];
		}
		for(int i = 0; i <p.POP_SIZE; i++ ) {
			if(p.fitness[i] < p.bestFitness) {
				p.bestFitness = p.fitness[i];
				p.Xleader = p.getIndividualList()[i];
			}
		}
	}

	
	public static Individual[] getIndividualList() {
		return individualList;
	}

	public static void setIndividualList(Individual[] individualList) {
		Population.individualList = individualList;
	}
	
}

package NEW;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;

public class Main {
    public static final int pop_size = 100;
    public static final int number_seed = 30;
    public static final int MAX_GENERATION = 250;
    public static int[] bestValue;  // Mang ghi lai gia tri fitness tot nhat sau moi lan chay
    public static double P = 0.5, FP = 0.618, CF1, CF2, KF1, KF2, omega, gama;
	public static int cf1 = 0, cf2 = 0, kf1 = 0, kf2 = 0, transferPoint = 0, tranferNumber = 0;
	public static double K, Miu;
	public static String task1Path, task2Path;
    public static Random random = new Random();
//    public static int targetFitness = 1;
    public static int taskCoupleNumber = 6;
    public static Graph graph1, graph2;

    public static void main(String[] args) {
    	// Get Current Directory
    	File file = new File("");
    	String currentDirectory = file.getAbsolutePath() + "\\src\\NEW\\";
    	
    	//Read Pairing File -> output: 2 file
    	String pairingPath = currentDirectory + "Pairing.txt";
    	PrepareData(pairingPath, taskCoupleNumber);
    	
    	//Read 2 file which is pairing together
    	String path1 = currentDirectory + Main.task1Path;
    	String path2 = currentDirectory + Main.task2Path;
        graph1 = new Graph(path1);
        graph2 = new Graph(path2);
        
        //Initialize the population and calculate the best fitness
        Population population1 = new Population(pop_size, graph1);
        graph1.best_finess = population1.Xleader.fitness;
        Population population2 = new Population(pop_size, graph2);
        graph2.best_finess = population2.Xleader.fitness;
        
        for (int k = 0; k< MAX_GENERATION; k++) {
        	//Prepare the variable
        	K = Math.sin(2*random.nextDouble())+ 1;
    		Miu = K*(1-(k/MAX_GENERATION));
    		CF1 = 1/FP*(random.nextDouble(0, 1));
    		CF2 = 1/FP*(random.nextDouble(0, 1));
    		KF1 = 1/FP*(random.nextDouble(0, 1));
    		KF2 = 1/FP*(random.nextDouble(0, 1));
    		
        	DSA_Algorithm(population1, k);
			DSA_Algorithm(population2, k);
			
			if(k == transferPoint) {
				gama = k/MAX_GENERATION;
				omega = pop_size*0.3*(1-gama);
				tranferNumber = (int) Math.round(omega) + 25;
				TransferBetweenTwoPopulation(tranferNumber, population1.ListIndividual, population2.ListIndividual);
    			transferPoint += (int) Math.round(omega);
			}
			
        }
        
        System.out.println("file 1 " + population1.Xleader.fitness);
        System.out.println("file 2 " + population2.Xleader.fitness);
    }
    
    public static void DSA_Algorithm(Population population, int generationNumber) {
		//Exploration phase
		Exploration(population);
		//Exploitation phase
		Exploitation(population);
		//System.out.println("Best Fitness lan " + generationNumber + " la: " + population.Xleader.fitness);
    }
    
    
    public static void Exploration(Population population) {
    	int domainTotalNumber = population.graph.d;
    	for(int j = 0; j < pop_size; j++) {
			Individual leader = new Individual(population.Xleader);
			Individual c = population.ListIndividual[j];
			double rand = random.nextDouble(0.0, 1.0);
			if(P > rand ) {
				c.chromosome = CalculateIndividual(domainTotalNumber, 1.0, c.chromosome, "+" , Miu, c.chromosome);
			}else {
				Individual cj = population.ListIndividual[random.nextInt(0, pop_size)];
				double[] chro1 = CalculateIndividual(domainTotalNumber, CF1, leader.chromosome, "-", CF1, c.chromosome);
				double[] chro2 = CalculateIndividual(domainTotalNumber, CF2, cj.chromosome, "-", CF2, c.chromosome);
				double[] chro3 = CalculateIndividual(domainTotalNumber, 1.0, chro2, "+", 1.0, chro1);
				c.chromosome = CalculateIndividual(domainTotalNumber, 1.0, c.chromosome, "+", 1.0, chro3);
			}
			c.graph.calculateFitness(c.chromosome);
			population.ListIndividual[j] = new Individual(c);
			Population.UpdatePopulation(population);
		}
    }

    public static void Exploitation(Population population) {
    	int domainTotalNumber = population.graph.d;
    	for(int j = 0; j < pop_size; j++) {
			Individual leader = population.Xleader;
			Individual c = population.ListIndividual[j];
			Individual base = new Individual(population.graph);
			double[] r1 = CalculateIndividual(domainTotalNumber, Miu, leader.chromosome, "-", Miu, c.chromosome);
			base.chromosome = CalculateIndividual(domainTotalNumber, 1.0, c.chromosome, "+", 1.0, r1);
			base.graph.calculateFitness(base.chromosome);
			c.graph.calculateFitness(c.chromosome);
			
			if(c.fitness > base.fitness) {
				c = new Individual(base);
				population.ListIndividual[j] = new Individual(base);
			}else {
				Individual cj = population.ListIndividual[random.nextInt(0, pop_size)];
				Individual ck = population.ListIndividual[random.nextInt(0, pop_size)];
				double[] chro1 = CalculateIndividual(domainTotalNumber, KF1, leader.chromosome, "-", KF1, c.chromosome);
				double[] chro2 = CalculateIndividual(domainTotalNumber, KF2, ck.chromosome, "-", KF2, cj.chromosome);
				double[] chro3 = CalculateIndividual(domainTotalNumber, 1.0, chro2, "+", 1.0, chro1);
				c.chromosome = CalculateIndividual(domainTotalNumber, 1.0, c.chromosome, "+", 1.0, chro3);
			}
			c.graph.calculateFitness(c.chromosome);
			
			population.ListIndividual[j] = new Individual(c);
			Population.UpdatePopulation(population);
		}
    }
    
    
    public static void PrepareData(String pairingPath, int lineNumber) {
    	BufferedReader bReader;
        int counter = 0;
        try  {
        	 bReader = new BufferedReader(new FileReader(pairingPath));
        	 String line = bReader.readLine(); 
        	 while(line != null) {
        		 if(counter == lineNumber) {
        			 String [] splitedString= line.split("\\s+");
                	 Main.task1Path = splitedString[0];
                	 Main.task2Path = splitedString[1];
        		 }
        		 counter++;
        		 line = bReader.readLine();
        	 }
        	 bReader.close();	
        }catch(Exception e) {
            System.out.println("Loi doc file");
            e.printStackTrace();
        }
    }
    
    
    public static double[] CalculateIndividual(int domainTotalNumber, double anpha, double[] chromosome1, String operator, double beta, double[] chromosome2) {
		double[] chromosome = new double [domainTotalNumber];
		if(operator=="+") {
			for(int i = 0 ; i < chromosome.length; i++) {
				chromosome[i] = anpha*chromosome1[i] + beta*chromosome2[i];
			}
		}else {
			for(int i = 0 ; i < chromosome.length; i++) {
				chromosome[i] = anpha*chromosome1[i] - beta*chromosome2[i];
			}
		}
		ClipChromosome(chromosome);
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
	public static void ClipChromosome(double[] chromosome) {
		for(int i = 0 ; i < chromosome.length; i++) {
			if(chromosome[i] > 0) {
				chromosome[i] = 1 - random.nextDouble(0,1)*0.01;
			}else if(chromosome[i] <= 0) {
				chromosome[i] = 1 + random.nextDouble(0,1)*0.01;
			}
		}
	}
	
//	public static void checkBF(Population population1, Population population2, int targetFitness1, int targetFitness2) {
//		if (population1.Xleader.fitness == targetFitness1) {
//			break;
//		}
//		if (population2.Xleader.fitness == targetFitness2) {
//			break;
//		}
//	}
	
	public static void TransferBetweenTwoPopulation(int transferNumber, Individual [] individualList1, Individual [] individualList2) {
		QuickSort(individualList1, 0, 99);
		QuickSort(individualList2, 0, 99);
		Individual [] SubIndividualList1 = new Individual[transferNumber];
		Individual [] SubIndividualList2 = new Individual[transferNumber];
		
		for(int i = 0; i < transferNumber; i++) {
			SubIndividualList1[i] = new Individual(individualList1[i]);
			SubIndividualList2[i] = new Individual(individualList2[i]);
		}
		
		BringToTheCommonSpace(SubIndividualList1, SubIndividualList2);
		
		if (graph1.d > graph2.d) {
			for(int i = 0; i < transferNumber; i++) {
				Crossover(graph1.d , individualList1[pop_size-transferNumber].chromosome, SubIndividualList2[i].chromosome);
			}
			ReturnToTheOldSpace(SubIndividualList1, graph2.d);
			for(int i = 0; i < transferNumber; i++) {
				Crossover(graph2.d , individualList2[pop_size-transferNumber].chromosome, SubIndividualList1[i].chromosome);
			}
		}else{
			for(int i = 0; i < transferNumber; i++) {
				Crossover(graph2.d , individualList2[pop_size-transferNumber].chromosome, SubIndividualList1[i].chromosome);
			}
			ReturnToTheOldSpace(SubIndividualList2, graph2.d);
			for(int i = 0; i < transferNumber; i++) {
				individualList1[pop_size-transferNumber].chromosome = SubIndividualList2[i].chromosome;
				Crossover(graph1.d , individualList1[pop_size-transferNumber].chromosome, SubIndividualList2[i].chromosome);
			}
		}
		
	}
	
	public static void Crossover(int totalDomainNumber, double[] chromosome1, double[] chromosome2) {
		chromosome1 = CalculateIndividual(totalDomainNumber, 1.0, chromosome1, "+", gama , chromosome2);
	}
	
	public static void BringToTheCommonSpace(Individual[] individualList1, Individual[] individualList2) {
		int d1 = graph1.d;
		int d2 = graph2.d;
		if(d1 > d2) {
			for(int i = 0; i < individualList2.length; i++) {
				double[] oldChromosome = individualList2[i].chromosome;
				double[] newChromosome = new double[d1];
				for(int k = 0; k < newChromosome.length; k++ ) {
					if(k < oldChromosome.length) {
						newChromosome[k] = oldChromosome[k];
					}else {
						newChromosome[k] = random.nextDouble(0,1);
					}
				}
				individualList2[i].chromosome = newChromosome;
			}
		} else if(d2 > d1) {
			
			for(int i = 0; i < individualList1.length; i++) {
				double[] oldChromosome = individualList1[i].chromosome;
				double[] newChromosome = new double[d2];
				for(int k = 0; k < newChromosome.length; k++ ) {
					if(k < oldChromosome.length) {
						newChromosome[k] = oldChromosome[k];
					}else {
						newChromosome[k] = random.nextDouble(0,1);
					}
				}
				individualList1[i].chromosome = newChromosome;
			}
		}
	}
	
	public static void ReturnToTheOldSpace(Individual [] IndividualList, int domainNumber) {
		for(int i = 0; i < IndividualList.length; i++) {
			double[] oldChromosome = IndividualList[i].chromosome;
			double[] newChromosome = new double[domainNumber];
			for(int k = 0; k < domainNumber; k++) {
				newChromosome[k] = oldChromosome[k];
			}
			IndividualList[i].chromosome = newChromosome;
		}
	}
	
	public static int Partition(Individual listIndividual[], int low, int high) {
        int pivot = listIndividual[high].fitness;
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {

            // Nếu phần tử hiện tại nhỏ hơn chốt
            if (listIndividual[j].fitness < pivot) {
                i++;

                // swap arr[i] và arr[j]
                Individual temp = new Individual(listIndividual[i]);
                listIndividual[i] = new Individual(listIndividual[j]);
                listIndividual[j] = new Individual(temp);
            }
        }

        // swap arr[i+1] và arr[high] (hoặc pivot)
        Individual temp = new Individual(listIndividual[i+1]);
        listIndividual[i + 1] = new Individual(listIndividual[high]);
        listIndividual[high] = new Individual(temp);

        return i + 1;
    }
	
	public static void QuickSort(Individual listIndividual[], int low, int high) {
        if (low < high) {

            // pi là chỉ mục của chốt, arr[pi] vị trí của chốt
            int pi = Partition(listIndividual, low, high);

            // Sắp xếp đệ quy các phần tử
            // trướcphân vùng và sau phân vùng
            QuickSort(listIndividual, low, pi - 1);
            QuickSort(listIndividual, pi + 1, high);
        }
    }

    // In các phần tử của mảng
//    static void printArray(int arr[]) {
//        int n = arr.length;
//        for (int i = 0; i < n; ++i)
//            System.out.print(arr[i] + " ");
//        System.out.println();
//    }
	
	
}
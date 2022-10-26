package NEW;
import java.util.Random;

public class Main {
    public static final int pop_size = 100;
    public static final int number_seed = 30;
    public static final int MAX_GENERATION = 500;
    public static int[] bestValue;  // Mang ghi lai gia tri fitness tot nhat sau moi lan chay
    public static double P = 0.5, FP = 0.618, CF1, CF2, KF1, KF2;
	public static int cf1 = 0, cf2 = 0, kf1 = 0, kf2 = 0;
	public static int iterTimes = 10000;
	public static double K, Miu;
    public static Random random = new Random();

    public static void main(String[] args) {
        String path = "C:\\Users\\GDCV\\Documents\\git\\DSA-IDPCNDU\\DSA\\src\\NEW\\idpc_ndu_502_12_10949.txt";
        System.out.println("======================");
        System.out.println("File: " + path);
        long[] timeList = new long[number_seed];
        Graph graph = new Graph(path);
        Individual.setGraph(graph);
        bestValue = new int[number_seed];
        
        for (int i = 0; i< number_seed; i++) {
            Population population = new Population(pop_size);
            Graph.best_finess = population.Xleader.fitness;
            final long startTime = System.currentTimeMillis();
            
			
            for (int k = 0; k< MAX_GENERATION; k++) {
            	
				
				//Prepare the data
				K = Math.sin(2*random.nextDouble())+ 1;
				Miu = K*(1-(i/MAX_GENERATION));
				CF1 = 1/FP*(random.nextDouble(0, 1));
				CF2 = 1/FP*(random.nextDouble(0, 1));
				KF1 = 1/FP*(random.nextDouble(0, 1));
				KF2 = 1/FP*(random.nextDouble(0, 1));
				
				//Exploration phase
				for(int j = 0; j < pop_size; j++) {
					Individual leader = new Individual(population.Xleader);
					Individual c = population.ListIndividual[j];
					double rand = random.nextDouble(0.0, 1.0);
					if(P > rand ) {
						c.chromosome = CalculateIndividual(1.0, c.chromosome, "+" , Miu, c.chromosome);
					}else {
						Individual cj = population.ListIndividual[random.nextInt(0, pop_size)];
						double[] chro1 = CalculateIndividual(CF1, leader.chromosome, "-", CF1, c.chromosome);
						double[] chro2 = CalculateIndividual(CF2, cj.chromosome, "-", CF2, c.chromosome);
						double[] chro3 = CalculateIndividual(1.0, chro2, "+", 1.0, chro1);
						c.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, chro3);
					}
					c.graph.calculateFitness(c.chromosome);
					population.ListIndividual[j] = new Individual(c);
					Population.UpdatePopulation(population);
				}
				
				//Exploitation phase
				for(int j = 0; j < pop_size; j++) {
					Individual leader = population.Xleader;
					Individual c = population.ListIndividual[j];
					Individual base = new Individual();
					double[] r1 = CalculateIndividual(Miu, leader.chromosome, "-", Miu, c.chromosome);
					base.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, r1);
					base.graph.calculateFitness(base.chromosome);
					c.graph.calculateFitness(c.chromosome);
					
					if(c.fitness > base.fitness) {
						population.ListIndividual[i] = new Individual(base);
					}else {
						Individual cj = population.ListIndividual[random.nextInt(0, pop_size)];
						Individual ck = population.ListIndividual[random.nextInt(0, pop_size)];
						double[] chro1 = CalculateIndividual(KF1, leader.chromosome, "-", KF1, c.chromosome);
						double[] chro2 = CalculateIndividual(KF2, ck.chromosome, "-", KF2, cj.chromosome);
						double[] chro3 = CalculateIndividual(1.0, chro2, "+", 1.0, chro1);
						c.chromosome = CalculateIndividual(1.0, c.chromosome, "+", 1.0, chro3);
					}
					c.graph.calculateFitness(c.chromosome);
					population.ListIndividual[j] = new Individual(c);
					Population.UpdatePopulation(population);
				}
//				System.out.println("Best Fitness lan " + timer + "-" + i + " la: " + p.bestFitness);
            }
            bestValue[i] = population.Xleader.fitness;
            System.out.println("Best Fitness lan: " + i +" la: " + bestValue[i]);
			final long endTime = System.currentTimeMillis();
			timeList[i] = endTime - startTime;
//            
        }
        System.out.println("==============");
		System.out.println("Average Fitness = " + CalculateAverageIntList(bestValue));
		System.out.println("Best Fitness = " + FindMinItem(bestValue));
		System.out.println("Average Time = " + CalculateAverageLongList(timeList));
    }
    
    
    
    
    public static double[] CalculateIndividual(double anpha, double[] chromosome1, String operator, double beta, double[] chromosome2) {
		double[] chromosome = new double [Graph.d];
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
}
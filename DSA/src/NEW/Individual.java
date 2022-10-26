package NEW;
import java.util.Random;

public class Individual {
    public int d;   // so chieu == so mien
    public double[] chromosome;
    public int fitness;
    public static Graph graph;
    public static Random random = new Random();

    public static void setGraph(Graph graph) {
        Individual.graph = graph;
    }

    public Individual () {
        this.d = this.graph.d;
        this.graph = Individual.graph;
        this.chromosome = new double[d];
//        System.out.print("I: ");
        for (int i = 0; i < d; i++) {
            this.chromosome[i] = random.nextDouble(0,1);
//            System.out.print(this.chromosome[i] +" ");
        }
        this.fitness = this.graph.calculateFitness(this.chromosome);
//        System.out.print("Fitness: " + this.fitness);
//        System.out.println();
    }
    
    public Individual(Individual c) {
		this.chromosome = c.chromosome;
		this.fitness = c.fitness;
		this.d = c.d;
		this.setGraph(c.graph);
	}

}

package NEW;
import java.util.Random;

public class Individual {
    public int d;   // so chieu == so mien
    public double[] chromosome;
    public int fitness;
    public Graph graph;
    public static Random random = new Random();

    public Individual (Graph graph) {
    	this.graph = graph;
        this.d = this.graph.d;
        this.chromosome = new double[d];
        for (int i = 0; i < d; i++) {
            this.chromosome[i] = random.nextDouble(0,1);
        }
        this.fitness = this.graph.calculateFitness(this.chromosome);
    }
    
    public Individual(Individual c) {
		this.chromosome = c.chromosome;
		this.fitness = c.fitness;
		this.d = c.d;
		this.graph= c.graph;
	}
}

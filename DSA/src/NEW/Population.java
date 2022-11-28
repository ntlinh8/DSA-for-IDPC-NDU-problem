package NEW;

public class Population {
    public int pop_size;
    public Individual[] ListIndividual;
    public Individual bestIndividual;
	public Individual Xleader;
	public Graph graph;
	
    public Population (int pop_size, Graph graph) {
    	this.graph = graph;
        this.pop_size = pop_size;
        this.ListIndividual = new Individual[pop_size];
        for (int i = 0; i<pop_size; i++) {
            Individual newIndividual = new Individual(graph);
            this.ListIndividual[i] = newIndividual;
        }
        this.Xleader = this.ListIndividual[0];
        FindXLeader(this);
//        System.out.println("Xleader Fitness: " + this.Xleader.fitness);
    }
    
    public static void UpdatePopulation(Population p) {
//		for(int i = 0; i < p.pop_size; i++) {
//			Individual c = p.ListIndividual[i];
//			c.graph.calculateFitness(c.chromosome);
//		}
		FindXLeader(p);
	}
    
    public static void FindXLeader(Population p) {
    	Individual Xleader = p.Xleader;
    	if(Xleader == null) {
    		Xleader = new Individual(p.ListIndividual[0]);
		}
    	int firnessXleader = Xleader.fitness;
		for(int i = 0; i <p.pop_size; i++ ) {
			int fitnessIndividual = p.ListIndividual[i].fitness;
			if(fitnessIndividual < firnessXleader && fitnessIndividual != 0) {
				p.Xleader = p.ListIndividual[i];
			}
		}
	}
}

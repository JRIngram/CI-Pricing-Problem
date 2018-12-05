package ingramj.particle;

import java.util.Arrays;
import ingramj.PricingProblem;
import ingramj.Tuple;
import ingramj.particle.Particle;

public class ParticleSwarm {
	private Particle[] swarm;
	private double[] globalBestPricing;
	private double globalBestResult;
	private PricingProblem problem;
	private double[] coefficients;
	private int adaptiveSetting;
	
	/**
	 * Creates a particle swarm of a user-defined size
	 * @param problem The pricing problem instance
	 * @param numberOfGoods The number of goods in the pricing problem.
	 * @param coefficients The coefficients for calculating the particle's new positions.
	 * @param size The size of the particle swarms
	 * @param adaptiveSetting Sets which adaptive mode will be used. 0: coefficients are equal; 1: bias to exploration; 2: initial bias to exploitation; 3: initial bias to exploration then bias switches to exploitation; 4: initial bias to exploitation then bias switches to exploration. 
	 */
	public ParticleSwarm(PricingProblem problem, int numberOfGoods, double[] coefficients, int size, int adaptiveSetting) {
		//Sets the adaptivity setting to default if not a valid value.
		if(adaptiveSetting < 0 || adaptiveSetting > 4) {
			adaptiveSetting = 0;
		}
		this.adaptiveSetting = adaptiveSetting;
		this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
		if(adaptiveSetting != 0) {
			//Updates coefficients to have a bias to exploration/cognitive bias
			if(adaptiveSetting == 1 || adaptiveSetting == 3){
				this.coefficients[1] = this.coefficients[1] * 1.1;
				this.coefficients[2] = this.coefficients[2] * 0.9;
			}
			//Updates coefficients to have a bias to exploitation/social bias
			else if(adaptiveSetting == 2 || adaptiveSetting == 4){
				this.coefficients[1] = this.coefficients[1] * 0.9;
				this.coefficients[2] = this.coefficients[2] * 1.1;
			}
		}
		swarm = new Particle[size];
		this.problem = problem;

		
		//Creates particle swarm
		for(int i = 0; i < size; i++) {
			swarm[i] = new Particle(numberOfGoods, problem, this.coefficients);
		}
		
		globalBestPricing = swarm[0].getPersonalBestPricing();
		globalBestResult = swarm[0].getPersonalBestResult();
		
		//Checks which Swarm has the initial best.
		for(int i = 0; i < size; i++){
			if(globalBestResult < swarm[i].getPersonalBestResult()) {
				globalBestPricing = swarm[i].getPersonalBestPricing();
				globalBestResult = swarm[i].getPersonalBestResult();
				System.out.println("[0] New Global Best " + globalBestPricing.toString() + " with a revenue of: " + globalBestResult );
			}
		}
		updateSwarmsGlobalBest();
	}
	
	/**
	 * Updates the global best of the swarm
	 */
	private void updateSwarmsGlobalBest() {
		for(int i = 0; i < swarm.length; i++){
			swarm[i].setGlobalBest(globalBestPricing, globalBestResult);
		}
	}
	
	/**
	 * Causes the particles to search the state space to find the best position for a certain number of iterations
	 * @param numberOfSearches The number of iterations before termination
	 * @return The best result found.
	 */
	public Tuple<double[], Double> searchSpace(int numberOfSearches){
		for(int i = 0; i < numberOfSearches; i++) {			
			//Each particle searches the state space
			for(int j = 0; j < swarm.length; j++) {
				swarm[j].searchSpace();
			}
			//Checks all particles and updates the global best
			for(int j = 0; j < swarm.length; j++){
				if(globalBestResult < swarm[j].getPersonalBestResult()  && problem.is_valid(swarm[j].getPersonalBestPricing())) {
					globalBestPricing = Arrays.copyOf(swarm[j].getPersonalBestPricing(), swarm[j].getPersonalBestPricing().length);
					globalBestResult = swarm[j].getPersonalBestResult();
					System.out.println("[" + i + "] New Global Best with a revenue of: " + globalBestResult );
					updateSwarmsGlobalBest();
				}
			}
			
			//If adaptiveSetting is set to be adaptive, cognitive and coefficients are swapped once the search is half completed.
			if(i >= (numberOfSearches / 2) && (adaptiveSetting == 3 || adaptiveSetting == 4)){
				double temp = coefficients[1];
				coefficients[1] = coefficients[2];
				coefficients[2] = temp;
				for(int j = 0; j < swarm.length; j++) {
					swarm[j].updateCoefficients(coefficients);
				}
			}
			
			//Calculates new velocity for all particles based on global best.
			for(int j = 0; j < swarm.length; j++) {
				swarm[j].calculateNewVelocity();
			}
		}
		System.out.println("Finished with a global best of: " + globalBestResult);
		Tuple bestFoundPricing = new Tuple(globalBestPricing, globalBestResult);
		return bestFoundPricing;
	}
	
	/**
	 * Causes the particles to search the state space to find the best position for a certain length of time.
	 * @param timeRestraint Number of seconds to run the algorithm for before termination.
	 * @return The best result found
	 */
	public Tuple<double[], Double> searchSpaceTimeRestrained(int timeRestraint){
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		long timeDifference = (now - start) / 1000;
		int counter = 0;
		while(timeDifference < timeRestraint) {
			//Each particle searches the state space
			for(int j = 0; j < swarm.length; j++) {
				swarm[j].searchSpace();
			}
			//Checks all particles and updates the global best
			for(int j = 0; j < swarm.length; j++){
				if(globalBestResult < swarm[j].getPersonalBestResult()  && problem.is_valid(swarm[j].getPersonalBestPricing())) {
					globalBestPricing = Arrays.copyOf(swarm[j].getPersonalBestPricing(), swarm[j].getPersonalBestPricing().length);
					globalBestResult = swarm[j].getPersonalBestResult();
					System.out.println("[" + counter + "] New Global Best with a revenue of: " + globalBestResult );
					updateSwarmsGlobalBest();
				}
			}
			
			now = System.currentTimeMillis();
			timeDifference = (now - start) / 1000;
			//If adaptiveSetting is set to be adaptive, cognitive and coefficients are swapped once the search is half completed.
			if(timeDifference >= (timeRestraint / 2) && (adaptiveSetting == 3 || adaptiveSetting == 4)){
				double temp = coefficients[1];
				coefficients[1] = coefficients[2];
				coefficients[2] = temp;
				for(int j = 0; j < swarm.length; j++) {
					swarm[j].updateCoefficients(coefficients);
				}
			}
			
			//Calculates new velocity for all particles based on global best.
			for(int j = 0; j < swarm.length; j++) {
				swarm[j].calculateNewVelocity();
			}
			counter++;
			now = System.currentTimeMillis();
			timeDifference = (now - start) / 1000;
		}
		System.out.println("Finished with a global best of: " + globalBestResult);
		Tuple bestFoundPricing = new Tuple(globalBestPricing, globalBestResult);
		return bestFoundPricing;
	}
	
}

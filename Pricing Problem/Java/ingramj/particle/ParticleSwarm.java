package ingramj.particle;

import java.util.Arrays;
import ingramj.PricingProblem;
import ingramj.genetic.Tuple;
import ingramj.particle.Particle;

public class ParticleSwarm {
	private Particle[] swarm;
	private double[] globalBestPricing;
	private double globalBestResult;
	private PricingProblem problem;
	private double[] coefficients;
	
	/**
	 * Creates a particle swarm of a user-defined size
	 * @param array The array to solve the problem for
	 * @param antennaNumber The number of antennae
	 * @param coefficients The coefficients for calculating the particle's new positions.
	 * @param size The size of the particle swarms
	 */
	public ParticleSwarm(PricingProblem problem, int numberOfGoods, double[] coefficients, int size) {
		swarm = new Particle[size];
		this.coefficients = coefficients;
		this.problem = problem;

		
		//Creates particle swarm
		for(int i = 0; i < size; i++) {
			swarm[i] = new Particle(numberOfGoods, problem, coefficients);
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
	 * Causes the particles to search the state space to find the best position
	 * @param numberOfSearches
	 * @return
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
					System.out.println("[" + i + "] New Global Best " + globalBestPricing.toString() + " with a revenue of: " + globalBestResult );
					updateSwarmsGlobalBest();
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
	 * Causes the particles to search the state space to find the best position
	 * @param numberOfSearches
	 * @return
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

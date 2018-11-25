package ingramj.particle;

import ingramj.PricingProblem;
import java.util.Random;
import java.util.Arrays;

public class Particle {
	private double[] velocity;
	private double[] position;
	
	private double[] personalBestPricing;
	private double personalBestResult;
	
	private double intertiaCoefficient;
	private double cognitiveCoefficient;
	private double socialCoefficient;
	
	private double[] globalBestPricing;
	private double globalBestResult;
	
	private PricingProblem problem;
	
	public Particle(int numberOfGoods, PricingProblem problem, double[] coefficients){
		this.problem = problem;
		
		//Set coefficients
		intertiaCoefficient = coefficients[0];
		cognitiveCoefficient = coefficients[1];
		socialCoefficient = coefficients[2];
		
		//Generates initial position from randomly generated prices
		double[] initialPosition = new double[numberOfGoods];
		Random rng = new Random();
        for (int i = 0; i < numberOfGoods; i++) {
            initialPosition[i] = rng.nextDouble() * 10;
        }
        
        //Sets current position and best design
        position = initialPosition;
		personalBestPricing = Arrays.copyOf(position, position.length);
		personalBestResult = problem.evaluate(personalBestPricing);
		
		velocity = new double[position.length];
		
		//Creation of a second position to calculate initial velocity.
		double[] secondPosition = new double[numberOfGoods];
	    for (int i = 0; i < numberOfGoods; i++) {
	    	secondPosition[i] = rng.nextDouble() * 10;
	    }
		for(int i = 0; i < velocity.length; i++) {
			velocity[i] = (position[i] + secondPosition[i]) / 2;
			if(i == velocity.length - 1) {
				velocity[i] = 0.0;
			}
		}
		
	}
	
	/**
	 * Updates the particle's position and checks if it's better than the previous position.
	 */
	public void searchSpace(){
		for(int i = 0; i < velocity.length; i++) {
			position[i] = position[i] + velocity[i];
		}
		if(problem.is_valid(position)) {
			double newRevenue = problem.evaluate(position);
			if(personalBestResult < newRevenue) {
				personalBestPricing = position;
				personalBestResult = newRevenue;
			}
		}
	}
	
	
	/**
	 * Calculates the new velocity by summing inertia, social attraction and cognitive attraction
	 * @see calculateInertia
	 * @see calculateCognitiveAttraction
	 * @see calculateSocialAttraction
	 */
	public double[] calculateNewVelocity() {
		double[] newInertiaVector = calculateInertia();
		double[] cognitiveAttractionVector = calculateCognitiveAttraction();
		double[] socialAttractionVector = calculateSocialAttraction();
		double[] newVelocity = new double[velocity.length];
		for(int i = 0; i < velocity.length; i++) {
			velocity[i] = newInertiaVector[i] + cognitiveAttractionVector[i] + socialAttractionVector[i];
			if(i + 1 == velocity.length) {
				newVelocity[i] = 0.0;
			}
		}
		return newVelocity;
	}
	
	/**
	 * Calculates the affect of the current velocity on how it moves to a new position.
	 **/
	private double[] calculateInertia() {
		double[] newInertia = new double[velocity.length];	
		for(int i = 0; i < velocity.length - 1; i++) {
			newInertia[i] = velocity[i] * intertiaCoefficient;
		}
		return newInertia;
	}
	
	/**
	 *	Calculates the attraction to current personal best with some degree of randomness.
	 */
	private double[] calculateCognitiveAttraction() {
		double[] cognitiveAttraction = new double[personalBestPricing.length];
		Random rng = new Random();
		double randomness = rng.nextDouble();
		for(int i = 0; i < personalBestPricing.length; i++) {
			double singleCognitiveAttraction = cognitiveCoefficient * randomness * (personalBestPricing[i] - position[i]);
			cognitiveAttraction[i] = singleCognitiveAttraction;
		}
		return cognitiveAttraction;
	}
	
	/**
	 * Calculates the attraction to the global best with some degree of randomness.
	 */
	private double[] calculateSocialAttraction() {
		double[] socialAttraction = new double[globalBestPricing.length];
		double[] globalBestPosition = globalBestPricing;
		Random rng = new Random();
		double randomness = rng.nextDouble();
		for(int i = 0; i < globalBestPricing.length; i++) {
			socialAttraction[i] = socialCoefficient * randomness * (globalBestPosition[i] - position[i]);
		}
		return socialAttraction;
	}
	
	/**
	 * Returns personal best pricing
	 */
	public double[] getPersonalBestPricing(){
		return personalBestPricing;
	}
	
	/**
	 * Returns personal best result
	 */
	public double getPersonalBestResult() {
		return personalBestResult;
	}
	
	/**
	 * Sets global best solution.
	 * @param pricing The current pricing of the global best
	 * @param result The current result of the global best
	 */
	public void setGlobalBest(double[] pricing, double result) {
		this.globalBestPricing = pricing;
		this.globalBestResult = result;
	}
}

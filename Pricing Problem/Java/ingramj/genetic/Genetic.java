package ingramj.genetic;

import java.util.ArrayList;
import java.util.Random;

import ingramj.PricingProblem;

public class Genetic {
	
	private PricingProblem problem;
	private Tuple<double[], Double>[] population;
	private int generationLimit;
	private int numberOfGoods;
	
	public Genetic(PricingProblem problem, int numberOfGoods, int populationSize, int generationLimit) {
		this.numberOfGoods = numberOfGoods;
		this.population = createPopulation(populationSize, numberOfGoods);
		this.generationLimit = generationLimit;
	}
	
	/**
	 * Generates a population for use in the genetic algorithm.
	 * @param populationSize The size of the desired population
	 * @return A tuple array containing a population of routes.
	 */
	private Tuple<double[], Double>[] createPopulation(int populationSize,  int numberOfGoods){
		Random rng = new Random();
		Tuple<double[], Double>[] population = new Tuple[populationSize];
        //Creates an array of Tuples with a size of population size.
		//Tuple contains Pricing and Revenue from the pricing.
		for(int i = 0; i < populationSize; i++) {
			double[] newPricing = new double[numberOfGoods];
        	for (int j = 0; j < numberOfGoods; j++) {
        		newPricing[j] = rng.nextDouble() * 10;
        	}
        	Tuple<double[], Double> newMember = new Tuple<double[], Double>(newPricing, problem.evaluate(newPricing));
			population[i] = newMember;
        }
		//sortPopulation(newPopulation);
		return population;
	}
	
	/**
	 * Sorts the current population
	 * @param population currentPopulation of the GA.
	 */
	private double[][] sortPopulation(double[][] population){
		boolean changeOccurred = true;
		while(changeOccurred){
			changeOccurred = false;
			for(int i = 0; i < population.length - 1; i++){
				if(problem.evaluate(population[i]) > problem.evaluate(population[i+1])){
					double[] temp = population[i+1];
					population[i+1] = population[i];
					population[i] = temp;
					changeOccurred = true;
				}
			}
		}
		return population;
	}
	
	/**
	 * Chooses the parents for the next generation, based on a 2k tournament search
	 * @param population Current population to pick parents from.
	 * @return
	 */
	private Tuple[] parentSelection(Tuple<double[], Double>[] population) {
		Tuple<double[], Double>[] parents = new Tuple[population.length / 2];
		ArrayList<Tuple<double[], Double>> tournamentMembers = new ArrayList<Tuple<double[], Double>>();
		Random rng = new Random();
		for(int i = 0; i < population.length; i++){
			tournamentMembers.add(population[i]);
		}
		
		//Perform tournament
		for(int i = 0; i < parents.length; i++) {
			Tuple<double[], Double> participantOne = tournamentMembers.remove(rng.nextInt(tournamentMembers.size()));
			Tuple<double[], Double> participantTwo = tournamentMembers.remove(rng.nextInt(tournamentMembers.size()));
			if(participantOne.getItemTwo() < participantTwo.getItemTwo()){
				parents[i] = participantOne;
			}
			else {
				parents[i] = participantTwo;
			}
		}
		return parents;
	}
	
	private Tuple<double[],Double>[] breed(Tuple<double[], Double> parentOne, Tuple<double[],Double> parentTwo){
		Tuple<double[], Double> childOne = new Tuple<double[], Double>(new double[numberOfGoods], 0.0);
		Tuple<double[], Double> childTwo = new Tuple<double[], Double>(new double[numberOfGoods], 0.0);
		Random rng = new Random();
		
		//uniform crossover - with bias to better parent
		Tuple<double[], Double> bestParent;
		Tuple<double[], Double> worstParent;
		boolean betterParentPresent = false; //Used to mark if one parent is better than another.
		if(parentOne.getItemTwo() < parentTwo.getItemTwo()) {
			bestParent = parentTwo;
			worstParent = parentOne;
			betterParentPresent = true;
		}
		else if(parentTwo.getItemTwo() < parentOne.getItemTwo()){
			bestParent = parentOne;
			worstParent = parentTwo;
		}
		else {
			bestParent = parentOne;
			worstParent = parentTwo;
		}
		
		//For each index in child array, chooses whether to inherit from better parent or worst parent - with 60:40 bias toward better parent.
		if(betterParentPresent) {
			//Loop to create child one
			for(int i = 0; i < childOne.getItemOne().length; i++){
				int chosenParent = rng.nextInt(101);
				if(chosenParent < 60) {
					childOne.getItemOne()[i] = bestParent.getItemOne()[i];
				}
				else {
					childOne.getItemOne()[i] = worstParent.getItemOne()[i];
				}
			}
			
			//Loop to create child two
			for(int i = 0; i < childTwo.getItemOne().length; i++){
				int chosenParent = rng.nextInt(101);
				if(chosenParent < 60) {
					childTwo.getItemOne()[i] = bestParent.getItemOne()[i];
				}
				else {
					childTwo.getItemOne()[i] = worstParent.getItemOne()[i];
				}
			}
		}
		
		//If no better parent is present - randomly inherits from each parent for each index.
		else {
			//Loop to create child one
			for(int i = 0; i < childOne.getItemOne().length; i++){
				int chosenParent = rng.nextInt(2);
				if(chosenParent == 0) {
					childOne.getItemOne()[i] = bestParent.getItemOne()[i];
				}
				else if(chosenParent == 1) {
					childOne.getItemOne()[i] = worstParent.getItemOne()[i];
				}
			}
			
			//Loop to create child two
			for(int i = 0; i < childTwo.getItemOne().length; i++){
				int chosenParent = rng.nextInt(2);
				if(chosenParent == 0) {
					childTwo.getItemOne()[i] = bestParent.getItemOne()[i];
				}
				else if(chosenParent == 1) {
					childTwo.getItemOne()[i] = worstParent.getItemOne()[i];
				}
			}
		}
		
		Tuple[] children = {childOne, childTwo};
		return children;
	}

}

package ingramj.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ingramj.PricingProblem;
import ingramj.Tuple;

/**
 * An implementation of a genetic algorithm for the Pricing Problem
 * @author ingramj
 *
 */
public class Genetic {
	
	private PricingProblem problem;
	private Tuple<double[], Double>[] population;
	private int numberOfGoods;
	private int biasToBest;
	
	/**
	 * Creates a genetic algorithm population with specific configurations
	 * @param problem The pricing problem
	 * @param numberOfGoods The number of goods in the pricing problem
	 * @param populationSize The size of the population for the genetic algorithm.
	 * @param biasToBest The bias to inherit from the best parent. Set to 50 for no bias to either parent.
	 */
	public Genetic(PricingProblem problem, int numberOfGoods, int populationSize, int biasToBest) {
		this.problem = problem;
		this.numberOfGoods = numberOfGoods;
		this.population = createPopulation(populationSize, numberOfGoods);
		this.biasToBest = biasToBest;
	}
	
	/**
	 * Performs a search using a genetic algorithm for a specified number of iterations.
	 * @param timeRestraint The number of iterations to run the search for before termination.
	 * @return The best solution.
	 */
	public Tuple<double[], Double> geneticSearch(int generationLimit){
		for(int i = 0; i < generationLimit; i++) {
			Tuple<double[], Double>[] parents = parentSelection(population);
			Tuple[] nextGeneration = createNextGeneration(parents);
			//Replaces the worst chromosome from the new generation with the best from the previous. 
			nextGeneration[nextGeneration.length - 1] = population[0];
			population = nextGeneration;
			sortPopulation(population);
			System.out.println("[Gen: " + (i) + "] Current best pricing has a revenue of " + population[0].getItemTwo());
		}
		return population[0];
	}
	
	/**
	 * Performs a search using a genetic algorithm for a specified number of seconds
	 * @param timeRestraint The number of seconds to run the search for before termination.
	 * @return The best solution.
	 */
	public Tuple<double[], Double> timeRestrainedGeneticSearch(int timeRestraint){
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		long timeDifference = (now - start) / 1000;
		int counter = 0;
		while(timeDifference < timeRestraint) {
			Tuple<double[], Double>[] parents = parentSelection(population);
			Tuple[] nextGeneration = createNextGeneration(parents);
			//Replaces the worst chromosome from the new generation with the best from the previous.
			now = System.currentTimeMillis();
			timeDifference = (now - start) / 1000;
			if(timeDifference < timeRestraint) {
				nextGeneration[nextGeneration.length - 1] = population[0];	
				population = nextGeneration;
				sortPopulation(population);
			}
			System.out.println("[Gen: " + (counter) + "] Current best pricing has a revenue of " + population[0].getItemTwo());
			now = System.currentTimeMillis();
			timeDifference = (now - start) / 1000;
			counter++;
		}
		System.out.println("Completed in " + timeDifference + " seconds");
		return population[0];
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
		sortPopulation(population);
		return population;
	}
	
	/**
	 * Sorts the current population
	 * @param population currentPopulation of the GA.
	 */
	private Tuple<double[], Double>[] sortPopulation(Tuple<double[], Double>[]  population){
		boolean changeOccurred = true;
		while(changeOccurred){
			changeOccurred = false;
			for(int i = 0; i < population.length - 1; i++){
				if(problem.evaluate(population[i].getItemOne()) < problem.evaluate(population[i+1].getItemOne())){
					Tuple<double[], Double> temp = population[i+1];
					population[i+1] = population[i];
					population[i] = temp;
					changeOccurred = true;
				}
			}
		}
		return population;
	}
	
	/**
	 * Creates a new generation by performing crossover and mutation.
	 * @param parents The parents from the previous generation.
	 * @return An array of solutions to use as the next generation.
	 */
	private Tuple[] createNextGeneration(Tuple<double[], Double>[] parents){
		ArrayList<Tuple<double[],Double>> newGeneration = new ArrayList<Tuple<double[],Double>>();
		Tuple[] nextGeneration = new Tuple[population.length];
		
		//Creates children from parents.
		for(int i = 0; i < parents.length; i++) {
			Tuple[] children = new Tuple[2];
			if(i+1 < parents.length) {
				children = breed(parents[i], parents[i + 1]);
			}
			else {
				children = breed(parents[i], parents[i - 1]);
			}
			newGeneration.add(children[0]);
			newGeneration.add(children[1]);
		}
		for(int i = 0; i < newGeneration.size(); i++) {
			nextGeneration[i] = newGeneration.get(i);
		}
		nextGeneration = sortPopulation(nextGeneration);
		return nextGeneration;
	}
	
	/**
	 * Chooses the parents for the next generation, based on a 2k tournament search
	 * @param population Current population to pick parents from.
	 * @return An array of parent solutions.
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
				parents[i] = participantTwo;
			}
			else {
				parents[i] = participantOne;
			}
		}
		return parents;
	}
	
	/**
	 * Performs uniform crossover between two parents, randomly selecting genotypes from a parent for each genotype, with some biases if set.
	 * @param parentOne 
	 * @param parentTwo
	 * @return The two new children.
	 */
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
			betterParentPresent = true;
		}
		else {
			bestParent = parentOne;
			worstParent = parentTwo;
		}
		
		//For each index in child array, chooses whether to inherit from better parent or worst parent - with bias toward better parent, defined by the user.
		if(betterParentPresent) {
			//Loop to create child one
			for(int i = 0; i < childOne.getItemOne().length; i++){
				int chosenParent = rng.nextInt(101);
				if(chosenParent < biasToBest) {
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
		childOne = mutate(childOne, 0.3);
		childTwo = mutate(childTwo, 0.3);
		childOne.setItemTwo(problem.evaluate(childOne.getItemOne()));
		childTwo.setItemTwo(problem.evaluate(childTwo.getItemOne()));
		Tuple[] children = {childOne, childTwo};
		return children;
	}
	
	/**
	 * Mutates a child by adding the result of Random.nextGaussian() to each genotype within the child.
	 * @param child The child to mutate
	 * @param mutationProbability The probability of the child mutating
	 * @return The mutated child and its revenue.
	 */
	private Tuple<double[], Double> mutate(Tuple<double[], Double> child, double mutationProbability){
		Random rng = new Random();
		double[] mutatedPrice = new double[numberOfGoods];
		if(rng.nextDouble() < mutationProbability){
			//If child chosen to mutate, all prices in the list will slightly mutate by adding the result of nextGaussian to each index
			//This ensures that although each item in the price list mutates, it will only slightly mutate.
			for(int i = 0; i < numberOfGoods; i++){
				mutatedPrice[i] = child.getItemOne()[i] + rng.nextGaussian();
			}
			Tuple<double[], Double> mutatedChild = new Tuple<double[], Double>(mutatedPrice, problem.evaluate(mutatedPrice));
			return mutatedChild;
		}
		else {
			return child;
		}

	}

}

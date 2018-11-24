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
		String[] childOneRouteArray = new String[parentOneRouteArray.length];
		String[] childTwoRouteArray = new String[parentTwoRouteArray.length];
		
		//Copy a random selection from one parent. Mask those cities in the other parent.
		//Place the selection in a set part of the child. 
		//Add the non-masked children from parent two in the order they are found.
		int startOfRouteSubset = rng.nextInt(parentOneRouteArray.length - (parentOneRouteArray.length / 2));
		String[] parentOneSubstring = new String[parentOneRouteArray.length];
		String[] parentTwoSubstring = new String[parentTwoRouteArray.length];
		
		//Inserts the substrings at the appropriate locations in the two children.
		for(int i = startOfRouteSubset; i < startOfRouteSubset + (parentOneRouteArray.length / 2); i++) {
			childOneRouteArray[i] = parentOneRouteArray[i];
			childTwoRouteArray[i] = parentTwoRouteArray[i];
		}
		
		//Finish creating child one
		for(int i = 0; i < parentOneRouteArray.length; i++) {
			boolean alreadyContained = false;
			Integer nextEmptyCell = null;
			for(int j = 0; j < childOneRouteArray.length; j++) {
				if(childOneRouteArray[j] == null && nextEmptyCell == null) {
					nextEmptyCell = j;
					break;
				}
			}
			for(int j = 0; j < childOneRouteArray.length; j++) {
				if(parentTwoRouteArray[i].equals(childOneRouteArray[j])){
					alreadyContained = true;
				}
			}
			if(alreadyContained == false) {
				//TODO NEED TO ADD IT IN THE NEXT EMPTY CELL NOT 'I'
				childOneRouteArray[nextEmptyCell] = parentTwoRouteArray[i];
			}
		}
		
		//Finish creating child two
		for(int i = 0; i < parentTwoRouteArray.length; i++) {
			boolean alreadyContained = false;
			Integer nextEmptyCell = null;
			for(int j = 0; j < childTwoRouteArray.length; j++) {
				if(childTwoRouteArray[j] == null && nextEmptyCell == null) {
					nextEmptyCell = j;
					break;
				}
			}
			for(int j = 0; j < childTwoRouteArray.length; j++) {
				if(parentOneRouteArray[i].equals(childTwoRouteArray[j])){
					alreadyContained = true;
				}
			}
			if(alreadyContained == false) {
				childTwoRouteArray[nextEmptyCell] = parentOneRouteArray[i];
			}
		}
		
		//Mutate the children
		childOneRouteArray = mutate(childOneRouteArray, 0.7);
		childTwoRouteArray = mutate(childTwoRouteArray, 0.7);
		
		//Add arrows back into route array
		String childOneRoute = "";
		String childTwoRoute = "";
		for(int i = 0; i < childOneRouteArray.length; i++) {
			if(i+1 != childOneRouteArray.length){
				childOneRoute += childOneRouteArray[i] + "->";
				childTwoRoute += childTwoRouteArray[i] + "->";
			}
			else {
				childOneRoute += childOneRouteArray[i];
				childTwoRoute += childTwoRouteArray[i];
			}
		}

}

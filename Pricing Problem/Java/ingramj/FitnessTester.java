package ingramj;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

import ingramj.genetic.Genetic;
import ingramj.particle.ParticleSwarm;

/**
 * @author pete
 */
public class FitnessTester {
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Starting CI-Pricing Problem...");
        int numberOfGoods = 20;
        Scanner input = new Scanner(System.in);
        // We are allowed to assume that prices may be anything from 1p to �10 per item.
        
        PricingProblem f = PricingProblem.courseworkInstance();
        
        //PricingProblem f = PricingProblem.randomInstance(20);
        System.out.println("\n*****CI-Pricing Problem...*****");
        System.out.println("Which mode would you like to run CI-Pricing Problem in? (Enter the number): ");
        System.out.println("[1] Genetic Tests (allows multiple configs, runs, and prints result to file).");
        System.out.println("[2] PSO Tests (allows multiple configs, runs, and prints result to file).");
        System.out.println("[3] Comparison Tests (runs PSO and Genetic multiple times, and prints result to file).");
        System.out.println("[4] Exit.");
        int runningMode = input.nextInt();
        input.nextLine();
        
		int numberOfGAAndPSOTests = 100; //Number of tests when just running the PSO or GA tests 
		int secondsToRun = 10;
        switch(runningMode) {
        	//Genetic algorithm tests
        	case 1:
        	{
        		System.out.println("Genetic Tests selected...");
        		System.out.println("This will run a series of tests for genetic algorithms.");
        		System.out.println("These tests will check the results of biases and population sizes on gentic algorithms");
        		System.out.println("Each test will run for 10 seconds 100 times.");
        		System.out.println("\t* A run of 10 seconds, with a 60:40 bias (bestParent:worstParent), population of 50");
        		System.out.println("\t* A run of 10 seconds, with a 40:60 bias (worstParent:bestParent), population of 50");
        		System.out.println("\t* A run of 10 seconds, with no bias to either parent, population of 50");
        		System.out.println("\t* A run of 10 seconds, with a 60:40 bias (bestParent:worstParent), population of 100");
        		System.out.println("\t* A run of 10 seconds, with a 40:60 bias (worstParent:bestParent), population of 100");
        		System.out.println("\t* A run of 10 seconds, with no bias to either parent, population of 100");
        		System.out.println("\t* A run of 10 seconds, with a 60:40 bias (bestParent:worstParent), population of 250");
        		System.out.println("\t* A run of 10 seconds, with a 40:60 bias (worstParent:bestParent), population of 250");
        		System.out.println("\t* A run of 10 seconds, with no bias to either parent, population of 250");
        		System.out.println("\t* A run of 10 seconds, with a 60:40 bias (bestParent:worstParent), population of 500");
        		System.out.println("\t* A run of 10 seconds, with a 40:60 bias (worstParent:bestParent), population of 500");
        		System.out.println("\t* A run of 10 seconds, with no bias to either parent, population of 500");
        		System.out.println("This will take approximately " + (12 * (secondsToRun * numberOfGAAndPSOTests)) + " seconds to run.");
        		System.out.println("Press [ENTER] to begin...");
        		input.nextLine();
        		
        		//Result arrays for populations of 50
        		double[] biasToBestPop50Results = new double[numberOfGAAndPSOTests];
        		double[] biasToWorstPop50Results = new double[numberOfGAAndPSOTests];
        		double[] noBiasPop50Results = new double[numberOfGAAndPSOTests];
        		
        		//Result arrays for populations of 100
        		double[] biasToBestPop100Results = new double[numberOfGAAndPSOTests];
        		double[] biasToWorstPop100Results = new double[numberOfGAAndPSOTests];
        		double[] noBiasPop100Results = new double[numberOfGAAndPSOTests];
        		
        		//Result arrays for populations of 250
        		double[] biasToBestPop250Results = new double[numberOfGAAndPSOTests];
        		double[] biasToWorstPop250Results = new double[numberOfGAAndPSOTests];
        		double[] noBiasPop250Results = new double[numberOfGAAndPSOTests];

        		//Result arrays for populations of 500
        		double[] biasToBestPop500Results = new double[numberOfGAAndPSOTests];
        		double[] biasToWorstPop500Results = new double[numberOfGAAndPSOTests];
        		double[] noBiasPop500Results = new double[numberOfGAAndPSOTests];
        		
        		//Runs tests for a specified number of times, for each configuration.
        		for(int i = 0; i < numberOfGAAndPSOTests; i++){
        			//Run tests for populations of 50
        			Genetic biasToBestPop50 = new Genetic(f, 20, 50, 60);
        			biasToBestPop50Results[i] = biasToBestPop50.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic biasToWorstPop50 = new Genetic(f, 20, 50, 40);
        			biasToWorstPop50Results[i] = biasToWorstPop50.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic noBiasPop50 = new Genetic(f, 20, 50, 50);
        			noBiasPop50Results[i] = noBiasPop50.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
            		
        			//Run tests for populations of 100
        			Genetic biasToBestPop100 = new Genetic(f, 20, 100, 60);
        			biasToBestPop100Results[i] = biasToBestPop100.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic biasToWorstPop100 = new Genetic(f, 20, 100, 40);
        			biasToWorstPop100Results[i] = biasToWorstPop100.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic noBiasPop100 = new Genetic(f, 20, 100, 50);
            		noBiasPop100Results[i] = noBiasPop100.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
            		
        			//Run tests for populations of 250
        			Genetic biasToBestPop250 = new Genetic(f, 20, 250, 60);
            		biasToBestPop250Results[i] = biasToBestPop250.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic biasToWorstPop250 = new Genetic(f, 20, 250, 40);
            		biasToWorstPop250Results[i] = biasToWorstPop250.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic noBiasPop250 = new Genetic(f, 20, 250, 50);
            		noBiasPop250Results[i] = noBiasPop250.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
            		
        			//Run tests for populations of 500
        			Genetic biasToBestPop500 = new Genetic(f, 20, 500, 60);
            		biasToBestPop500Results[i] = biasToBestPop500.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
            		Genetic biasToWorstPop500 = new Genetic(f, 20, 500, 40);
            		biasToWorstPop500Results[i] = biasToWorstPop500.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();
        			Genetic noBiasPop500 = new Genetic(f, 20, 500, 50);
            		noBiasPop500Results[i] = noBiasPop500.timeRestrainedGeneticSearch(secondsToRun).getItemTwo();	
        		}
        		PrintWriter gaWriter = new PrintWriter("GA_Test_Results.csv", "UTF-8");
        		gaWriter.print("Genetic Results (60:40) Pop 50,Genetic Results (40:60) Pop 50,Genetic Results (50:50) Pop 50," +
        				"Genetic Results (60:40) Pop 100,Genetic Results (40:60) Pop 100,Genetic Results (50:50) Pop 100," +
        				"Genetic Results (60:40) Pop 250,Genetic Results (40:60) Pop 250,Genetic Results (50:50) Pop 250," +
        				"Genetic Results (60:40) Pop 500,Genetic Results (40:60) Pop 500,Genetic Results (50:50) Pop 500\n");
        		for(int i = 0; i < numberOfGAAndPSOTests; i++) {
              		gaWriter.print(biasToBestPop50Results[i] + "," + biasToWorstPop50Results[i] + "," + noBiasPop50Results[i]   + "," +
              				biasToBestPop100Results[i] + "," + biasToWorstPop100Results[i] + "," + noBiasPop100Results[i]   + "," +
              				biasToBestPop250Results[i] + "," + biasToWorstPop250Results[i] + "," + noBiasPop250Results[i]   + "," +
              				biasToBestPop500Results[i] + "," + biasToWorstPop500Results[i] + "," + noBiasPop500Results[i] + "\n");
        		}
                gaWriter.close();
                System.out.println("Genetic Tests completed!");
        		break;
        	}
        	//PSO Tests
        	case 2:
        	{
        		System.out.println("PSO Tests Selected...");
        		System.out.println("This will run a series of tests for particle swarm optimisation algorithms.");
        		System.out.println("These tests will check the results of biases for coefficients and swarm sizes for particle swarm");
        		System.out.println("Each test will run for 10 seconds 100 times.");
        		System.out.println("Exploration can be defined as: more likely to follow personal best rather than the swarm's best");
        		System.out.println("Exploitation can be defined as: more likely to follow swarm's best");
        		//Populations of 50
        		System.out.println("\t* A run of 10 seconds, with no bias toward exploration or exploitation , population of 50");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploration over exploitation, i.e. higher cognitive coefficient, population of 50");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploitation over exploration, i.e. higher social coefficient, population of 50");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher cognitive coefficient and then changes for a higher social coefficient, population of 50");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher social coefficient and then changes for a higher cognitive coefficient, population of 50");
        		//Populations of 100
        		System.out.println("\t* A run of 10 seconds, with no bias toward exploration or exploitation , population of 100");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploration over exploitation, i.e. higher cognitive coefficient, population of 100");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploitation over exploration, i.e. higher social coefficient, population of 100");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher cognitive coefficient and then changes for a higher social coefficient, population of 100");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher social coefficient and then changes for a higher cognitive coefficient, population of 100");
        		//Populations of 250
        		System.out.println("\t* A run of 10 seconds, with no bias toward exploration or exploitation , population of 250");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploration over exploitation, i.e. higher cognitive coefficient, population of 250");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploitation over exploration, i.e. higher social coefficient, population of 250");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher cognitive coefficient and then changes for a higher social coefficient, population of 250");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher social coefficient and then changes for a higher cognitive coefficient, population of 250");
        		//Populations of 500
        		System.out.println("\t* A run of 10 seconds, with no bias toward exploration or exploitation , population of 500");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploration over exploitation, i.e. higher cognitive coefficient, population of 500");
        		System.out.println("\t* A run of 10 seconds, with a bias toward exploitation over exploration, i.e. higher social coefficient, population of 500");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher cognitive coefficient and then changes for a higher social coefficient, population of 500");
        		System.out.println("\t* A run of 10 seconds, with an 'adaptive coeefficent'. This begins initially with a higher social coefficient and then changes for a higher cognitive coefficient, population of 500");
        		System.out.println("This will take approximately " + (20 * (secondsToRun * numberOfGAAndPSOTests)) + " seconds to run.");
        		System.out.println("Press [ENTER] to begin...");
        		input.nextLine();
        		
        		double[] coefficients = {(1 / (2 * Math.log(2))), (0.5 + Math.log(2)), (0.5 + Math.log(2))};
    			//Results for populations of 50
        		double[]noBias50Results = new double[numberOfGAAndPSOTests];
    			double[]exploreBias50Results = new double[numberOfGAAndPSOTests];
    			double[]exploitBias50Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploreBias50Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploitBias50Results = new double[numberOfGAAndPSOTests];
    			
    			//Results for populations of 100
    			double[]noBias100Results = new double[numberOfGAAndPSOTests];
    			double[]exploreBias100Results = new double[numberOfGAAndPSOTests];
    			double[]exploitBias100Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploreBias100Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploitBias100Results = new double[numberOfGAAndPSOTests];
    			
    			//Results for populations of 250
    			double[]noBias250Results = new double[numberOfGAAndPSOTests];
    			double[]exploreBias250Results = new double[numberOfGAAndPSOTests];
    			double[]exploitBias250Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploreBias250Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploitBias250Results = new double[numberOfGAAndPSOTests];
    			
    			//Results for populations of 500
    			double[]noBias500Results = new double[numberOfGAAndPSOTests];
    			double[]exploreBias500Results = new double[numberOfGAAndPSOTests];
    			double[]exploitBias500Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploreBias500Results = new double[numberOfGAAndPSOTests];
    			double[]adaptiveExploitBias500Results = new double[numberOfGAAndPSOTests];
    			
        		for(int i = 0; i < numberOfGAAndPSOTests; i++){
        			//Populations of 50
        			ParticleSwarm noBias50 = new ParticleSwarm(f, numberOfGoods, coefficients, 50, 0);
        			noBias50Results[i] = noBias50.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploreBias50 = new ParticleSwarm(f, numberOfGoods, coefficients, 50, 1);
        			exploreBias50Results[i] = exploreBias50.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploitBias50 = new ParticleSwarm(f, numberOfGoods, coefficients, 50, 2);
        			exploitBias50Results[i] = exploitBias50.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploreBias50 = new ParticleSwarm(f, numberOfGoods, coefficients, 50, 3);
        			adaptiveExploreBias50Results[i] = adaptiveExploreBias50.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploitBias50 = new ParticleSwarm(f, numberOfGoods, coefficients, 50, 4);
        			adaptiveExploitBias50Results[i] = adaptiveExploitBias50.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			
        			//Populations of 100
        			ParticleSwarm noBias100 = new ParticleSwarm(f, numberOfGoods, coefficients, 100, 0);
        			noBias100Results[i] = noBias100.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploreBias100 = new ParticleSwarm(f, numberOfGoods, coefficients, 100, 1);
        			exploreBias100Results[i] = exploreBias100.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploitBias100 = new ParticleSwarm(f, numberOfGoods, coefficients, 100, 2);
        			exploitBias100Results[i] = exploitBias100.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploreBias100 = new ParticleSwarm(f, numberOfGoods, coefficients, 100, 3);
        			adaptiveExploreBias100Results[i] = adaptiveExploreBias100.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploitBias100 = new ParticleSwarm(f, numberOfGoods, coefficients, 100, 4);
        			adaptiveExploitBias100Results[i] = adaptiveExploitBias100.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			
        			//Populations of 250
        			ParticleSwarm noBias250 = new ParticleSwarm(f, numberOfGoods, coefficients, 250, 0);
        			noBias250Results[i] = noBias250.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploreBias250 = new ParticleSwarm(f, numberOfGoods, coefficients, 250, 1);
        			exploreBias250Results[i] = exploreBias250.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploitBias250 = new ParticleSwarm(f, numberOfGoods, coefficients, 250, 2);
        			exploitBias250Results[i] = exploitBias250.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploreBias250 = new ParticleSwarm(f, numberOfGoods, coefficients, 250, 3);
        			adaptiveExploreBias250Results[i] = adaptiveExploreBias250.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploitBias250 = new ParticleSwarm(f, numberOfGoods, coefficients, 250, 4);
        			adaptiveExploitBias250Results[i] = adaptiveExploitBias250.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			
        			//Populations of 500
        			ParticleSwarm noBias500 = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 0);
        			noBias500Results[i] = noBias500.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploreBias500 = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 1);
        			exploreBias500Results[i] = exploreBias500.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm exploitBias500 = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 2);
        			exploitBias500Results[i] = exploitBias500.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploreBias500 = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 3);
        			adaptiveExploreBias500Results[i] = adaptiveExploreBias500.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        			ParticleSwarm adaptiveExploitBias500 = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 4);
        			adaptiveExploitBias500Results[i] = adaptiveExploitBias500.searchSpaceTimeRestrained(secondsToRun).getItemTwo();
        		}
        		PrintWriter psoWriter = new PrintWriter("PSO_Test_Results.csv", "UTF-8");
        		psoWriter.print("No Bias (Population 50), Explore Bias (Population 50),Exploit Bias (Population 50),Adaptive Bias (Initial Explore Bias) Bias (Population 50), Adaptive Bias (Initial Exploit Bias) Bias (Population 50)," +
        				"No Bias (Population 100), Explore Bias (Population 100), Exploit Bias (Population 100), Adaptive Bias (Initial Explore Bias) Bias (Population 100), Adaptive Bias (Initial Exploit Bias) Bias (Population 100)," +
        				"No Bias (Population 250), Explore Bias (Population 250), Exploit Bias (Population 250), Adaptive Bias (Initial Explore Bias) Bias (Population 250), Adaptive Bias (Initial Exploit Bias) Bias (Population 250)," +
        				"No Bias (Population 500), Explore Bias (Population 500), Exploit Bias (Population 500), Adaptive Bias (Initial Explore Bias) Bias (Population 500), Adaptive Bias (Initial Exploit Bias) Bias (Population 500)\n");
        		for(int i = 0; i < numberOfGAAndPSOTests; i++) {
        			psoWriter.print(noBias50Results[i] + "," + exploreBias50Results[i] + "," + exploitBias50Results[i] + "," + adaptiveExploreBias50Results[i] + "," + adaptiveExploitBias50Results[i] + "," + 
        					noBias100Results[i] + "," + exploreBias100Results[i] + "," + exploitBias100Results[i] + "," + adaptiveExploreBias100Results[i] + "," + adaptiveExploitBias100Results[i] + "," + 
        					noBias250Results[i] + "," + exploreBias250Results[i] + "," + exploitBias250Results[i] + "," + adaptiveExploreBias250Results[i] + "," + adaptiveExploitBias250Results[i] + "," + 
        					noBias500Results[i] + "," + exploreBias500Results[i] + "," + exploitBias500Results[i] + "," + adaptiveExploreBias500Results[i] + "," + adaptiveExploitBias500Results[i] + "\n");
        		}
                psoWriter.close();
                System.out.println("PSO completed!");

        		break;
        	}
    		//Comparisons
        	case 3:
        	{
        		int numberOfComparisonTests = 1000; //Number of tests ran per algorithm when running comparison tests.
        		System.out.println("Comparison Tests Select...");
        		System.out.println("This will run:\n"+
        				"\t* A genetic algorithm with a 60% bias to inherit from the better parent during crossover, with a population size of 50.\n" +
        				"\t* A particle swarm optimisation with adaptive coefficients. The initial bias is attraction to the swarm's best, and then swaps to personal best, with a swarm size of 500");
        		System.out.println("Each test will run for 10 seconds 1000 times.");
        		System.out.println("This will take approximately " + (20 * (secondsToRun * numberOfComparisonTests)) + " seconds to run.");
				System.out.println("Press [ENTER] to begin...");
        		input.nextLine();
        		double[] coefficients = {(1 / (2 * Math.log(2))), (0.5 + Math.log(2)), (0.5 + Math.log(2))};
        		Tuple<double[], Double>[] gaResults = new Tuple[numberOfComparisonTests];
        		Tuple<double[], Double>[] psoResults = new Tuple[numberOfComparisonTests];
        		for(int i = 0; i < numberOfComparisonTests; i++) {
        			//Runs a genetic algorithm with a population, and a 60% chance of inheriting from the best parent during crossover.
        			Genetic gen = new Genetic(f, numberOfGoods, 50,60);
        			gaResults[i] = gen.timeRestrainedGeneticSearch(10);
        			ParticleSwarm pso = new ParticleSwarm(f, numberOfGoods, coefficients, 500, 4);
        			psoResults[i] = pso.searchSpaceTimeRestrained(secondsToRun);
        		}
        		System.out.println("Comparison tests completed!");
        		System.out.println("Writing results to file...");
        		PrintWriter comparisonWriter = new PrintWriter("Comparison_Test_Results.csv", "UTF-8");
        		comparisonWriter.print("Genetic 60% bias to parent (Population 50) , PSO Adaptive with Initial Exploit Bias (Population 500)\n");
        		for(int i = 0; i < numberOfComparisonTests; i++) {
        			comparisonWriter.write(gaResults[i].getItemTwo() + "," + psoResults[i].getItemTwo() + "\n"); 
        		}
        		comparisonWriter.close();
        		System.out.println("File Writing Complete!");
        		System.out.println("Finding best result...");
        		Tuple<double[], Double> bestResult = gaResults[0];
        		boolean geneticBest = true;
        	    for(int i = 0; i < numberOfComparisonTests; i++){
        	    	 //Marks which algorithm produced the best result.
        	    	if(bestResult.getItemTwo() < gaResults[i].getItemTwo()) {
        	    		geneticBest = true;
        	    		bestResult = gaResults[i];
        	    	}
        	    	if(bestResult.getItemTwo() < psoResults[i].getItemTwo()) {
        	    		geneticBest = false;
        	    		bestResult = psoResults[i];
        	    	}
        	    }
                String resultString = "";
                for(int i = 0 ; i < bestResult.getItemOne().length; i++){
                	 resultString += "* " + bestResult.getItemOne()[i] + "\n";
                }
                if(geneticBest) {
                    System.out.print("Best pricing was found by the Genetic Algorithm:\n" + resultString + "\nWith a revenue of: " + bestResult.getItemTwo() + "\n");
                }
                else{
                	System.out.print("Best pricing was found by the Particle Swarm Optimisation:\n" + resultString + "\nWith a revenue of: " + bestResult.getItemTwo() + "\n");
                }
        		
        		break;
        	}
        	case 4:
        		System.exit(0);
        		break;
        }
        
    }
    
    /**
     * Performs a random search of the pricing problem space
     * @param f The pricing problem
     * @param numberOfGoods The number of goods in the pricing problem.
     */
    public static void randomSearch(PricingProblem f, int numberOfGoods){
        Random rng = new Random(0);
        double[] prices = new double[numberOfGoods];
        double[] newPrices = new double[numberOfGoods];
        
        // Generate some random prices, keep the best, see what happens
        for (int i = 0; i < numberOfGoods; i++) {
            prices[i] = rng.nextDouble() * 10;
        }
        double bestRevenue = f.evaluate(prices);
        double newRevenue = 0.0;
        
        for (int iteration = 0; iteration < 100; iteration++) {
        
            //System.out.println("Best revenue so far is " + bestRevenue);
            
            // Generate more!
            for (int i = 0; i < numberOfGoods; i++) {
                newPrices[i] = rng.nextDouble() * 10;
            }
        
            newRevenue = f.evaluate(newPrices);
            if (newRevenue > bestRevenue) {
                for (int i = 0; i < prices.length; i++) {
                    prices[i] = newPrices[i];
                }
                bestRevenue = newRevenue;
            }
            
        }
        
        System.out.println("Final best revenue was " + bestRevenue);
    }
}

package ingramj;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

import ingramj.genetic.Genetic;
import ingramj.genetic.Tuple;
import ingramj.particle.ParticleSwarm;

/**
 *
 * @author pete
 */
public class FitnessTester {
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Starting CI-Pricing Problem...");
        int numberOfGoods = 20;
        Random rng = new Random(0);
        Scanner input = new Scanner(System.in);
        // We are allowed to assume that prices may be anything from 1p to �10 per item.
        
        PricingProblem f = PricingProblem.courseworkInstance();
        
        //PricingProblem f = PricingProblem.randomInstance(20);
        System.out.println("\n*****CI-Pricing Problem...*****");
        System.out.println("Which mode would you like to run CI-Pricing Problem in? (Enter the number): ");
        System.out.println("[1] Random Search for 100 iterations.");
        System.out.println("[2] Genetic Search.");
        System.out.println("[3] Particle Swarm Optimisation (PSO).");
        System.out.println("[4] Genetic Tests (allows multiple configs, runs, and prints result to file).");
        System.out.println("[5] PSO Tests (allows multiple configs, runs, and prints result to file).");
        System.out.println("[6] Comparison Tests (runs PSO and Genetic multiple times, and prints result to file).");
        System.out.println("[7] Exit.");
        int runningMode = input.nextInt();
        input.nextLine();
		int numberOfTests = 100;
		int secondsToRun = 10;
        switch(runningMode) {
        	case 1:
        		randomSearch(f,20);
        		break;
        	case 2:
        		System.out.println("Genetic Algorithm selected.");
        		System.out.print("Enter population size: ");
        		int popSize = 100;
        		try{
            		popSize= input.nextInt();
            		input.nextLine();
        		}
        		catch(Exception e) {
        			System.out.println("Invalid input. Default population size of 100 used.");
        			System.out.println(e.toString());
        		}
        		System.out.println("Would you like a percentage to inherit from the best parent (e.g. 60 = 60 percent change to inherit from best parent)? [Y/N]");
            	String confirmBias = input.nextLine();
            	confirmBias = confirmBias.toUpperCase();
            	int biasRate = 50;
            	if(confirmBias.equals("Y")) {
            		System.out.print("Enter bias (as whole number, e.g. 60 for 60%): ");
            		try {
                		biasRate =  input.nextInt();
                		input.nextLine();
                		while(biasRate < 0 || biasRate > 100) {
                			System.out.println("Please enter a valid bias rate (between 0 - 100)");
                			biasRate =  input.nextInt(); 
                    		input.nextLine();
                		}
                		System.out.println("Bias of " + biasRate + " selected.");
            		}
            		catch(Exception e) {
            			System.out.println("Error on input, default bias of 50 chosen");
            		}
            	}
            	else {
            		System.out.println("No bias selected.");
            	}
            	System.out.println("How many seconds would you like the algorithm to run for?");
            	int runTime = 10; //Default run time of 10 seconds
        		try {
            		runTime =  input.nextInt();
            		input.nextLine();
            		while(runTime <= 0) {
            			System.out.println("Please enter a valid run time of more than 0.");
                		runTime =  input.nextInt();
                		input.nextLine();
            		}
            		System.out.println("Run time of " + runTime + " selected.");
        		}
        		catch(Exception e) {
        			runTime = 10;
        			System.out.println("Error on input, default run time of " + runTime + " selected.");
        		}
            	System.out.print("Running Genetic Algorithm");
                Genetic gen = new Genetic(f, 20, popSize, biasRate);
                gen.timeRestrainedGeneticSearch(runTime);
        		break;
        	case 3:
        		//pso
        		break;
        	case 4:
        		System.out.println("This will run a series of tests for genetic algorithms.");
        		System.out.println("These tests will check the results of biases and population sizes on gentic algorithms");
        		System.out.println("Each test will run 100 times, for 10 seconds.");
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
        		System.out.println("This will take approximately " + (12 * (secondsToRun * numberOfTests)) + " seconds to run.");
        		System.out.println("Press any key to begin...");
        		input.nextLine();
        		
        		//Result arrays for populations of 50
        		double[] biasToBestPop50Results = new double[numberOfTests];
        		double[] biasToWorstPop50Results = new double[numberOfTests];
        		double[] noBiasPop50Results = new double[numberOfTests];
        		
        		//Result arrays for populations of 100
        		double[] biasToBestPop100Results = new double[numberOfTests];
        		double[] biasToWorstPop100Results = new double[numberOfTests];
        		double[] noBiasPop100Results = new double[numberOfTests];
        		
        		//Result arrays for populations of 250
        		double[] biasToBestPop250Results = new double[numberOfTests];
        		double[] biasToWorstPop250Results = new double[numberOfTests];
        		double[] noBiasPop250Results = new double[numberOfTests];

        		//Result arrays for populations of 500
        		double[] biasToBestPop500Results = new double[numberOfTests];
        		double[] biasToWorstPop500Results = new double[numberOfTests];
        		double[] noBiasPop500Results = new double[numberOfTests];
        		
        		//Runs tests for a specified number of times, for each configuration.
        		for(int i = 0; i < numberOfTests; i++){
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
        		PrintWriter writer = new PrintWriter("GA_Test_Results.csv", "UTF-8");
        		writer.print("Genetic Results (60:40) Pop 50,Genetic Results (40:60) Pop 50,Genetic Results (50:50) Pop 50," +
        				"Genetic Results (60:40) Pop 100,Genetic Results (40:60) Pop 100,Genetic Results (50:50) Pop 100," +
        				"Genetic Results (60:40) Pop 250,Genetic Results (40:60) Pop 250,Genetic Results (50:50) Pop 250," +
        				"Genetic Results (60:40) Pop 500,Genetic Results (40:60) Pop 500,Genetic Results (50:50) Pop 500\n");
        		for(int i = 0; i < numberOfTests; i++) {
              		writer.print(biasToBestPop50Results[i] + "," + biasToWorstPop50Results[i] + "," + noBiasPop50Results[i]   + "," +
              				biasToBestPop100Results[i] + "," + biasToWorstPop100Results[i] + "," + noBiasPop100Results[i]   + "," +
              				biasToBestPop250Results[i] + "," + biasToWorstPop250Results[i] + "," + noBiasPop250Results[i]   + "," +
              				biasToBestPop500Results[i] + "," + biasToWorstPop500Results[i] + "," + noBiasPop500Results[i] + "\n");
        		}
                writer.close();
                System.out.println("Genetic Tests completed!");
        		break;
        	case 5:

        		break;
        	
        	case 6:
        		//comparisons
        		break;
        	case 7:
        		System.exit(0);
        		break;
        }
        
        /*
        //Performs a random search - existing code was moved to a new method.
        
		int numberOfTests = 1;
		int secondsToRun = 10;
        Tuple<double[], Double>[] genResults = new Tuple[numberOfTests];
        for(int i = 0; i < genResults.length; i++) {
            Genetic gen = new Genetic(f, 20, 100, 60);
            genResults[i] = gen.timeRestrainedGeneticSearch(secondsToRun);
        }
        
        Tuple<double[], Double>[] psResults = new Tuple[numberOfTests];
		double[] coefficients = {(1 / (2 * Math.log(2))), (0.5 + Math.log(2)), (0.5 + Math.log(2))};
        for(int i = 0; i < psResults.length; i++) {
            ParticleSwarm ps = new ParticleSwarm(f, 20, coefficients,100, 0);
            psResults[i] = ps.searchSpaceTimeRestrained(secondsToRun);
        }
        System.out.println("Tests completed!");
        System.out.println("Adding test results to file...");
        
        //Prints test results to a file.
        PrintWriter writer = new PrintWriter("testResults.csv", "UTF-8");
        writer.print("Genetic Results,Particle Results\n");
        for(int i = 0; i< numberOfTests; i++){
        	writer.print(genResults[i].getItemTwo()+ "," + psResults[i].getItemTwo()+"\n");
        }
        writer.close();
        System.out.println("Completed adding results to file!");
        System.out.println("Finding best result...");
    	Tuple<double[], Double> bestResult = genResults[0];
    	
    	//Finds the best result.
        for(int i = 0; i < numberOfTests; i++) {
        	if(bestResult.getItemTwo() <  genResults[i].getItemTwo() || bestResult.getItemTwo() < genResults[i].getItemTwo()){
        		if(genResults[i].getItemTwo() < psResults[i].getItemTwo()){
        			bestResult = psResults[i];
        		}
        		else {
        			bestResult = genResults[i];
        		}
        	}
        }
        //Lists pricings of the best result.
        String resultString = "";
        for(int i = 0 ; i < bestResult.getItemOne().length; i++){
        	 resultString += "* " + bestResult.getItemOne()[i] + "\n";
        }
        System.out.print("Best priciing found is:\n" + resultString + "\nWith a revenue of: " + bestResult.getItemTwo());
        */
    }
    
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

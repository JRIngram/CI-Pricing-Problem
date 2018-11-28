package ingramj;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import ingramj.genetic.Genetic;
import ingramj.genetic.Tuple;
import ingramj.particle.ParticleSwarm;

/**
 *
 * @author pete
 */
public class FitnessTester {
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        int numberOfGoods = 20;
        Random rng = new Random(0);
        // We are allowed to assume that prices may be anything from 1p to �10 per item.
        
        PricingProblem f = PricingProblem.courseworkInstance();
        //PricingProblem f = PricingProblem.randomInstance(20);
        
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
        
		int numberOfTests = 250;
		int secondsToRun = 10;
        Tuple<double[], Double>[] genResults = new Tuple[numberOfTests];
        for(int i = 0; i < genResults.length; i++) {
            Genetic gen = new Genetic(f, 20, 100);
            genResults[i] = gen.timeRestrainedGeneticSearch(secondsToRun);
        }
        
        Tuple<double[], Double>[] psResults = new Tuple[numberOfTests];
		double[] coefficients = {0.721, 1.1193, 1.1193};
        for(int i = 0; i < psResults.length; i++) {
            ParticleSwarm ps = new ParticleSwarm(f, 20, coefficients,100);
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
        
    }
}

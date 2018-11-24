package ingramj;
import java.util.Random;

import ingramj.genetic.Genetic;
import ingramj.genetic.Tuple;
import ingramj.particle.ParticleSwarm;

/**
 *
 * @author pete
 */
public class FitnessTester {
    
    public static void main(String[] args) {
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
        //particleSwarm(f, 20);
        Tuple[] results = new Tuple[50];
        for(int i = 0; i < 50; i++){
        	System.out.println("***Test " + i + "***");
            Genetic gen = new Genetic(f, 20, 100, 1000);
        	results[i] = gen.GeneticSearch();
        }
        for(int i = 0; i < results.length; i++) {
        	System.out.println(results[i].getItemTwo());
        }
    }
    
    public static void particleSwarm(PricingProblem problem, int numberOfGoods){
		double[] coefficients = {0.721, 1.1193, 1.1193};
    	ParticleSwarm ps = new ParticleSwarm(problem, numberOfGoods, coefficients, 100);
    	ps.searchSpace(100);
    } 
}

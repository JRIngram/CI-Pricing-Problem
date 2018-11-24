#ifndef PRICING_PROBLEM_H
#define PRICING_PROBLEM_H

#include <vector>
#include <functional>

//! Market pricing problem
class PricingProblem{
public:
  /**
   * Creates the instance of pricing problem to be solved in the CS3910
   * coursework.
   * @return The problem instance.
   */
  static PricingProblem courseworkInstance();
  /**
   * Creates a random instance of pricing problem, drawn from the same
   * distribution as the CS3910 coursework.
   * @return The problem instance.
   */
  static PricingProblem randomInstance(int n_goods);
  /*!
   * Creates an evaluation model.
   * @param n_goods The number of goods to model.
   * @param rng A random number generator.
   */
  PricingProblem(int n_goods,std::function<double()>& rng);
  /*!
   * Rectangular bounds on the search space.
   * @return Vector b such that b[i][0] is the minimum permissible value of the
   * ith solution component and b[i][1] is the maximum.
   */
  std::vector<std::vector<double> > bounds();
  /*!
   * Check whether a vector of prices is valid.
   * A valid price vector is one in which all prices are at least 1p and at most
   * £10.00.
   */
  bool is_valid(std::vector<double> prices);
  /*!
   * Gets the total revenue from pricing the goods as given in the parameter.
   * @param prices An array of prices, of length n, where n is the number of
   * goods in the model.
   * @return The total revenue.
   */
   double evaluate(std::vector<double> prices);
private:
  // 0: Linear, 1: Constant elasticity, 2: Fixed demand.
  std::vector<int> priceResponseType; 
  std::vector<std::vector<double> > priceResponse;
  std::vector<std::vector<double> > impact;
  std::vector<std::vector<double> > bnds;
  
  int getDemand(int i, std::vector<double> prices);
  int getGoodDemand(int i, double p);
  int getResidualDemand(int i, std::vector<double> p);
  double getRandomTotalDemand(std::function<double()>& rng);
  double getRandomSatiatingPrice(std::function<double()>& rng);
  double getRandomElasticity(std::function<double()>& rng);
};

#endif /* PRICING_PROBLEM_H */

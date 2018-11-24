#include "PricingProblem.h"

#include <math.h>

#include <iostream>
#include <utility>
#include <random>

template <class T>
std::pair<std::vector<double>,double> random_search(T problem,int max_fevals)
{
  std::default_random_engine engine;
  std::uniform_real_distribution<double> distribution(0.0,1.0);
  
  int fevals = 0;
  std::vector<double> best_solution;
  double best_cost = std::numeric_limits<double>::max();
  while(fevals < max_fevals){
    while(true){
      std::vector<double> candidate;
      for(const auto& b : problem.bounds())
        candidate.push_back((b[1]-b[0])*distribution(engine)+b[0]);
      if(problem.is_valid(candidate)){
        double cost = problem.evaluate(candidate);
        if(cost < best_cost){
          best_cost = cost;
          best_solution = candidate;
        }
        ++fevals;
        break;
      }
    }
  }
  return {best_solution,best_cost};
}
int main(){
  const int number_of_goods = 20;
  unsigned int max_fevals = 100;
  
  //auto problem = PricingProblem::courseworkInstance();
  auto problem = PricingProblem::randomInstance(number_of_goods);

  std::cout<<"\nFinal best value was "<<random_search(problem,max_fevals).second<<"\n";
}

#include "PricingProblem.h"

#include<random>
#include<chrono>
#include<utility>
#include<iostream>
#include<cmath>
#include<string>

PricingProblem PricingProblem::courseworkInstance()
{
  const int n_goods = 20;
  
  const int SEED = 0;
  std::default_random_engine engine(SEED);
  std::uniform_real_distribution<double> distribution(0.0, 1.0);
  std::function<double()> rng = std::bind(distribution,engine);

  return PricingProblem(n_goods,rng);
}

PricingProblem PricingProblem::randomInstance(int n_goods)
{
  static const auto SEED = std::chrono::system_clock::now().time_since_epoch().count();
  static std::default_random_engine engine(SEED);
  static std::uniform_real_distribution<double> distribution(0.0, 1.0);
  static std::function<double()> rng = std::bind(distribution,engine);

  return PricingProblem(n_goods,rng);
}

PricingProblem::PricingProblem(int n,std::function<double()>& rng)
{

  // Set up random price response curves and impacts
  for (int i = 0; i < n; ++i) {
    std::cout<<"Setting up good "+std::to_string(i)+" with type: ";
    double type = rng();
    if (type <= 0.4) {
      // Linear
      priceResponseType.push_back(0);
      priceResponse.push_back(
        {getRandomTotalDemand(rng),getRandomSatiatingPrice(rng)});
      std::cout<<" L ("+std::to_string(priceResponse[i][0])+
        "/"+std::to_string(priceResponse[i][1])+")\n";
      
    } else if (type > 0.4 && type < 0.9) {
      // Constant elasticity
      priceResponseType.push_back(1);
      priceResponse.push_back(
        {getRandomTotalDemand(rng),getRandomElasticity(rng)});
      std::cout<<" CS ("+std::to_string(priceResponse[i][0])+
        "/"+std::to_string(priceResponse[i][1])+")\n";
      
    } else {
      // Fixed demand
      priceResponseType.push_back(2);
      priceResponse.push_back({getRandomTotalDemand(rng),0});
      std::cout<<" FD ("+std::to_string(priceResponse[i][0])+
        "/"+std::to_string(priceResponse[i][1])+")\n";
    }

    impact.push_back({});
    for (int j = 0; j < n; ++j)
      impact[i].push_back(rng()*0.1);
    impact[i][i] = 0.0;
  }
  
  std::vector<double> dim_bnd{0.01,10.0};
  while(bnds.size()<n)
    bnds.push_back(dim_bnd);
}

std::vector<std::vector<double> > PricingProblem::bounds()
{
  return bnds;
}

bool PricingProblem::is_valid(std::vector<double> prices)
{
  if(prices.size() != bounds().size()) return false;
  //All antennae lie within the problem bounds
  for(int i = 0;i<prices.size();++i)
    if(prices[i] < bounds()[i][0] || prices[i] > bounds()[i][1] )
      return false;
  return true;
}

double PricingProblem::evaluate(std::vector<double> prices)
{
  if(prices.size() != bounds().size())
    throw std::runtime_error(
      std::string("PricingProblem::evaluate called on price array of the ") +
      "wrong size. Expected: " + std::to_string(bounds().size()) +
      ". Actual: " +
      std::to_string(prices.size())
    );
  if(!is_valid(prices)) return 0;

  double revenue = 0.0;
  for (size_t i = 0; i < prices.size(); ++i) {
    revenue += getDemand(i, prices) * prices[i];
  }

  return round(revenue*100.0)/100.0;
}

int PricingProblem::getDemand(int i, std::vector<double> prices)
{
  int demand = getGoodDemand(i, prices[i]) + getResidualDemand(i, prices);
    
  // Second sanity check - still cannot have more demand than the market holds
  if (demand > priceResponse[i][0])
    demand = (int)round(priceResponse[i][0]);
  
  return demand;
}

int PricingProblem::getGoodDemand(int i, double p)
{
  double demand = 0.0;
  switch (priceResponseType[i]) {
  case(0): // Linear
    demand = priceResponse[i][0] - ((priceResponse[i][0] / priceResponse[i][1]) * p);
    break;
  case(1): // Constant elasticity
    demand = priceResponse[i][0] / (pow(p, priceResponse[i][1]));
    break;
  case(2): // Fixed demand
    demand = priceResponse[i][0];
    break;
  default:
    std::cout<<"Error! Incorrect price response curve specified!";
  }
  
  // Sanity check - cannot have more demand than the market holds
  if (demand > priceResponse[i][0])
    demand = (int)round(priceResponse[i][0]);
  
  // Or less than 0 demand
  if (demand < 0)
    demand = 0;
  

  return (int)round(demand);
}

int PricingProblem::getResidualDemand(int i, std::vector<double> p)
{
  double demand = 0;
  for (int j = 0; j < priceResponse.size(); ++j)
    if (i != j) 
      demand = demand +  (double)getGoodDemand(j, p[j]) * impact[j][i];
  return (int)round(demand);
}

double PricingProblem::getRandomTotalDemand(std::function<double()>& rng)
{
  return 100.0*rng();
}

double PricingProblem::getRandomSatiatingPrice(std::function<double()>& rng)
{
  return 10.0*rng();
}

double PricingProblem::getRandomElasticity(std::function<double()>& rng)
{
  return rng();
}

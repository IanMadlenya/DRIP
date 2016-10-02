
package org.drip.execution.capture;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for fixed income analysts and developers -
 * 		http://www.credit-trader.org/Begin.html
 * 
 *  DRIP is a free, full featured, fixed income rates, credit, and FX analytics library with a focus towards
 *  	pricing/valuation, risk, and market making.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * LinearImpactBlockTrajectoryEstimator estimates the Price/Cost Distribution associated with the Single
 *  Block Trading Trajectory generated using the Linear Market Impact Evolution Parameters. The References
 *  are:
 * 
 * 	- Almgren, R., and N. Chriss (1999): Value under Liquidation, Risk 12 (12).
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3 (2)
 * 		5-39.
 * 
 * 	- Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs, Journal of Financial Markets,
 * 		1, 1-50.
 *
 * 	- Chan, L. K. C., and J. Lakonishak (1995): The Behavior of Stock Prices around Institutional Trades,
 * 		Journal of Finance, 50, 1147-1174.
 *
 * 	- Keim, D. B., and A. Madhavan (1997): Transaction Costs and Investment Style: An Inter-exchange
 * 		Analysis of Institutional Equity Trades, Journal of Financial Economics, 46, 265-292.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class LinearImpactBlockTrajectoryEstimator extends
	org.drip.execution.capture.TrajectoryShortfallEstimator {

	/**
	 * LinearImpactBlockTrajectoryCost Constructor
	 * 
	 * @param mvttBlock The Block (i.e., Minimum Variance) Trading Trajectory
	 *  
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public LinearImpactBlockTrajectoryEstimator (
		final org.drip.execution.strategy.MinimumVarianceTradingTrajectory mvttBlock)
		throws java.lang.Exception
	{
		super (mvttBlock);
	}

	@Override public org.drip.measure.gaussian.R1UnivariateNormal totalCostDistributionSynopsis (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep)
	{
		if (null == apep) return null;

		org.drip.execution.impact.TransactionFunctionLinear tflTemporaryExpectation =
			((org.drip.execution.dynamics.LinearExpectationParameters) apep).linearTemporaryExpectation();

		org.drip.execution.strategy.MinimumImpactTradingTrajectory mitt =
			(org.drip.execution.strategy.MinimumImpactTradingTrajectory) trajectory();

		double dblBlockSize = mitt.tradeSize();

		try {
			return new org.drip.measure.gaussian.R1UnivariateNormal (tflTemporaryExpectation.offset() *
				java.lang.Math.abs (dblBlockSize) + tflTemporaryExpectation.slope() * dblBlockSize *
					dblBlockSize / mitt.executionTime(), 0.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

package org.drip.execution.optimum;

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
 * AC2000TradingTrajectoryWithDrift contains the Trading Trajectory generated by the Almgren and Chriss
 *  (2000) Scheme under the Criterion of Non-zero Drift. The References are:
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

public class AC2000TradingTrajectoryWithDrift extends org.drip.execution.optimum.AC2000TradingTrajectory {
	private double[] _adblHoldingsDriftAdjustment = null;
	private double[] _adblTradeListDriftAdjustment = null;
	private double _dblResidualHolding = java.lang.Double.NaN;
	private double _dblDriftGainUpperBound = java.lang.Double.NaN;

	/**
	 * AC2000TradingTrajectoryWithDrift Constructor
	 * 
	 * @param adblExecutionTimeNode Array containing the Trajectory Time Nodes
	 * @param adblHoldings Array containing the Holdings
	 * @param adblTradeList Array containing the Trade List
	 * @param adblHoldingsDriftAdjustment Array containing the Holdings Drift Adjustment
	 * @param adblTradeListDriftAdjustment Array containing the Trade List Drift Adjustment
	 * @param dblKappaTilda AC2000 Kappa-Tilda
	 * @param dblKappa AC2000 Kappa
	 * @param dblResidualHolding The Residual Holdings induced by the Drift
	 * @param dblDriftGainUpperBound The Upper Bound of the Gain induced by Drift
	 * @param dblTransactionCostExpectation The Expected Transaction Cost
	 * @param dblTransactionCostVariance The Variance of the Transaction Cost
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public AC2000TradingTrajectoryWithDrift (
		final double[] adblExecutionTimeNode,
		final double[] adblHoldings,
		final double[] adblTradeList,
		final double[] adblHoldingsDriftAdjustment,
		final double[] adblTradeListDriftAdjustment,
		final double dblKappaTilda,
		final double dblKappa,
		final double dblResidualHolding,
		final double dblDriftGainUpperBound,
		final double dblTransactionCostExpectation,
		final double dblTransactionCostVariance)
		throws java.lang.Exception
	{
		super (adblExecutionTimeNode, adblHoldings, adblTradeList, dblKappaTilda, dblKappa,
			dblTransactionCostExpectation, dblTransactionCostVariance);

		if (null == (_adblHoldingsDriftAdjustment = adblHoldingsDriftAdjustment) || null ==
			(_adblTradeListDriftAdjustment = adblTradeListDriftAdjustment) ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblResidualHolding = dblResidualHolding) ||
					!org.drip.quant.common.NumberUtil.IsValid (_dblDriftGainUpperBound =
						dblDriftGainUpperBound))
			throw new java.lang.Exception ("AC2000TradingTrajectoryWithDrift Constructor => Invalid Inputs");

		int iNumNode = _adblHoldingsDriftAdjustment.length;

		if (0 == iNumNode || iNumNode != _adblTradeListDriftAdjustment.length + 1)
			throw new java.lang.Exception ("AC2000TradingTrajectoryWithDrift Constructor => Invalid Inputs");

		for (int i = 0; i < iNumNode; ++i) {
			if (!org.drip.quant.common.NumberUtil.IsValid (_adblHoldingsDriftAdjustment[i]))
				throw new java.lang.Exception
					("AC2000TradingTrajectoryWithDrift Constructor => Invalid Inputs");

			if (0 != i) {
				if (!org.drip.quant.common.NumberUtil.IsValid (_adblTradeListDriftAdjustment[i - 1]))
					throw new java.lang.Exception
						("AC2000TradingTrajectoryWithDrift Constructor => Invalid Inputs");
			}
		}
	}

	/**
	 * Retrieve the Array of the Holdings Drift Adjustment
	 * 
	 * @return The Array of the Holdings Drift Adjustment
	 */

	public double[] holdingsDriftAdjustment()
	{
		return _adblHoldingsDriftAdjustment;
	}

	/**
	 * Retrieve the Array of the Trade List Drift Adjustment
	 * 
	 * @return The Array of the Trade List Drift Adjustment
	 */

	public double[] tradeListDriftAdjustment()
	{
		return _adblTradeListDriftAdjustment;
	}

	/**
	 * Retrieve the Residual Holdings induced by the Drift
	 * 
	 * @return The Residual Holdings induced by the Drift
	 */

	public double residualHolding()
	{
		return _dblResidualHolding;
	}

	/**
	 * Retrieve the Gain Upper Bound induced by the Drift
	 * 
	 * @return The Gain Upper Bound induced by the Drift
	 */

	public double driftGainUpperBound()
	{
		return _dblDriftGainUpperBound;
	}
}

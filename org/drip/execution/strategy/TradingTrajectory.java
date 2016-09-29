
package org.drip.execution.strategy;

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
 * TradingTrajectory holds the Trajectory of a Trading Block that is to be executed over Time. The References
 * 	are:
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

public class TradingTrajectory {
	private double[] _adblHoldings = null;
	private double[] _adblTradeList = null;
	private double[] _adblExecutionTimeNode = null;

	/**
	 * Construct a Standard TradingTrajectory Instance
	 * 
	 * @param adblExecutionTimeNode Array containing the Trajectory Time Nodes
	 * @param adblHoldings Array containing the Holdings
	 * 
	 * @return The TradingTrajectory Instance
	 */

	public static TradingTrajectory Standard (
		final double[] adblExecutionTimeNode,
		final double[] adblHoldings)
	{
		if (null == adblHoldings) return null;

		int iNumExecutionTime = adblHoldings.length;
		double[] adblTradeList = 1 >= iNumExecutionTime ? null : new double[iNumExecutionTime - 1];

		if (1 >= iNumExecutionTime) return null;

		for (int i = 0; i < iNumExecutionTime - 1; ++i)
			adblTradeList[i] = adblHoldings[i + 1] - adblHoldings[i];

		try {
			return new TradingTrajectory (adblExecutionTimeNode, adblHoldings, adblTradeList);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct a Linear TradingTrajectory Instance
	 * 
	 * @param adblExecutionTimeNode Array of the Execution Time Nodes
	 * @param dblStartHoldings Trajectory Start Holdings
	 * @param dblFinishHoldings Trajectory Finish Holdings
	 * 
	 * @return The Linear TradingTrajectory Instance
	 */

	public static final TradingTrajectory Linear (
		final double[] adblExecutionTimeNode,
		final double dblStartHoldings,
		final double dblFinishHoldings)
	{
		if (null == adblExecutionTimeNode || !org.drip.quant.common.NumberUtil.IsValid
			(adblExecutionTimeNode) || !org.drip.quant.common.NumberUtil.IsValid (dblStartHoldings) ||
				!org.drip.quant.common.NumberUtil.IsValid (dblFinishHoldings))
			return null;

		int iNumNode = adblExecutionTimeNode.length;
		double[] adblHoldings = 1 >= iNumNode ? null : new double[iNumNode];
		double dblHoldingsChangeRate = (dblFinishHoldings - dblStartHoldings) /
			(adblExecutionTimeNode[iNumNode - 1] - adblExecutionTimeNode[0]);

		if (1 >= iNumNode) return null;

		for (int i = 0; i < iNumNode; ++i)
			adblHoldings[i] = dblStartHoldings + dblHoldingsChangeRate * i;

		return TradingTrajectory.Standard (adblExecutionTimeNode, adblHoldings);
	}

	/**
	 * TradingTrajectory Constructor
	 * 
	 * @param adblExecutionTimeNode Array containing the Trajectory Time Nodes
	 * @param adblHoldings Array containing the Holdings
	 * @param adblTradeList Array containing the Trade List
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public TradingTrajectory (
		final double[] adblExecutionTimeNode,
		final double[] adblHoldings,
		final double[] adblTradeList)
		throws java.lang.Exception
	{
		if (null == (_adblHoldings = adblHoldings) || null == (_adblTradeList = adblTradeList) || null ==
			(_adblExecutionTimeNode = adblExecutionTimeNode))
			throw new java.lang.Exception ("TradingTrajectory Constructor => Invalid Inputs!");

		int iNumExecutionTime = _adblExecutionTimeNode.length;

		if (1 >= iNumExecutionTime || iNumExecutionTime != _adblHoldings.length || iNumExecutionTime - 1 !=
			_adblTradeList.length || !org.drip.quant.common.NumberUtil.IsValid (_adblHoldings[0]) ||
				!org.drip.quant.common.NumberUtil.IsValid (_adblExecutionTimeNode[0]))
			throw new java.lang.Exception ("TradingTrajectory Constructor => Invalid Inputs!");

		for (int i = 1; i < iNumExecutionTime; ++i) {
			if (!org.drip.quant.common.NumberUtil.IsValid (_adblHoldings[i]) ||
				!org.drip.quant.common.NumberUtil.IsValid (_adblTradeList[i - 1]) ||
					!org.drip.quant.common.NumberUtil.IsValid (_adblExecutionTimeNode[i]) ||
						_adblExecutionTimeNode[i - 1] >= _adblExecutionTimeNode[i])
				throw new java.lang.Exception ("TradingTrajectory Constructor => Invalid Inputs!");
		}
	}

	/**
	 * Retrieve the Array containing the Execution Time Nodes Sequence
	 * 
	 * @return The Array containing the Execution Time Nodes Sequence
	 */

	public double[] executionTimeNode()
	{
		return _adblExecutionTimeNode;
	}

	/**
	 * Retrieve the Trade Size
	 * 
	 * @return The Trade Size
	 */

	public double tradeSize()
	{
		return _adblHoldings[0];
	}

	/**
	 * Retrieve the Array of the Number of Units Outstanding
	 * 
	 * @return The Array of the Number of Units Outstanding
	 */

	public double[] holdings()
	{
		return _adblHoldings;
	}

	/**
	 * Retrieve the Trade List, i.e., the Array of the Number of Units executed
	 * 
	 * @return The Trade List, i.e., the Array of the Number of Units executed
	 */

	public double[] tradeList()
	{
		return _adblTradeList;
	}

	/**
	 * Retrieve the Executed Block Size
	 * 
	 * @return The Executed Block Size
	 */

	public double executedBlockSize()
	{
		return _adblHoldings[_adblHoldings.length - 1] - _adblHoldings[0];
	}

	/**
	 * Retrieve the Execution Time
	 * 
	 * @return The Execution Time
	 */

	public double executionTime()
	{
		return _adblExecutionTimeNode[_adblExecutionTimeNode.length - 1] - _adblExecutionTimeNode[0];
	}

	/**
	 * Retrieve the Number of Trades
	 * 
	 * @return The Number of Trades
	 */

	public int numberOfTrades()
	{
		return _adblHoldings.length - 1;
	}

	/**
	 * Retrieve the Array of the Inner Holdings
	 * 
	 * @return The Array of the Inner Holdings
	 */

	public double[] innerHoldings()
	{
		int iNumInnerHoldings = _adblHoldings.length - 2;
		double[] adblInnerHoldings = new double[iNumInnerHoldings];

		for (int i = 0; i < iNumInnerHoldings; ++i)
			adblInnerHoldings[i] = _adblHoldings[i + 1];

		return adblInnerHoldings;
	}
}

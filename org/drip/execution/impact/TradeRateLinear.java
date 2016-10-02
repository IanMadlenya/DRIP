
package org.drip.execution.impact;

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
 * TradeRateLinear implements a Linear Temporary/Permanent Market Impact Function where the Price Change
 *  scales linearly with the Trade Rate, along with an Offset. The References are:
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
 * 	- Keim, D. B., and A. Madhavan (1997): Transaction Costs and Investment Style: An Inter-exchange Analysis
 * 		of Institutional Equity Trades, Journal of Financial Economics, 46, 265-292.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class TradeRateLinear extends org.drip.execution.impact.TransactionFunctionLinear {
	private double _dblSlope = java.lang.Double.NaN;
	private double _dblOffset = java.lang.Double.NaN;

	/**
	 * Construct a Vanilla Zero-Impact TradeRateLinear Instance
	 * 
	 * @return The Vanilla Zero-Impact TradeRateLinear Instance
	 */

	public static final TradeRateLinear NoImpact()
	{
		try {
			return new TradeRateLinear (0., 0.);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct a Vanilla Slope-Only TradeRateLinear Instance
	 * 
	 * @param dblSlope The Slope
	 *  
	 * @return The Vanilla Slope-Only TradeRateLinear Instance
	 */

	public static final TradeRateLinear SlopeOnly (
		final double dblSlope)
	{
		try {
			return new TradeRateLinear (0., dblSlope);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * TradeRateLinear Constructor
	 * 
	 * @param dblOffset The Offset Market Impact Parameter
	 * @param dblSlope The Linear Market Impact Slope Parameter
	 * 
	 * @throws java.lang.Exception Propagated up from R1ToR1
	 */

	public TradeRateLinear (
		final double dblOffset,
		final double dblSlope)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblOffset = dblOffset) || 0. > _dblOffset ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblSlope = dblSlope) || 0. > _dblSlope)
			throw new java.lang.Exception ("TradeRateLinear Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Linear Market Impact Slope Parameter
	 * 
	 * @return The Linear Market Impact Slope Parameter
	 */

	public double slope()
	{
		return _dblSlope;
	}

	/**
	 * Retrieve the Offset Market Impact Parameter
	 * 
	 * @return The Offset Market Impact Parameter
	 */

	public double offset()
	{
		return _dblOffset;
	}

	@Override public double regularize (
		final double dblTradeInterval)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblTradeInterval) || 0 >= dblTradeInterval)
			throw new java.lang.Exception ("TradeRateLinear::regularize => Invalid Inputs");

		return 1. / dblTradeInterval;
	}

	@Override public double modulate (
		final double dblTradeInterval)
		throws java.lang.Exception
	{
		return 1.;
	}

	@Override public double evaluate  (
		final double dblTradeRate)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblTradeRate))
			throw new java.lang.Exception ("TradeRateLinear::evaluate => Invalid Inputs");

		return (dblTradeRate < 0. ? -1. : 1.) * _dblOffset + _dblSlope * dblTradeRate;
	}

	@Override public double derivative  (
		final double dblTradeRate,
		final int iOrder)
		throws java.lang.Exception
	{
		if (0 >= iOrder) throw new java.lang.Exception ("TradeRateLinear::derivative => Invalid Inputs");

		return 1 == iOrder ? _dblSlope : 0.;
	}
}
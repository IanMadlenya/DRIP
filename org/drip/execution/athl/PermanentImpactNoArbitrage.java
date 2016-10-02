
package org.drip.execution.athl;

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
 * PermanentImpactNoArbitrage implements the Linear Permanent Market Impact with Coefficients that have been
 *  determined empirically by Almgren, Thum, Hauptmann, and Li (2005), using the no Quasi-Arbitrage Criterion
 *  identified by Huberman and Stanzl (2004). The References are:
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3 (2)
 * 		5-39.
 * 
 * 	- Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk,
 * 		Applied Mathematical Finance 10 (1) 1-18.
 *
 * 	- Almgren, R., and N. Chriss (2003): Bidding Principles, Risk 97-102.
 *
 * 	- Huberman, G., and W. Stanzl (2004): Price Manipulation and Quasi-arbitrage, Econometrica 72 (4)
 * 		1247-1275.
 * 
 * 	- Almgren, R., C. Thum, E. Hauptmann, and H. Li (2005): Equity Market Impact, Risk 18 (7) 57-62.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PermanentImpactNoArbitrage extends org.drip.execution.impact.TransactionFunctionLinear {
	private double _dblLiquidityFactor = java.lang.Double.NaN;
	private org.drip.execution.parameters.AssetFlowParameters _afp = null;

	/**
	 * PermanentImpactNoArbitrage Constructor
	 * 
	 * @param afp The Asset Flow Parameters
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PermanentImpactNoArbitrage (
		final org.drip.execution.parameters.AssetFlowParameters afp)
		throws java.lang.Exception
	{
		if (null == (_afp = afp))
			throw new java.lang.Exception ("PermanentImpactNoArbitrage Constructor => Invalid Inputs");

		_dblLiquidityFactor = java.lang.Math.pow (afp.inverseTurnover(),
			org.drip.execution.athl.CalibrationEmpirics.PERMANENT_IMPACT_INVERSE_TURNOVER_EXPONENT);
	}

	/**
	 * Retrieve the Liquidity Factor
	 * 
	 * @return The Liquidity Factor
	 */

	public double liquidityFactor()
	{
		return _dblLiquidityFactor;
	}

	/**
	 * Retrieve the Asset Flow Parameters
	 * 
	 * @return The Asset Flow Parameters
	 */

	public org.drip.execution.parameters.AssetFlowParameters assetFlowParameters()
	{
		return _afp;
	}

	@Override public double regularize (
		final double dblTradeInterval)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblTradeInterval) || 0 >= dblTradeInterval)
			throw new java.lang.Exception ("PermanentImpactNoArbitrage::regularize => Invalid Inputs");

		return 1. / (_afp.averageDailyVolume() * dblTradeInterval);
	}

	@Override public double modulate (
		final double dblTradeInterval)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblTradeInterval) || 0 >= dblTradeInterval)
			throw new java.lang.Exception ("PermanentImpactNoArbitrage::modulate => Invalid Inputs");

		return dblTradeInterval * _afp.dailyVolatility();
	}

	@Override public double slope()
	{
		return org.drip.execution.athl.CalibrationEmpirics.PERMANENT_IMPACT_COEFFICIENT *
			_dblLiquidityFactor;
	}

	@Override public double offset()
	{
		return 0.;
	}

	@Override public double evaluate (
		final double dblNormalizedX)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblNormalizedX))
			throw new java.lang.Exception ("PermanentImpactNoArbitrage::evaluate => Invalid Inputs");

		return 0.5 * org.drip.execution.athl.CalibrationEmpirics.PERMANENT_IMPACT_COEFFICIENT *
			dblNormalizedX * _dblLiquidityFactor;
	}

	@Override public double derivative  (
		final double dblNormalizedX,
		final int iOrder)
		throws java.lang.Exception
	{
		if (0 >= iOrder || !org.drip.quant.common.NumberUtil.IsValid (dblNormalizedX))
			throw new java.lang.Exception ("PermanentImpactNoArbitrage::derivative => Invalid Inputs");

		return 1 < iOrder ? 0. : 0.5 *
			org.drip.execution.athl.CalibrationEmpirics.PERMANENT_IMPACT_COEFFICIENT * _dblLiquidityFactor;
	}
}

package org.drip.execution.evolution;

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
 * MarketImpactComponent exposes the Evolution Increment Components of the Movements exhibited by an Asset's
 *  Manifest Measures owing to either Stochastic or Deterministic Factors. The References are:
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

public class MarketImpactComponent {
	private double _dblPermanentImpact = java.lang.Double.NaN;
	private double _dblTemporaryImpact = java.lang.Double.NaN;
	private double _dblCurrentStepMarketCore = java.lang.Double.NaN;
	private double _dblPreviousStepMarketCore = java.lang.Double.NaN;

	/**
	 * MarketImpactComponent Constructor
	 * 
	 * @param dblCurrentStepMarketCore The Current Step Core Market Volatility Component Contribution
	 * @param dblPreviousStepMarketCore The Previous Step Core Market Volatility Component Contribution
	 * @param dblPermanentImpact The Permanent Market Impact Contribution
	 * @param dblTemporaryImpact The Temporary Market Impact Contribution
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public MarketImpactComponent (
		final double dblCurrentStepMarketCore,
		final double dblPreviousStepMarketCore,
		final double dblPermanentImpact,
		final double dblTemporaryImpact)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblCurrentStepMarketCore = dblCurrentStepMarketCore)
			|| !org.drip.quant.common.NumberUtil.IsValid (_dblPreviousStepMarketCore =
				dblPreviousStepMarketCore) || !org.drip.quant.common.NumberUtil.IsValid (_dblPermanentImpact
					= dblPermanentImpact) || !org.drip.quant.common.NumberUtil.IsValid (_dblTemporaryImpact =
						dblTemporaryImpact))
			throw new java.lang.Exception ("MarketImpactComponent Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Previous Step Market Core Contribution
	 * 
	 * @return The Previous Step Market Core Contribution
	 */

	public double previousStepMarketCore()
	{
		return _dblPreviousStepMarketCore;
	}

	/**
	 * Retrieve the Current Step Market Core Contribution
	 * 
	 * @return The Current Step Market Core Contribution
	 */

	public double currentStepMarketCore()
	{
		return _dblCurrentStepMarketCore;
	}

	/**
	 * Retrieve the Permanent Market Impact Contribution
	 * 
	 * @return The Permanent Market Impact Contribution
	 */

	public double permanentImpact()
	{
		return _dblPermanentImpact;
	}

	/**
	 * Retrieve the Temporary Market Impact Contribution
	 * 
	 * @return The Temporary Market Impact Contribution
	 */

	public double temporaryImpact()
	{
		return _dblTemporaryImpact;
	}

	/**
	 * Retrieve the Total Component Impact
	 * 
	 * @return The Total Component Impact
	 */

	public double total()
	{
		return previousStepMarketCore() + currentStepMarketCore() + permanentImpact() + temporaryImpact();
	}
}

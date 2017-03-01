
package org.drip.sequence.custom;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
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
 * LongestCommonSubsequence contains Variance Bounds on the Critical Measures of the Longest Common
 *  Subsequence between two Strings.
 *
 * @author Lakshmi Krishnamurthy
 */

public class LongestCommonSubsequence extends org.drip.sequence.functional.BoundedMultivariateRandom {

	/**
	 * Lower Bound of the Conjecture of the Expected Value of the LCS Length
	 * 
	 * @param adblVariate Array of Input Variates
	 * 
	 * @return Lower Bound of the Conjecture of the Expected Value of the LCS Length
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double expectationConjectureLowerBound (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		if (null == adblVariate)
			throw new java.lang.Exception
				("LongestCommonSubsequence::expectationConjectureLowerBound => Invalid Inputs");

		return 0.37898;
	}

	/**
	 * Upper Bound of the Conjecture of the Expected Value of the LCS Length
	 * 
	 * @param adblVariate Array of Input Variates
	 * 
	 * @return Upper Bound of the Conjecture of the Expected Value of the LCS Length
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double expectationConjectureUpperBound (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		if (null == adblVariate)
			throw new java.lang.Exception
				("LongestCommonSubsequence::expectationConjectureUpperBound => Invalid Inputs");

		return 0.418815;
	}

	/**
	 * Conjecture of the Expected Value of the LCS Length
	 * 
	 * @param adblVariate Array of Input Variates
	 * 
	 * @return Conjecture of the Expected Value of the LCS Length
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double expectationConjecture (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		if (null == adblVariate)
			throw new java.lang.Exception
				("LongestCommonSubsequence::expectationConjecture => Invalid Inputs");

		return adblVariate.length / (1. + java.lang.Math.sqrt (2.));
	}

	@Override public int dimension()
	{
		return org.drip.function.definition.RdToR1.DIMENSION_NOT_FIXED;
	}

	@Override public double evaluate (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		return 0.25 * (expectationConjectureLowerBound (adblVariate) + expectationConjectureUpperBound
			(adblVariate)) + 0.5 * expectationConjecture (adblVariate);
	}

	@Override public double targetVariateVarianceBound (
		final int iTargetVariateIndex)
		throws java.lang.Exception
	{
		return 1.0;
	}
}

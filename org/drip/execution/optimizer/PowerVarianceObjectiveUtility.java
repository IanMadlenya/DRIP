
package org.drip.execution.optimizer;

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
 * PowerVarianceObjectiveUtility implements the Mean-Power-Variance Objective Utility Function that needs to
 *  be optimized to extract the Optimal Execution Trajectory. The Exact Objective Function is of the Form:
 *  
 *  			U[x] = E[x] + lambda * (V[x] ^p)
 *  
 *  where p > 0. p = 1 is the Regular Mean-Variance, and p = 0.5 is VaR Minimization (L-VaR). The References
 *  are:
 * 
 * 	- Almgren, R., and N. Chriss (1999): Value under Liquidation, Risk 12 (12).
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk, 3 (2),
 * 		5-39.
 * 
 * 	- Almgren, R. (2003): Optimal Execution with Non-linear Impact Functions and Trading Enhanced Risk,
 * 		Applied Mathematical Finance, 10, 1-18.
 *
 * 	- Artzner, P., F. Delbaen, J. M. Eber, and D. Heath (1999): Coherent Measures of Risk, Mathematical
 * 		Finance, 9, 203-228.
 *
 * 	- Basak, S., and A. Shapiro (2001): Value-at-Risk Based Risk Management: Optimal Policies and Asset
 * 		Prices, Review of Financial Studies, 14, 371-405.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PowerVarianceObjectiveUtility implements org.drip.execution.optimizer.ObjectiveUtility {
	private double _dblRiskAversion = java.lang.Double.NaN;
	private double _dblVarianceExponent = java.lang.Double.NaN;

	/**
	 * Generate the Liquidity VaR Version of the Power Variance Utility Function
	 * 
	 * @param dblRiskAversion The Risk Aversion Parameter
	 * 
	 * @return The Liquidity VaR Version of the Power Variance Utility Function
	 */

	public static final PowerVarianceObjectiveUtility LiquidityVaR (
		final double dblRiskAversion)
	{
		try {
			return new PowerVarianceObjectiveUtility (0.5, dblRiskAversion);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * PowerVarianceObjectiveUtility Constructor
	 * 
	 * @param dblVarianceExponent The Variance Exponent
	 * @param dblRiskAversion The Risk Aversion Parameter
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PowerVarianceObjectiveUtility (
		final double dblVarianceExponent,
		final double dblRiskAversion)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblVarianceExponent = dblVarianceExponent) || 0. >
			_dblVarianceExponent || !org.drip.quant.common.NumberUtil.IsValid (_dblRiskAversion =
				dblRiskAversion) || 0. > _dblRiskAversion)
			throw new java.lang.Exception ("PowerVarianceObjectiveUtility Constructor => Invalid Inputs!");
	}

	/**
	 * Retrieve the Risk Aversion Parameter
	 * 
	 * @return The Risk Aversion Parameter
	 */

	public double riskAversion()
	{
		return _dblRiskAversion;
	}

	/**
	 * Retrieve the Variance Exponent
	 * 
	 * @return The Variance Exponent
	 */

	public double varianceExponent()
	{
		return _dblVarianceExponent;
	}

	@Override public org.drip.execution.sensitivity.ControlNodesGreek sensitivity (
		final org.drip.execution.sensitivity.TrajectoryControlNodesGreek tcngExpectation,
		final org.drip.execution.sensitivity.TrajectoryControlNodesGreek tcngVariance)
	{
		if (null == tcngExpectation || null == tcngVariance) return null;

		double[] adblVarianceJacobian = tcngVariance.innerJacobian();

		if (null == adblVarianceJacobian) return null;

		double dblVarianceValue = tcngVariance.value();

		double[][] aadblVarianceHessian = tcngVariance.innerHessian();

		double[] adblExpectationJacobian = tcngExpectation.innerJacobian();

		double[][] aadblExpectationHessian = tcngExpectation.innerHessian();

		int iNumControlNode = adblVarianceJacobian.length;
		double[] adblObjectiveJacobian = new double[iNumControlNode];
		double[][] aadblObjectiveHessian = new double[iNumControlNode][iNumControlNode];

		double dblJacobianMultiplier = _dblVarianceExponent * _dblRiskAversion * java.lang.Math.pow
			(dblVarianceValue, _dblVarianceExponent - 1.);

		double dblJacobianProductMultiplier = _dblVarianceExponent * (_dblVarianceExponent - 1.) *
			_dblRiskAversion * java.lang.Math.pow (dblVarianceValue, _dblVarianceExponent - 2.);

		for (int i = 0; i < iNumControlNode; ++i) {
			adblObjectiveJacobian[i] = adblExpectationJacobian[i] + dblJacobianMultiplier *
				adblVarianceJacobian[i];

			for (int j = 0; j < iNumControlNode; ++j)
				aadblObjectiveHessian[i][j] = aadblExpectationHessian[i][j] + dblJacobianProductMultiplier *
					adblVarianceJacobian[i] * adblVarianceJacobian[j] + dblJacobianMultiplier *
						aadblVarianceHessian[i][j];
		}

		try {
			return new org.drip.execution.sensitivity.ControlNodesGreek (tcngExpectation.value() +
				_dblRiskAversion * java.lang.Math.pow (dblVarianceValue, _dblVarianceExponent),
					adblObjectiveJacobian, aadblObjectiveHessian);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

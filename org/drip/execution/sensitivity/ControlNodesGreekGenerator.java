
package org.drip.execution.sensitivity;

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
 * ControlNodesGreekGenerator exposes the Functionality to compute the Base Value, the Jacobian, and the
 *  Hessian Sensitivities of the Mean and the Variance Contributions to the Permanent Impact, Temporary
 *  Impact, and the Market Core Components. The References are:
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

public interface ControlNodesGreekGenerator {

	/**
	 * Generate the Permanent Impact Expectation Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Permanent Impact Expectation Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek permanentImpactExpectation (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Permanent Impact Variance Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Permanent Impact Variance Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek permanentImpactVariance (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Temporary Impact Expectation Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Temporary Impact Expectation Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek temporaryImpactExpectation (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Temporary Impact Variance Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Temporary Impact Variance Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek temporaryImpactVariance (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Market Core Expectation Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Market Core Expectation Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek marketCoreExpectation (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Market Core Variance Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Market Core Variance Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek marketCoreVariance (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Total Expectation Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Total Expectation Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek expectationContribution (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);

	/**
	 * Generate the Total Variance Contribution
	 * 
	 * @param apep The Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Total Variance Contribution
	 */

	public abstract org.drip.execution.sensitivity.ControlNodesGreek varianceContribution (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep);
}

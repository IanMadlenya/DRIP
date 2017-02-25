
package org.drip.xva.netting;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2017 Lakshmi Krishnamurthy
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
 * CollateralGroupDigest rolls up the Path Realizations of the Sequence in a Single Path Projection Run over
 *  Multiple Collateral Groups onto a Single Netting Group. The References are:
 *  
 *  - Burgard, C., and M. Kjaer (2014): PDE Representations of Derivatives with Bilateral Counter-party Risk
 *  	and Funding Costs, Journal of Credit Risk, 7 (3) 1-19.
 *  
 *  - Burgard, C., and M. Kjaer (2014): In the Balance, Risk, 24 (11) 72-75.
 *  
 *  - Gregory, J. (2009): Being Two-faced over Counter-party Credit Risk, Risk 20 (2) 86-90.
 *  
 *  - Li, B., and Y. Tang (2007): Quantitative Analysis, Derivatives Modeling, and Trading Strategies in the
 *  	Presence of Counter-party Credit Risk for the Fixed Income Market, World Scientific Publishing,
 *  	Singapore.
 * 
 *  - Piterbarg, V. (2010): Funding Beyond Discounting: Collateral Agreements and Derivatives Pricing, Risk
 *  	21 (2) 97-102.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CollateralGroupDigest {
	private org.drip.xva.collateral.GroupTrajectoryPath[] _aGTP = null;

	/**
	 * Generate a "Mono" CollateralGroupDigest Instance
	 * 
	 * @param gtp The "Mono" Collateral Group Path
	 * 
	 * @return The "Mono" CollateralGroupDigest Instance
	 */

	public static final CollateralGroupDigest Mono (
		final org.drip.xva.collateral.GroupTrajectoryPath gtp)
	{
		try {
			return new org.drip.xva.netting.CollateralGroupDigest (new
				org.drip.xva.collateral.GroupTrajectoryPath[] {gtp});
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * CollateralGroupDigest Constructor
	 * 
	 * @param aGTP Array of the Collateral Group Trajectory Paths
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public CollateralGroupDigest (
		final org.drip.xva.collateral.GroupTrajectoryPath[] aGTP)
		throws java.lang.Exception
	{
		if (null == (_aGTP = aGTP) || 0 == _aGTP.length)
			throw new java.lang.Exception ("CollateralGroupDigest Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Array of the Collateral Group Trajectory Paths
	 * 
	 * @return Array of the Collateral Group Trajectory Paths
	 */

	public org.drip.xva.collateral.GroupTrajectoryPath[] collateralGroupTrajectoryPaths()
	{
		return _aGTP;
	}

	/**
	 * Retrieve the Array of the Vertex Dates
	 * 
	 * @return The Array of the Vertex Dates
	 */

	public org.drip.analytics.date.JulianDate[] vertexes()
	{
		org.drip.xva.collateral.GroupTrajectoryEdge[] aGTE = _aGTP[0].edges();

		int iNumVertex = aGTE.length + 1;
		org.drip.analytics.date.JulianDate[] adtVertex = new org.drip.analytics.date.JulianDate[iNumVertex];

		adtVertex[0] = aGTE[0].head().vertex();

		for (int i = 1; i < iNumVertex; ++i)
			adtVertex[i] = aGTE[i - 1].tail().vertex();

		return adtVertex;
	}

	/**
	 * Retrieve the Expected CVA
	 * 
	 * @return The Expected CVA
	 */

	public double cva()
	{
		double dblCVASum = 0.;
		int iNumCollateralGroup = _aGTP.length;

		for (int i = 0; i < iNumCollateralGroup; ++i)
			dblCVASum += _aGTP[i].credit();

		return dblCVASum / iNumCollateralGroup;
	}

	/**
	 * Retrieve the Expected DVA
	 * 
	 * @return The Expected DVA
	 */

	public double dva()
	{
		double dblDVASum = 0.;
		int iNumCollateralGroup = _aGTP.length;

		for (int i = 0; i < iNumCollateralGroup; ++i)
			dblDVASum += _aGTP[i].debt();

		return dblDVASum / iNumCollateralGroup;
	}

	/**
	 * Retrieve the Expected FCA
	 * 
	 * @return The Expected FCA
	 */

	public double fca()
	{
		double dblFCASum = 0.;
		int iNumCollateralGroup = _aGTP.length;

		for (int i = 0; i < iNumCollateralGroup; ++i)
			dblFCASum += _aGTP[i].funding();

		return dblFCASum / iNumCollateralGroup;
	}

	/**
	 * Retrieve the Expected Total VA
	 * 
	 * @return The Expected Total VA
	 */

	public double total()
	{
		double dblTotalSum = 0.;
		int iNumCollateralGroup = _aGTP.length;

		for (int i = 0; i < iNumCollateralGroup; ++i)
			dblTotalSum += _aGTP[i].total();

		return dblTotalSum / iNumCollateralGroup;
	}

	/**
	 * Retrieve the Array of Collateralized Exposures
	 * 
	 * @return The Array of Collateralized Exposures
	 */

	public double[] collateralizedExposure()
	{
		int iNumEdge = _aGTP[0].edges().length;

		int iNumCollateralGroup = _aGTP.length;
		double[] adblCollateralizedExposure = new double[iNumEdge];

		for (int j = 0; j < iNumEdge; ++j)
			adblCollateralizedExposure[j] = 0.;

		for (int iCollateralGroupIndex = 0; iCollateralGroupIndex < iNumCollateralGroup;
			++iCollateralGroupIndex) {
			double[] adblPathCollateralizedExposure = _aGTP[iCollateralGroupIndex].collateralizedExposure();

			for (int iEdgeIndex = 0; iEdgeIndex < iNumEdge; ++iEdgeIndex)
				adblCollateralizedExposure[iEdgeIndex] += adblPathCollateralizedExposure[iEdgeIndex];
		}

		return adblCollateralizedExposure;
	}

	/**
	 * Retrieve the Array of Uncollateralized Exposures
	 * 
	 * @return The Array of Uncollateralized Exposures
	 */

	public double[] uncollateralizedExposure()
	{
		int iNumEdge = _aGTP[0].edges().length;

		int iNumCollateralGroup = _aGTP.length;
		double[] adblUncollateralizedExposure = new double[iNumEdge];

		for (int j = 0; j < iNumEdge; ++j)
			adblUncollateralizedExposure[j] = 0.;

		for (int iCollateralGroupIndex = 0; iCollateralGroupIndex < iNumCollateralGroup;
			++iCollateralGroupIndex) {
			double[] adblPathUncollateralizedExposure =
				_aGTP[iCollateralGroupIndex].uncollateralizedExposure();

			for (int iEdgeIndex = 0; iEdgeIndex < iNumEdge; ++iEdgeIndex)
				adblUncollateralizedExposure[iEdgeIndex] += adblPathUncollateralizedExposure[iEdgeIndex];
		}

		return adblUncollateralizedExposure;
	}

	/**
	 * Retrieve the Array of Collateralized Exposure PV's
	 * 
	 * @return The Array of Collateralized Exposure PV's
	 */

	public double[] collateralizedExposurePV()
	{
		int iNumEdge = _aGTP[0].edges().length;

		int iNumCollateralGroup = _aGTP.length;
		double[] adblCollateralizedExposurePV = new double[iNumEdge];

		for (int j = 0; j < iNumEdge; ++j)
			adblCollateralizedExposurePV[j] = 0.;

		for (int iCollateralGroupIndex = 0; iCollateralGroupIndex < iNumCollateralGroup;
			++iCollateralGroupIndex) {
			double[] adblPathCollateralizedExposurePV =
				_aGTP[iCollateralGroupIndex].collateralizedExposurePV();

			for (int iEdgeIndex = 0; iEdgeIndex < iNumEdge; ++iEdgeIndex)
				adblCollateralizedExposurePV[iEdgeIndex] += adblPathCollateralizedExposurePV[iEdgeIndex];
		}

		return adblCollateralizedExposurePV;
	}

	/**
	 * Retrieve the Array of Uncollateralized Exposure PV's
	 * 
	 * @return The Array of Uncollateralized Exposure PV's
	 */

	public double[] uncollateralizedExposurePV()
	{
		int iNumEdge = _aGTP[0].edges().length;

		int iNumCollateralGroup = _aGTP.length;
		double[] adblUncollateralizedExposurePV = new double[iNumEdge];

		for (int j = 0; j < iNumEdge; ++j)
			adblUncollateralizedExposurePV[j] = 0.;

		for (int iCollateralGroupIndex = 0; iCollateralGroupIndex < iNumCollateralGroup;
			++iCollateralGroupIndex) {
			double[] adblPathUncollateralizedExposurePV =
				_aGTP[iCollateralGroupIndex].uncollateralizedExposurePV();

			for (int iEdgeIndex = 0; iEdgeIndex < iNumEdge; ++iEdgeIndex)
				adblUncollateralizedExposurePV[iEdgeIndex] += adblPathUncollateralizedExposurePV[iEdgeIndex];
		}

		return adblUncollateralizedExposurePV;
	}
}

package org.drip.execution.parameters;

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
 * AssetFlowParameters contains the Asset's Market Flow Parameters that are determined empirically from
 * 	Almgren, Thum, Hauptmann, and Li (2005), using the Parameterization of Almgren (2003). The References
 *  are:
 * 
 * 	- Almgren, R., and N. Chriss (1999): Value under Liquidation, Risk 12 (12).
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3 (2)
 * 		5-39.
 * 
 * 	- Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk,
 * 		Applied Mathematical Finance 10 (1) 1-18.
 *
 * 	- Almgren, R., and N. Chriss (2003): Bidding Principles, Risk 97-102.
 * 
 * 	- Almgren, R., C. Thum, E. Hauptmann, and H. Li (2005): Equity Market Impact, Risk 18 (7) 57-62.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class AssetFlowParameters {
	private java.lang.String _strAssetID = "";
	private double _dblDailyVolatility = java.lang.Double.NaN;
	private double _dblNumberOutstanding = java.lang.Double.NaN;
	private double _dblAverageDailyVolume = java.lang.Double.NaN;

	/**
	 * AssetFlowParameters Constructor
	 * 
	 * @param strAssetID The Asset ID
	 * @param dblAverageDailyVolume The Asset Average Daily Volume
	 * @param dblNumberOutstanding The Number of Trade-able Asset Units Outstanding
	 * @param dblDailyVolatility The Asset Daily Volatility
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public AssetFlowParameters (
		final java.lang.String strAssetID,
		final double dblAverageDailyVolume,
		final double dblNumberOutstanding,
		final double dblDailyVolatility)
		throws java.lang.Exception
	{
		if (null == (_strAssetID = strAssetID) || _strAssetID.isEmpty() ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblAverageDailyVolume = dblAverageDailyVolume) ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblNumberOutstanding = dblNumberOutstanding) ||
					!org.drip.quant.common.NumberUtil.IsValid (_dblDailyVolatility = dblDailyVolatility) ||
						0. >= _dblNumberOutstanding || 0. >= _dblDailyVolatility)
			throw new java.lang.Exception ("AssetFlowParameters Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Asset ID
	 * 
	 * @return The Asset ID
	 */

	public java.lang.String assetID()
	{
		return _strAssetID;
	}

	/**
	 * Retrieve the Average Daily Volume
	 * 
	 * @return The Average Daily Volume
	 */

	public double averageDailyVolume()
	{
		return _dblAverageDailyVolume;
	}

	/**
	 * Retrieve the Daily Volatility
	 * 
	 * @return The Daily Volatility
	 */

	public double dailyVolatility()
	{
		return _dblDailyVolatility;
	}

	/**
	 * Retrieve the Outstanding Number of the Traded Units 
	 * 
	 * @return The Outstanding Number of the Traded Units
	 */

	public double OutstandingUnits()
	{
		return _dblNumberOutstanding;
	}

	/**
	 * Retrieve the Daily Turnover 
	 * 
	 * @return The Daily Turnover
	 */

	public double turnover()
	{
		return _dblAverageDailyVolume / _dblNumberOutstanding;
	}

	/**
	 * Retrieve the Daily Inverse Turnover 
	 * 
	 * @return The Daily Inverse Turnover
	 */

	public double inverseTurnover()
	{
		return _dblNumberOutstanding / _dblAverageDailyVolume;
	}

	/**
	 * Retrieve the Normalized Trade Size
	 * 
	 * @param dblRawTradeSize The Raw Trade Size
	 * @param dblTime The Time
	 * 
	 * @return The Normalized Trade Size
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double normalizeTradeSize (
		final double dblRawTradeSize,
		final double dblTime)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblRawTradeSize) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblTime))
			throw new java.lang.Exception ("AssetFlowParameters::normalizeTradeSize => Invalid Inputs");

		return dblRawTradeSize / (_dblAverageDailyVolume * dblTime);
	}

	/**
	 * De-normalize the Specified Temporary/Permanent Impact
	 * 
	 * @param dblNormalizedImpact The Normalized Impact
	 * 
	 * @return The De-normalized Temporary/Permanent Impact
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double denormalizeImpact (
		final double dblNormalizedImpact)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblNormalizedImpact))
			throw new java.lang.Exception ("AssetFlowParameters::denormalizeImpact => Invalid Inputs");

		return dblNormalizedImpact * _dblDailyVolatility;
	}
}

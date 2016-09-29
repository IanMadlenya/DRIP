
package org.drip.portfolioconstruction.alm;

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
 * NetLiabilityMetrics holds the Results of the Computation of the Net Liability Cash Flows and PV Metrics.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NetLiabilityMetrics {
	private double _dblBasicConsumptionPV = java.lang.Double.NaN;
	private double _dblWorkingAgeIncomePV = java.lang.Double.NaN;
	private double _dblPensionBenefitsIncomePV = java.lang.Double.NaN;
	private java.util.List<org.drip.portfolioconstruction.alm.NetLiabilityCashFlow> _lsNLCF = null;

	/**
	 * NetLiabilityMetrics Constructor
	 * 
	 * @param lsNLCF List of Net Liability Cash Flows
	 * @param dblWorkingAgeIncomePV PV of the Working Age Income
	 * @param dblPensionBenefitsIncomePV PV of the Pension Benefits Income
	 * @param dblBasicConsumptionPV PV of the Basic Consumption
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NetLiabilityMetrics (
		final java.util.List<org.drip.portfolioconstruction.alm.NetLiabilityCashFlow> lsNLCF,
		final double dblWorkingAgeIncomePV,
		final double dblPensionBenefitsIncomePV,
		final double dblBasicConsumptionPV)
		throws java.lang.Exception
	{
		if (null == (_lsNLCF = lsNLCF) || !org.drip.quant.common.NumberUtil.IsValid (_dblWorkingAgeIncomePV =
			dblWorkingAgeIncomePV) || !org.drip.quant.common.NumberUtil.IsValid (_dblPensionBenefitsIncomePV
				= dblPensionBenefitsIncomePV) || !org.drip.quant.common.NumberUtil.IsValid
					(_dblBasicConsumptionPV = dblBasicConsumptionPV))
			throw new java.lang.Exception ("NetLiabilityMetrics Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the List of Net Liability Cash Flows
	 * 
	 * @return The List of Net Liability Cash Flows
	 */

	public java.util.List<org.drip.portfolioconstruction.alm.NetLiabilityCashFlow> cashFlowList()
	{
		return _lsNLCF;
	}

	/**
	 * Retrieve the PV of the Working Age Income
	 * 
	 * @return The PV of the Working Age Income
	 */

	public double workingAgeIncomePV()
	{
		return _dblWorkingAgeIncomePV;
	}

	/**
	 * Retrieve the PV of the Pension Benefits Income
	 * 
	 * @return The PV of the Pension Benefits Income
	 */

	public double pensionBenefitsIncomePV()
	{
		return _dblPensionBenefitsIncomePV;
	}

	/**
	 * Retrieve the PV of the Basic Consumption
	 * 
	 * @return The PV of the Basic Consumption
	 */

	public double basicConsumptionPV()
	{
		return _dblBasicConsumptionPV;
	}
}

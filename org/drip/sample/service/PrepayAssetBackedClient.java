
package org.drip.sample.service;

import org.drip.analytics.date.*;
import org.drip.json.simple.JSONObject;
import org.drip.service.env.EnvManager;
import org.drip.service.json.KeyHoleSkeleton;

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
 * PrepayAssetBackedClient demonstrates the Invocation and Examination of the JSON-based Pre-payable
 *  Constant Payment Asset Backed Loan Service Client.
 *
 * @author Lakshmi Krishnamurthy
 */

public class PrepayAssetBackedClient {

	@SuppressWarnings ("unchecked") public static void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double dblBeginPrincipalFactor = 1.;
		double dblCouponRate = 0.1189;
		double dblServiceFeeRate = 0.00;
		double dblCPR = 0.01;
		double dblBondNotional = 147544.28;
		String strDayCount = "Act/365";
		String strCurrency = "USD";
		int iNumPayment = 48;
		int iPayFrequency = 12;
		double dblFixedMonthlyAmount = 3941.98;

		JulianDate dtEffective = DateUtil.CreateFromYMD (
			2015,
			DateUtil.OCTOBER,
			22
		);

		JulianDate dtSettle = DateUtil.CreateFromYMD (
			2015,
			DateUtil.DECEMBER,
			1
		);

		JSONObject jsonParameters = new JSONObject();

		jsonParameters.put ("Name", "FPMG 11.89 2019");

		jsonParameters.put ("SettleDate", dtSettle.toString());

		jsonParameters.put ("EffectiveDate", dtEffective.toString());

		jsonParameters.put ("BeginPrincipalFactor", dblBeginPrincipalFactor);

		jsonParameters.put ("CouponRate", dblCouponRate);

		jsonParameters.put ("ServiceFeeRate", dblServiceFeeRate);

		jsonParameters.put ("CPR", dblCPR);

		jsonParameters.put ("BondNotional", dblBondNotional);

		jsonParameters.put ("DayCount", strDayCount);

		jsonParameters.put ("NumPayment", iNumPayment);

		jsonParameters.put ("Currency", strCurrency);

		jsonParameters.put ("PayFrequency", iPayFrequency);

		jsonParameters.put ("FixedMonthlyAmount", dblFixedMonthlyAmount);

		JSONObject jsonRequest = new JSONObject();

		jsonRequest.put ("API", "PREPAYASSETBACKED::SECULARMETRICS");

		jsonRequest.put ("Parameters", jsonParameters);

		System.out.println ("\n\t|---------------- JSON REQUEST -----------------|\n");

		System.out.println (jsonRequest.toJSONString());

		System.out.println ("\n\t|---------------- JSON RESPONSE ----------------|\n");

		System.out.println (KeyHoleSkeleton.Thunker (jsonRequest.toJSONString()));
	}
}


package org.drip.sample.forwardratefuturespnl;

import java.util.List;

import org.drip.analytics.date.JulianDate;
import org.drip.feed.loader.*;
import org.drip.historical.attribution.*;
import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.service.product.FundingFuturesAPI;

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
 * EF1Attribution demonstrates the Invocation of the Historical PnL Horizon PnL Attribution analysis for the
 *  EF1 Series.
 *
 * @author Lakshmi Krishnamurthy
 */

public class EF1Attribution {

	public static final void main (
		final String[] args)
		throws Exception
	{
		EnvManager.InitEnv ("");

		String strCurrency = "JPY";
		String strPrintLocation = "C:\\DRIP\\CreditAnalytics\\Daemons\\Transforms\\FundingFuturesCloses\\EF1ClosesReconstitutor.csv";

		CSVGrid csvGrid = CSVParser.StringGrid (
			strPrintLocation,
			true
		);

		JulianDate[] adtSpot = csvGrid.dateArrayAtColumn (0);

		double[] adblForwardRate = csvGrid.doubleArrayAtColumn (2);

		JulianDate[] adtExpiry = csvGrid.dateArrayAtColumn (3);

		List<PositionChangeComponents> lsPCC = FundingFuturesAPI.HorizonChangeAttribution (
			adtSpot,
			adtExpiry,
			adblForwardRate,
			strCurrency
		);

		System.out.println ("FirstDate, SecondDate, Previous DV01, Previous Forward Rate, Spot DV01, Spot Forward Rate, 1D Gross PnL, 1D Market PnL, 1D Roll-down PnL, 1D Accrual PnL, 1D Explained PnL, 1D Unexplianed PnL, Floater Label");

		for (PositionChangeComponents pcc : lsPCC)
			System.out.println (
				pcc.firstDate() + ", " +
				pcc.secondDate() + ", " +
				FormatUtil.FormatDouble (pcc.pmsFirst().r1 ("DV01"), 1, 8, 1.) + ", " +
				FormatUtil.FormatDouble (pcc.pmsFirst().r1 ("ForwardRate"), 1, 8, 100.) + ", " +
				FormatUtil.FormatDouble (pcc.pmsSecond().r1 ("DV01"), 1, 8, 1.) + ", " +
				FormatUtil.FormatDouble (pcc.pmsSecond().r1 ("ForwardRate"), 1, 8, 100.) + ", " +
				FormatUtil.FormatDouble (pcc.grossChange(), 1, 8, 10000.) + ", " +
				FormatUtil.FormatDouble (pcc.marketRealizationChange(), 1, 8, 10000.) + ", " +
				FormatUtil.FormatDouble (pcc.marketRollDownChange(), 1, 8, 10000.) + ", " +
				FormatUtil.FormatDouble (pcc.accrualChange(), 1, 8, 10000.) + ", " +
				FormatUtil.FormatDouble (pcc.explainedChange(), 1, 8, 10000.) + ", " +
				FormatUtil.FormatDouble (pcc.unexplainedChange(), 1, 8, 10000.) + ", " +
				pcc.pmsFirst().c1 ("FloaterLabel")
			);
	}
}

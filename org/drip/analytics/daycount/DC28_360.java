
package org.drip.analytics.daycount;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * This class implements the 28/360 day count convention.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DC28_360 implements org.drip.analytics.daycount.DCFCalculator {

	/**
	 * Empty DC28_360 constructor
	 */

	public DC28_360()
	{
	}

	@Override public java.lang.String baseCalculationType()
	{
		return "DC28_360";
	}

	@Override public java.lang.String[] alternateNames()
	{
		return new java.lang.String[] {"28/360", "DC28_360"};
	}

	@Override public double yearFraction (
		final int iStartDate,
		final int iEndDate,
		final boolean bApplyEOMAdj,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA30_360 (iStartDate, iEndDate, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DC28_360::yearFraction => Cannot create DateEOMAdjustment!");

		return (360.* (org.drip.analytics.date.DateUtil.Year (iEndDate) -
			org.drip.analytics.date.DateUtil.Year (iStartDate)) + 28. *
				(org.drip.analytics.date.DateUtil.Month (iEndDate) - org.drip.analytics.date.DateUtil.Month
					(iStartDate)) + (org.drip.analytics.date.DateUtil.Date (iEndDate) -
						org.drip.analytics.date.DateUtil.Date (iStartDate) + dm.posterior() - dm.anterior()))
							/ 360.;
	}

	@Override public int daysAccrued (
		final int iStartDate,
		final int iEndDate,
		final boolean bApplyEOMAdj,
		final ActActDCParams actactParams,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		DateEOMAdjustment dm = DateEOMAdjustment.MakeDEOMA30_360 (iStartDate, iEndDate, bApplyEOMAdj);

		if (null == dm)
			throw new java.lang.Exception ("DC28_360::daysAccrued => Cannot create DateEOMAdjustment!");

		return 360 * (org.drip.analytics.date.DateUtil.Year (iEndDate) -
			org.drip.analytics.date.DateUtil.Year (iStartDate)) + 28 *
				(org.drip.analytics.date.DateUtil.Month (iEndDate) - org.drip.analytics.date.DateUtil.Month
					(iStartDate)) + (org.drip.analytics.date.DateUtil.Date (iEndDate) -
						org.drip.analytics.date.DateUtil.Date (iStartDate) + dm.posterior() - dm.anterior());
	}
}

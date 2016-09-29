
package org.drip.market.issue;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
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
 * TreasurySetting contains the Definitions of the Settings of different Jurisdiction Treasuries.
 *
 * @author Lakshmi Krishnamurthy
 */

public class TreasurySetting {
	private int _iFrequency = -1;
	private java.lang.String _strCode = "";
	private java.lang.String _strCalendar = "";
	private java.lang.String _strCurrency = "";
	private java.lang.String _strDayCount = "";

	/**
	 * TreasurySetting Constructor
	 * 
	 * @param strCode Treasury Code
	 * @param strCurrency Currency
	 * @param iFrequency Frequency
	 * @param strDayCount Day Count
	 * @param strCalendar Calendar
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public TreasurySetting (
		final java.lang.String strCode,
		final java.lang.String strCurrency,
		final int iFrequency,
		final java.lang.String strDayCount,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == (_strCode = strCode) || _strCode.isEmpty() || null == (_strCurrency = strCurrency) ||
			_strCurrency.isEmpty() || 0 >= (_iFrequency = iFrequency) || null == (_strDayCount =
				strDayCount) || _strDayCount.isEmpty() || null == (_strCalendar = strCalendar) ||
					_strCalendar.isEmpty())
			throw new java.lang.Exception ("TreasurySetting ctr => Invalid Inputs");
	}

	/**
	 * Retrieve the Treasury Code
	 * 
	 * @return The Treasury Code
	 */

	public java.lang.String code()
	{
		return _strCode;
	}

	/**
	 * Retrieve the Currency
	 * 
	 * @return The Currency
	 */

	public java.lang.String currency()
	{
		return _strCurrency;
	}

	/**
	 * Retrieve the Frequency
	 * 
	 * @return The Frequency
	 */

	public int frequency()
	{
		return _iFrequency;
	}

	/**
	 * Retrieve the Day Count
	 * 
	 * @return The Day Count
	 */

	public java.lang.String dayCount()
	{
		return _strDayCount;
	}

	/**
	 * Retrieve the Calendar
	 * 
	 * @return The Calendar
	 */

	public java.lang.String calendar()
	{
		return _strCalendar;
	}
}

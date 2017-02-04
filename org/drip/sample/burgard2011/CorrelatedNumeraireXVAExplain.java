
package org.drip.sample.burgard2011;

import org.drip.measure.discretemarginal.SequenceGenerator;
import org.drip.measure.dynamics.*;
import org.drip.measure.process.*;
import org.drip.measure.realization.*;
import org.drip.quant.common.FormatUtil;
import org.drip.quant.linearalgebra.Matrix;
import org.drip.service.env.EnvManager;
import org.drip.xva.custom.Settings;
import org.drip.xva.definition.*;
import org.drip.xva.derivative.*;
import org.drip.xva.pde.*;

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
 * CorrelatedNumeraireXVAExplain constructs the XVA PnL Explain arising out of the Joint Evolution of
 * 	Numeraires - the Continuous Asset, the Collateral, the Bank, and the Counter-Party Numeraires involved in
 *  the Dynamic XVA Replication Portfolio of the Burgard and Kjaer (2011) Methodology. The References are:
 *  
 *  - Burgard, C., and M. Kjaer (2014): PDE Representations of Derivatives with Bilateral Counter-party Risk
 *  	and Funding Costs, Journal of Credit Risk, 7 (3) 1-19.
 *  
 *  - Cesari, G., J. Aquilina, N. Charpillon, X. Filipovic, G. Lee, and L. Manda (2009): Modeling, Pricing,
 *  	and Hedging Counter-party Credit Exposure - A Technical Guide, Springer Finance, New York.
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

public class CorrelatedNumeraireXVAExplain {

	private static final EdgeEvolutionTrajectory RunStep (
		final TrajectoryEvolutionScheme tes,
		final SpreadIntensity si,
		final BurgardKjaerOperator bko,
		final EdgeEvolutionTrajectory eetStart,
		final JumpDiffusionVertex r1sAsset,
		final JumpDiffusionVertex r1sCollateral,
		final JumpDiffusionVertex r1sBank,
		final JumpDiffusionVertex r1sCounterParty)
		throws Exception
	{
		EdgeAssetGreek eagStart = eetStart.edgeAssetGreek();

		EdgeReplicationPortfolio erpStart = eetStart.replicationPortfolio();

		double dblDerivativeXVAValueStart = eagStart.derivativeXVAValue();

		double dblTimeWidth = tes.timeIncrement();

		double dblTimeStart = eetStart.time();

		double dblTime = dblTimeStart - 0.5 * dblTimeWidth;

		UniverseSnapshot usStart = eetStart.tradeableAssetSnapshot();

		TwoWayRiskyUniverse twru = tes.universe();

		double dblCollateralBondNumeraire = usStart.zeroCouponCollateralBondNumeraire().finish();

		UniverseSnapshot usFinish = new UniverseSnapshot (
			twru.asset().priceNumeraire().weinerIncrement (
				r1sAsset,
				dblTimeWidth
			),
			twru.zeroCouponCollateralBond().priceNumeraire().weinerIncrement (
				r1sCollateral,
				dblTimeWidth
			),
			twru.zeroCouponBankBond().priceNumeraire().weinerIncrement (
				r1sBank,
				dblTimeWidth
			),
			twru.zeroCouponCounterPartyBond().priceNumeraire().weinerIncrement (
				r1sCounterParty,
				dblTimeWidth
			)
		);

		MasterAgreementCloseOut maco = tes.boundaryCondition();

		LevelBurgardKjaerRun lbkr = bko.timeIncrementRun (
			si,
			eetStart
		);

		double dblTheta = lbkr.theta();

		double dblAssetNumeraireBump = lbkr.assetNumeraireBump();

		double dblThetaAssetNumeraireUp = lbkr.thetaAssetNumeraireUp();

		double dblThetaAssetNumeraireDown = lbkr.thetaAssetNumeraireDown();

		double dblDerivativeXVAValueDeltaFinish = eagStart.derivativeXVAValueDelta() +
			0.5 * (dblThetaAssetNumeraireUp - dblThetaAssetNumeraireDown) * dblTimeWidth / dblAssetNumeraireBump;

		double dblDerivativeXVAValueGammaFinish = eagStart.derivativeXVAValueGamma() +
			(dblThetaAssetNumeraireUp + dblThetaAssetNumeraireDown - 2. * dblTheta) * dblTimeWidth /
				(dblAssetNumeraireBump * dblAssetNumeraireBump);

		double dblDerivativeXVAValueFinish = dblDerivativeXVAValueStart - dblTheta * dblTimeWidth;

		double dblGainOnBankDefaultFinish = -1. * (dblDerivativeXVAValueFinish - maco.bankDefault
			(dblDerivativeXVAValueFinish));

		double dblGainOnCounterPartyDefaultFinish = -1. * (dblDerivativeXVAValueFinish - maco.counterPartyDefault
			(dblDerivativeXVAValueFinish));

		org.drip.xva.derivative.LevelCashAccount lca = tes.rebalanceCash (
			eetStart,
			usFinish
		).cashAccount();

		double dblCashAccountAccumulationFinish = lca.accumulation();

		double dblAssetPriceFinish = usFinish.assetNumeraire().finish();

		double dblZeroCouponBankPriceFinish = usFinish.zeroCouponBankBondNumeraire().finish();

		double dblZeroCouponCounterPartyPriceFinish = usFinish.zeroCouponCounterPartyBondNumeraire().finish();

		EdgeReplicationPortfolio erpFinish = new EdgeReplicationPortfolio (
			-1. * dblDerivativeXVAValueDeltaFinish,
			dblGainOnBankDefaultFinish / dblZeroCouponBankPriceFinish,
			dblGainOnCounterPartyDefaultFinish / dblZeroCouponCounterPartyPriceFinish,
			erpStart.cashAccount() + dblCashAccountAccumulationFinish
		);

		System.out.println ("\t||" +
			FormatUtil.FormatDouble (dblTime, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (dblDerivativeXVAValueFinish, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (dblAssetPriceFinish, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (dblZeroCouponBankPriceFinish, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (dblZeroCouponCounterPartyPriceFinish, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (usFinish.zeroCouponCollateralBondNumeraire().finish(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpFinish.assetUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpFinish.bankBondUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpFinish.counterPartyBondUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpFinish.cashAccount(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (dblCashAccountAccumulationFinish, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (lca.assetAccumulation(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (lca.bankAccumulation(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (lca.counterPartyAccumulation(), 1, 6, 1.) + " | " +
			(r1sBank.jumpOccurred() ? "BANK DEFAULT" : "BANK SURVIVE") + " | " +
			(r1sCounterParty.jumpOccurred() ? "CP DEFAULT" : "CP SURVIVE") + " ||"
		);

		return new EdgeEvolutionTrajectory (
			dblTimeStart - dblTimeWidth,
			usFinish,
			erpFinish,
			new EdgeAssetGreek (
				dblDerivativeXVAValueFinish,
				dblDerivativeXVAValueDeltaFinish,
				dblDerivativeXVAValueGammaFinish,
				eagStart.derivativeValue() * Math.exp (
					-1. * dblTimeWidth * twru.zeroCouponCollateralBond().priceNumeraire().evaluator().drift().value (
						new JumpDiffusionVertex (
							dblTime,
							dblCollateralBondNumeraire,
							0.,
							false
						)
					)
				)
			),
			dblGainOnBankDefaultFinish,
			dblGainOnCounterPartyDefaultFinish
		);
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double dblTimeWidth = 1. / 24.;
		double dblTime = 1.;
		double[][] aadblCorrelation = new double[][] {
			{1.00, 0.20, 0.15, 0.05}, // #0 ASSET
			{0.20, 1.00, 0.13, 0.25}, // #1 COLLATERAL
			{0.15, 0.13, 1.00, 0.00}, // #2 BANK
			{0.05, 0.25, 0.00, 1.00}  // #3 COUNTER PARTY
		};
		double dblAssetDrift = 0.06;
		double dblAssetVolatility = 0.15;
		double dblAssetRepo = 0.03;
		double dblAssetDividend = 0.02;
		double dblInitialAssetNumeraire = 1.;

		double dblZeroCouponCollateralBondDrift = 0.01;
		double dblZeroCouponCollateralBondVolatility = 0.05;
		double dblZeroCouponCollateralBondRepo = 0.005;
		double dblInitialCollateralNumeraire = 1.;

		double dblZeroCouponBankBondDrift = 0.03;
		double dblZeroCouponBankBondVolatility = 0.10;
		double dblZeroCouponBankBondRepo = 0.028;
		double dblBankHazardRate = 0.03;
		double dblBankRecoveryRate = 0.45;
		double dblInitialBankNumeraire = 1.;

		double dblZeroCouponCounterPartyBondDrift = 0.03;
		double dblZeroCouponCounterPartyBondVolatility = 0.10;
		double dblZeroCouponCounterPartyBondRepo = 0.028;
		double dblCounterPartyHazardRate = 0.05;
		double dblCounterPartyRecoveryRate = 0.30;
		double dblInitialCounterPartyNumeraire = 1.;

		double dblTerminalXVADerivativeValue = 1.;

		double dblSensitivityShiftFactor = 0.001;

		int iNumTimeStep = (int) (1. / dblTimeWidth);
		double dblDerivativeValue = dblTerminalXVADerivativeValue;
		double dblDerivativeXVAValue = dblTerminalXVADerivativeValue;

		Settings settings = new Settings (
			Settings.CLOSEOUT_GREGORY_LI_TANG,
			dblSensitivityShiftFactor
		);

		MasterAgreementCloseOut maco = new MasterAgreementCloseOut (
			dblBankRecoveryRate,
			dblCounterPartyRecoveryRate
		);

		DiffusionEvolver meAsset = new DiffusionEvolver (
			DiffusionEvaluatorLogarithmic.Standard (
				dblAssetDrift,
				dblAssetVolatility
			)
		);

		DiffusionEvolver meZeroCouponCollateralBond = new DiffusionEvolver (
			DiffusionEvaluatorLogarithmic.Standard (
				dblZeroCouponCollateralBondDrift,
				dblZeroCouponCollateralBondVolatility
			)
		);

		DiffusionEvolver meZeroCouponBankBond = new JumpDiffusionEvolver (
			DiffusionEvaluatorLogarithmic.Standard (
				dblZeroCouponBankBondDrift,
				dblZeroCouponBankBondVolatility
			),
			HazardJumpEvaluator.Standard (
				dblBankHazardRate,
				dblBankRecoveryRate
			)
		);

		DiffusionEvolver meZeroCouponCounterPartyBond = new JumpDiffusionEvolver (
			DiffusionEvaluatorLogarithmic.Standard (
				dblZeroCouponCounterPartyBondDrift,
				dblZeroCouponCounterPartyBondVolatility
			),
			HazardJumpEvaluator.Standard (
				dblCounterPartyHazardRate,
				dblCounterPartyRecoveryRate
			)
		);

		TwoWayRiskyUniverse twru = new TwoWayRiskyUniverse (
			new Equity (
				meAsset,
				dblAssetRepo,
				dblAssetDividend
			),
			new Tradeable (
				meZeroCouponCollateralBond,
				dblZeroCouponCollateralBondRepo
			),
			new Tradeable (
				meZeroCouponBankBond,
				dblZeroCouponBankBondRepo
			),
			new Tradeable (
				meZeroCouponCounterPartyBond,
				dblZeroCouponCounterPartyBondRepo
			)
		);

		TrajectoryEvolutionScheme tes = new TrajectoryEvolutionScheme (
			twru,
			maco,
			settings,
			dblTimeWidth
		);

		BurgardKjaerOperator bko = new BurgardKjaerOperator (
			twru,
			maco,
			settings
		);

		SpreadIntensity si = new SpreadIntensity (
			dblZeroCouponBankBondDrift - dblZeroCouponCollateralBondDrift,
			(dblZeroCouponBankBondDrift - dblZeroCouponCollateralBondDrift) / dblBankRecoveryRate,
			(dblZeroCouponCounterPartyBondDrift - dblZeroCouponCollateralBondDrift) / dblCounterPartyRecoveryRate
		);

		double[][] aadblNumeraireTimeSeries = Matrix.Transpose (
			SequenceGenerator.GaussianJoint (
				iNumTimeStep,
				aadblCorrelation
			)
		);

		double[] adblBankDefaultIndicator = SequenceGenerator.Uniform (iNumTimeStep);

		double[] adblCounterPartyDefaultIndicator = SequenceGenerator.Uniform (iNumTimeStep);

		JumpDiffusionVertex[] aR1SAsset = meAsset.vertexSequence (
			new JumpDiffusionVertex (
				0.,
				dblInitialAssetNumeraire,
				0.,
				false
			),
			UnitRandom.Diffusion (aadblNumeraireTimeSeries[0]),
			dblTimeWidth
		);

		JumpDiffusionVertex[] aR1SCollateral = meZeroCouponCollateralBond.vertexSequence (
			new JumpDiffusionVertex (
				0.,
				dblInitialCollateralNumeraire,
				0.,
				false
			),
			UnitRandom.Diffusion (aadblNumeraireTimeSeries[1]),
			dblTimeWidth
		);

		JumpDiffusionVertex[] aR1SBank = meZeroCouponBankBond.vertexSequence (
			new JumpDiffusionVertex (
				0.,
				dblInitialBankNumeraire,
				0.,
				false
			),
			UnitRandom.JumpDiffusion (
				aadblNumeraireTimeSeries[2],
				adblBankDefaultIndicator
			),
			dblTimeWidth
		);

		JumpDiffusionVertex[] aR1SCounterParty = meZeroCouponCounterPartyBond.vertexSequence (
			new JumpDiffusionVertex (
				0.,
				dblInitialCounterPartyNumeraire,
				0.,
				false
			),
			UnitRandom.JumpDiffusion (
				aadblNumeraireTimeSeries[3],
				adblCounterPartyDefaultIndicator
			),
			dblTimeWidth
		);

		EdgeAssetGreek eagInitial = new EdgeAssetGreek (
			dblDerivativeXVAValue,
			-1.,
			0.,
			dblDerivativeValue
		);

		double dblGainOnBankDefaultInitial = -1. * (dblDerivativeXVAValue - maco.bankDefault (dblDerivativeXVAValue));

		double dblGainOnCounterPartyDefaultInitial = -1. * (dblDerivativeXVAValue - maco.counterPartyDefault (dblDerivativeXVAValue));

		EdgeReplicationPortfolio erpInitial = new EdgeReplicationPortfolio (
			1.,
			dblGainOnBankDefaultInitial,
			dblGainOnCounterPartyDefaultInitial,
			0.
		);

		System.out.println();

		System.out.println ("\t||-----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t||                                            BILATERAL XVA EVOLVER - BURGARD & KJAER (2011) REPLICATION PORTFOLIO EVOLUTION                                             ||");

		System.out.println ("\t||-----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t||    L -> R:                                                                                                                                                            ||");

		System.out.println ("\t||            - Time                                                                                                                                                     ||");

		System.out.println ("\t||            - Derivative XVA Value                                                                                                                                     ||");

		System.out.println ("\t||            - Asset Price Realization                                                                                                                                  ||");

		System.out.println ("\t||            - Realization of the Zero Coupon Bank Bond Price                                                                                                           ||");

		System.out.println ("\t||            - Realization of the Zero Coupon Counter Party Bond Price                                                                                                  ||");

		System.out.println ("\t||            - Realization of the Zero Coupon Collateral Bond Price                                                                                                     ||");

		System.out.println ("\t||            - Derivative XVA Asset Replication Units                                                                                                                   ||");

		System.out.println ("\t||            - Derivative XVA Value Bank Bond Replication Units                                                                                                         ||");

		System.out.println ("\t||            - Derivative XVA Value Counter Party Bond Replication Units                                                                                                ||");

		System.out.println ("\t||            - Derivative XVA Value Cash Account Replication Units                                                                                                      ||");

		System.out.println ("\t||            - Derivative Cash Account Accumulation Component                                                                                                           ||");

		System.out.println ("\t||            - Asset Cash Account Accumulation Component                                                                                                                ||");

		System.out.println ("\t||            - Bank Cash Account Accumulation Component                                                                                                                 ||");

		System.out.println ("\t||            - Counter Party Cash Account Accumulation Component                                                                                                        ||");

		System.out.println ("\t||-----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t||" +
			FormatUtil.FormatDouble (dblTime, 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (eagInitial.derivativeXVAValue(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (1., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (1., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (1., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (1., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpInitial.assetUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpInitial.bankBondUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpInitial.counterPartyBondUnits(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (erpInitial.cashAccount(), 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (0., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (0., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (0., 1, 6, 1.) + " | " +
			FormatUtil.FormatDouble (0., 1, 6, 1.) + " ||"
		);

		EdgeEvolutionTrajectory eet = new EdgeEvolutionTrajectory (
			dblTime,
			new UniverseSnapshot (
				meAsset.weinerIncrement (
					aR1SAsset[iNumTimeStep - 1],
					dblTimeWidth
				),
				meZeroCouponCollateralBond.weinerIncrement (
					aR1SCollateral[iNumTimeStep - 1],
					dblTimeWidth
				),
				meZeroCouponBankBond.weinerIncrement (
					aR1SBank[iNumTimeStep - 1],
					dblTimeWidth
				),
				meZeroCouponCounterPartyBond.weinerIncrement (
					aR1SCounterParty[iNumTimeStep - 1],
					dblTimeWidth
				)
			),
			new EdgeReplicationPortfolio (
				1.,
				0.,
				0.,
				0.
			),
			eagInitial,
			dblGainOnBankDefaultInitial,
			dblGainOnCounterPartyDefaultInitial
		);

		for (int i = iNumTimeStep - 2; i >= 0; --i)
			eet = RunStep (
				tes,
				si,
				bko,
				eet,
				aR1SAsset[i],
				aR1SCollateral[i],
				aR1SBank[i],
				aR1SCounterParty[i]
			);

		System.out.println ("\t||-----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println();
	}
}
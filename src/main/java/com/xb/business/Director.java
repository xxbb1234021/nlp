package com.xb.business;

import com.xb.business.hmm.HmmBaseModelBuilder;

/**
 * Created by kevin on 2016/1/13.
 */
public class Director {
	private HmmBaseModelBuilder hmmBuilder;

	public Director(HmmBaseModelBuilder builder) {
		this.hmmBuilder = builder;
	}

	public void constructHmmModel() {
        hmmBuilder.transformFrequencySum();
        hmmBuilder.emissonFrequencySum();
        hmmBuilder.calculatePrioriProbability();
        hmmBuilder.calculateTransformProbability();
        hmmBuilder.calculateEmissionProbability();
	}
}

package com.xb.business.hmm;

/**
 * Created by kevin on 2016/1/13.
 */
public class Director {
	private AbstractHmmModel hmmBuilder;

	public Director(AbstractHmmModel builder) {
		this.hmmBuilder = builder;
	}

	public void constructHmmModel() {
        hmmBuilder.transformFrequencySum();
        hmmBuilder.emissonFrequencySum();
        hmmBuilder.calculateInitProbability();
        hmmBuilder.calculateTransformProbability();
        hmmBuilder.calculateEmissionProbability();
	}
}

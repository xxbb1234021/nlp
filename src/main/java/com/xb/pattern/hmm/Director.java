package com.xb.pattern.hmm;

/**
 * Created by kevin on 2016/1/13.
 */
public class Director {
	private HmmModelBuilder builder;

	public Director(HmmModelBuilder builder) {
		this.builder = builder;
	}

	public void construct() {
        builder.transformFrequencySum();
        builder.emissonFrequencySum();
        builder.calculatePrioriProbability();
        builder.calculateTransformProbability();
        builder.calculateEmissionProbability();
	}
}

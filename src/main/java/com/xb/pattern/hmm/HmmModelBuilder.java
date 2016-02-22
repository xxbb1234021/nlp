package com.xb.pattern.hmm;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2016/1/13.
 */
public abstract class HmmModelBuilder {

	public abstract void transformFrequencySum();

	public abstract void emissonFrequencySum();

	public abstract void calculatePrioriProbability();

	public abstract void calculateTransformProbability();

	public abstract void calculateEmissionProbability();
}

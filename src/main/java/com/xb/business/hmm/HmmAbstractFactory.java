package com.xb.business.hmm;

import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.builderImpl.AbstractWordTagginModel;

/**
 * Created by kevin on 2016/4/14.
 */
public abstract class HmmAbstractFactory {
    public abstract AbstractWordTagginModel createWordTagginModelBuilder();

    public abstract AbstractPinyingToHanziModel createPinyingToHanziModelBuilder();
}

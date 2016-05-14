package com.xb.business.hmm.factoryImpl;

import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.builderImpl.AbstractWordTagginModel;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.WordTaggingModelBuilder;
import com.xb.constant.Constant;

/**
 * Created by kevin on 2016/4/14.
 */
public class WordTaggingFactory extends HmmAbstractFactory {
    @Override
    public AbstractWordTagginModel createWordTagginModelBuilder() {
        return WordTaggingModelBuilder.getInstance(Constant.WORD_TAG_TRAINDATA);
    }

    @Override
    public AbstractPinyingToHanziModel createPinyingToHanziModelBuilder() {
        return null;
    }

    @Override
    public AbstractPinyingToHanziModel createPinyingToHanziModelBuilder2() {
        return null;
    }
}

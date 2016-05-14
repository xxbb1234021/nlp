package com.xb.business.hmm.factoryImpl;

import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.builderImpl.AbstractWordTagginModel;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.PinyingToHanziModelBuilder;
import com.xb.business.hmm.builderImpl.PinyingToHanziModelBuilder2;
import com.xb.constant.Constant;

/**
 * Created by kevin on 2016/4/14.
 */
public class PinyingToHanziFactory extends HmmAbstractFactory {
    @Override
    public AbstractWordTagginModel createWordTagginModelBuilder() {
        return null;
    }

    @Override
	public AbstractPinyingToHanziModel createPinyingToHanziModelBuilder() {
		return PinyingToHanziModelBuilder.getInstance(Constant.PINYIN_TAG_TRAINDATA);
	}

    @Override
    public AbstractPinyingToHanziModel createPinyingToHanziModelBuilder2() {
        return PinyingToHanziModelBuilder2.getInstance("");
    }
}

package com.xb.business.hmm.builderImpl;

import java.util.Map;

import com.xb.business.hmm.AbstractHmmModel;
import org.apache.log4j.Logger;

/**
 * Created by kevin on 2016/4/14.
 */
public abstract class AbstractPinyingToHanziModel extends AbstractHmmModel {

	public abstract Map<String, Integer> getPinyinPositionMap();

	public abstract int getWordNum();

	public abstract String[] getDiffWord();
}

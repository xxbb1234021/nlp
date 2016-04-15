package com.xb.business.hmm.builderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xb.business.hmm.AbstractHmmModel;
import org.apache.log4j.Logger;

/**
 * Created by kevin on 2016/4/14.
 */
public abstract class AbstractWordTagginModel extends AbstractHmmModel {

	public abstract Map<String, Integer> getWordPositionMap();

	public abstract int getWordTagNum();

	public abstract String[] getDiffWordTag();
}

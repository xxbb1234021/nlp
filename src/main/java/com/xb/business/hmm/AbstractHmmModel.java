package com.xb.business.hmm;

import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/4/14.
 */
public abstract class AbstractHmmModel {
	private static Logger LOGGER = Logger.getLogger(AbstractHmmModel.class);
    protected StringBuffer content = new StringBuffer();

    protected List<String> lineList = new ArrayList<String>();

	protected double[] prioriProbability;// 词的先验概率

	protected double[][] transformProbability;

	protected double[][] emissionProbability;

	public double[] getPrioriProbability() {
		return prioriProbability;
	}

	public double[][] getTransformProbability() {
		return transformProbability;
	}

	public double[][] getEmissionProbability() {
		return emissionProbability;
	}

	protected void readCorpus(String fileName, String charset) {
		AutoDetector.loadRes(new ResourceLoader() {
			@Override
			public void clear() {
			}

			@Override
			public void load(List<String> lines) {
				LOGGER.info("初始化");
				//syntaxRulesList = lines;
				for (String line : lines) {
					content.append(line).append(" ");
				}
				LOGGER.info("初始化完毕，数据条数：" + lines.size());
			}

			@Override
			public void add(String line) {
			}

			@Override
			public void remove(String line) {
			}

		}, ResTools.get(fileName, "classpath:" + fileName), charset);
	}

	public abstract void transformFrequencySum();

	public abstract void emissonFrequencySum();

	public abstract void calculateInitProbability();

	public abstract void calculateTransformProbability();

	public abstract void calculateEmissionProbability();
}

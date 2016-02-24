package com.xb.pattern.hmm;

import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2016/1/13.
 */
public abstract class HmmModelBuilder {
	private static Logger LOGGER = Logger.getLogger(HmmModelBuilder.class);

	protected StringBuffer content = new StringBuffer();

	protected List<String> lineList = new ArrayList<String>();

	protected void readCorpus(String fileName, String charset) {
		AutoDetector.loadAndWatch(new ResourceLoader() {
			@Override
			public void clear() {
			}

			@Override
			public void load(List<String> lines) {
				LOGGER.info("初始化");
				lineList = lines;
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

	public abstract void calculatePrioriProbability();

	public abstract void calculateTransformProbability();

	public abstract void calculateEmissionProbability();
}

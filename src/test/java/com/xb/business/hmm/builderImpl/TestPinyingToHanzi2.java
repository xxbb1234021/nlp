package com.xb.business.hmm.builderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.xb.bean.hmm.Hmm;
import com.xb.business.hmm.Director;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.factoryImpl.PinyingToHanziFactory;
import com.xb.services.hmm.HmmService;
import com.xb.utils.PinyingUtil;

/**
 * Created by kevin on 2016/5/15.
 */
public class TestPinyingToHanzi2 {

	@Test
	public void testPinyingToHanzi() {
		String source = "wodasini";

		List<String> wordList = new ArrayList<String>();

		HmmAbstractFactory factory = new PinyingToHanziFactory();
		AbstractPinyingToHanziModel builder = factory.createPinyingToHanziModelBuilder2();
		Director director = new Director(builder);
		director.constructHmmModel();

		Hmm h = new Hmm();

		Map<String, Integer> pinyingPositionMap = builder.getPinyingPositionMap();

		System.out.println("输入：" + source);
		String splitSpell = "wo ai ni ma";
		System.out.println("切分后：" + splitSpell);

		String[] pinying = splitSpell.split(" ");
		int[] obs = new int[pinying.length];
		for (int i = 0; i < pinying.length; i++) {
			if (StringUtils.isBlank(pinying[i])) {
				continue;
			}
			obs[i] = pinyingPositionMap.get(pinying[i]) == null ? 1 : pinyingPositionMap.get(pinying[i]);
		}

		h.setObs(obs);

		int[] states = new int[builder.getWordNum()];
		for (int i = 0; i < builder.getWordNum(); i++) {
			states[i] = i;
		}
		h.setStates(states);
		h.setStartProb(builder.getPrioriProbability());
		h.setTransProb(builder.getTransformProbability());
		h.setEmitProb(builder.getEmissionProbability());

		HmmService hs = new HmmService();
		Integer[] result = hs.caculateHmmResult(h);
		for (int i = 0; i < result.length; i++) {
			//System.out.print(r + " ");
			System.out.print(builder.getDiffWord()[result[i]] + " ");
		}
	}
}
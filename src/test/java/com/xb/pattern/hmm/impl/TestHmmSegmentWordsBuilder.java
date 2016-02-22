package com.xb.pattern.hmm.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.xb.bean.hmm.Hmm;
import com.xb.constant.Constant;
import com.xb.constant.HmmSegmentConstant;
import com.xb.pattern.hmm.Director;
import com.xb.services.hmm.HmmService;

/**
 * Created by kevin on 2016/1/21.
 */
public class TestHmmSegmentWordsBuilder {
	@Test
	public void testHmmSegment() {
		String words = "我们这么做别人怎么看我们";

		HmmSegmentWordsBuilder builder = HmmSegmentWordsBuilder.getInstance(Constant.HMM_SEGMENT_CHINESECODE,
				Constant.HMM_SEGMENT_TRAINDATA);
		Director director = new Director(builder);
		director.construct();

		Hmm h = new Hmm();

		Map<String, Integer> chineseCodeMap = builder.getChineseCodeMap();

		int[] obs = new int[words.length()];
		for (int i = 0; i < words.length(); i++) {
			String word = words.charAt(i) + "";
			if (StringUtils.isBlank(word)) {
				continue;
			}
			obs[i] = chineseCodeMap.get(word) == null ? 1 : chineseCodeMap.get(word);
		}

		h.setObs(obs);

		int[] states = new int[4];
		for (int i = 0; i < 4; i++) {
			states[i] = i;
		}
		h.setStates(states);
		h.setStartProb(builder.getPrioriProbability());
		h.setTransProb(builder.getTransformProbability());
		h.setEmitProb(builder.getEmissionProbability());

		HmmService hs = new HmmService();
        StringBuilder sb = new StringBuilder();
        Integer[] result = hs.caculateHmmResult(h);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
            sb.append(HmmSegmentConstant.HMM_SEGMENT_MAP.get(result[i]));
        }
        System.out.println();

        String newSeqChar = sb.toString();
        System.out.println(newSeqChar);
    }
}
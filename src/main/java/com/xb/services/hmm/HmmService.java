package com.xb.services.hmm;

import com.xb.algoritm.hmm.Viterbi;
import com.xb.algoritm.segment.PinYinSegmenter;
import com.xb.bean.hmm.Hmm;
import com.xb.business.hmm.Director;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.factoryImpl.PinyingToHanziFactory;
import com.xb.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HmmService {

	public double forward(Hmm h) {
		return Viterbi.forward(h);
	}

	/**
	 * 求解HMM模型
	 *
	 * @param h HMM模型
	 * @return
	 */
	public Integer[] caculateHmmResult(Hmm h) {

		return Viterbi.compute(h);
	}

	public void test(){
		System.out.println("test");
	}

	public void getHanzi(String splitSpell){
		PinYinSegmenter mmsegger = new PinYinSegmenter(Constant.PINYIN_TRIE_TREE);
		String splitPinYin = mmsegger.segment(splitSpell);

		HmmAbstractFactory factory = new PinyingToHanziFactory();
		AbstractPinyingToHanziModel builder = factory.createPinyingToHanziModelBuilder2();
		Director director = new Director(builder);
		director.constructHmmModel();

		Hmm h = new Hmm();
		Map<String, Integer> pinyingPositionMap = builder.getPinyinPositionMap();
		String[] pinyin = splitPinYin.split("\\|");
		int[] obs = new int[pinyin.length];
		for (int i = 0; i < pinyin.length; i++) {
			if (StringUtils.isBlank(pinyin[i])) {
				continue;
			}
			obs[i] = pinyingPositionMap.get(pinyin[i]) == null ? 1 : pinyingPositionMap.get(pinyin[i]);
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

		Integer[] result = caculateHmmResult(h);
		for (int i = 0; i < result.length; i++) {
			//System.out.print(r + " ");
			System.out.print(builder.getDiffWord()[result[i]] + " ");
		}
	}
}

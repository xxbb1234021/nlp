package com.xb.services.hmm;

import com.xb.algoritm.hmm.Viterbi;
import com.xb.bean.hmm.Hmm;

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
}

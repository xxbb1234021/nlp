package com.xb.algoritm.hmm;

import com.xb.bean.hmm.Hmm;

/**
 * Created by kevin on 2016/6/30.
 */
public class ForwardAlgorithm {
	/**
	 * 向前算法
	 * 参数(states,transProb,emitProb,startProb)已知的情况下，求解观察值序列
	 *
	 * @param h
	 * @return
	 */
	public static double forward(Hmm h) {
		int i, j; /* 状态索引 */
		int t; /* 时间索引 */
		double sum; /* 求局部概率时的中间值 */

		// 1. 初始化：计算t=1时刻所有状态的局部概率alpha：
		double[][] alpha = new double[h.getObs().length + 1][h.getStates().length + 1];
		for (i = 0; i < h.getStates().length; i++) {
			alpha[0][i] = h.getStartProb()[i] * h.getEmitProb()[i][h.getObs()[0]];
			System.out.printf("a[1][%d] = pi[%d] * b[%d][%d] = %f * %f = %f\n", i, i, i, h.getObs()[i],
					h.getStartProb()[i], h.getEmitProb()[i][h.getObs()[0]], alpha[0][i]);
			System.err.println();
		}

		// 2. 归纳：递归计算每个时间点，t=2，… ，T时的局部概率
		for (t = 1; t < h.getObs().length; t++) {
			for (j = 0; j < h.getStates().length; j++) {
				sum = 0.0;
				for (i = 0; i < h.getObs().length; i++) {
					sum += alpha[t - 1][i] * (h.getTransProb()[i][j]);
					System.out.printf("a[%d][%d] * A[%d][%d] = %f * %f = %f\n", t, i, i, j, alpha[t - 1][i],
							h.getTransProb()[i][j], alpha[t - 1][i] * (h.getTransProb()[i][j]));
					System.out.printf("sum = %f\n", sum);
				}

				alpha[t][j] = sum * (h.getEmitProb()[j][h.getObs()[t]]);
				System.out.printf("a[%d][%d] = sum * b[%d][%d]] = %f * %f = %f\n", t, j, j, h.getObs()[t], sum,
						h.getEmitProb()[j][h.getObs()[t]], alpha[t][j]);
			}
		}

		// 3. 终止：观察序列的概率等于T时刻所有局部概率之和
		double prob = 0.0;
		for (i = 0; i < h.getStates().length; i++) {
			prob += alpha[h.getStates().length - 1][i];
			System.out.printf("alpha[%d][%d] = %f\n", h.getStates().length - 1, i, alpha[h.getStates().length - 1][i]);
			System.out.printf("pprob = %f\n", prob);
		}

		return prob;
	}
}

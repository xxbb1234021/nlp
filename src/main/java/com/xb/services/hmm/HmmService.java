package com.xb.services.hmm;

import com.xb.bean.hmm.Hmm;
import com.xb.utils.MatrixUtils;

public class HmmService {
	private Hmm hmm = null;

	public void initHmm() {
		hmm = new Hmm();

		int m = 4;
		int n = 3;
		double[][] a = { { 0.500, 0.375, 0.125 }, { 0.250, 0.125, 0.625 },
				{ 0.250, 0.375, 0.375 } };
		double[][] b = { { 0.60, 0.20, 0.15, 0.05 },
				{ 0.25, 0.25, 0.25, 0.25 }, { 0.05, 0.10, 0.35, 0.50 } };
		double[] pi = { 0.63, 0.17, 0.20 };

		// double[][] a = { { 0.5, 0.2, 0.3 }, { 0.3, 0.5, 0.2 },
		// { 0.2, 0.3, 0.5 } };
		// double[][] b = { { 0.5, 0.5 },
		// { 0.4, 0.6 }, { 0.7, 0.3 } };
		// double[] pi = { 0.2, 0.4, 0.4 };

		hmm.setM(m);
		hmm.setN(n);
		hmm.setA(a);
		hmm.setB(b);
		hmm.setPi(pi);
	}

	public Hmm getHmm() {
		return hmm;
	}

	public void setHmm(Hmm hmm) {
		this.hmm = hmm;
	}

	/**
	 * 前向算法
	 * 
	 * @param phmm
	 *            已知的HMM模型
	 * @param observeLenght
	 *            观察符号序列长度
	 * @param observeSequence
	 *            观察序列
	 * @param alpha
	 *            局部概率
	 */
	public double forward(Hmm phmm, int observeLenght, int[] observeSequence, double[][] alpha) {
		int i, j; /* 状态索引 */
		int t; /* 时间索引 */
		double sum; /* 求局部概率时的中间值 */

		// 1. 初始化：计算t=1时刻所有状态的局部概率alpha：
		for (i = 0; i < phmm.getN(); i++) {
			alpha[0][i] = phmm.getPi()[i] * phmm.getB()[i][observeSequence[0]];
			System.out.printf("a[1][%d] = pi[%d] * b[%d][%d] = %f * %f = %f\n",
					i, i, i, observeSequence[i], phmm.getPi()[i], phmm.getB()[i][observeSequence[0]],
					alpha[0][i]);
			System.err.println();
		}

		// 2. 归纳：递归计算每个时间点，t=2，… ，T时的局部概率
		for (t = 1; t < observeLenght; t++) {
			for (j = 0; j < phmm.getN(); j++) {
				sum = 0.0;
				for (i = 0; i < phmm.getN(); i++) {
					sum += alpha[t - 1][i] * (phmm.getA()[i][j]);
					System.out.printf("a[%d][%d] * A[%d][%d] = %f * %f = %f\n",
							t, i, i, j, alpha[t - 1][i], phmm.getA()[i][j],
							alpha[t - 1][i] * (phmm.getA()[i][j]));
					System.out.printf("sum = %f\n", sum);
				}

				alpha[t][j] = sum * (phmm.getB()[j][observeSequence[t]]);
				System.out.printf(
						"a[%d][%d] = sum * b[%d][%d]] = %f * %f = %f\n", t, j,
						j, observeSequence[t], sum, phmm.getB()[j][observeSequence[t]], alpha[t][j]);
			}
		}

		// 3. 终止：观察序列的概率等于T时刻所有局部概率之和
		double prob = 0.0;
		for (i = 0; i < phmm.getN(); i++) {
			prob += alpha[observeLenght - 1][i];
			System.out
					.printf("alpha[%d][%d] = %f\n", observeLenght - 1, i, alpha[observeLenght - 1][i]);
			System.out.printf("pprob = %f\n", prob);
		}

		return prob;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int observeLenght = 3;
		int[] observeSequence = { 0, 1, 0 };

		HmmService hs = new HmmService();
		hs.initHmm();
		hs.forward(hs.getHmm(), observeLenght, observeSequence,
				MatrixUtils.dMatrix(1, observeLenght, 1, hs.getHmm().getN()));
	}
}

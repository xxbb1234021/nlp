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

	public void forward(Hmm phmm, int T, int[] O, double[][] alpha,
			double[] pprob) {
		int i, j; /* 状态索引 */
		int t; /* 时间索引 */
		double sum; /* 求局部概率时的中间值 */

		/* 1. 初始化：计算t=1时刻所有状态的局部概率alpha： */
		for (i = 0; i < phmm.getN(); i++) {
			alpha[0][i] = phmm.getPi()[i] * phmm.getB()[i][O[0]];
			System.out.printf("a[1][%d] = pi[%d] * b[%d][%d] = %f * %f = %f\n",
					i, i, i, O[i], phmm.getPi()[i], phmm.getB()[i][O[0]],
					alpha[0][i]);
			System.err.println();
		}

		for (t = 0; t < T; t++) {
			for (j = 0; j < phmm.getN(); j++) {
				sum = 0.0;
				for (i = 0; i < phmm.getN(); i++) {
					sum += alpha[t][i] * (phmm.getA()[i][j]);
					System.out.printf(
							"a[%d][%d] * A[%d][%d] = %f * %f = %f\\n", t, i, i,
							j, alpha[t][i], phmm.getA()[i][j], alpha[t][i]
									* (phmm.getA()[i][j]));
					System.out.printf("sum = %f\n", sum);
				}

				alpha[t][j] = sum * (phmm.getB()[j][O[t]]);
				System.out.printf(
						"a[%d][%d] = sum * b[%d][%d]] = %f * %f = %f\n", t, j,
						j, O[t], sum, phmm.getB()[j][O[t]], alpha[t][j]);
			}
		}

		/* 3. 终止：观察序列的概率等于T时刻所有局部概率之和*/
		double prob = 0.0;
		for (i = 0; i < phmm.getN(); i++) {
			prob += alpha[T-1][i];
			System.out.printf("alpha[%d][%d] = %f\n", T, i, alpha[T-1][i]);
			System.out.printf("pprob = %f\\n", prob);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int T = 3;
		int[] O = { 0, 2, 3 };

		HmmService hs = new HmmService();
		hs.initHmm();
		hs.forward(hs.getHmm(), T, O,
				MatrixUtils.dMatrix(1, T, 1, hs.getHmm().getN()), null);
	}
}

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

	public void forward(Hmm phmm, int T, int[] O, double[][] alpha, double[] pprob)
	{
		int i, j; /* 状态索引 */
		int t;  /* 时间索引 */
		double sum; /*求局部概率时的中间值 */
		
		/* 1. 初始化：计算t=1时刻所有状态的局部概率alpha： */
		for (i = 0; i < phmm.getN(); i++){
			alpha[0][i] = phmm.getPi()[i]* phmm.getB()[i][O[0]];
			System.out.printf( "a[1][%d] = pi[%d] * b[%d][%d] = %f * %f = %f\n",i, i, i, O[i], phmm.getPi()[i], phmm.getB()[i][O[0]], alpha[0][i] );
			System.err.println();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int T = 3;
		int[] O = { 0, 2, 3 };
		
		HmmService hs = new HmmService();
		hs.initHmm();
		hs.forward(hs.getHmm(), T, O, MatrixUtils.dMatrix(1, T, 1, hs.getHmm().getN()), null);
	}
}

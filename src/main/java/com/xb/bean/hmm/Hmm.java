package com.xb.bean.hmm;

public class Hmm {
	int[] states; /* 隐藏状态数目;states={1,2,…,N} */
	int[] obs; /* 观察符号数目; obs={1,2,…,M} */
	double[][] transProb; /* 状态转移矩阵transProb[1..N][1..N]. [i][j] 是从t时刻状态i到t+1时刻状态j的转移概率 */
	double[][] emitProb; /* 混淆矩阵emitProb[1..N][1..M]. [j][k]在状态j时观察到符合k的概率。 */
	double[] startProb; /* 初始向量startProb[1..N]，[i] 是初始状态概率分布 */

	public int[] getStates() {
		return states;
	}

	public void setStates(int[] states) {
		this.states = states;
	}

	public int[] getObs() {
		return obs;
	}

	public void setObs(int[] obs) {
		this.obs = obs;
	}

	public double[][] getTransProb() {
		return transProb;
	}

	public void setTransProb(double[][] transProb) {
		this.transProb = transProb;
	}

	public double[][] getEmitProb() {
		return emitProb;
	}

	public void setEmitProb(double[][] emitProb) {
		this.emitProb = emitProb;
	}

	public double[] getStartProb() {
		return startProb;
	}

	public void setStartProb(double[] startProb) {
		this.startProb = startProb;
	}
}

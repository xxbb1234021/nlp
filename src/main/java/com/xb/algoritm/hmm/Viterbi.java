package com.xb.algoritm.hmm;

import com.xb.bean.hmm.Hmm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/1/11.
 */
public class Viterbi {

    /**
     * 向前算法
     * 参数(states,transProb,emitProb,startProb)已知的情况下，求解观察值序列
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

    /**
     * 维特比算法
     * 参数(obs,transProb,emitProb,startProb)已知的情况下，求解状态值序列
     * @param h
     *            HMM模型
     * @return
     */
    public static Integer[] compute(Hmm h) {
        // 路径概率表 V[时间][隐状态] = 概率
        double[][] V = new double[h.getObs().length][h.getStates().length];
        // int[][] path = new int[obs.length][states.length];

        // 初始化初始状态 (t == 0)
        for (int i : h.getStates()) {
            //System.out.println(h.getStartProb()[i]);
            //System.out.println(h.getEmitProb()[i][h.getObs()[0]]);
            //System.out.println(h.getStartProb()[i] * h.getEmitProb()[i][h.getObs()[0]]);
            V[0][i] = h.getStartProb()[i] * h.getEmitProb()[i][h.getObs()[0]];
            // path[i][0] = -1;
        }

        // 对 t > 0 做一遍维特比算法
        for (int t = 1; t < h.getObs().length; ++t) {
            int[][] newPath = new int[h.getStates().length][h.getObs().length];

            for (int i : h.getStates()) {
                double prob = -1;
                int state = 0;

                for (int j : h.getStates()) {
                    // 概率 隐状态 = 前状态是j的概率 * j转移到i的概率 * i表现为当前状态的概率
                    double nprob = V[t - 1][j] * h.getTransProb()[j][i] * h.getEmitProb()[i][h.getObs()[t]];
                    if (nprob > prob) {
                        prob = nprob;
                        // 记录最大概率
                        V[t][i] = prob;
                        // path[t][i] = j;
                    }
                }
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < V.length; i++) {
            list.add(getMaxColumn(V[i]));
        }

        return list.toArray(new Integer[list.size()]);
    }

    private static int getMaxColumn(double[] v) {
        if (v.length == 1) {
            return 0;
        }

        int index = 0;
        double prob = v[0];
        for (int i = 1; i < v.length; i++) {
            if (prob < v[i]) {
                prob = v[i];
                index = i;
            }
        }
        return index;
    }
}

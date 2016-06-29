package com.xb.algoritm.hmm;

import com.xb.bean.hmm.Hmm;

import java.util.*;

/**
 * Created by kevin on 2016/1/11.
 */
public class Viterbi {

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

    /**
     * 获得概率最大的序列值
     * 参数(obs,transProb,emitProb,startProb)已知的情况下，求解状态值序列
     *
     * @param h HMM模型
     * @return
     */
    public static Integer[] getMaxProbGroup(Hmm h) {
        // 路径概率表 V[时间][隐状态] = 概率
        double[][] probArray = compute(h);

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < probArray.length; i++) {
            list.add(getMaxColumn(probArray[i]));
        }

        return list.toArray(new Integer[list.size()]);
    }

    /**
     * 获得概率最大的序列值组
     * 参数(obs,transProb,emitProb,startProb)已知的情况下，求解状态值序列
     *
     * @param h HMM模型
     * @return
     */
    public static List<List<Integer>> getAllProbGroup(Hmm h) {
        double[][] probArray = compute(h);

        List<Map<Integer, Double>> allMap = new ArrayList<Map<Integer, Double>>();
        List<List<Integer>> allList = new ArrayList<List<Integer>>();
        Map<Integer, Double> indexMap = null;
        List<Integer> indexList = null;
        Map<Integer, Double> tempMap = null;
        for (int i = 0; i < probArray.length; i++) {
            indexMap = new LinkedHashMap<Integer, Double>();
            indexList = new ArrayList<Integer>();
            for (int j = 0; j < probArray[i].length; j++) {
                indexMap.put(j, probArray[i][j]);
            }
            tempMap = sortMapByValue(indexMap);
            for (Map.Entry<Integer, Double> entry : tempMap.entrySet()) {
                indexList.add(entry.getKey());
            }

            allMap.add(tempMap);
            allList.add(indexList);
        }

        List<List<Integer>> allSequenceList = new ArrayList<List<Integer>>();
        List<Integer> sequenceList = null;
        List<Integer> firstList = allList.get(0);
        Map<Integer, Double> firstMap = allMap.get(0);
        for (int i = 0; i < firstList.size(); i++) {
            Integer index = firstList.get(i);
            if(firstMap.get(index) > 0.0) {
                sequenceList = new ArrayList<Integer>();
                sequenceList.add(index);
                for (int j = 1; j < allList.size(); j++) {
                    sequenceList.add(allList.get(j).get(i));
                }
                allSequenceList.add(sequenceList);
            }else{
                break;
            }
        }

        return allSequenceList;
    }

    /**
     * 维特比算法
     * 参数(obs,transProb,emitProb,startProb)已知的情况下，求解状态值序列
     *
     * @param h HMM模型
     * @return
     */
    private static double[][] compute(Hmm h) {
        // 路径概率表 V[时间][隐状态] = 概率
        double[][] probArray = new double[h.getObs().length][h.getStates().length];

        // 初始化初始状态 (t == 0)
        for (int i : h.getStates()) {
            probArray[0][i] = h.getStartProb()[i] * h.getEmitProb()[i][h.getObs()[0]];
        }

        // 对 t > 0 做一遍维特比算法
        for (int t = 1; t < h.getObs().length; ++t) {
            for (int i : h.getStates()) {
                double prob = -1;

                for (int j : h.getStates()) {
                    // 概率 隐状态 = 前状态是j的概率 * j转移到i的概率 * i表现为当前状态的概率
                    double nprob = probArray[t - 1][j] * h.getTransProb()[j][i] * h.getEmitProb()[i][h.getObs()[t]];
                    if (nprob > prob) {
                        prob = nprob;
                        // 记录最大概率
                        probArray[t][i] = prob;
                    }
                }
            }
        }
        return probArray;
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

    /**
     * 用概率大小进行排序
     *
     * @param oriMap
     * @return
     */
    private static Map<Integer, Double> sortMapByValue(Map<Integer, Double> oriMap) {
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<Integer, Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(oriMap.entrySet());
            Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
                public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2) {
                    if (entry1.getValue() < entry2.getValue())
                        return 1;
                    if (entry1.getValue() == entry2.getValue())
                        return 0;
                    else
                        return -1;
                }
            });
            Iterator<Map.Entry<Integer, Double>> iter = entryList.iterator();
            Map.Entry<Integer, Double> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }
}

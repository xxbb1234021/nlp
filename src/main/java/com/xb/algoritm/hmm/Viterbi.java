package com.xb.algoritm.hmm;

import com.xb.bean.hmm.Hmm;

import java.util.*;

/**
 * Created by kevin on 2016/1/11.
 */
public class Viterbi {

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

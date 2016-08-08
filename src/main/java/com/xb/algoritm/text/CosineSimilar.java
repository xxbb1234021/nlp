package com.xb.algoritm.text;

import com.xb.utils.SegWordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2016/8/7 0007.
 */
public class CosineSimilar {
	/**
	 * 得到两个字符串的相似性
	 * @param first
	 * @param second
	 * @return
	 */
	public static Double cosSimilarity(String first, String second) {
		try {
			Map<String, Integer> firstTfMap = SegWordUtils.segWords(first, false);
			Map<String, Integer> secondTfMap = SegWordUtils.segWords(second, false);
			if (firstTfMap.size() < secondTfMap.size()) {
				Map<String, Integer> temp = firstTfMap;
				firstTfMap = secondTfMap;
				secondTfMap = temp;
			}
			return calculateCos(firstTfMap, secondTfMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0d;
	}

	/**
	 * 计算余弦相似性
	 * @param first
	 * @param second
	 * @return
	 */
	private static Double calculateCos(Map<String, Integer> first, Map<String, Integer> second) {
		List<Map.Entry<String, Integer>> firstList = new ArrayList<Map.Entry<String, Integer>>(first.entrySet());
		List<Map.Entry<String, Integer>> secondList = new ArrayList<Map.Entry<String, Integer>>(second.entrySet());
		//计算相似度
		double vectorFirstModulo = 0.00;//向量1的模
		double vectorSecondModulo = 0.00;//向量2的模
		double vectorProduct = 0.00; //向量积
		int secondSize = second.size();
		for(int i = 0; i < firstList.size(); i++) {
			if (i < secondSize) {
				vectorSecondModulo +=
						secondList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
				vectorProduct += firstList.get(i).getValue().doubleValue() * secondList.get(i).getValue().doubleValue();
			}
			vectorFirstModulo += firstList.get(i).getValue().doubleValue() * firstList.get(i).getValue().doubleValue();
		}
		return vectorProduct / (Math.sqrt(vectorFirstModulo) * Math.sqrt(vectorSecondModulo));
	}
}

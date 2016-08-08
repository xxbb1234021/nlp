package com.xb.algoritm.summary;

import java.util.*;

/**
 * Created by kevin on 2016/8/4 0004.
 */
public class KeywordExtraction {
	/**
	 * 阻尼系数，一般取值为0.85
	 */
	private static final float d = 0.85f;
	/**
	 * 最大迭代次数
	 */
	private static final int maxIter = 200;
	private static final float minDiff = 0.001f;

	private static int nKeyword = 10;

	/**
	 * 获取关键词
	 * @param wordList
	 * @return
	 */
	public static String getKeyword(List<String> wordList) {
		Map<String, Set<String>> words = new HashMap<String, Set<String>>();
		Queue<String> que = new LinkedList<String>();
		for(String w : wordList) {
			if (!words.containsKey(w)) {
				words.put(w, new HashSet<String>());
			}
			que.offer(w);
			if (que.size() > 5) {
				que.poll();
			}

			for(String w1 : que) {
				for(String w2 : que) {
					if (w1.equals(w2)) {
						continue;
					}

					words.get(w1).add(w2);
					words.get(w2).add(w1);
				}
			}
		}

		Map<String, Float> score = new HashMap<String, Float>();
		for(int i = 0; i < maxIter; ++i) {
			Map<String, Float> m = new HashMap<String, Float>();
			float maxDiff = 0;
			float tempNum = 0;
			for(Map.Entry<String, Set<String>> entry : words.entrySet()) {
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				m.put(key, 1 - d);
				for(String other : value) {
					int size = words.get(other).size();
					if (key.equals(other) || size == 0) {
						continue;
					}

					tempNum = score.get(other) == null ? 0 : score.get(other);
					m.put(key, m.get(key) + d / size * tempNum);
				}
				maxDiff = Math.max(maxDiff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
			}
			score = m;
			if (maxDiff <= minDiff)
				break;
		}

		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
			@Override public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return (o1.getValue() - o2.getValue() > 0 ? -1 : 1);
			}
		});

		if (nKeyword > entryList.size()) {
			nKeyword = entryList.size();
		}
		String result = "";
		for(int i = 0; i < nKeyword; ++i) {
			result += entryList.get(i).getKey() + '#';
		}
		return result;
	}
}

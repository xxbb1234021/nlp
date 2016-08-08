package com.xb.algoritm.summary;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.constant.FileConstant;
import com.xb.utils.SegWordUtils;
import com.xb.utils.StopWordsUtil;

import java.util.*;

/**
 * Created by kevin on 2016/8/7 0007.
 */
public class SentencesExtraction {
	/**
	 * 把段落按. ! ?分隔成句组
	 * @param input
	 * @return
	 */
	public static String[] getSentences(String input) {
		if (input == null) {
			return new String[0];
		} else {
			return input.split("[.，,。:：“”？?！!；;]");
		}
	}

	/**
	 * 文章摘要实现
	 * @param input
	 * @param numSentences
	 * @return
	 */
	public static String summarise(String input, int numSentences) {
		Map<String, Integer> wordFrequencies = SegWordUtils.segWords(input, true);
		Set<String> mostFrequentWords = getMostFrequentWords(100, wordFrequencies).keySet();

		String[] actualSentences = getSentences(input);

		Set<String> outputSentences = new HashSet<String>();
		Iterator<String> it = mostFrequentWords.iterator();
		while (it.hasNext()) {
			String word = it.next();
			for(int i = 0; i < actualSentences.length; i++) {
				if (actualSentences[i].indexOf(word) >= 0) {
					outputSentences.add(actualSentences[i]);
					break;
				}
				if (outputSentences.size() >= numSentences) {
					break;
				}
			}
			if (outputSentences.size() >= numSentences) {
				break;
			}
		}

		List<String> reorderedOutputSentences = reorderSentences(outputSentences, input);

		StringBuffer result = new StringBuffer("");
		it = reorderedOutputSentences.iterator();
		while (it.hasNext()) {
			String sentence = it.next();
			result.append(sentence);
			result.append("#");
			if (it.hasNext()) {
				result.append(" ");
			}
		}

		return result.toString();
	}

	/**
	 * 将句子按顺序输出
	 * @param outputSentences
	 * @param input
	 * @return
	 */
	private static List<String> reorderSentences(Set<String> outputSentences, final String input) {
		ArrayList<String> result = new ArrayList<String>(outputSentences);
		Collections.sort(result, new Comparator<String>() {
			public int compare(String arg0, String arg1) {
				String sentence1 = arg0;
				String sentence2 = arg1;

				int indexOfSentence1 = input.indexOf(sentence1.trim());
				int indexOfSentence2 = input.indexOf(sentence2.trim());
				int result = indexOfSentence1 - indexOfSentence2;

				return result;
			}
		});
		return result;
	}

	/**
	 * 对分词进行按数量排序,取出前num个
	 * @param num
	 * @param words
	 * @return
	 */
	public static Map<String, Integer> getMostFrequentWords(int num, Map<String, Integer> words) {

		Map<String, Integer> keywords = new LinkedHashMap<String, Integer>();
		int count = 0;
		// 词频排序
		List<Map.Entry<String, Integer>> info = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
		Collections.sort(info, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
				return obj2.getValue() - obj1.getValue();
			}
		});

		// 高频词输出
		for(int j = 0; j < info.size(); j++) {
			if (num > count) {
				keywords.put(info.get(j).getKey(), info.get(j).getValue());
				count++;
			} else {
				break;
			}
		}
		return keywords;
	}
}

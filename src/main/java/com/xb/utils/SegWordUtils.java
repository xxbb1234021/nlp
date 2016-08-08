package com.xb.utils;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.constant.FileConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2016/8/8 0008.
 */
public class SegWordUtils {

	/**
	 * 返回分词
	 * @param content
	 * @param flag 是否去掉停词
	 * @return
	 */
	public static Map<String, Integer> segWords(String content, boolean flag) {
		MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(FileConstant.WORD_TRIE_TREE);
		String words = mmsegger.segment(content);
		String[] wordArray = words.split("\\|");

		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		String word = null;
		for(int i = 0; i < wordArray.length; i++) {
			word = wordArray[i];
			if (flag && StopWordsUtil.isStopWord(wordArray[i])) {
				continue;
			}
			if (wordMap.containsKey(word)) {
				wordMap.put(word, wordMap.get(word) + 1);
			} else {
				wordMap.put(word, 1);
			}
		}
		return wordMap;
	}
}

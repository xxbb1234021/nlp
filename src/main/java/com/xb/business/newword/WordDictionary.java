package com.xb.business.newword;

import com.xb.constant.Constant;
import com.xb.constant.FileConstant;
import com.xb.utils.CountMap;
import com.xb.utils.FileNIOUtil;
import com.xb.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordDictionary {
	private static Logger LOGGER = LoggerFactory.getLogger(WordDictionary.class);

	private Set<String> dictionary = new HashSet<String>();

	private CountMap<Character> wordCountMap = new CountMap<Character>();

	private int totalWordCount;

	private static WordDictionary instance;

	public static WordDictionary getInstance() {
		if (instance == null) {
			synchronized (WordDictionary.class) {
				if (instance == null) {
					instance = new WordDictionary();
				}
			}
		}
		return instance;
	}

	private WordDictionary() {
		initWordDic();
		//initCountMap();
	}

	private void initCountMap() {
        List<String> letterResource =
                FileNIOUtil.readFileLine(FileConstant.WORD_NEW_TEXT, Constant.CHARSET_UTF8);
        char c;
		String s = "";
		for (int i = 0; i < letterResource.size(); i++) {
			s = letterResource.get(i);
			for (int j = 0; j < s.length(); j++) {
				c = s.charAt(i);
				if (TextUtils.isChineseChar(c)) {
					wordCountMap.increase(c);
				}
			}
		}
		totalWordCount = wordCountMap.count();
	}

	/**
	 * 初始化字典
	 */
	private void initWordDic() {
        List<String> wordResource =
                FileNIOUtil.readFileLine(FileConstant.WORD_TRIE_TREE, Constant.CHARSET_UTF8);
        for (int i = 0; i < wordResource.size(); i++) {
			dictionary.add(wordResource.get(i).trim());
		}
	}

	public boolean contains(String word) {
		return dictionary.contains(word);
	}

	public double rate(char c) {
		return (double) wordCountMap.get(c) / totalWordCount;
	}

	public int size() {
		return dictionary.size();
	}
}

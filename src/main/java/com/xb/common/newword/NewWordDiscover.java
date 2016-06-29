package com.xb.common.newword;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.xb.business.newword.WordDictionary;
import com.xb.business.newword.WordIndexer;
import com.xb.business.newword.WordSelector;

/**
 * Created by kevin on 2016/6/27.
 */
public class NewWordDiscover {
	private WordDictionary dictionary;

	//词的最小长度
	private final static int MIN_CANDIDATE_LEN = 2;

	//词的最大长度
	private final static int MAX_CANDIDATE_LEN = 6;

	private static Set<Character> specialWordSet = new HashSet<Character>();

	private static char[] specialWord = { '我', '你', '您', '他', '她', '谁', '哪', '那', '这', '的', '了', '着', '也', '是', '有',
			'不', '在', '与', '呢', '啊', '呀', '吧', '嗯', '哦', '哈', '呐' };

	static {
		for (char c : specialWord) {
			specialWordSet.add(c);
		}
	}

	public NewWordDiscover() {
		dictionary = WordDictionary.getInstance();
	}

	/**
	 * 发现新词的方法
	 * @param document
	 * @return
	 */
	public Set<String> discover(String document) {
		Set<String> set = new HashSet<String>();
		WordIndexer indexer = new WordIndexer(document);
		WordSelector selector = new WordSelector(document, MIN_CANDIDATE_LEN, MAX_CANDIDATE_LEN);
		NewWrodEntropy judger = new NewWrodEntropy(indexer);
		String word;
		while (!selector.isEnd()) {
			word = selector.next();

			if (StringUtils.isBlank(word)) {
				continue;
			}
			if (specialWordSet.contains(word.charAt(0)) || specialWordSet.contains(word.charAt(word.length() - 1))) {
				continue;
			}

			if (dictionary.contains(word) || set.contains(word)) {
				selector.select();
			} else if (judger.judge(word)) {
				set.add(word);
			}
		}
		return set;
	}
}

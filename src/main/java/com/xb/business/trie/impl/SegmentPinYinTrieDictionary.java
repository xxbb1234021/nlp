package com.xb.business.trie.impl;

import org.apache.log4j.Logger;

import com.xb.bean.trie.WordTrieNode;
import com.xb.business.trie.TrieDictionary;
import com.xb.constant.Constant;

public class SegmentPinYinTrieDictionary extends TrieDictionary {
	private static Logger LOGGER = Logger.getLogger(SegmentPinYinTrieDictionary.class);

	private static SegmentPinYinTrieDictionary dict = null;

	private SegmentPinYinTrieDictionary(String fn) {
		importDict(fn);
	}

	public static SegmentPinYinTrieDictionary getInstance(String fileName) {
		if (dict == null) {
			synchronized (SegmentPinYinTrieDictionary.class) {
				if (dict == null) {
					dict = new SegmentPinYinTrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName) {
		readCorpus(fileName, Constant.TRIE_CATEGORY_PINYIN);

		return true;
	}

	public boolean search(String words) {
		WordTrieNode node = wordRoot;

		int i = 0;
		for (i = 0; i < words.length(); i++) {
			char c = words.charAt(i);
			WordTrieNode pNode = node.getChilds().get(c);
			if (pNode == null)
				break;
			node = pNode;
		}

		return (i == words.length()) && (node.isBound());
	}

	public WordTrieNode getNodeRoot() {
		return wordRoot;
	}
}
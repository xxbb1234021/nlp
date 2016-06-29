package com.xb.business.trie.impl;

import com.xb.bean.trie.WordTrieNode;


import com.xb.business.trie.TrieDictionary;
import com.xb.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentWordTrieDictionary extends TrieDictionary {
	private static Logger LOGGER = LoggerFactory.getLogger(SegmentWordTrieDictionary.class);

	private static SegmentWordTrieDictionary dict = null;

	private SegmentWordTrieDictionary(String fn) {
		importDict(fn);
	}

	public static SegmentWordTrieDictionary getInstance(String fileName) {
		if (dict == null) {
			synchronized (SegmentWordTrieDictionary.class) {
				if (dict == null) {
					dict = new SegmentWordTrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName) {
		readCorpus(fileName, Constant.TRIE_CATEGORY_WORD);

		return true;
	}

	public boolean search(String words) {
		WordTrieNode node = wordRoot;

		int i = 0;
		for (i = 0; i < words.length(); i++) {
			char c = words.charAt(i);
			WordTrieNode pNode = node.getChilds().get(Character.valueOf(c));
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
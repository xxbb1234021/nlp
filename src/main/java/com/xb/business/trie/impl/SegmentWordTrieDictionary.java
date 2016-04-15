package com.xb.business.trie.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.xb.bean.trie.ParticipleTrieNode;
import com.xb.business.trie.TrieDictionary;
import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;

public class SegmentWordTrieDictionary extends TrieDictionary {
	private static Logger LOGGER = Logger.getLogger(SegmentWordTrieDictionary.class);

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
		ParticipleTrieNode node = participleRoot;

		int i = 0;
		for (i = 0; i < words.length(); i++) {
			char c = words.charAt(i);
			ParticipleTrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null)
				break;
			node = pNode;
		}

		return (i == words.length()) && (node.isBound());
	}

	public ParticipleTrieNode getNodeRoot() {
		return participleRoot;
	}
}
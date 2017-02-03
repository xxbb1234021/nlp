package com.xb.utils.pinyin;

import com.xb.utils.ConsoleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardTree {
	private static Logger LOGGER = LoggerFactory.getLogger(ConsoleReader.class);

	private TrieNode root = new BranchNode(' ');

	public void insert(String word) {
		TrieNode curNode = this.root;
		word = word + "$";
		char[] chars = word.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '$') {
				curNode.points[26] = new LeafNode('$');
			} else {
				int pSize = chars[i] - 'a';
				try {
					if (curNode.points[pSize] == null) {
						curNode.points[pSize] = new BranchNode(chars[i]);

						curNode = curNode.points[pSize];
					} else {
						curNode = curNode.points[pSize];
					}
				} catch (Exception e) {
					LOGGER.error("the error is :" + String.valueOf(chars[i]) + "  " + pSize + "  " + word);
				}
			}
		}
	}

	public boolean fullMatch(String word) {
		TrieNode curNode = this.root;
		char[] chars = word.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int pSize = chars[i] - 'a';
			if (curNode.points[pSize] == null) {
				return false;
			}
			curNode = curNode.points[pSize];
			if (i == chars.length - 1) {
				curNode = curNode.points[26];
				if ((curNode != null) && (curNode.key == '$')) {
					return true;
				}
			}
		}

		return false;
	}
}
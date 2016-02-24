package com.xb.algoritm.trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.xb.bean.trie.TrieNode;
import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;
import org.apache.log4j.Logger;

public class TrieDictionary {
	private static Logger LOGGER = Logger.getLogger(TrieDictionary.class);

	private static TrieDictionary dict = null;
	private TrieNode root = new TrieNode();

	private TrieDictionary(String fn) {
		importDict(fn);
	}

	public static TrieDictionary getInstance(String fileName) {
		if (dict == null) {
			synchronized (TrieDictionary.class) {
				if (dict == null) {
					dict = new TrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName) {
		AutoDetector.loadAndWatch(new ResourceLoader() {
			@Override
			public void clear() {
			}

			@Override
			public void load(List<String> lines) {
				LOGGER.info("初始化");
				for (String line : lines) {
					addWord(line);
				}
				LOGGER.info("初始化完毕，数据条数：" + lines.size());
			}

			@Override
			public void add(String line) {
			}

			@Override
			public void remove(String line) {
			}

		}, ResTools.get(fileName, "classpath:" + fileName), Constant.CHARSET_UTF8);

		return true;
	}

	/**
	 * 添加新词
	 * @param word
	 */
	public void addWord(String word) {
		TrieNode node = this.root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			TrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null) {
				pNode = new TrieNode(c);
				node.getChilds().put(Character.valueOf(c), pNode);
			}
			node = pNode;
		}
		node.setBound(true);
	}

	public boolean contains(String word) {
		TrieNode node = this.root;

		int i = 0;
		for (i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			TrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null)
				break;
			node = pNode;
		}

		return (i == word.length()) && (node.isBound());
	}

	public TrieNode getRoot() {
		return this.root;
	}
}
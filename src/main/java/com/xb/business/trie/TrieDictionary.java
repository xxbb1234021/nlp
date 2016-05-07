package com.xb.business.trie;

import java.util.List;

import com.xb.bean.trie.WordTrieNode;
import org.apache.log4j.Logger;

import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.bean.trie.TrieNode;
import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;

/**
 * Created by kevin on 2016/4/14.
 */
public abstract class TrieDictionary {
	private static Logger LOGGER = Logger.getLogger(TrieDictionary.class);

	protected WordTrieNode wordRoot = new WordTrieNode();

	protected SyntaxTrieNode syntaxRoot = new SyntaxTrieNode();

	protected void readCorpus(String fileName, final String type) {
		AutoDetector.loadRes(new ResourceLoader() {
			@Override
			public void clear() {
			}

			@Override
			public void load(List<String> lines) {
				LOGGER.info("初始化");
				for (String line : lines) {
					add(line);
				}
				LOGGER.info("初始化完毕，数据条数：" + lines.size());
			}

			@Override
			public void add(String line) {
				if (Constant.TRIE_CATEGORY_WORD.equals(type)) {
					readWordCorpus(line);
				} else if (Constant.TRIE_CATEGORY_SYNTAX.equals(type)) {
					readSyntaxCorpus(line);
				} else if (Constant.TRIE_CATEGORY_PINYIN.equals(type)) {
					readPinYinCorpus(line);
				}
			}

			@Override
			public void remove(String line) {
			}

		}, ResTools.get(fileName, "classpath:" + fileName), Constant.CHARSET_UTF8);
	}

	private void readWordCorpus(String line) {
		WordTrieNode node = wordRoot;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			WordTrieNode pNode = node.getChilds().get(Character.valueOf(c));
			if (pNode == null) {
				pNode = new WordTrieNode(Character.valueOf(c));
				node.getChilds().put(Character.valueOf(c), pNode);
			}
			node = pNode;
		}
		node.setBound(true);
	}

	private void readPinYinCorpus(String line) {
		WordTrieNode node = wordRoot;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			WordTrieNode pNode = node.getChilds().get(c);
			if (pNode == null) {
				pNode = new WordTrieNode(c);
				node.getChilds().put(c, pNode);
			}
			node = pNode;
		}
		node.setBound(true);
	}

	private void readSyntaxCorpus(String line) {
		SyntaxTrieNode node = syntaxRoot;
		SyntaxTrieNode parentNode = null;
		String prob = null;
		String chars = null;
		String[] lineArray = line.split(" ");
		int level = 0;
		for (int i = 0; i < lineArray.length; i++) {
			if (i == 0) {
				prob = lineArray[i];
				continue;
			} else if (lineArray[i].equals("-->")) {
				continue;
			}

			level++;
			parentNode = node;
			chars = lineArray[i];
			SyntaxTrieNode pNode = node.subNode(chars);
			if (pNode == null) {
				node.getChildList().add(new SyntaxTrieNode(chars));
				node = node.subNode(chars);
				node.setParent(parentNode);
			} else {
				node = pNode;
			}

			node.setLevel(level);
		}

		node.setProb(Double.parseDouble(prob));
		node.setBound(true);
	}

	public abstract TrieNode getNodeRoot();
}

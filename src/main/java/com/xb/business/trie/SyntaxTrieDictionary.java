package com.xb.business.trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xb.bean.trie.SyntaxTrieNode;
import com.xb.constant.Constant;
import com.xb.utils.res.AutoDetector;
import com.xb.utils.res.ResTools;
import com.xb.utils.res.ResourceLoader;

/**
 * Created by kevin on 2016/3/25.
 */
public class SyntaxTrieDictionary {
	private static Logger LOGGER = Logger.getLogger(SyntaxTrieDictionary.class);

	private static SyntaxTrieDictionary dict = null;
	private SyntaxTrieNode root = new SyntaxTrieNode();

	private SyntaxTrieDictionary(String fn) {
		importDict(fn);
	}

	public static SyntaxTrieDictionary getInstance(String fileName) {
		if (dict == null) {
			synchronized (SegmentWordTrieDictionary.class) {
				if (dict == null) {
					dict = new SyntaxTrieDictionary(fileName);
				}
			}
		}
		return dict;
	}

	private boolean importDict(String fileName) {
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
				//System.out.println(line);
				SyntaxTrieNode node = root;
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

			@Override
			public void remove(String line) {
			}

		}, ResTools.get(fileName, "classpath:" + fileName), Constant.CHARSET_UTF8);

		return true;
	}

	public SyntaxTrieNode search(SyntaxTrieNode tNode, String... word) {
		SyntaxTrieNode current = null;

		List<SyntaxTrieNode> nodeList = new ArrayList<SyntaxTrieNode>();
		LinkedList<SyntaxTrieNode> list = tNode.getChildList();
		for (SyntaxTrieNode node : list) {
			LinkedList<SyntaxTrieNode> childList = node.getChildList();
			for (SyntaxTrieNode childNode : childList) {
				if(word.length == 1 && childNode.isBound()){
					if(word[0].equals(childNode.getKey())){
						current = childNode;
					}
				}else if(word.length > 1) {
					for (int i = 0; i < word.length - 1; i++) {
						if (!word[i].equals(childNode.getKey()))
							break;
						else {
							childNode = childNode.subNode(word[i + 1]);
							if (childNode == null)
								break;

							current = childNode;
						}
					}
				}
			}

			if (current != null && current.isBound()) {
				nodeList.add(current);
				current = null;
			}
		}

		return getMaxProbNode(nodeList);
	}

	private SyntaxTrieNode getMaxProbNode(List<SyntaxTrieNode> nodeList){
		double maxProb = 0.0;
		SyntaxTrieNode syntaxTrieNode = null;

		for (SyntaxTrieNode node : nodeList) {
			if(maxProb < node.getProb()){
				maxProb = node.getProb();
				syntaxTrieNode = node;
			}
		}

		return syntaxTrieNode;
	}

	public SyntaxTrieNode getRoot() {
		return root;
	}
}

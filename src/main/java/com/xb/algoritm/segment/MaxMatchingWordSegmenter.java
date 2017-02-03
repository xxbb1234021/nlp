package com.xb.algoritm.segment;

import com.xb.bean.trie.WordTrieNode;
import com.xb.business.trie.TrieDictionaryContext;
import com.xb.business.trie.impl.SegmentWordTrieDictionary;
import com.xb.constant.FileConstant;
import com.xb.utils.TextUtils;

import java.io.IOException;

public class MaxMatchingWordSegmenter {
	private TrieDictionaryContext context = null;

	public MaxMatchingWordSegmenter(String path) {
		//加载词典
		context = new TrieDictionaryContext(SegmentWordTrieDictionary.getInstance(path));
	}

	/**
	 * 词典：用Trie树表示，每个节点都是一个TrieNode节点
	 * 每个TrieNode节点中有:
	 * 1.表示一个字
	 * 2.以该字为前缀的所有的下一个字的HashMap<"字"， 字的TrieNode>
	 * 3.bound标记，该字是不是一个词的结尾。在最大匹配中有用（Maximum Matching）
	 * <p>
	 * 正向MM（Maximum Matching）算法的核心思想：
	 * 1. 从句子中，取词
	 * 2. 将词添加到分词列表中
	 * 3. 将分词标记 "|"添加到分词表
	 * <p>
	 * 其中的句子中的成分分为以下几种：
	 * 1. 非分词：如分隔符，直接跳过
	 * 2. 分词： 分词分为以下几种：
	 * a. 非中文分词：将分隔符分隔的连续的非中文字符作为一个分词
	 * b. 中文分词：
	 * i. 词典中的词：作为一个分词
	 * ii. 词典中的词的前缀：将每个字作为一个分词
	 * iii. 非词典中的词： 将每个字作为一个分词
	 * <p>
	 * 该分词的核心：对于前缀词的划分
	 */
	public String segment(String sentence) {
		StringBuffer segBuffer = new StringBuffer();

		WordTrieNode p = (WordTrieNode) context.getRoot();
		WordTrieNode pChild = null;

		int length = sentence.length();
		int segBoundIndex = -1; //保存上次分词结束字符在sentence中的位置

		for(int i = 0; i < length; ++i) {
			char c = sentence.charAt(i);
			if (TextUtils.isCharSeperator(c)) {// 分隔符
				segBuffer.append(c);
			} else if (TextUtils.isOtherChar(c)) {// 其他语言字符
				do {
					segBuffer.append(c);
					if (++i == length) {
						break;
					}
					c = sentence.charAt(i);
				} while (TextUtils.isOtherChar(c));
				if (i != length)
					--i; //还原现场
			} else if (TextUtils.isChineseChar(c)) {
				pChild = p.getChilds().get(Character.valueOf(c));
				if (pChild == null) {// 不在词典中的中文字符
					segBuffer.append(c);
				} else {
					do {// 在词典中的词
						segBuffer.append(c);
						if (p == context.getRoot() || pChild.isBound()) { // 算法的关键，能够保证前缀词，被划分。
							segBoundIndex = i;
						}
						if (++i >= length) {
							break;
						}
						c = sentence.charAt(i);
						p = pChild;
						pChild = p.getChilds().get(Character.valueOf(c));
					} while (pChild != null);
					//切除非词典中词的前缀词
					if (--i >= segBoundIndex) {
						segBuffer.delete(segBuffer.length() - (i - segBoundIndex), segBuffer.length());
					}
					//还原现场
					i = segBoundIndex;
					p = (WordTrieNode) context.getRoot();
				}
			}
			segBuffer.append('|'); //添加分词标记
		}

		return segBuffer.toString();
	}

	public static void main(String args[]) throws IOException {
		MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(FileConstant.WORD_TRIE_TREE);
		System.out.println(mmsegger.segment("中华人民共和国是一个伟大的国家hello world"));
		System.out.println(mmsegger.segment("欢迎光临上海浦东发展银行的主页"));
		System.out.println(mmsegger.segment("三角形和平行四边形"));
		System.out.println(mmsegger.segment("售后和服务"));
	}
}
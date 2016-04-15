package com.xb.algoritm.segment;

import java.io.IOException;

import com.xb.bean.trie.ParticipleTrieNode;
import com.xb.business.trie.TrieDictionaryContext;
import com.xb.business.trie.impl.SegmentWordTrieDictionary;
import com.xb.constant.Constant;
import com.xb.utils.CharacterTypeUtil;

public class WordSegmenter {
	//public static SegmentWordTrieDictionary dict = null;

	TrieDictionaryContext context = null;

	public WordSegmenter() {
		//加载词典  
		//		String dictionaryName = Constant.TRIE_TREE;
		//		dict = SegmentWordTrieDictionary.getInstance(dictionaryName);

		context = new TrieDictionaryContext(SegmentWordTrieDictionary.getInstance(Constant.TRIE_TREE));
		//		SyntaxTrieNode r = (SyntaxTrieNode)context.getNodeRoot();
	}

	/** 
	 * 词典：用Trie树表示，每个节点都是一个TrieNode节点 
	 * 每个TrieNode节点中有: 
	 *   1.表示一个字 
	 *   2.以该字为前缀的所有的下一个字的HashMap<"字"， 字的TrieNode> 
	 *   3.bound标记，该字是不是一个词的结尾。在最大匹配中有用（Maximum Matching）  
	 *  
	 * 正向MM（Maximum Matching）算法的核心思想： 
	 *  1. 从句子中，取词  
	 *  2. 将词添加到分词列表中  
	 *  3. 将分词标记 "|"添加到分词表 
	 *  
	 * 其中的句子中的成分分为以下几种：  
	 * 1. 非分词：如分隔符，直接跳过  
	 * 2. 分词： 分词分为以下几种： 
	 *      a. 非中文分词：将分隔符分隔的连续的非中文字符作为一个分词  
	 *      b. 中文分词：  
	 *          i. 词典中的词：作为一个分词  
	 *         ii. 词典中的词的前缀：将每个字作为一个分词  
	 *        iii. 非词典中的词： 将每个字作为一个分词 
	 *  
	 * 该分词的核心：对于前缀词的划分 
	 */

	public String segment(String sentence) {
		StringBuffer segBuffer = new StringBuffer();

		ParticipleTrieNode p = (ParticipleTrieNode) context.getRoot();//dict.getNodeRoot();
		ParticipleTrieNode pChild = null;

		int length = sentence.length();
		int segBoundIndex = -1; //保存上次分词结束字符在sentence中的位置     

		for (int i = 0; i < length; ++i) {
			char c = sentence.charAt(i);
			if (CharacterTypeUtil.isCharSeperator(c)) {// 分隔符  
				segBuffer.append(c);
			} else if (CharacterTypeUtil.isCharOther(c)) {// 其他语言字符               
				do {
					segBuffer.append(c);
					if (++i == length) {
						break;
					}
					c = sentence.charAt(i);
				} while (CharacterTypeUtil.isCharOther(c));
				if (i != length)
					--i; //还原现场              
			} else if (CharacterTypeUtil.isCharChinese(c)) {
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
					p = (ParticipleTrieNode) context.getRoot();
				}
			}
			segBuffer.append('|'); //添加分词标记  
		}

		return new String(segBuffer);
	}

	//	public String segment(String sentence, String verison) {
	//		StringBuffer segBuffer = new StringBuffer();
	//
	//		int segBoundIdx = 0;
	//		int length = sentence.length();
	//		ParticipleTrieNode p = null;
	//		ParticipleTrieNode pChild = null;
	//
	//		for (int i = 0; i < length; i++) {
	//			char c = sentence.charAt(i);
	//
	//			p = dict.getNodeRoot();
	//			pChild = p.getChilds().get(Character.valueOf(c));
	//
	//			// 不在词典中的字符  
	//			if (pChild == null) {
	//				if (CharacterTypeUtil.isCharSeperator(c)) {
	//					segBuffer.append(c);// do something;  
	//				}
	//				if (CharacterTypeUtil.isCharChinese(c)) {
	//					segBuffer.append(c);
	//				} else {
	//					do { // 非中文字符  
	//						segBuffer.append(c);
	//						if (++i == length) {
	//							break;
	//						}
	//						c = sentence.charAt(i);
	//					} while (CharacterTypeUtil.isCharOther(c));
	//					if (i != length)
	//						--i; //还原现场  
	//				}
	//			} else { // 中文字词  
	//				while (pChild != null) {
	//					if (p == dict.getNodeRoot() || pChild.isBound()) { //词典中的词或者词典中词的前缀词；前缀词将被单字划分
	//						segBoundIdx = i;
	//					}
	//					segBuffer.append(c);
	//					if (++i == length) {
	//						break;
	//					}
	//					c = sentence.charAt(i);
	//					p = pChild;
	//					pChild = p.getChilds().get(Character.valueOf(c));
	//				}
	//				//切除分词表中不在词典中的前缀字词  
	//				if (--i > segBoundIdx) {
	//					segBuffer.delete(segBuffer.length() - (i - segBoundIdx), segBuffer.length());
	//				}
	//				//还原现场  
	//				i = segBoundIdx;
	//			}
	//			segBuffer.append('|');
	//		}
	//
	//		return new String(segBuffer);
	//	}

	public static void main(String args[]) throws IOException {
		WordSegmenter mmsegger = new WordSegmenter();
		System.out.println(mmsegger.segment("中华人民共和国是一个伟大的国家hello world"));
		System.out.println(mmsegger.segment("欢迎光临上海浦东发展银行的主页"));
		System.out.println(mmsegger.segment("小红是个爱学习的好学生!!!!!"));
		//        System.out.println(mmsegger.segment("中华民de hello world!人民共"));  
		//        System.out.println(mmsegger.segment("中华人民共"));  
		//        System.out.println(mmsegger.segment("中华人民共和国家"));  
		//        System.out.println(mmsegger.segment("爱国"));  
		//        System.out.println(mmsegger.segment("爱我Love你"));  
		//        System.out.println(mmsegger.segment("京华时报２００８年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。"));  

		//        System.out.println("another version: ");          
		//		System.out.println(mmsegger.segment("中华人民共和国是一个伟大的国家hello world", " "));
		//        System.out.println(mmsegger.segment("小红是个爱学习的好学生!!!!!", " "));  
		//        System.out.println(mmsegger.segment("中华民de hello world!人民共", " "));  
		//        System.out.println(mmsegger.segment("中华人民共", " "));  
		//        System.out.println(mmsegger.segment("中华人民共和国家", " "));  
		//        System.out.println(mmsegger.segment("爱国", " "));  
		//        System.out.println(mmsegger.segment("爱我Love你", " "));  
		//        System.out.println(mmsegger.segment("京华时报2008年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。", ""));  

		//System.out.println(CharacterTypeUtil.isCharSeperator(' '));

		//		System.out.println("1:" + Thread.currentThread().getContextClassLoader().getResource("ChiCoreDict.utf8"));
		//		System.out.println("2:" + WordSegmenter.class.getClassLoader().getResource(""));
		//		System.out.println("3:" + ClassLoader.getSystemResource(""));
		//		System.out.println("4:" + WordSegmenter.class.getResource(""));//IdcardClient.class文件所在路径  
		//		System.out.println("5:" + WordSegmenter.class.getResource("/")); // Class包所在路径，得到的是URL对象，用url.getPath()获取绝对路径String  
		//		System.out.println("6:" + new File("").getAbsolutePath());
		//		System.out.println("7:" + System.getProperty("user.dir"));
		//		System.out.println("8:" + System.getProperty("file.encoding"));//获取文件编码
		//		System.out.println("9:" + Thread.currentThread().getContextClassLoader().getResourceAsStream("xxx.txt"));
	}
}
package com.xb.algoritm.segment;

import com.xb.bean.trie.WordTrieNode;
import com.xb.business.trie.TrieDictionaryContext;
import com.xb.business.trie.impl.SegmentWordTrieDictionary;
import com.xb.constant.FileConstant;
import com.xb.utils.TextUtils;

import java.io.IOException;

public class ReverseMaxMatchingWordSegmenter {
	private TrieDictionaryContext context = null;

	public ReverseMaxMatchingWordSegmenter(String path) {
		//加载词典
		context = new TrieDictionaryContext(SegmentWordTrieDictionary.getInstance(path));
	}

	public String segment(String sentence) {
		StringBuffer segBuffer = new StringBuffer();

		WordTrieNode p = (WordTrieNode) context.getReverseRoot();
		WordTrieNode pChild = null;

		int length = sentence.length();
		int segBoundIndex = -1; //保存上次分词结束字符在sentence中的位置

		for(int i = length - 1; i >= 0; --i) {
			char c = sentence.charAt(i);
			if (TextUtils.isCharSeperator(c)) {// 分隔符
				segBuffer.append(c);
			} else if (TextUtils.isOtherChar(c)) {// 其他语言字符
				do {
					segBuffer.append(c);
					if (--i == 0) {
						break;
					}
					c = sentence.charAt(i);
				} while (TextUtils.isOtherChar(c));
				if (i != length)
					++i; //还原现场
			} else if (TextUtils.isChineseChar(c)) {
				pChild = p.getChilds().get(Character.valueOf(c));
				if (pChild == null) {// 不在词典中的中文字符
					segBuffer.append(c);
				} else {
					do {// 在词典中的词
						segBuffer.append(c);
						if (p == context.getReverseRoot() || pChild.isBound()) { // 算法的关键，能够保证前缀词，被划分。
							segBoundIndex = i;
						}
						if (i-- <= 0) {
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
					p = (WordTrieNode) context.getReverseRoot();
				}
			}
			segBuffer.append('|'); //添加分词标记
		}

		return segBuffer.reverse().toString();
	}

	public static void main(String args[]) throws IOException {
		ReverseMaxMatchingWordSegmenter mmsegger = new ReverseMaxMatchingWordSegmenter(FileConstant.WORD_TRIE_TREE);
		System.out.println(mmsegger.segment("中华人民共和国是一个伟大的国家hello world"));
		System.out.println(mmsegger.segment("欢迎光临上海浦东发展银行的主页"));
		System.out.println(mmsegger.segment("三角形和平行四边形"));
		System.out.println(mmsegger.segment("售后和服务"));
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

		//System.out.println(TextUtils.isCharSeperator(' '));

		//		System.out.println("1:" + Thread.currentThread().getContextClassLoader().getResource("chiCoreDict.utf8"));
		//		System.out.println("2:" + MaxMatchingWordSegmenter.class.getClassLoader().getResource(""));
		//		System.out.println("3:" + ClassLoader.getSystemResource(""));
		//		System.out.println("4:" + MaxMatchingWordSegmenter.class.getResource(""));//IdcardClient.class文件所在路径
		//		System.out.println("5:" + MaxMatchingWordSegmenter.class.getResource("/")); // Class包所在路径，得到的是URL对象，用url.getPath()获取绝对路径String
		//		System.out.println("6:" + new File("").getAbsolutePath());
		//		System.out.println("7:" + System.getProperty("user.dir"));
		//		System.out.println("8:" + System.getProperty("file.encoding"));//获取文件编码
		//		System.out.println("9:" + Thread.currentThread().getContextClassLoader().getResourceAsStream("xxx.txt"));
	}
}

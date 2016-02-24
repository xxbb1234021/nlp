package com.xb.pattern.hmm.impl;

import java.io.*;
import java.util.*;

import com.xb.constant.Constant;
import com.xb.pattern.hmm.HmmModelBuilder;
import org.apache.log4j.Logger;

/**
 * Created by kevin on 2016/1/21.
 */
public class HmmSegmentWordsBuilder2 extends HmmModelBuilder {
	private static Logger LOGGER = Logger.getLogger(HmmSegmentWordsBuilder2.class);

	private int wordTagNum = 0;

	private int wordNum = 0;

	private String[] text; // 用于存储训练样本中的词语和词性

	private String[] word; // 用于存储训练样本中的词语

	private String[] wordTag; // 用于存储单个词的词性标注符号,是语料库中的词性序列

	private String[] diffWordTag;// 存储不同的词性符号

	private String[] diffWord;// 存储不同的词组

	private double[] prioriProbability;// 词性的先验概率

	private double[][] transformProbability;

	private double[][] emissionProbability;

	private Map<String, Integer> wordTagMap = new HashMap<String, Integer>(); // 创建wordTagMap，存储单个词的词性及其频率

	private Map<String, Integer> wordMap = new HashMap<String, Integer>(); // 创建wordMap，存储不同的词组及其频率

	private Map<String, Integer> transformFrequencyMap = new HashMap<String, Integer>(); // 创建transformFrequencyMap，存储每两个词的词性及其频率

	private Map<String, Integer> emissionsFrequencyMap = new HashMap<String, Integer>(); // 创建emissionsFrequencyMap，存储每两个词的词性及其频率

	private Map<String, Integer> wordPositionMap = new HashMap<String, Integer>();// 为了后面的检索速度，保存语料库中的不同词组的位置，和diffWord对应

	private static HmmSegmentWordsBuilder2 ghm = null;

	private HmmSegmentWordsBuilder2(String fn) {
		splitCorpus(fn);
		wordTagSum();
		wordSum();
	}

	public static HmmSegmentWordsBuilder2 getInstance(String fileName) {
		if (ghm == null) {
			synchronized (HmmSegmentWordsBuilder2.class) {
				if (ghm == null) {
					ghm = new HmmSegmentWordsBuilder2(fileName);
				}
			}
		}
		return ghm;
	}

	private boolean splitCorpus(String fileName) {
		readCorpus(fileName, Constant.CHARSET_UTF8);

		// 获取预料语料库中的一个个不同的词组(以空格分开)，词组后附有相应的词性
		text = content.toString().split("\\s{1,}");
		List<String> pinyingList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		String[] split = null;
		for (String s : text) {
			split = s.split("/");
			pinyingList.add(split[0]);
			wordList.add(split[1]);
		}

		// 保存汉字
		word = pinyingList.toArray(new String[pinyingList.size()]);
		// 保存词性
		wordTag = wordList.toArray(new String[wordList.size()]);

		return true;
	}

	/**
	 * 该函数用来统计不同的词性及其频率
	 * wordTagMap：保存不同的词性及其频率
	 * diffWordTag：只保存不同的词性
	 */
	public void wordTagSum() {
		for (int i = 1; i < wordTag.length; i++) {
			if (wordTagMap.containsKey(wordTag[i])) {
				wordTagMap.put(wordTag[i], wordTagMap.get(wordTag[i]) + 1);
			} else {
				wordTagMap.put(wordTag[i], 1);
			}
		}
		wordTagNum = wordTagMap.size();
		diffWordTag = new String[wordTagNum];

		List<String> diffWordTagList = new ArrayList<String>();
		for (String s : wordTagMap.keySet()) {
			diffWordTagList.add(s);
		}
		diffWordTag = diffWordTagList.toArray(new String[diffWordTagList.size()]);
	}

	/**
	 * 统计语料库中的不同词组
	 * wordMap：用来保存词组和频率
	 * diffWord：只用来保存不同的词组
	 * wordPositionMap：保存词组在diffWord中的位置
	 */
	public void wordSum() {
		for (int i = 0; i < word.length; i++) {
			if (wordMap.containsKey(word[i])) {
				wordMap.put(word[i], wordMap.get(word[i]) + 1);
			} else {
				wordMap.put(word[i], 1);
			}
		}
		wordNum = wordMap.size();
		diffWord = new String[wordNum];

		List<String> diffWordList = new ArrayList<String>();
		int index = 0;
		for (String s : wordMap.keySet()) {
			diffWordList.add(s);
			wordPositionMap.put(s, index++);
		}
		diffWord = diffWordList.toArray(new String[diffWordList.size()]);
	}

	/**
	 * 计算转移频数
	 */
	public void transformFrequencySum() {
		String temp = "";
		for (int i = 0; i < wordTag.length - 1; i++) {
			temp = wordTag[i] + "," + wordTag[i + 1];
			if (transformFrequencyMap.containsKey(temp)) {
				transformFrequencyMap.put(temp, transformFrequencyMap.get(temp) + 1);
			} else {
				transformFrequencyMap.put(temp, 1);
			}
		}
	}

	/**
	 * 计算发射频数
	 */
	public void emissonFrequencySum() {
		for (int i = 0; i < text.length; i++) {
			if (emissionsFrequencyMap.containsKey(text[i])) {
				emissionsFrequencyMap.put(text[i], emissionsFrequencyMap.get(text[i]) + 1);
			} else {
				emissionsFrequencyMap.put(text[i], 1);
			}
		}
	}

	/**
	 * 计算初始概率 也即不同词性在预料中出现的次数
	 */
	public void calculatePrioriProbability() {
		prioriProbability = new double[wordTagNum];

		int allCharacterCount = 0;
		for (int i = 0; i < wordTagNum; i++) {
			allCharacterCount += wordTagMap.get(diffWordTag[i]);
		}
		for (int i = 0; i < wordTagNum; i++) {
			if (diffWordTag[i].equals("S") || diffWordTag[i].equals("B"))
				prioriProbability[i] = wordTagMap.get(diffWordTag[i]) * 1.0 / allCharacterCount;
		}
	}

	/**
	 * 计算转移概率 根据统计的不同词性，计算由一个词性转移到另一个词性的概率
	 */
	public void calculateTransformProbability() {
		transformProbability = new double[wordTagNum][wordTagNum];

		String front = "";
		String last = "";
		int numerator = 0;
		int denominator = 0;

		for (int i = 0; i < wordTagNum; i++) {
			for (int j = 0; j < wordTagNum; j++) {
				front = diffWordTag[i];
				last = diffWordTag[j];

				if (transformFrequencyMap.containsKey(front + "," + last)) {
					numerator = transformFrequencyMap.get(front + "," + last);
					denominator = wordTagMap.get(front);
					transformProbability[i][j] = numerator * 100.0 / denominator;// 因为有些概率很小，为了精确，将其放大100倍
				}
			}
		}
	}

	/**
	 * 计算发射概率 根据语料库中的不同词组和词性，
	 * 计算发射概率 即在某个词性下，是某个词组的概率
	 */
	public void calculateEmissionProbability() {
		emissionProbability = new double[wordTagNum][wordNum];
		String wordTag = "";
		String word = "";
		int numerator = 0;
		int denominator = 0;
		for (int i = 0; i < wordTagNum; i++) {
			for (int j = 0; j < wordNum; j++) {
				wordTag = diffWordTag[i];
				word = diffWord[j];

				if (emissionsFrequencyMap.containsKey(word + "/" + wordTag)) {
					numerator = emissionsFrequencyMap.get(word + "/" + wordTag);
					denominator = wordTagMap.get(wordTag);
					emissionProbability[i][j] = numerator * 100.0 / denominator;// 因为有些概率很小，为了精确，将其放大100倍
				}
			}
		}
	}

	public String[] getWordTag() {
		return wordTag;
	}

	public void setWordTag(String[] wordTag) {
		this.wordTag = wordTag;
	}

	public double[] getPrioriProbability() {
		return prioriProbability;
	}

	public void setPrioriProbability(double[] prioriProbability) {
		this.prioriProbability = prioriProbability;
	}

	public double[][] getTransformProbability() {
		return transformProbability;
	}

	public void setTransformProbability(double[][] transformProbability) {
		this.transformProbability = transformProbability;
	}

	public double[][] getEmissionProbability() {
		return emissionProbability;
	}

	public void setEmissionProbability(double[][] emissionProbability) {
		this.emissionProbability = emissionProbability;
	}

	public Map<String, Integer> getWordPositionMap() {
		return wordPositionMap;
	}

	public void setWordPositionMap(Hashtable<String, Integer> wordPositionMap) {
		this.wordPositionMap = wordPositionMap;
	}

	public int getWordTagNum() {
		return wordTagNum;
	}

	public void setWordTagNum(int wordTagNum) {
		this.wordTagNum = wordTagNum;
	}

	public String[] getDiffWordTag() {
		return diffWordTag;
	}

	public void setDiffWordTag(String[] diffWordTag) {
		this.diffWordTag = diffWordTag;
	}

	public String[] getDiffWord() {
		return diffWord;
	}

	public void setDiffWord(String[] diffWord) {
		this.diffWord = diffWord;
	}
}

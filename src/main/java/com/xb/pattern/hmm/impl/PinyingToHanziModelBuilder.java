package com.xb.pattern.hmm.impl;

import java.io.*;
import java.util.*;

import com.xb.pattern.hmm.HmmModelBuilder;

/**
 * Created by kevin on 2016/1/19.
 */
public class PinyingToHanziModelBuilder extends HmmModelBuilder {
	private int wordNum = 0;

	private int pinyingNum = 0;

	private String[] text; // 用于存储训练样本中的拼音和词

	private String[] pinying; // 用于存储训练样本中的拼音

	private String[] word; // 用于存储单个词

	private String[] diffWord;// 存储不同的词

	private String[] diffPinying;// 存储不同的拼音

	private double[] prioriProbability;// 词的先验概率

	private double[][] transformProbability;

	private double[][] emissionProbability;

	private Map<String, Integer> wordMap = new HashMap<String, Integer>(); // 创建wordTagMap，存储单个词的词及其频率

	private Map<String, Integer> pinyingMap = new HashMap<String, Integer>(); // pinyingMap，存储不同的拼音及其频率

	private Map<String, Integer> transformFrequencyMap = new HashMap<String, Integer>(); // transformFrequencyMap，存储每两个词的词及其频率

	private Map<String, Integer> emissionsFrequencyMap = new HashMap<String, Integer>(); // emissionsFrequencyMap，存储每两个词的词及其频率

	private Map<String, Integer> pinyingPositionMap = new HashMap<String, Integer>();// 为了后面的检索速度，保存语料库中的不同词组的位置，和diffWord对应

	private static PinyingToHanziModelBuilder ghm = null;

	private PinyingToHanziModelBuilder(String fn) {
		readCorpus(fn);
		wordSum();
		pinyingSum();
	}

	public static PinyingToHanziModelBuilder getInstance(String fileName) {
		if (ghm == null) {
			synchronized (PinyingToHanziModelBuilder.class) {
				if (ghm == null) {
					ghm = new PinyingToHanziModelBuilder(fileName);
				}
			}
		}
		return ghm;
	}

	private boolean readCorpus(String fileName) {
		String line;
		BufferedReader br = null;
		StringBuffer content = new StringBuffer();
		try {
			File file = new File(this.getClass().getResource("/").getPath() + fileName);
			InputStream is = new FileInputStream(file);

			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 获取预料语料库中的一个个不同的词组(以空格分开)，词组后附有相应的词
		text = content.toString().split("\\s{1,}");

		List<String> pinyingList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		String[] split = null;
		for (String s : text) {
			split = s.split("/");
			pinyingList.add(split[0]);
			wordList.add(split[1]);
		}

		// 保存拼音
		pinying = pinyingList.toArray(new String[pinyingList.size()]);
		// 保存汉字
		word = wordList.toArray(new String[wordList.size()]);
		return true;
	}

	/**
	 * 该函数用来统计不同的词及其频率
	 * wordMap：保存不同的词及其频率
	 * diffWord：只保存不同的词
	 */
	public void wordSum() {
		for (int i = 1; i < word.length; i++) {
			if (wordMap.containsKey(word[i])) {
				wordMap.put(word[i], wordMap.get(word[i]) + 1);
			} else {
				wordMap.put(word[i], 1);
			}
		}
		wordNum = wordMap.size();
		diffWord = new String[wordNum];

		List<String> diffWordList = new ArrayList<String>();
		for (String s : wordMap.keySet()) {
			diffWordList.add(s);
		}
		diffWord = diffWordList.toArray(new String[diffWordList.size()]);
	}

	/**
	 * 统计语料库中的不同拼音
	 * pinyingMap：用来保存词组和频率
	 * diffPinying：只用来保存不同的词组
	 * pinyingPositionMap：保存词组在diffWord中的位置
	 */
	public void pinyingSum() {
		for (int i = 0; i < pinying.length; i++) {
			if (pinyingMap.containsKey(pinying[i])) {
				pinyingMap.put(pinying[i], pinyingMap.get(pinying[i]) + 1);
			} else {
				pinyingMap.put(pinying[i], 1);
			}
		}
		pinyingNum = pinyingMap.size();
		diffPinying = new String[pinyingNum];

		List<String> diffWordList = new ArrayList<String>();
		int index = 0;

		for (String s : pinyingMap.keySet()) {
			diffWordList.add(s);
			pinyingPositionMap.put(s, index++);
		}
		diffPinying = diffWordList.toArray(new String[diffWordList.size()]);
	}

	/**
	 * 计算转移频数
	 */
	public void transformFrequencySum() {
		String temp = "";
		for (int i = 0; i < word.length - 1; i++) {
			temp = word[i] + "," + word[i + 1];
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
	 * 计算初始概率 也即不同词在预料中出现的次数
	 */
	public void calculatePrioriProbability() {
		prioriProbability = new double[wordNum];

		int allCharacterCount = 0;
		for (int i = 0; i < wordNum; i++) {
			allCharacterCount += wordMap.get(diffWord[i]);
		}
		for (int i = 0; i < wordNum; i++) {
			prioriProbability[i] = wordMap.get(diffWord[i]) * 1.0 / allCharacterCount;
		}
	}

	/**
	 * 计算转移概率 根据统计的不同词，计算由一个词转移到另一个词的概率
	 */
	public void calculateTransformProbability() {
		transformProbability = new double[wordNum][wordNum];

		String front = "";
		String last = "";
		int numerator = 0;
		int denominator = 0;

		for (int i = 0; i < wordNum; i++) {
			for (int j = 0; j < wordNum; j++) {
				front = diffWord[i];
				last = diffWord[j];

				if (transformFrequencyMap.containsKey(front + "," + last)) {
					numerator = transformFrequencyMap.get(front + "," + last);
					denominator = wordMap.get(front);
					transformProbability[i][j] = numerator * 100.0 / denominator;// 因为有些概率很小，为了精确，将其放大100倍
				}
			}
		}
	}

	/**
	 * 计算发射概率 根据语料库中的不同词组和词，
	 * 计算发射概率 即在某个词下，是某个词组的概率
	 */
	public void calculateEmissionProbability() {
		emissionProbability = new double[wordNum][pinyingNum];
		String word = "";
		String pinying = "";
		int numerator = 0;
		int denominator = 0;
		for (int i = 0; i < wordNum; i++) {
			for (int j = 0; j < pinyingNum; j++) {
				word = diffWord[i];
				pinying = diffPinying[j];

				if (emissionsFrequencyMap.containsKey(pinying + "/" + word)) {
					numerator = emissionsFrequencyMap.get(pinying + "/" + word);
					denominator = wordMap.get(word);
					emissionProbability[i][j] = numerator * 100.0 / denominator;// 因为有些概率很小，为了精确，将其放大100倍
				}
			}
		}
	}

	public String[] getWord() {
		return word;
	}

	public void setWord(String[] word) {
		this.word = word;
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

	public Map<String, Integer> getPinyingPositionMap() {
		return pinyingPositionMap;
	}

	public void setPinyingPositionMap(Hashtable<String, Integer> pinyingPositionMap) {
		this.pinyingPositionMap = pinyingPositionMap;
	}

	public int getWordNum() {
		return wordNum;
	}

	public void setWordNum(int wordNum) {
		this.wordNum = wordNum;
	}

	public String[] getDiffWord() {
		return diffWord;
	}

	public void setDiffWord(String[] diffWord) {
		this.diffWord = diffWord;
	}

	public String[] getDiffPinying() {
		return diffPinying;
	}

	public void setDiffPinying(String[] diffPinying) {
		this.diffPinying = diffPinying;
	}
}

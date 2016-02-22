package com.xb.pattern.hmm.impl;

import java.io.*;
import java.util.*;

import com.xb.pattern.hmm.HmmModelBuilder;

/**
 * Created by kevin on 2016/1/11.
 * 1.找出所有的词性个数，词性的频率
 */
public class WordTaggingModelBuilder extends HmmModelBuilder {
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

	private static WordTaggingModelBuilder ghm = null;

	private WordTaggingModelBuilder(String fn) {
		readCorpus(fn);
		wordTagSum();
		wordSum();
	}

	public static WordTaggingModelBuilder getInstance(String fileName) {
		if (ghm == null) {
			synchronized (WordTaggingModelBuilder.class) {
				if (ghm == null) {
					ghm = new WordTaggingModelBuilder(fileName);
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

			br = new BufferedReader(new InputStreamReader(is, "GBK"));
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 获取预料语料库中的一个个不同的词组(以空格分开)，词组后附有相应的词性
		text = content.toString().split("\\s{1,}");
		// 去除词性标注，只保存词组
		word = content.toString().split("(/[a-z]*\\s{0,})");// "/"后面跟着一个或者多个字母然后是多个空格

		/*
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < word.length; i++)
		{
			String s = word[i];
			if(s.startsWith("1998")){
				if(StringUtils.isBlank(sb.toString()))
					continue;
				list.add(sb.toString());
				sb = new StringBuilder();
				continue;
			}
			sb.append(s);
		}
		list.add(sb.toString());
		MakePinyingCorpus m = new MakePinyingCorpus();
		m.writeCorpusToFile("D:\\workspace\\nlp\\src\\main\\resources\\tag\\199801.utf8", list.toArray(new String[list.size()]));
		*/

		// 获取语料库中从前往后的所有词组的词性
		wordTag = content.toString().split("[0-9|-]*/|\\s{1,}[^a-z]*"); // 开头的日期或者空格+非字母作为分隔符
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

	/**
	 * 分词方法是从左往右，最大匹配模式。但是程序中采用的语料库却倾向于
	 * 最小匹配模式。所以我们初次分词的结果有可能不在语料库中。在此我们将语料库不能识别的 词组再次进行分词尝试让算法找到更多的词。
	 *
	 * @param seg
	 * @return
	 */
	public List<String> smallSeg(List<String> seg) {
		ArrayList<String> smallArrayList = new ArrayList<String>();
		String temp = "";
		boolean canSpilt = false;
		int index = 0;
		for (int i = 0; i < seg.size(); i++) {
			temp = seg.get(i);
			canSpilt = false;
			index = 0;
			if (wordPositionMap.get(temp) == null) {
				for (int j = 1; j < temp.length(); j++) {
					if (wordPositionMap.get(temp.substring(0, j)) != null
							&& wordPositionMap.get(temp.substring(j)) != null) {
						canSpilt = true;
						index = j;
						break;
					}
				}
			}

			if (canSpilt) {
				smallArrayList.add(temp.substring(0, index));
				smallArrayList.add(temp.substring(index));
			} else {
				smallArrayList.add(temp);
			}
		}
		return smallArrayList;
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

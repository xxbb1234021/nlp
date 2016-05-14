package com.xb.business.hmm.builderImpl;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.xb.constant.Constant;
import com.xb.utils.ReadAndWriteJson;
import org.apache.log4j.Logger;

/**
 * Created by kevin on 2016/5/15.
 */
public class PinyingToHanziModelBuilder2 extends AbstractPinyingToHanziModel {
	private static Logger LOGGER = Logger.getLogger(PinyingToHanziModelBuilder2.class);

	private int wordNum = 0;

	private int pinyinNum = 0;

	private String[] word; // 用于存储单个词

	private String[] diffWord;// 存储不同的词

	private String[] diffPinyin;// 存储不同的拼音

	private Map<String, Integer> pinyingPositionMap = new HashMap<String, Integer>();// 为了后面的检索速度，保存语料库中的不同词组的位置，和diffWord对应

	private static PinyingToHanziModelBuilder2 ghm = null;

	private JSONObject initJsonObjectData = null;

	private JSONObject pinyinJsonObject = null;

	private JSONObject transitionJsonObjectData = null;

	private JSONObject emissionJsonObjectData = null;

	private PinyingToHanziModelBuilder2(String fn) {
		readCorpus(fn);
		wordSum();
		pinyingSum();
	}

	public static PinyingToHanziModelBuilder2 getInstance(String fileName) {
		if (ghm == null) {
			synchronized (PinyingToHanziModelBuilder2.class) {
				if (ghm == null) {
					ghm = new PinyingToHanziModelBuilder2(fileName);
				}
			}
		}
		return ghm;
	}

	private boolean readCorpus(String fileName) {
		String s = ReadAndWriteJson.readFile(Constant.PINYIN_TAG_START);
		JSONObject initJsonObject = JSONObject.parseObject(s);
		initJsonObjectData = initJsonObject.getJSONObject("data");

		s = ReadAndWriteJson.readFile(Constant.PINYIN_TAG_PY2HZ);
		pinyinJsonObject = JSONObject.parseObject(s);

		s = ReadAndWriteJson.readFile(Constant.PINYIN_TAG_TRANSITION);
		JSONObject transitionJsonObject = JSONObject.parseObject(s);
		transitionJsonObjectData = transitionJsonObject.getJSONObject("data");

		s = ReadAndWriteJson.readFile(Constant.PINYIN_TAG_EMISSION);
		JSONObject emissionJsonObject = JSONObject.parseObject(s);
		emissionJsonObjectData = emissionJsonObject.getJSONObject("data");

		return true;
	}

	/**
	 * 该函数用来统计不同的词及其频率
	 * wordMap：保存不同的词及其频率
	 * diffWord：只保存不同的词
	 */
	public void wordSum() {
		wordNum = initJsonObjectData.size();

		diffWord = new String[wordNum];
		List<String> diffWordList = new ArrayList<String>();
		for (java.util.Map.Entry<String, Object> entry : initJsonObjectData.entrySet()) {
			diffWordList.add(entry.getKey());
		}
		diffWord = diffWordList.toArray(new String[diffWordList.size()]);
	}

	/**
	 * 统计语料库中的不同拼音
	 * pinyingMap：用来保存词组和频率
	 * diffPinyin：只用来保存不同的词组
	 * pinyingPositionMap：保存词组在diffWord中的位置
	 */
	public void pinyingSum() {
		pinyinNum = pinyinJsonObject.size();

		diffPinyin = new String[wordNum];
		List<String> diffPinyinList = new ArrayList<String>();
		int index = 0;
		for (java.util.Map.Entry<String, Object> entry : pinyinJsonObject.entrySet()) {
			diffPinyinList.add(entry.getKey());
			pinyingPositionMap.put(entry.getKey(), index++);
		}
		diffPinyin = diffPinyinList.toArray(new String[diffPinyinList.size()]);
	}

	/**
	 * 计算转移频数
	 */
	public void transformFrequencySum() {
	}

	/**
	 * 计算发射频数
	 */
	public void emissonFrequencySum() {
	}

	/**
	 * 计算初始概率 也即不同词在预料中出现的次数
	 */
	public void calculateInitProbability() {
		prioriProbability = new double[wordNum];

		int i = 0;
		for (java.util.Map.Entry<String, Object> entry : initJsonObjectData.entrySet()) {
			prioriProbability[i++] = Double.parseDouble(entry.getValue() + "");
		}
	}

	/**
	 * 计算转移概率 根据统计的不同词，计算由一个词转移到另一个词的概率
	 */
	public void calculateTransformProbability() {
		transformProbability = new double[wordNum][wordNum];
		String front = "";
		String last = "";
		JSONObject lastJsonObject = null;

		for (int i = 0; i < wordNum; i++) {
			for (int j = 0; j < wordNum; j++) {
				front = diffWord[i];
				last = diffWord[j];

				lastJsonObject = transitionJsonObjectData.getJSONObject(front);
				Object prob = lastJsonObject.get(last);
				if (prob == null) {
					transformProbability[i][j] = 0.0;
				} else {
					transformProbability[i][j] = Double.parseDouble(prob + "");
				}
			}
		}
	}

	/**
	 * 计算发射概率 根据语料库中的不同词组和词，
	 * 计算发射概率 即在某个词下，是某个词组的概率
	 */
	public void calculateEmissionProbability() {
		emissionProbability = new double[wordNum][pinyinNum];
		String word = "";
		String pinying = "";
		JSONObject pyJsonObject = null;

		for (int i = 0; i < wordNum; i++) {
			for (int j = 0; j < pinyinNum; j++) {
				word = diffWord[i];
				pinying = diffPinyin[j];

				pyJsonObject = emissionJsonObjectData.getJSONObject(word);
				Object prob = pyJsonObject.get(pinying);
				if (prob == null) {
					emissionProbability[i][j] = 0.0;
				} else {
					emissionProbability[i][j] = Double.parseDouble(prob + "");
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

	public String[] getDiffPinyin() {
		return diffPinyin;
	}

	public void setDiffPinyin(String[] diffPinyin) {
		this.diffPinyin = diffPinyin;
	}
}

package com.xb.utils.pinyin.util;

import com.alibaba.fastjson.JSONObject;
import com.xb.constant.Constant;
import com.xb.constant.FileConstant;
import com.xb.utils.FileNIOUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2016/5/19.
 */
public class MakePinyingCorpus {
	private static int wordNum = 0;

	private static int pinyingNum = 0;

	private static String[] diffWord;// 存储不同的词

	private static String[] diffPinyin;// 存储不同的拼音

	private static Map<String, Integer> wordMap = new HashMap<String, Integer>(); // 创建wordMap，存储单个词的词及其频率

	private static Map<String, Integer> pinyinMap = new HashMap<String, Integer>(); // pinyinMap，存储不同的拼音及其频率

	private static Map<String, Integer> transformFrequencyMap = new HashMap<String, Integer>(); // transformFrequencyMap，存储每两个词的词及其频率

	private static Map<String, Integer> emissionsFrequencyMap = new HashMap<String, Integer>(); // emissionsFrequencyMap，存储每两个词的词及其频率

	public static void readCorpus(String path, String charCode) {
		List<String> lines = FileNIOUtil.readFileLine(path, charCode);

		for (String str : lines) {
			wordSum(str);
			pinyingSum(str);
			transformFrequencySum(str);
			emissonFrequencySum(str);
		}

		writeInitJson();
		writeTransformJson();
		writeEmissionJson();

		//writePy2hzJson();
	}

	public static void wordSum(String line) {
		String[] words = line.split(" ");
		for (int i = 0; i < words.length; i++) {
			String[] word = words[i].split("\\/");

			if (wordMap.containsKey(word[1])) {
				wordMap.put(word[1], wordMap.get(word[1]) + 1);
			} else {
				wordMap.put(word[1], 1);
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

	public static void pinyingSum(String line) {
		String[] pinyins = line.split(" ");
		for (int i = 0; i < pinyins.length; i++) {
			String[] pinyin = pinyins[i].split("\\/");

			if (pinyinMap.containsKey(pinyin[0])) {
				pinyinMap.put(pinyin[0], pinyinMap.get(pinyin[0]) + 1);
			} else {
				pinyinMap.put(pinyin[0], 1);
			}
		}

		pinyingNum = pinyinMap.size();
		diffPinyin = new String[pinyingNum];

		List<String> diffWordList = new ArrayList<String>();

		for (String s : pinyinMap.keySet()) {
			diffWordList.add(s);
		}
		diffPinyin = diffWordList.toArray(new String[diffWordList.size()]);
	}

	/**
	 * 计算转移频数
	 */
	public static void transformFrequencySum(String line) {
		String temp = "";
		String[] words = line.split(" ");
		for (int i = 0; i < words.length - 1; i++) {
			String[] first = words[i].split("\\/");
			String[] second = words[i + 1].split("\\/");
			temp = first[1] + "," + second[1];
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
	public static void emissonFrequencySum(String line) {
		String[] words = line.split(" ");
		for (int i = 0; i < words.length; i++) {
			if (emissionsFrequencyMap.containsKey(words[i])) {
				emissionsFrequencyMap.put(words[i], emissionsFrequencyMap.get(words[i]) + 1);
			} else {
				emissionsFrequencyMap.put(words[i], 1);
			}
		}
	}

	public static void writeInitJson() {
		int allCharacterCount = 0;
		JSONObject initJsonObjectData = new JSONObject();
		JSONObject initProbJsonObjectData = new JSONObject();
		for (int i = 0; i < wordNum; i++) {
			allCharacterCount += wordMap.get(diffWord[i]);
		}

		for (java.util.Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			double prob = entry.getValue() * 1.0 / allCharacterCount;
			initProbJsonObjectData.put(entry.getKey(), prob);
		}

		initJsonObjectData.put("data", initProbJsonObjectData);

        //FileNIOUtil.writeFile("pinyin_data/start.json", initJsonObjectData.toJSONString(), Constant.CHARSET_UTF8);
	}

	public static void writeTransformJson() {
		String front = "";
		String last = "";
		int numerator = 0;
		int denominator = 0;

		JSONObject transformJson = new JSONObject();
		JSONObject probTransformJson = null;

		for (int i = 0; i < wordNum; i++) {
			probTransformJson = new JSONObject();
			for (int j = 0; j < wordNum; j++) {

				front = diffWord[i];
				last = diffWord[j];

				if (transformFrequencyMap.containsKey(front + "," + last)) {
					numerator = transformFrequencyMap.get(front + "," + last);
					denominator = wordMap.get(front);
					double prob = numerator * 1.0 / denominator;
					probTransformJson.put(last, prob);
				}
			}
			transformJson.put(front, probTransformJson);
		}

		JSONObject transformJsonData = new JSONObject();
		transformJsonData.put("data", transformJson);

        //FileNIOUtil.writeFile("pinyin_data/transition.json", transformJsonData.toJSONString(), Constant.CHARSET_UTF8);
	}

	public static void writeEmissionJson() {
		String word = "";
		String pinyin = "";
		int numerator = 0;
		int denominator = 0;

		JSONObject emissionJson = new JSONObject();
		JSONObject probEmissionJson = null;

		for (int i = 0; i < wordNum; i++) {
			probEmissionJson = new JSONObject();
			for (int j = 0; j < pinyingNum; j++) {
				word = diffWord[i];
				pinyin = diffPinyin[j];

				if (emissionsFrequencyMap.containsKey(pinyin + "/" + word)) {
					numerator = emissionsFrequencyMap.get(pinyin + "/" + word);
					denominator = wordMap.get(word);
					double prob = numerator * 1.0 / denominator;
					probEmissionJson.put(pinyin, prob);
				}
			}
			emissionJson.put(word, probEmissionJson);
		}

		JSONObject emissionJsonData = new JSONObject();
		emissionJsonData.put("data", emissionJson);

		//FileNIOUtil.writeFile("pinyin_data/emission.json", emissionJsonData.toJSONString(), Constant.CHARSET_UTF8);
	}

	public static void writePy2hzJson(){
		JSONObject j = new JSONObject();

		String s = FileNIOUtil.readFile(FileConstant.PINYIN_TAG_PY2HZ, Constant.CHARSET_UTF8);
		JSONObject pinyinJsonObject = JSONObject.parseObject(s);
		for (java.util.Map.Entry<String, Object> entry : pinyinJsonObject.entrySet()) {
			j.put(entry.getKey(), entry.getValue());
		}

		FileNIOUtil.writeFile("pinyin_data/py2hz.json", j.toJSONString(), Constant.CHARSET_UTF8);
	}

	public static void main(String[] args) {
		readCorpus(FileConstant.PINYIN_TAG_TRAINDATA, Constant.CHARSET_UTF8);
	}
}

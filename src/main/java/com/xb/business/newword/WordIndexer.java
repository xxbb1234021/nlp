package com.xb.business.newword;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.xb.bean.newword.WordPosInfo;
import com.xb.utils.TextUtils;

public class WordIndexer {

	private String document;

	private Map<Character, Vector<Integer>> posMap;

	public WordIndexer(String document) {
		this.document = document;
		init();
	}

	/**
	 * 初始化posMap，并把新词文本中的每个字的位置信息保存
	 */
	private void init() {
		final int len = document.length();

		char c;

		Vector<Integer> posVector;

		posMap = new HashMap<Character, Vector<Integer>>();

		for (int i = 0; i < len; i++) {
			c = document.charAt(i);
			if (!TextUtils.isChineseChar(c)) {
				continue;
			}
			posVector = posMap.get(c);
			if (posVector == null) {
				posVector = new Vector<Integer>();
				posMap.put(c, posVector);
			}
			posVector.add(i);
		}
	}

	/**
	 * 根据每个字出现的位置，计算这个词组是否一样，并计数
	 * @param text
	 * @return
     */
	public int count(String text) {
		if (StringUtils.isBlank(text)) {
			return 0;
		}

		Vector<Integer> vector = posMap.get(text.charAt(0));

		if (vector == null) {
			return 0;
		}

		if (text.length() == 1) {
			return vector.size();
		}

		int size = vector.size();
		int count = 0;

		for (int i = 0; i < size; i++) {
			if (TextUtils.match(document, vector.get(i), text)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * 根据每个字出现的位置，计算这个词组是否一样
	 * @param pos
	 * @return
     */
	public WordPosInfo find(WordPosInfo pos) {
		String text = pos.getTargetWord();

		pos.setFound(false);

		if (StringUtils.isBlank(text)) {
			return pos;
		}

		Vector<Integer> vector = posMap.get(text.charAt(0));

		if (vector == null) {
			return pos;
		}

		int size = vector.size();
		int arrayIndex = pos.getArrayIndex() + 1;

		for (int i = arrayIndex; i < size; i++) {
			if (TextUtils.match(document, vector.get(i), text)) {
				pos.setFound(true);
				pos.setPos(vector.get(i));
				pos.setArrayIndex(i);
				break;
			}
		}

		return pos;
	}

	public int getLen() {
		return document.length();
	}

	public String sub(int off, int len) {
		if (off < 0 || off + len >= document.length()) {
			return "";
		}
		return document.substring(off, off + len);
	}

	public char charAt(int index) {
		if (index < 0 || index >= document.length()) {
			return 0;
		}
		return document.charAt(index);
	}
}
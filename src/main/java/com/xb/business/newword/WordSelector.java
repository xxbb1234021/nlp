package com.xb.business.newword;

import com.xb.utils.TextUtils;

/**
 * Created by kevin on 2016/6/29.
 */
public class WordSelector {
	private String document;

	private int pos = 0;

	private int maxSelectLen;

	private int minSelectLen;

	private int currentLen;

	private int docLen;

	public WordSelector(String document, int minSelectLen, int maxSelectLen) {
		this.document = document;
		this.minSelectLen = minSelectLen;
		this.maxSelectLen = maxSelectLen;
		this.docLen = document.length();
		changeCurrentLen();
	}

	/**
	 * 改变当前字符串的长度
	 */
	public void changeCurrentLen() {
		while (pos < docLen && !TextUtils.isChineseChar(document.charAt(pos))) {
			pos++;
		}
		for (int i = 0; i < maxSelectLen && pos + i < docLen; i++) {
			if (!TextUtils.isChineseChar(document.charAt(pos + i))) {
				currentLen = i;
				if (currentLen < minSelectLen) {
					pos++;
					changeCurrentLen();
				}
				return;
			}
		}

		currentLen = pos + maxSelectLen > docLen ? docLen - pos : maxSelectLen;
	}

	/**
	 * 如果当前词存在，就移动游标到下一个词的地方
	 */
	public void select() {
		pos += ++currentLen;
		changeCurrentLen();
	}

	/**
	 * 返回下一个词
	 */
	public String next() {
		if (currentLen < minSelectLen) {
			pos++;
			changeCurrentLen();
		}

		if (pos + currentLen <= docLen && currentLen >= minSelectLen) {
			return document.substring(pos, pos + currentLen--);
		} else {
			currentLen--;
			return "";
		}
	}

	public boolean isEnd() {
		return currentLen < minSelectLen && currentLen + pos >= docLen - 1;
	}

	public int getCurrentPos() {
		return pos;
	}
}

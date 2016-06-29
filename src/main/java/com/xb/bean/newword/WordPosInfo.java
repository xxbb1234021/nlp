package com.xb.bean.newword;

public class WordPosInfo {
	private int pos = -1;

	private int arrayIndex = -1;

	private String targetWord;

	private boolean found = false;

	public WordPosInfo(String targetWord) {
		this.targetWord = targetWord;
	}

	public String getTargetWord() {
		return targetWord;
	}

	public void setTargetWord(String targetWord) {
		this.targetWord = targetWord;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getArrayIndex() {
		return arrayIndex;
	}

	public void setArrayIndex(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}
}
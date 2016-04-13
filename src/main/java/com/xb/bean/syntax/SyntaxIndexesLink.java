package com.xb.bean.syntax;

public class SyntaxIndexesLink {

	private String phrase;
	private int index;
	private String word;
	private String secondPhrase;
	private String thirdPhrase;

	// 指针域  
	private SyntaxIndexesLink next;

	// 构造函数  
	public SyntaxIndexesLink(String phrase, int index, String word, String secondPhrase, String thirdPhrase) {
		this.phrase = phrase;
		this.index = index;
		this.word = word;
		this.secondPhrase = secondPhrase;
		this.thirdPhrase = thirdPhrase;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSecondPhrase() {
		return secondPhrase;
	}

	public void setSecondPhrase(String secondPhrase) {
		this.secondPhrase = secondPhrase;
	}

	public String getThirdPhrase() {
		return thirdPhrase;
	}

	public void setThirdPhrase(String thirdPhrase) {
		this.thirdPhrase = thirdPhrase;
	}

	public SyntaxIndexesLink getNext() {
		return next;
	}

	public void setNext(SyntaxIndexesLink next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "SyntaxIndexesLink{" +
				"phrase='" + phrase + '\'' +
				", index=" + index +
				", word='" + word + '\'' +
				", secondPhrase='" + secondPhrase + '\'' +
				", thirdPhrase='" + thirdPhrase + '\'' +
				", next=" + next +
				'}';
	}
}
package com.xb.bean.syntax;

public class SyntaxProbLink {

	private String phrase;
	private double prob;

	// 指针域
	private SyntaxProbLink syntaxProbLink = null;

	// 构造函数
	public SyntaxProbLink(String phrase, double prob) {
		this.phrase = phrase;
		this.prob = prob;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

	public SyntaxProbLink getSyntaxProbLink() {
		return syntaxProbLink;
	}

	public void setSyntaxProbLink(SyntaxProbLink syntaxProbLink) {
		this.syntaxProbLink = syntaxProbLink;
	}

	@Override
	public String toString() {
		return "SyntaxProbLink{" +
				"phrase='" + phrase + '\'' +
				", prob=" + prob +
				", syntaxProbLink=" + syntaxProbLink +
				'}';
	}
}
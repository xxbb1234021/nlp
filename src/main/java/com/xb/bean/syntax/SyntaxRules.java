package com.xb.bean.syntax;

/**
 * Created by kevin on 2016/3/15.
 */
public class SyntaxRules implements Comparable<SyntaxRules> {
	private String syntax;
	private double prob;

	public SyntaxRules(String syntax) {
		this.syntax = syntax;
	}

    public SyntaxRules(String syntax, double prob) {
        this.syntax = syntax;
        this.prob = prob;
    }

    public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

    @Override
    public int compareTo(SyntaxRules o) {
        String char1 = this.getSyntax().split(" ")[2];
        String char2 = o.getSyntax().split(" ")[2];
        return char1.compareTo(char2);
    }

	@Override
	public String toString() {
		return "SyntaxRules{" +
				"syntax='" + syntax + '\'' +
				", prob=" + prob +
				'}';
	}
}

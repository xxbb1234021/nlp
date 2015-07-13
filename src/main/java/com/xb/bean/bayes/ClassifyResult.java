package com.xb.bean.bayes;

/**
 * 分类结果
 */
public class ClassifyResult {
	public double probility;// 分类的概率
	public String classification;// 分类

	public ClassifyResult() {
		this.probility = 0;
		this.classification = null;
	}

	public double getProbility() {
		return probility;
	}

	public void setProbility(double probility) {
		this.probility = probility;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
}
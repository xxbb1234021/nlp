package com.xb.services.bayes.prob;

import com.xb.services.bayes.TrainingDataManager;
import com.xb.constant.Constant;

/**
* 类条件概率计算
*
* 类条件概率P(xj|cj)=( N(X=xi, C=cj )+1 ) / ( N(C=cj)+M+V ) 
* 其中，N(X=xi, C=cj）表示类别cj中包含属性x i的训练文本数量；(containKeyCount)
* N(C=cj)表示类别cj中的训练文本数量；(fileCount)
* M值用于避免 N(X=xi, C=cj）过小所引发的问题；
* dirCount表示类别的总数
*
*条件概率
* 定义 设A, B是两个事件，且P(A)>0 称
* P(B∣A)=P(AB)/P(A)
* 为在条件A下发生的条件事件B发生的条件概率。
*/

public class ConditionalProbability
{
	private static final double M = 0F;

	/**
	* 计算类条件概率
	* @param x 给定的文本属性
	* @param c 给定的分类
	* @return 给定条件下的类条件概率
	*/
	public static double calculatePxc(String x, String c)
	{
		double ret = 0F;
		double containKeyCount = TrainingDataManager.getInstance(Constant.BAYESTRAINDATA).getCountContainKeyOfClassification(c, x);
		double fileCount = TrainingDataManager.getInstance(Constant.BAYESTRAINDATA).getTrainingFileCountOfClassification(c);
		double dirCount = TrainingDataManager.getInstance(Constant.BAYESTRAINDATA).getTraningClassifications().length;
		ret = (containKeyCount + 1) / (fileCount + M + dirCount); //为了避免出现0这样极端情况，进行加权处理
		return ret;
	}
}
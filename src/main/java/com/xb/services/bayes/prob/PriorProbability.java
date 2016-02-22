package com.xb.services.bayes.prob;

import com.xb.services.bayes.TrainingDataManager;
import com.xb.constant.Constant;

/**
  * 先验概率计算
  *P(cj)=N(C=cj)/N 
  *其中，N(C=cj)表示类别cj中的训练文本数量； (fileCount)
  *trainFileCount表示训练文本集总数量。
  */
public class PriorProbability
{
	/**
	* 先验概率
	* @param c 给定的分类
	* @return 给定条件下的先验概率
	*/
	public static double calculatePc(String c)
	{
		double ret = 0F;
		double fileCount = TrainingDataManager.getInstance(Constant.BAYESTRAINDATA).getTrainingFileCountOfClassification(c);
		double trainFileCount = TrainingDataManager.getInstance(Constant.BAYESTRAINDATA).getTrainingFileCount();
		ret = fileCount / trainFileCount;
		return ret;
	}
}
package com.xb.algoritm.bayes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.bean.bayes.ClassifyResult;
import com.xb.algoritm.bayes.prob.ConditionalProbability;
import com.xb.algoritm.bayes.prob.PriorProbability;
import com.xb.constant.Constant;
import com.xb.utils.StopWordsUtil;

/**
 * 朴素贝叶斯分类器
 */
public class BayesClassifier {
	private final TrainingDataManager tdm;//训练集管理器
	private static double zoomFactor = 15.0f;

	/**
	 * 默认的构造器，初始化训练集
	 */
	public BayesClassifier(String fileName) {
		tdm = TrainingDataManager.getInstance(fileName);
	}

	/**
	 * 计算给定的文本属性向量X在给定的分类Cj中的类条件概率
	 * ConditionalProbability连乘值
	 *
	 * @param x  给定的文本属性向量
	 * @param cj 给定的类别
	 * @return 分类条件概率连乘值<br>
	 */
	double calcProd(String[] x, String cj) {
		double ret = 1.0F;
		// 类条件概率连乘
		for (int i = 0; i < x.length; i++) {
			String xi = x[i];
			//因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
			ret *= ConditionalProbability.calculatePxc(xi, cj) * zoomFactor;
		}
		// 再乘以先验概率
		ret *= PriorProbability.calculatePc(cj);
		return ret;
	}

	/**
	 * 去掉停用词
	 *
	 * @param oldWords 给定的文本
	 * @return 去停用词后结果
	 */
	public String[] dropStopWords(String[] oldWords) {
		Vector<String> v1 = new Vector<String>();
		for (int i = 0; i < oldWords.length; ++i) {
			if (StopWordsUtil.isStopWord(oldWords[i]) == false) {//不是停用词
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}

	/**
	 * 对给定的文本进行分类
	 *
	 * @param text 给定的文本
	 * @return 分类结果
	 */
	@SuppressWarnings("unchecked")
	public String classify(String text) {
		String[] terms = null;
		MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(Constant.WORD_TRIE_TREE);
		terms = mmsegger.segment(text).split("\\|");
		//terms= ChineseSpliter.split(text, " ").split(" ");//中文分词处理(分词后结果可能还包含有停用词）
		terms = dropStopWords(terms);//去掉停用词，以免影响分类

		String[] classes = tdm.getTraningClassifications();//分类
		double probility = 0.0F;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//分类结果
		for (int i = 0; i < classes.length; i++) {
			String ci = classes[i];//第i个分类
			probility = calcProd(terms, ci);//计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
			//保存分类结果
			ClassifyResult cr = new ClassifyResult();
			cr.setClassification(ci);//分类
			cr.setProbility(probility);//关键字在分类的条件概率
			//System.out.println("In process.");
			// System.out.println(ci + "：" + probility);
			crs.add(cr);
		}
		//对最后概率结果进行排序
		java.util.Collections.sort(crs, new Comparator<ClassifyResult>() {
			@Override
			public int compare(final ClassifyResult m1, final ClassifyResult m2) {
				if (m1.getProbility() < m2.getProbility())
					return 1;
				if (m1.getProbility() > m2.getProbility())
					return -1;
				return 0;
			}
		});
		//返回概率最大的分类
		return crs.get(0).classification;
	}

	public static void main(String[] args) {
		String text = "微软公司提出以446亿美元的价格收购雅虎中国网2月1日报道 美联社消息，微软公司提出以446亿美元现金加股票的价格收购搜索网站雅虎公司。微软提出以每股31美元的价格收购雅虎。微软的收购报价较雅虎1月31日的收盘价19.18美元溢价62%。微软公司称雅虎公司的股东可以选择以现金或股票进行交易。微软和雅虎公司在2006年底和2007年初已在寻求双方合作。而近两年，雅虎一直处于困境：市场份额下滑、运营业绩不佳、股价大幅下跌。对于力图在互联网市场有所作为的微软来说，收购雅虎无疑是一条捷径，因为双方具有非常强的互补性。(小桥)";
		BayesClassifier classifier = new BayesClassifier(Constant.BAYESTRAINDATA);//构造Bayes分类器
		String result = classifier.classify(text);//进行分类
		System.out.println("此项属于[" + result + "]");
	}
}
package com.xb.common.newword;

import com.xb.bean.newword.WordPosInfo;
import com.xb.business.newword.WordIndexer;
import com.xb.utils.CountMap;
import com.xb.utils.TextUtils;

/**
 * Created by kevin on 2016/6/29.
 */
public class NewWrodEntropy {
	private static double ENTROPY_THRESHOL = 1.0;

	private WordIndexer indexer;

	public NewWrodEntropy(WordIndexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * 比较信息熵的大小是否满足要求
	 * @param word
	 * @return
     */
	public boolean judge(String word) {
		double entropy = getEntropy(word);

		if (entropy < ENTROPY_THRESHOL) {
			return false;
		}
		return true;
	}

    /**
     * 计算一个词的左右信息熵
     * @param word
     * @return
     */
	private double getEntropy(String word) {
        final int wordLen = word.length();
        int off = 0;
        char c;
        double rate, leftEntropy = 0, rightEntropy = 0;

		WordPosInfo pos = new WordPosInfo(word);
		CountMap<Character> leftCountMap = new CountMap<Character>();
		CountMap<Character> rightCountMap = new CountMap<Character>();

		while (indexer.find(pos).isFound()) {
			off = pos.getPos();

			c = indexer.charAt(off - 1);
			if (TextUtils.isChineseChar(c)) {
				leftCountMap.increase(c);
			}
			c = indexer.charAt(off + wordLen);
			if (TextUtils.isChineseChar(c)) {
				rightCountMap.increase(c);
			}
		}

		for (char key : leftCountMap.keySet()) {
			rate = (double) leftCountMap.get(key) / leftCountMap.count();
			leftEntropy -= rate * Math.log(rate);
		}
		for (char key : rightCountMap.keySet()) {
			rate = (double) rightCountMap.get(key) / rightCountMap.count();
			rightEntropy -= rate * Math.log(rate);
		}

		return leftEntropy > rightEntropy ? rightEntropy : leftEntropy;
	}
}

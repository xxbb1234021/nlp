package com.xb.text;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.algoritm.text.SimHashTextSimilarity;
import com.xb.constant.FileConstant;
import org.junit.Test;

/**
 * Created by kevin on 2016/8/6.
 */
public class TestSimHashTextSimilarity {

	@Test
	public void testSimHashTextSimilarity() {
		MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(FileConstant.WORD_TRIE_TREE);
		String text1 = mmsegger.segment("有限的确定性算法，");
		String text2 = mmsegger.segment("有限的非确定算法，");

		SimHashTextSimilarity shs = new SimHashTextSimilarity();
		String simHash1 = shs.simHash(text1);
		String simHash2 = shs.simHash(text2);

		int hammingDistance = shs.hammingDistance(simHash1, simHash2);
		System.out.println(hammingDistance);
	}
}

package com.xb.summary;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.algoritm.summary.KeywordExtraction;
import com.xb.algoritm.summary.SentencesExtraction;
import com.xb.constant.FileConstant;
import com.xb.utils.StopWordsUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/8/7.
 */
public class TestSentencesExtraction {

    @Test
    public void testSentencesExtraction() {
        String s = "无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
		System.out.println(SentencesExtraction.summarise(s, 1));
	}
}
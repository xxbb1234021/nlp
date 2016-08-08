package com.xb.summary;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import com.xb.algoritm.summary.KeywordExtraction;
import com.xb.constant.FileConstant;
import com.xb.utils.StopWordsUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/8/7.
 */
public class TestKeywordExtraction {

	@Test public void testNewWord() {
		String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
		MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(FileConstant.WORD_TRIE_TREE);
		String words = mmsegger.segment(content);
		String[] wordArray = words.split("\\|");
		List<String> wordList = new ArrayList<String>();
		for(int i = 0; i < wordArray.length; i++) {
			if (!StopWordsUtil.isStopWord(wordArray[i])) {
				wordList.add(wordArray[i]);
			}
		}

		System.out.println(KeywordExtraction.getKeyword(wordList));
	}
}
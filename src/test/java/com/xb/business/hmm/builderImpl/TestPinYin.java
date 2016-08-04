package com.xb.business.hmm.builderImpl;

import com.xb.algoritm.segment.MaxMatchingPinYinSegmenter;
import com.xb.constant.FileConstant;
import org.junit.Test;

/**
 * Created by kevin on 2016/5/7.
 */
public class TestPinYin {

    @Test
    public void testViterbi() {
        String source = "wodasini";
        MaxMatchingPinYinSegmenter mmsegger =
                new MaxMatchingPinYinSegmenter(FileConstant.PINYIN_TRIE_TREE);
        String splitPinYin = mmsegger.segment(source);

        System.out.println(splitPinYin);
    }
}

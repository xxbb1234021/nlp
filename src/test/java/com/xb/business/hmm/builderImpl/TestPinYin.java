package com.xb.business.hmm.builderImpl;

import com.xb.algoritm.segment.PinYinSegmenter;
import org.junit.Test;

import com.xb.constant.Constant;

/**
 * Created by kevin on 2016/5/7.
 */
public class TestPinYin {

    @Test
    public void testViterbi() {
        String source = "wodasini";
        PinYinSegmenter mmsegger = new PinYinSegmenter(Constant.PINYIN_TRIE_TREE);
        String splitPinYin = mmsegger.segment(source);

        System.out.println(splitPinYin);
    }
}

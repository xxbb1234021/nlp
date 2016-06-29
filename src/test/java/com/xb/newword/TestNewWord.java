package com.xb.newword;

import com.xb.common.newword.NewWordDiscover;
import com.xb.constant.Constant;
import com.xb.utils.FileNIOUtil;
import org.junit.Test;

import java.util.Set;

/**
 * Created by kevin on 2016/6/29.
 */
public class TestNewWord {

	@Test
	public void testNewWord() {
		String document = FileNIOUtil.readFile(Constant.WORD_NEW_TEXT, Constant.CHARSET_UTF8);

		NewWordDiscover discover = new NewWordDiscover();
		Set<String> words = discover.discover(document);
		System.out.println("发现新词有: " + words.size() + "个");
        for (String word : words) {
            System.out.println(word);
        }
        System.out.println();
	}
}

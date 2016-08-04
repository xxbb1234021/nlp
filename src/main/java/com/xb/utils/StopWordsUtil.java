package com.xb.utils;

import com.xb.constant.Constant;
import com.xb.constant.FileConstant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 停用词处理器
 */
public class StopWordsUtil {
    private static Set<String> wordSet = new HashSet<String>();

    static {
        List<String> lines =
                FileNIOUtil.readFileLine(FileConstant.STOPWORD_DATA, Constant.CHARSET_UTF8);
        for (String line : lines) {
            wordSet.add(line);
        }
    }

    public static boolean isStopWord(String word) {
        if (wordSet.contains(word)) {
            return true;
        }
        return false;
    }

    public static Set<String> getWordSet() {
        return wordSet;
    }

    public static void main(String[] args) {
        System.out.println(isStopWord("xxxxxxxx"));
    }
}

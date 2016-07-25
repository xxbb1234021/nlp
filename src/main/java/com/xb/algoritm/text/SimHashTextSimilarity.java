package com.xb.algoritm.text;

import java.math.BigInteger;

import com.xb.algoritm.segment.MaxMatchingWordSegmenter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xb.constant.Constant;

/**
 * Created by kevin on 2016/7/18.
 * <p>
 * 1、分词，把需要判断文本分词形成这个文章的特征单词。最后形成去掉噪音词的单词序列并为每个词加上权重，我们假设权重分为5个级别（1~5）。比如：“ 美国“51区”雇员称内部有9架飞碟，曾看见灰色外星人 ” ==> 分词后为 “ 美国（4） 51区（5） 雇员（3） 称（1） 内部（2） 有（1） 9架（3） 飞碟（5） 曾（1） 看见（3） 灰色（4） 外星人（5）”，括号里是代表单词在整个句子里重要程度，数字越大越重要。
 * 2、hash，通过hash算法把每个词变成hash值，比如“美国”通过hash算法计算为 100101,“51区”通过hash算法计算为 101011。这样我们的字符串就变成了一串串数字，还记得文章开头说过的吗，要把文章变为数字计算才能提高相似度计算性能，现在是降维过程进行时。
 * 3、加权，通过 2步骤的hash生成结果，需要按照单词的权重形成加权数字串，比如“美国”的hash值为“100101”，通过加权计算为“4 -4 -4 4 -4 4”；“51区”的hash值为“101011”，通过加权计算为 “ 5 -5 5 -5 5 5”。
 * 4、合并，把上面各个单词算出来的序列值累加，变成只有一个序列串。比如 “美国”的 “4 -4 -4 4 -4 4”，“51区”的 “ 5 -5 5 -5 5 5”， 把每一位进行累加， “4+5 -4+-5 -4+5 4+-5 -4+5 4+5” ==》 “9 -9 1 -1 1 9”。这里作为示例只算了两个单词的，真实计算需要把所有单词的序列串累加。
 * 5、降维，把4步算出来的 “9 -9 1 -1 1 9” 变成 0 1 串，形成我们最终的simhash签名。 如果每一位大于0 记为 1，小于0 记为 0。最后算出结果为：“1 0 1 0 1 1”。
 */
public class SimHashTextSimilarity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimHashTextSimilarity.class);

    private int hashBitCount = 64;

    private int weight = 1;

    /**
     * 计算词列表的SimHash值
     *
     * @param segWord 词列表
     * @return SimHash值
     */
    public String simHash(String segWord) {
        int[] hashBit = new int[hashBitCount];
        String[] wordArray = segWord.split("\\|");
        String word = null;
        for (int i = 0, len = wordArray.length; i < len; i++) {
            word = wordArray[i];
            if (StringUtils.isNotBlank(word)) {
                BigInteger hash = hash(word);
                for (int j = 0; j < hashBitCount; j++) {
                    BigInteger bitMask = new BigInteger("1").shiftLeft(j);
                    if (hash.and(bitMask).signum() != 0) {
                        hashBit[j] += weight;
                    } else {
                        hashBit[j] -= weight;
                    }
                }
            }
        }

        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < hashBitCount; i++) {
            if (hashBit[i] >= 0) {
                simHashBuffer.append("1");
            } else {
                simHashBuffer.append("0");
            }
        }
        return simHashBuffer.toString();
    }

    /**
     * 计算等长的SimHash值的汉明距离
     * 如不能比较距离（比较的两段文本长度不相等），则返回-1
     *
     * @param simHash1 SimHash值1
     * @param simHash2 SimHash值2
     * @return 汉明距离
     */
    private int hammingDistance(String simHash1, String simHash2) {
        if (simHash1.length() != simHash2.length()) {
            return -1;
        }
        int distance = 0;
        int len = simHash1.length();
        for (int i = 0; i < len; i++) {
            if (simHash1.charAt(i) != simHash2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    /**
     * 计算词的哈希值
     *
     * @param word 词
     * @return 哈希值
     */
    private BigInteger hash(String word) {
        if (word == null || word.length() == 0) {
            return new BigInteger("0");
        }
        char[] charArray = word.toCharArray();
        BigInteger x = BigInteger.valueOf(((long) charArray[0]) << 7);
        BigInteger m = new BigInteger("1000003");
        BigInteger mask = new BigInteger("2").pow(hashBitCount).subtract(new BigInteger("1"));
        long sum = 0;
        for (char c : charArray) {
            sum += c;
        }
        x = x.multiply(m).xor(BigInteger.valueOf(sum)).and(mask);
        x = x.xor(new BigInteger(String.valueOf(word.length())));
        if (x.equals(new BigInteger("-1"))) {
            x = new BigInteger("-2");
        }
        return x;
    }

    public static void main(String[] args) {
        MaxMatchingWordSegmenter mmsegger = new MaxMatchingWordSegmenter(Constant.WORD_TRIE_TREE);
        String text1 = mmsegger.segment("我爱买书");
        String text2 = mmsegger.segment("我爱购物");

        SimHashTextSimilarity shs = new SimHashTextSimilarity();
        String simHash1 = shs.simHash(text1);
        String simHash2 = shs.simHash(text2);

        int hammingDistance = shs.hammingDistance(simHash1, simHash2);
        System.out.println(hammingDistance);
    }
}

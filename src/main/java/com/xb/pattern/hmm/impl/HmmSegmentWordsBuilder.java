package com.xb.pattern.hmm.impl;

import java.io.*;
import java.util.*;

import com.xb.constant.Constant;
import com.xb.pattern.hmm.HmmModelBuilder;
import org.apache.log4j.Logger;

/**
 * Created by kevin on 2016/1/21.
 */
public class HmmSegmentWordsBuilder extends HmmModelBuilder {
    private static Logger LOGGER = Logger.getLogger(HmmSegmentWordsBuilder.class);

    private static final int CHAR_LABEL_NUM = 4;

    private double[] prioriProbability;// 初始概率

    private long[][] transformMatrix = new long[4][5];

    private double[][] transformProbability;

    private long[][] emissionMatrix;

    private double[][] emissionProbability;

    //存储汉字编码表
    private Map<String, Integer> chineseCodeMap = new HashMap<String, Integer>();

    private static HmmSegmentWordsBuilder ghm = null;

    private HmmSegmentWordsBuilder(String chineseCodeFile, String corpusFile) {
        readChineseCode(chineseCodeFile);
        splitCorpus(corpusFile);
    }

    public static HmmSegmentWordsBuilder getInstance(String chineseCodeFile, String corpusFile) {
        if (ghm == null) {
            synchronized (HmmSegmentWordsBuilder.class) {
                if (ghm == null) {
                    ghm = new HmmSegmentWordsBuilder(chineseCodeFile, corpusFile);
                }
            }
        }
        return ghm;
    }

    private boolean readChineseCode(String chineseCodeFile) {
        readCorpus(chineseCodeFile, Constant.CHARSET_UTF8);
        for (String line : lineList){
            String[] cc = line.trim().split("\\s{1,}");
            chineseCodeMap.put(cc[0], Integer.parseInt(cc[1]));
        }

        emissionMatrix = new long[4][chineseCodeMap.size() + 1];
        return true;
    }

    private boolean getChineseCode(List<String> textList) {
        int index = 1;
        for (String text : textList) {
            String[] words = text.split("  ");
            for (int i = 0; i < words.length; i++) {
                String word = words[i].trim();
                int length = word.length();
                if (length < 1)
                    continue;

                if (length == 1) {
                    if (chineseCodeMap.get(word) == null) {
                        chineseCodeMap.put(word, index++);
                    }
                } else {
                    for (int j = 0; j < word.length(); j++) {
                        if (chineseCodeMap.get(word.charAt(j) + "") == null) {
                            chineseCodeMap.put(word.charAt(j) + "", index++);
                        }
                    }
                }
            }
        }
        emissionMatrix = new long[4][chineseCodeMap.size()];
        return true;
    }

    private boolean splitCorpus(String fileName) {
        readCorpus(fileName, Constant.CHARSET_UTF8);

        List<String> textList = new ArrayList<String>();
        for (String line : lineList) {
            textList.add(line.replaceAll("\\pP", " ").trim());
        }

        //getChineseCode(textList);
        buildTransformMatrix(textList);
        buildEmissionMatrix(textList);
        return true;
    }

    /**
     * 把内容转换成如下矩阵,存入transformMatrix
     *
     * @param textList 传入句子集合
     *                 <p/>
     *                   ALL B M E S
     *                 B *   * * * *
     *                 M *   * * * *
     *                 E *   * * * *
     *                 S *   * * * *
     *                 <p/>
     *                 transformMatrix[0][0] B：词的开始 总数
     *                 transformMatrix[1][0] M：词的中间 总数
     *                 transformMatrix[2][0] E：词的结束 总数
     *                 transformMatrix[3][0] S：单字成词 总数
     */
    private void buildTransformMatrix(List<String> textList) {
        String lastWord = null;
        for (String text : textList) {

            String[] words = text.split(" ");
            for (int i = 0; i < words.length; i++) {
                String word = words[i].trim();
                int length = word.length();
                if (length < 1)
                    continue;
                if (length == 1) {
                    transformMatrix[3][0]++;
                    if (lastWord != null) {
                        if (lastWord.length() == 1) {//表示上一个词是单字
                            transformMatrix[3][4]++;//S-->S
                        } else {//表示上一个词是一个词的结束
                            transformMatrix[2][4]++;//E-->S
                        }
                    }
                } else {
                    transformMatrix[0][0]++;
                    transformMatrix[2][0]++;

                    if (length > 2) {
                        transformMatrix[1][0] += length - 2;//
                        transformMatrix[0][2]++;//B-->M
                        if (length - 2 > 1) {
                            transformMatrix[1][2] += length - 3;//M-->M
                        }
                        transformMatrix[1][3]++;//M-->E
                    } else {
                        transformMatrix[0][3]++;//B-->E
                    }

                    if (lastWord != null) {
                        if (lastWord.length() == 1) {
                            transformMatrix[3][1]++;//S-->B
                        } else {
                            transformMatrix[2][1]++;//E-->B
                        }
                    }
                }
                lastWord = word;
            }
        }
    }

    /**
     * 把内容转换成如下矩阵,存入emissionMatrix
     *
     * @param textList 传入句子集合
     *                 <p/>
     *                   ALL C1 C2 C3 CN
     *                 B  *  *  *  *  *
     *                 M  *  *  *  *  *
     *                 E  *  *  *  *  *
     *                 S  *  *  *  *  *
     *                 <p/>
     *                 NOTE:
     *                 emissionMatrix[0][0] B：词的开始 总数
     *                 emissionMatrix[1][0] M：词的中间 总数
     *                 emissionMatrix[2][0] E：词的结束 总数
     *                 emissionMatrix[3][0] S：单字成词 总数
     */
    private void buildEmissionMatrix(List<String> textList) {
        for (int i = 0; i < emissionMatrix.length; i++) {
            Arrays.fill(emissionMatrix[i], 1);
            emissionMatrix[i][0] = chineseCodeMap.size();
        }

        for (String text : textList) {
            String[] words = text.split(" ");
            for (int i = 0; i < words.length; i++) {
                String word = words[i].trim();
                int length = word.length();
                if (length < 1)
                    continue;

                if (length == 1) {
                    if (chineseCodeMap.get(word) != null) {
                        int index = chineseCodeMap.get(word);
                        emissionMatrix[3][0]++;
                        emissionMatrix[3][index]++;
                    }
                } else {
                    for (int j = 0; j < word.length(); j++) {
                        if (chineseCodeMap.get(word.charAt(j) + "") != null) {
                            int index = chineseCodeMap.get(word.charAt(j) + "");
                            if (j == 0) {
                                emissionMatrix[0][0]++;
                                emissionMatrix[0][index]++;
                            } else if (j == word.length() - 1) {
                                emissionMatrix[2][0]++;
                                emissionMatrix[2][index]++;
                            } else {
                                emissionMatrix[1][0]++;
                                emissionMatrix[1][index]++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println();
    }

    @Override
    public void transformFrequencySum() {

    }

    @Override
    public void emissonFrequencySum() {

    }

    /**
     * 计算初始概率
     * <p/>
     * 状态值集合为(B, M, E, S): {B:begin, M:middle, E:end, S:single}。
     * 分别代表每个状态代表的是该字在词语中的位置，
     * B代表该字是词语中的起始字，M代表是词语中的中间字，E代表是词语中的结束字，S则代表是单字成词
     * <p/>
     * E和M的概率都是0，这和实际相符合，开头的第一个字只可能是词语的首字(B)，或者是单字成词(S)。
     */
    @Override
    public void calculatePrioriProbability() {
        prioriProbability = new double[CHAR_LABEL_NUM];

        //        prioriProbability[0] = 0.5;
        //        prioriProbability[3] = 0.5;

        long allWordCount = transformMatrix[2][0] + transformMatrix[3][0];
        prioriProbability[0] = (double) transformMatrix[2][0] / allWordCount;
        prioriProbability[3] = (double) transformMatrix[3][0] / allWordCount;

        System.out.println(Arrays.toString(prioriProbability));
    }

    @Override
    public void calculateTransformProbability() {
        transformProbability = new double[4][4];
        for (int i = 0; i < transformProbability.length; i++) {
            for (int j = 0; j < transformProbability[i].length; j++) {
                transformProbability[i][j] = (double) transformMatrix[i][j + 1] / transformMatrix[i][0];
            }
        }

        for (int i = 0; i < transformMatrix.length; i++)
            System.out.println(Arrays.toString(transformMatrix[i]));

        System.out.println();

        for (int i = 0; i < transformProbability.length; i++)
            System.out.println(Arrays.toString(transformProbability[i]));

    }

    @Override
    public void calculateEmissionProbability() {
        emissionProbability = new double[emissionMatrix.length][emissionMatrix[0].length];
        for (int i = 0; i < emissionProbability.length; i++) {
            for (int j = 0; j < emissionProbability[i].length; j++) {
                emissionProbability[i][j] = (double) emissionMatrix[i][j] / emissionMatrix[i][0];

                if (j < 50) {
                    System.out.print(emissionProbability[i][j] + " ");
                }
            }
            System.out.println();
        }

        System.out.printf("");
    }

    public double[] getPrioriProbability() {
        return prioriProbability;
    }

    public void setPrioriProbability(double[] prioriProbability) {
        this.prioriProbability = prioriProbability;
    }

    public double[][] getTransformProbability() {
        return transformProbability;
    }

    public void setTransformProbability(double[][] transformProbability) {
        this.transformProbability = transformProbability;
    }

    public double[][] getEmissionProbability() {
        return emissionProbability;
    }

    public void setEmissionProbability(double[][] emissionProbability) {
        this.emissionProbability = emissionProbability;
    }

    public Map<String, Integer> getChineseCodeMap() {
        return chineseCodeMap;
    }

    public void setChineseCodeMap(Map<String, Integer> chineseCodeMap) {
        this.chineseCodeMap = chineseCodeMap;
    }
}
